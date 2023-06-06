# Act of God

## Features

* User login
* Google Maps integration
* Directions to the selected location.

## Technologies and Libraries
* Android Studio - Development environment
* Java 17 - Programming language
* Retrofit - Library for making HTTP requests
* Google Maps API - Service used for location selection and directions

## Installation
* Install the backend and frontend first: [Act of God API](https://github.com/ahmettyavzz/ActOfGod_API) [Act of God API Frontend](https://github.com/azizCan10/ActOfGod_API_Frontend)
* Clone the repository: `git clone https://github.com/ahmettyavzz/ActOfGod_Android.git`
* Open Android Studio and import the project.
* AndroidManifest.xml on line 40;

```xml
android:value="YOUR_API_KEY" />
```
Replace YOUR_API_KEY with your own Google Maps API key.
* Add the following line to the local.properties file inside the project folder:
```properties
sdk.dir=/YOUR/ANDROID/SDK/PATH
```
* Replace /YOUR/ANDROID/SDK/PATH with the file path to your Android SDK.
* Run the application on an Android device or emulator.