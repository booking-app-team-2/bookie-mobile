# Bookie Mobile
Mobile app for Bookie.
# Contributors
- Darko Svilar SV50/2021
- Gojko Vučković SV49/2021
- Boris Markov SV73/2021
# Usage instructions
## Prerequisites
Clone the project from this repository.<br>
<br>
Clone the **[Bookie Backend](https://github.com/booking-app-team-2/bookie-backend)** project and run it.<br>
<br>
Download the Android SDK Platform Tools for your desired operating system.<br>
Connect your mobile device to your localhost device where the server is running.<br>
In your favourite shell, position yourself inside the downloaded directory or add the downloaded directory to PATH and run the command ```adb reverse tcp:8081 tcp8081```<br>
This is required in order to be able to connect to the server from your mobile device, as they are on different networks. With a USB connection you will be able to expose the first port in the command on your mobile device to the second port in the command on your localhost device.<br>
This command's effect will expire when you disconnect your mobile device from the localhost device.<br>
<br>
In your browser, head over to [Postman](https://www.postman.com), create a new workspace and, then, a new POST request to the ```localhost:8081/authentication/login``` route.<br>
In the request headers, add the ```User-Agent``` header with a value of ```Mobile-Android```.<br>
In the body of the request, select the ```raw``` body type and input JSON in the following form:
```json
{
  "username": "<desired_user_username>",
  "password": "<desired_user_password>"
}
```
This is a list of valid users by role that should suffice for testing the project:
- Guest
  ```json
  {
    "username": "darko@gmail.com",
    "password": "darko123"
  }
  ```
- Owner
  ```json
  {
    "username": "owner@gmail.com",
    "password": "owner123"
  }
  ```
- Admin
  ```json
  {
    "username": "bookie@gmail.com",
    "password": "bookie123"
  }
  ```
Now, copy the content of the ```jwt``` field in the response into the ```app.src.main.java.ftn.booking_app_team_2.bookie.clients.ClientUtils.JWT``` constant.<br>
This will ensure that the session of the user you logged in as lasts for a day. Plenty of time for testing.
## Usage
Run the app on your mobile device or emulator.<br>
On the login screen, press the login button. You will be redirected to the landing page of the user you logged in as via Postman.<br>
Use the app as regular.<br>
### Note
If you change any data that needs reverting during the testing of the app, just rerun the server. The server drops and recreates the DB schema every time it is rerun and has a test script setup that inserts the same test data upon running the app.
