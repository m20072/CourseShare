package com.example.courseshare.views;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.courseshare.adapters.CourseAdapter;
import com.example.courseshare.R;
import com.example.courseshare.models.CourseList;
import com.example.courseshare.interfaces.MaterialFragmentCallback;
import com.example.courseshare.utilities.SignalGenerator;
import com.example.courseshare.viewmodels.CourseListViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class CourseListFragment extends Fragment
{
    private MaterialFragmentCallback materialFragmentCallback;
    private CourseAdapter courseAdapter;
    private RecyclerView main_LST_courses;
    private CourseListViewModel courseListViewModel;
    private ExtendedFloatingActionButton main_FAB_addcourse;
    private AppCompatEditText main_ET_searchcourse;
    private TextWatcher textWatcher;


    private Observer<CourseList> observer = new Observer<CourseList>() //when courselist livedata is changed, it observes and does the following
    {
        @Override
        public void onChanged(CourseList courseList)
        {
            if(courseList!=null) //prevent crash if DB is null
                courseAdapter.updateCourseList(courseList.getCourses());
        }
    };

    public void setMaterialFragmentCallback(MaterialFragmentCallback materialFragmentCallback)
    {
        this.materialFragmentCallback = materialFragmentCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        findViews(view);
        initViews(view);
        initTextWatcher();
        applyListeners();
        return view;
    }


    private void initViews(View view)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        main_LST_courses.setLayoutManager(linearLayoutManager);

        courseListViewModel = new ViewModelProvider(this).get(CourseListViewModel.class);
        courseAdapter = new CourseAdapter(courseListViewModel.getmCourseList().getValue().getCourses());
        main_LST_courses.setAdapter(courseAdapter);
        courseListViewModel.getmCourseList().observe(getViewLifecycleOwner(), observer); //observe changes in the live data.

        courseAdapter.setCourseCallback(position -> {materialFragmentCallback.moveToMaterialList(position);});
    }

    private void initTextWatcher()
    {
        textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {courseAdapter.getFilter().filter(s);}

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private void applyListeners()
    {
        main_FAB_addcourse.setOnClickListener(view1 -> showAddCourseDialog());
        main_ET_searchcourse.addTextChangedListener(textWatcher);
    }

    private void findViews(View view)
    {
        main_LST_courses = view.findViewById(R.id.main_LST_courses);
        main_FAB_addcourse = view.findViewById(R.id.main_FAB_addcourse);
        main_ET_searchcourse = view.findViewById(R.id.main_ET_searchcourse);
    }

    private void showAddCourseDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_add_course, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        AppCompatEditText editTextCourseName = view.findViewById(R.id.dialog_ET_coursename);
        AppCompatEditText editTextCourseNumber = view.findViewById(R.id.dialog_ET_coursecode);
        MaterialButton buttonSave = view.findViewById(R.id.dialog_MB_savecourse);

        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String courseName = editTextCourseName.getText().toString();
                String courseCode = editTextCourseNumber.getText().toString();
                if(courseName.length() != 0 && courseCode.length() !=0)
                {
                    courseListViewModel.addCourse(courseName, courseCode);
                    dialog.dismiss();
                }
                else
                {
                    SignalGenerator.getInstance().toast("Please fill up all required fields.", Toast.LENGTH_SHORT);
                    if(courseName.length() == 0)
                        editTextCourseName.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));

                    if(courseCode.length() == 0)
                        editTextCourseNumber.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
                    SignalGenerator.getInstance().vibrate(500);
                }
            }
        });

        dialog.show();
    }

}