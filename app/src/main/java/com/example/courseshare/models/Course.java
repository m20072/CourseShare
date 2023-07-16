package com.example.courseshare.models;

import java.util.ArrayList;

public class Course
{
    private String name;
    private int code;
    private int count;
    private ArrayList<Material> materials = new ArrayList<>();

    public Course(){}

    public String getName() {return name;}

    public Course setName(String name)
    {
        this.name = name;
        return this;
    }

    public ArrayList<Material> getMaterials()
    {
        return materials;
    }

    public int getCode()
    {
        return code;
    }

    public Course setCode(int code)
    {
        this.code = code;
        return this;
    }

    public int getCount()
    {
        return count;
    }

    public Course increaseCount()
    {
        this.count++;
        return this;
    }
}
