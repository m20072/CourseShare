package com.example.courseshare.views;

import static com.example.courseshare.utilities.Constants.BundleKeys.COURSE_INDEX;
import static com.example.courseshare.utilities.Constants.BundleKeys.USER_NAME;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.courseshare.adapters.MaterialAdapter;
import com.example.courseshare.R;
import com.example.courseshare.models.CourseList;
import com.example.courseshare.utilities.SignalGenerator;
import com.example.courseshare.viewmodels.MaterialListViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MaterialListFragment extends Fragment
{
    private RecyclerView main_LST_materials;
    private ExtendedFloatingActionButton main_FAB_addmaterial;
    private MaterialListViewModel materialListViewModel;
    private MaterialAdapter materialAdapter;
    private ActivityResultLauncher<Intent> sActivityResultLauncher;

    private final Observer<CourseList> observer = new Observer<CourseList>() //when courselist livedata is changed.
    {
        @Override
        public void onChanged(CourseList courseList)
        {
            materialAdapter.updateMaterialList(materialListViewModel.getCurrentCourse().getMaterials());
        }
    };


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1); //id of the downloaded file.
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = downloadManager.getUriForDownloadedFile(downloadId); //uri of downloaded file according to the file id.
            String mimeType = downloadManager.getMimeTypeForDownloadedFile(downloadId); //mime type, helps default to correct app to open with.


            Intent openFileIntent = new Intent(Intent.ACTION_QUICK_VIEW); //ACTION_VIEW also possible.
            openFileIntent.setDataAndType(uri, mimeType);
            openFileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //must have the second flag otherwise no permission to grant URI to the intent
            try
            {
                startActivity(openFileIntent);
            }
            catch (ActivityNotFoundException e)
            {
                // Handle the case where no activity is available to handle the file
            }
        }
    };


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        materialListViewModel = new ViewModelProvider(this).get(MaterialListViewModel.class);
        initializeActivityLauncher();

        Bundle arguments = getArguments();
        if (arguments != null)
        {
            materialListViewModel.setCourseIndex(arguments.getInt(COURSE_INDEX));
            materialListViewModel.getMaterialMediator().setUploadUser(arguments.getString(USER_NAME));
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_material_list, container, false);
        findViews(view);
        initViews(view);
        return view;
    }


    private void initViews(View view)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_materials.setLayoutManager(linearLayoutManager);
        materialAdapter = new MaterialAdapter(new ArrayList<>());
        main_LST_materials.setAdapter(materialAdapter);
        materialListViewModel.getmCourseList().observe(getViewLifecycleOwner(), observer);


        materialAdapter.setMaterialCallback(material ->
        {
            String fileRefPath = material.getFileRefPath();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference fileRef = storage.getReference(fileRefPath); //fileRef is referring to exactly the path(not url) of the material within the storage.

            fileRef.getDownloadUrl().addOnSuccessListener(uri ->
            {
                String url=uri.toString();
                downloadFile(getContext(),material.getFileType(),"",Environment.DIRECTORY_DOWNLOADS,url);
            });
        });

        main_FAB_addmaterial.setOnClickListener(view1 -> showAddMaterialDialog());
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url)
    {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);


        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension); //saves to to directory in app
        //request.setDestinationInExternalPublicDir(destinationDirectory, fileName); //saves to directory in phone.

         downloadManager.enqueue(request);
    }

    private void findViews(View view)
    {
        main_LST_materials = view.findViewById(R.id.main_LST_materials);
        main_FAB_addmaterial = view.findViewById(R.id.main_FAB_addmaterial);
    }

    private void showAddMaterialDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_add_material, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        AppCompatEditText editTextMaterialName = view.findViewById(R.id.dialog_ET_materialname);
        MaterialButton saveMaterial = view.findViewById(R.id.dialog_MB_savematerial);
        MaterialButton selectFile = view.findViewById(R.id.dialog_MB_selectfile);

        saveMaterial.setOnClickListener(v ->
        {
            if(editTextMaterialName.length() != 0 && materialListViewModel.getMaterialMediator().getFileType() != null)
            {
                materialListViewModel.getMaterialMediator().setName(editTextMaterialName.getText().toString());
                materialListViewModel.generateMaterial();
                dialog.dismiss();
            }
            else
            {
                if(editTextMaterialName.length() == 0 && materialListViewModel.getMaterialMediator().getFileType() != null)
                {
                    SignalGenerator.getInstance().toast("Please enter a name for the material.", Toast.LENGTH_SHORT);
                    editTextMaterialName.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
                    SignalGenerator.getInstance().vibrate(500);
                }

                else if(editTextMaterialName.length() != 0 && materialListViewModel.getMaterialMediator().getFileType() == null)
                {
                    SignalGenerator.getInstance().toast("Please select a file.", Toast.LENGTH_SHORT);
                    SignalGenerator.getInstance().vibrate(500);
                }
                else
                {
                    SignalGenerator.getInstance().toast("Please enter a name for the material and select a file.", Toast.LENGTH_SHORT);
                    editTextMaterialName.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
                    SignalGenerator.getInstance().vibrate(500);
                }
            }
        });

        selectFile.setOnClickListener(view1 -> showFileChooser());

        dialog.show();
    }

    private void showFileChooser()
    {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.addCategory(Intent.CATEGORY_OPENABLE);
        data.setType("*/*");
        data = Intent.createChooser(data, "Choose a file");

        sActivityResultLauncher.launch(data);

    }

    void initializeActivityLauncher()
    {
        //callback when selected file info comes back
        sActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null, null);
                        materialListViewModel.getFileInfo(cursor, uri);
                    }
                }
        );
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getContext().registerReceiver(broadcastReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        getContext().unregisterReceiver(broadcastReceiver);
    }
}