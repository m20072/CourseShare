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

- **Material List within Courses with File Information**: Within each course, CourseShare utilizes another RecyclerView to display the material list. This feature provides additional information for each material, including the size and name of the file, as well as the name of the uploading user.

- **Material Count for Courses**: CourseShare includes a counter that specifies the number of materials within each course. This feature helps users get an overview of the amount of study materials available within a particular course.

- **CourseList Fragment and ViewModel**: CourseShare includes a CourseListFragment that displays the course list. The fragment is associated with a corresponding ViewModel class, enabling separation of concerns and efficient data handling.

- **MaterialList Fragment and ViewModel**: CourseShare includes a MaterialListFragment that displays the material list within each course. The fragment is associated with a corresponding ViewModel class, ensuring proper data management and interaction.

- **Dialog for Adding a Course**: CourseShare provides a dialog that opens when adding a new course. Users are required to fill in the course code and course name fields in the dialog before they can continue adding the course.

- **Dialog for Adding a Material**: CourseShare includes a dialog that opens when adding a new material. Users must provide the material name and choose the corresponding file for the material in the dialog. The fields in the dialog must be filled before the material can be added.

- **Navigation between CourseList and MaterialList Fragments**: CourseShare allows users to navigate from the CourseListFragment to the MaterialListFragment using a callback to the MainActivity. This enables seamless transitions and a smooth user experience.

- **Back Button Navigation**: CourseShare supports back button navigation, allowing users to move from the MaterialListFragment back to the CourseListFragment using the back button provided by the Android operating system.

- **Efficient File Downloading**: CourseShare leverages Android's DownloadManager to streamline the process of downloading study materials from Firebase Storage. This feature ensures that users can easily retrieve the files they need for offline access.

