# Crime Management App - Android

This project is a **practice exercise** to learn and apply various technologies in the development of an Android application. In this app, crime records are managed, allowing the creation, visualization, and organization of crimes with date and time.

## Project Goals

- **Android Application** to manage crimes.
- Register crimes with details like date, time, and resolution.
- Display the list of crimes ordered by date and time of creation.
- **Practice using modern technologies** in mobile app development.

## Technologies Used

- **Kotlin**: The main programming language used in the app development.
- **Room Database**: To store the crime records locally in the app.
- **Coroutines**: For efficiently performing asynchronous tasks.
- **RecyclerView**: To display the list of crimes.
- **Navigation Component**: To manage navigation between screens.
- **ViewModel**: To handle the presentation logic and maintain the interface state.
- **LiveData**: To efficiently manage observable data.

## Features

- **Main screen**: Displays the list of crimes ordered by date and time.
- **Add crime**: Allows the addition of new crimes with date, time, and resolution status.
- **Date filtering**: Crimes of today are shown first, followed by older crimes ordered by date.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/VCL-tt/app-crime-tracker-android.git
    ```

2. Open the project in [Android Studio](https://developer.android.com/studio).

3. Run the app on a device or Android emulator.

## License

This project is licensed under the **MIT License**. For more details, check the LICENSE file.
