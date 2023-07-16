# CourseShare

<p align="center">
  <img src="https://i.imgur.com/d7Aiyrs.png" alt="CourseShare Logo" width="300">
</p>

CourseShare is an Android application designed to facilitate the sharing of study materials among students. It provides an easy-to-use and lightweight platform for students to access and contribute to a wide range of course materials.

## Features

CourseShare offers the following key features:

- **User Authentication**: CourseShare utilizes Firebase Authentication to enable secure user registration and login, ensuring that only authorized users can access the application.

- **Material Upload and Storage**: With CourseShare, users can effortlessly upload and store study materials. The application leverages Firebase Storage to securely store these materials, making them easily accessible to other users.

- **Course Management**: CourseShare incorporates Firebase Realtime Database to store course objects and their respective material lists. This feature allows users to organize materials by course, making it simple to navigate and search for specific study resources.

- **Course List Display with Filtering**: CourseShare implements a RecyclerView to display a list of available courses. Users can easily browse through the list and filter it to search for a specific course or course code. This filtering feature helps users quickly find the desired course based on keywords, course names, or course codes.

- **Material List within Courses**: Within each course, CourseShare utilizes another RecyclerView to display the material list. This feature allows users to quickly browse and access the study materials specific to a particular course.

- **Efficient File Downloading**: CourseShare leverages Android's DownloadManager to streamline the process of downloading study materials from Firebase Storage. This feature ensures that users can easily retrieve the files they need for offline access.
