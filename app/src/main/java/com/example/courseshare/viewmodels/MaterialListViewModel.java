package com.example.courseshare.viewmodels;

import static com.example.courseshare.utilities.Constants.DBKeys.FILESFOLDER;

import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.courseshare.models.Course;
import com.example.courseshare.models.CourseList;
import com.example.courseshare.models.Material;
import com.example.courseshare.utilities.Constants;
import com.example.courseshare.utilities.SignalGenerator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MaterialListViewModel extends ViewModel
{
    private Material materialMediator; //holds info about the material that the user wants to upload
    private int courseIndex; //index of the chosen course in the previous fragment.
    private Uri tmpFileUri; //location of file on device. (NOT FIREBASE STORAGE)

    private final MutableLiveData<CourseList> mCourseList;

    public MaterialListViewModel()
    {
        mCourseList = new MutableLiveData<>();
        materialMediator = new Material();
        getCourseListFromFirebase();
    }

    public MutableLiveData<CourseList> getmCourseList()
    {
        return mCourseList;
    }

    public Material getMaterialMediator()
    {
        return materialMediator;
    }

    private void getCourseListFromFirebase()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(Constants.DBKeys.COURSELIST);

        databaseReference.addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        mCourseList.setValue(snapshot.getValue(CourseList.class)); //every time data changes in realltime db, mCourseList (livedata) gets the new updated courselist
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        SignalGenerator.getInstance().toast("Update download failed.", Toast.LENGTH_SHORT);
                        SignalGenerator.getInstance().vibrate(500);
                    }
                }
        );
    }

    public void generateMaterial()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference fileRef = storageRef.child(FILESFOLDER + "/" + materialMediator.getFileType());
        materialMediator.setFileRefPath(fileRef.getPath());


        UploadTask uploadTask = fileRef.putFile(tmpFileUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                uploadNewCourseList(materialMediator);
            }

        }).addOnFailureListener(e ->
        {
            SignalGenerator.getInstance().toast("Upload failed.", Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate(500);
        });
    }

    private void uploadNewCourseList(Material material)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        CourseList tempList = mCourseList.getValue();
        tempList.getCourses().get(courseIndex).increaseCount().getMaterials().add(material);
        db.getReference(Constants.DBKeys.COURSELIST).setValue(tempList);
        materialMediator.setFileType(null); //prevent save button from working on the next material upload.
    }

    public MaterialListViewModel setCourseIndex(int courseIndex)
    {
        this.courseIndex = courseIndex;
        return this;
    }

     public Course getCurrentCourse()
     {
         return mCourseList.getValue().getCourses().get(courseIndex);
     }

    public String getExtension(Cursor cursor)
    {
        String fileName = null;

        try {
            if (cursor != null && cursor.moveToFirst())
            {
                // get file name
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if(columnIndex >= 0)
                    fileName = cursor.getString(columnIndex);
            }
        } finally {}
        return fileName;
    }

    public int getSize(Cursor cursor)
    {
        int fileSize = 0;
        try {
            if (cursor != null && cursor.moveToFirst())
            {

                // get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex))
                {
                    fileSize = (cursor.getInt(sizeIndex)/1000) + 1; //division by 1000 for kbs instead ob bytes, +1 to show true size.
                }
            }
        } finally {}
        return fileSize;
    }

    public void getFileInfo(Cursor cursor, Uri uri)
    {
        getMaterialMediator().setFileType(getExtension(cursor));
        getMaterialMediator().setFileSize(getSize(cursor));
        cursor.close();
        tmpFileUri = uri;
    }
}
