package com.example.courseshare.models;

public class Material
{
    private String name;
    private int fileSize;
    private String fileType;
    private String uploadUser;
    private String fileRefPath;

    public Material(){}

    public String getName()
    {
        return name;
    }

    public Material setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getFileRefPath()
    {
        return fileRefPath;
    }

    public Material setFileRefPath(String fileRefPath)
    {
        this.fileRefPath = fileRefPath;
        return this;
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public Material setFileSize(int fileSize)
    {
        this.fileSize = fileSize;
        return this;
    }

    public String getFileType()
    {
        return fileType;
    }

    public Material setFileType(String fileType)
    {
        this.fileType = fileType;
        return this;
    }

    public String getUploadUser()
    {
        return uploadUser;
    }

    public Material setUploadUser(String uploadUser)
    {
        this.uploadUser = uploadUser;
        return this;
    }
}
