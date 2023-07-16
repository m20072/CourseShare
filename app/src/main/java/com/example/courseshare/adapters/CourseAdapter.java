package com.example.courseshare.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.courseshare.models.Course;
import com.example.courseshare.R;
import com.example.courseshare.interfaces.CourseCallback;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> implements Filterable
{
    private ArrayList<Course> courses;
    private ArrayList<Course> filteredCourses;
    private CourseCallback courseCallback;
    public CourseAdapter(ArrayList<Course> courses) {this.courses = courses; filteredCourses = courses;}
    //?must have filtered courses equated to courses on creation, otherwise it can disappear suddenly sometimes when backing from material fragment?
    @Override
    public Filter getFilter()
    {
        Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults filterResults = new FilterResults();
                if(charSequence == null || charSequence.length() == 0) //edit text is empty.
                {
                    filterResults.values = filteredCourses;
                    filterResults.count = filteredCourses.size();
                }
                else //edit text not empty
                {
                    String searchStr = charSequence.toString().toLowerCase();
                    ArrayList<Course> coursesTmp = new ArrayList<>(); //will add only items that contain same subname or subcode
                    for(Course course: filteredCourses)
                    {
                        if(course.getName().toLowerCase().contains(searchStr) || Integer.toString(course.getCode()).toLowerCase().contains(searchStr))
                        {
                            coursesTmp.add(course);
                        }
                    }

                    filterResults.values = coursesTmp;
                    filterResults.count = coursesTmp.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                courses = (ArrayList<Course>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public void setCourseCallback(CourseCallback courseCallback)
    {
        this.courseCallback = courseCallback;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        CourseViewHolder courseViewHolder = new CourseViewHolder(view);
        return courseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position)
    {
        Course course = getItem(position);
        holder.course_LBL_courseName.setText(course.getName());
        holder.course_LBL_courseCode.setText("code: " + course.getCode() + "");
        holder.course_LBL_materialCount.setText("count: " + course.getCount() + "");
    }

    @Override
    public int getItemCount() {return this.courses == null ? 0 : this.courses.size();}

    private Course getItem(int position)
    {
        return this.courses.get(position);
    }

    public void updateCourseList(ArrayList<Course> courses)
    {
        this.courses = courses;
        this.filteredCourses = courses;
        notifyDataSetChanged();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder
    {
        private final MaterialTextView course_LBL_courseName;
        private final MaterialTextView course_LBL_courseCode;
        private final MaterialTextView course_LBL_materialCount;

        public CourseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            course_LBL_courseName = itemView.findViewById(R.id.course_LBL_courseName);
            course_LBL_courseCode = itemView.findViewById(R.id.course_LBL_courseCode);
            course_LBL_materialCount = itemView.findViewById(R.id.course_LBL_materialCount);

            itemView.setOnClickListener(v ->
            {
                if (courseCallback != null)
                    courseCallback.itemClicked(getAdapterPosition());
            });
        }
    }

}
