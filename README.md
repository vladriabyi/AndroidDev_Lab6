## Fitness Tracker App

This application allows users to track their daily exercises, manage workout data, and view summaries on the home screen via a widget.

### Author

Vladyslav Riabyi
Group: IM-24

### Description

The Fitness Tracker App is an Android application designed to help users log and monitor their physical activities. Key functionalities include:

*   Tracking exercises for specific dates.
*   Adding new exercises with details like repetitions, sets, and duration.
*   Selecting exercises from predefined categories (Cardio, Strength, Flexibility, Balance).
*   Option to add custom exercise names.
*   Viewing exercises added on a selected date.
*   Deleting exercises.
*   A home screen widget displaying the total number of exercises logged for the current day and the name of the last added exercise.

The app utilizes a local database to persist exercise data, providing a simple yet effective way to keep track of workouts.

### Technical Details

The application is built using:

*   Android (Kotlin)
*   Jetpack Compose for building the user interface.
*   Room Database for local data persistence.
*   Coroutines for asynchronous operations (database access, widget updates).
*   App Widgets for displaying daily exercise summaries on the home screen.

### How to Run

1.  Clone the repository to your local machine:
    ```bash
    git clone https://github.com/vladriabyi/AndroidDev_Lab6.git
    ```
2.  Open the project in Android Studio.
3.  Build and run the application on an Android emulator or a physical device.

### Dependencies

*   Android SDK
*   Jetpack Compose Libraries
*   AndroidX Room (runtime, compiler, ktx)
*   Kotlin Coroutines
*   Android App Widget Libraries

### Features

*   Date-based exercise logging.
*   Categorized predefined exercises.
*   Custom exercise entry.
*   Exercise details (reps, sets, duration).
*   Add and delete exercises.
*   Daily exercise summary widget.

### Usage

1.  Launch the application.
2.  Use the calendar interface to select a date.
3.  Tap the "+" button to add a new exercise.
4.  Choose an exercise from a category or enter a custom name.
5.  Fill in the details (Reps, Sets, Min).
6.  Tap "Add" to save the exercise.
7.  View the list of added exercises for the selected date.
8.  Add the Fitness Tracker widget to your home screen to see a daily summary.

### Notes

*   Exercise data is stored locally on the device.
*   The widget updates periodically and when exercises are added or deleted within the app.
*   Requires access to the device's storage for the database.
