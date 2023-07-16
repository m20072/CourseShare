package com.example.courseshare.activities;

import static com.example.courseshare.utilities.Constants.BundleKeys.COURSE_INDEX;
import static com.example.courseshare.utilities.Constants.BundleKeys.USER_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.courseshare.R;
import com.example.courseshare.views.CourseListFragment;
import com.example.courseshare.views.MaterialListFragment;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity
{
    private CourseListFragment courseListFragment;
    private MaterialListFragment materialListFragment;
    private MaterialTextView main_LBL_title;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
        initFragments();
        beginTransactions();
    }

    private void beginTransactions()
    {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_FRAME_list, courseListFragment);
        transaction.commit();
    }

    private void swapFragment(int courseIndex)
    {
        transaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent();
        String userName = intent.getStringExtra(USER_NAME);
        Bundle bundle = new Bundle();
        bundle.putInt(COURSE_INDEX, courseIndex);
        bundle.putString(USER_NAME, userName);
        materialListFragment.setArguments(bundle);
        transaction.replace(R.id.main_FRAME_list, materialListFragment);
        transaction.addToBackStack(null);//calling addtobackstack before commit, allows going back to what was before that commit when calling popbackstack.
        transaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void initFragments()
    {
        courseListFragment = new CourseListFragment();
        courseListFragment.setMaterialFragmentCallback(courseIndex -> swapFragment(courseIndex));
        materialListFragment = new MaterialListFragment();
    }

    private void initViews()
    {
        Intent intent = getIntent();
        main_LBL_title.setText("Hello, " + intent.getStringExtra(USER_NAME) + "!");
    }

    private void findViews()
    {
        main_LBL_title = findViewById(R.id.main_LBL_title);
    }
}