
# Weather Demo

This is a demo weather application for an assignment which showcases the use of clean architecture with MVVM, jetpack compose, Hilt, Unit Test cases.
This project makes use of https://openweathermap.org/ APIs to fetch the current weather and display it to the user while storing the weather history in the local db. This project also makes use of local db to make an authentication system that allows users to register and then login with the registered credentials.


## Tech Stack
The architecture used inside the project is MVVM clean architecture.

Android studio version - Android Studio Koala | 2024.1.1 Patch 2

Programming Language - Kotlin

Devices Supported - Android version 7.0.0 and up
## Project Structure

The project structure follows a clean architecture. This is the folder structure.
![PS](https://i.ibb.co/fvZPYvN/Screenshot-2024-10-09-at-6-17-51-AM.png)
## API Key and Cipher Key
This project makes use of https://openweathermap.org/ as previously mentioned. That API requires an API key to be used. Make sure you put your API key inside the gradle.properties file as API_KEY="YOUR_KEY", else your API call would fail.
Also this project encrypts all the data saved to the local DB so a CIPHER_KEY is also required which is stored in the same location as API_KEY. Please add that as well in gradle.properties file.
## Unit test
We have a unit test coverage of more than 95% which includes all of our viewmodels, usecases, utility classes etc.
![TEST_CASES](https://i.ibb.co/CK6LdZt/Untitled-design.png)
## App Screenshots
![1](https://i.ibb.co/HhGTmzY/Screenshot-2024-10-08-17-36-20-316-com-example-weatherapp.jpg)
![2](https://i.ibb.co/Dw8f85P/Screenshot-2024-10-08-17-36-33-100-com-example-weatherapp.jpg)
![3](https://i.ibb.co/rFNPX3W/Screenshot-2024-10-08-17-36-37-399-com-example-weatherapp.jpg)
![Screenshot_2024-10-09-08-16-18-740_com example weatherapp](https://github.com/user-attachments/assets/ecb8bf08-c290-414d-80e3-58478a36c657)
![Screenshot_2024-10-09-08-16-13-103_com example weatherapp](https://github.com/user-attachments/assets/e6979196-f0cf-4877-b1e9-7462a7071170)

