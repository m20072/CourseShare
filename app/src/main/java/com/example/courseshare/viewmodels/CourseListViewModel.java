package com.example.courseshare.viewmodels;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.courseshare.models.Course;
import com.example.courseshare.models.CourseList;
import com.example.courseshare.utilities.Constants;
import com.example.courseshare.utilities.SignalGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseListViewModel extends ViewModel
{
    private final MutableLiveData<CourseList> mCourseList;

    public CourseListViewModel()
    {
        mCourseList = new MutableLiveData<>();
        mCourseList.setValue(getCourseListFromFirebase());
    }

    private CourseList getCourseListFromFirebase()
    {
        CourseList courseList = new CourseList();
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
        return courseList;
    }

    public MutableLiveData<CourseList> getmCourseList()
    {
        return mCourseList;
    }

    public void addCourse(String courseName, String courseCode)
    {
        Course c = new Course();
        c.setName(courseName).setCode(Integer.parseInt(courseCode));
        uploadCourse(c); //add a course to the DB
    }

    private void uploadCourse(Course course)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        CourseList tempList = mCourseList.getValue();

        //this is only needed once for the db
        if(tempList==null) //if db is empty, mCourseList will be null and therefore tempList will be null, and then tempList.getCourses() will crash. so instead make new CourseList
            tempList = new CourseList();
        //this is only needed once for the DB
        tempList.getCourses().add(course);
        db.getReference(Constants.DBKeys.COURSELIST).setValue(tempList);
    }
}
