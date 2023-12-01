# NYC_JPMC_CodingChallenge-
Coding challenge from JPMC to consume RESTful endpoints and display required data with Jetpack Compose and Navigation Components along with Hilt &amp; MVVM.


## Assumptions:

* The School SAT scores are found by "dbn" field from School's List API.
* Displaying the Required Details like School name, Email, Phone Number, Total students and Location on home page.
* If the **Internet goes down** then the next screen would be recomposed and would be prompted to retry for after active internet Connection.
* Created Cards for both Portrait and Landscape views in mind.
* Implemented **Search functionality** with filtering Cities 
* Schools SAT Card design accompanies content scrolling incase if data is overgrown from the card layout.
* Both **HomeScreen** and **SchoolDetails** Screens support "No Internet Connection" with a retry button to get the data when online.
* Implemented **MVVM**.
* Implemented **Hilt Dependency Injection**.
* Implemented **Unit test** cases for View Model, repository's and Api Service calls.
* Implemented with **Kotlin** and used kotlin tools.
* Implemented **Jetpack Compose** Ui Toolkit.
* Error Handling for **Repository's** and **View Model**.


## Screen recording for the application:
https://github.com/Praneeth-10/NYC_JPMC_CodingChallenge-/blob/master/Screenrecorder(1).mp4

****


## Attached the sample build apk file to test the android app on physical device:
https://github.com/Praneeth-10/NYC_JPMC_CodingChallenge-/blob/master/NYCSchools-Test.apk
