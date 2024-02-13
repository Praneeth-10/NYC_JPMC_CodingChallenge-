package com.example.nycschools.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nycschools.models.schools.SchoolListItem
import com.example.nycschools.utils.Utils
import com.example.nycschools.viewmodel.SchoolsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    schoolsViewModel: SchoolsViewModel = hiltViewModel()
) {
    val context = LocalContext.current // Getting current context for Internet Connectivity check
    val checkInternet =
        remember { mutableStateOf(checkInternetConnectivity(context)) } // Checking internet connectivity
    val retryTrigger =
        remember { mutableIntStateOf(0) } // Trigger for retrying after active internet connectivity
    val searchQuery = schoolsViewModel.searchQuery

    // Launching an effect to check internet connectivity and get the list if internet is available
    LaunchedEffect(retryTrigger.intValue) {
        checkInternet.value = checkInternetConnectivity(context)
        if (checkInternet.value) {
            schoolsViewModel.setLoading(true)
            schoolsViewModel.getNYCSchoolsList()
        } else {
            schoolsViewModel.setLoading(true)
            delay(3000) // Delay of 3 seconds before showing the retry button
            schoolsViewModel.setLoading(false)
        }
    }

    // If internet is available, displaying the list of schools, else displaying the message with retry option
    if (checkInternet.value) {
        Surface {
            Column {
                Header() // Display the header
                IndeterminateCircularIndicator(remember { schoolsViewModel.loadingIndicatorState }) // Display loading indicator while fetching data
                TextField(
                    value = searchQuery.value,
                    onValueChange = { newValue -> searchQuery.value = newValue },
                    label = { Text("Search by city") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(1f)
                ) // Add a spacer for better UI
                Column(modifier = Modifier.fillMaxHeight(1f)) {
                    LazyColumn {

                        items(schoolsViewModel.filteredSchoolList.value.size) { index ->
                            SchoolListItem(schoolItem = schoolsViewModel.filteredSchoolList.value[index],
                                onPhoneNumberClick = {
                                    //To do a phone call
                                    navController.context.startActivity(
                                        Intent.createChooser(
                                            Intent(
                                                Intent.ACTION_CALL,
                                                Uri.parse("tel:+1$it")
                                            ), "NYC Schools"
                                        )
                                    )
                                }
                            ) { item ->
                                navController.currentBackStackEntry?.savedStateHandle?.
                                    set(Utils.NavUtils.SCHOOL_DBN, item)

                                navController.navigate(Utils.ScreenUtils.SCHOOL_INFO)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(1f)
                    )
                }
            }
        }
    } else {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.LightGray) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IndeterminateCircularIndicator(remember { schoolsViewModel.loadingIndicatorState }) // Display loading indicator while fetching data
                Text(
                    text = "No Internet connection",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { retryTrigger.intValue++ }) { // Retry button to check internet connectivity again
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
fun IndeterminateCircularIndicator(loadingIndicatorState: MutableState<Boolean>) {
    if (!loadingIndicatorState.value) return // If not loading, return without displaying anything
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview
@Composable
fun PreviewIndeterminateCircularIndicator() {
    val loadingIndicatorState = remember { mutableStateOf(true) }
    IndeterminateCircularIndicator(loadingIndicatorState)
}

// Function to check internet connectivity. Returns true if either Wi-Fi or Cellular network is available.
fun checkInternetConnectivity(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

@Composable
fun SchoolListItem(
    schoolItem: SchoolListItem, onPhoneNumberClick: (phoneNumber: String) -> Unit,
    onSchoolItemClick: (schoolItem: SchoolListItem) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp // Get the current screen width
    val paddingWidth = screenWidth * 0.05f // Calculate the padding width

    // Create a card for each school item
    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(horizontal = paddingWidth, vertical = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = BorderStroke(2.dp, Color.Gray),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFADD8E6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .clickable {
                    onSchoolItemClick.invoke(schoolItem) // Invoking the click action when the school item is clicked
                }
                .padding(horizontal = 5.dp, vertical = 5.dp)
        ) {//Row alignment of School Data with Icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                SetIcon(imgVector = Icons.Default.Home, description = "School Name")
                Spacer(Modifier.width(5.dp))
                Text(
                    //Displaying Placeholder if data is null or empty
                    text = if (schoolItem.school_name.isEmpty()) "No School Found" else " ${schoolItem.school_name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = if (schoolItem.school_name.isEmpty()) Color.Red else Color.Black,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(1f),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SetIcon(imgVector = Icons.Default.Email, description = "Email ID")
                Spacer(Modifier.width(5.dp))
                Text(
                    //Displaying Placeholder if data is null or empty and with Red color
                    text = if (schoolItem.school_email.isNullOrEmpty()) "No Email Found" else " ${schoolItem.school_email}",
                    color = if (schoolItem.school_email.isNullOrEmpty()) Color.Red else Color.Black,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SetIcon(imgVector = Icons.Default.Phone, description = "Phone Number")
                Spacer(Modifier.width(5.dp))
                Text(//Displaying Placeholder if data is null or empty
                    text = if (schoolItem.phone_number.isNullOrEmpty()) "No Phone Found" else " ${schoolItem.phone_number}",
                    color = Color.Blue,
                    fontSize = 16.sp,
                    style = TextStyle(textDecoration = TextDecoration.Underline),
                    modifier =
                    if (schoolItem.phone_number.isNullOrEmpty())
                        Modifier
                            .border(2.dp, Color.Red, RoundedCornerShape(5.dp))
                            .padding(horizontal = 3.dp, vertical = 2.dp)
                            .background(Color(0xFFFFFFFF))
                    else
                        Modifier
                            .clickable { onPhoneNumberClick.invoke(schoolItem.phone_number) } // Invoke the click action when the phone number is clicked
                            .border(2.dp, Color.Black, RoundedCornerShape(5.dp))
                            .padding(horizontal = 3.dp, vertical = 2.dp)
                            .background(Color(0xFFB2C6D5))
                )
                Spacer(Modifier.width(90.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SetIcon(imgVector = Icons.Default.Person, description = "Total Students")
                    Spacer(Modifier.width(2.dp))
                    Text(//Displaying Placeholder if data is null or empty
                        text = if (schoolItem.total_students.isNullOrEmpty()) "N/A" else schoolItem.total_students,
                        color = if (schoolItem.total_students.isNullOrEmpty()) Color.Red else Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.End) {
                SetIcon(Icons.Default.LocationOn, "Location")
                Spacer(Modifier.width(5.dp))
                Text(//Displaying Placeholder if data is null or empty
                    text = if (schoolItem.city.isNullOrEmpty()) "N/A," else " ${schoolItem.city},",
                    color = if (schoolItem.city.isNullOrEmpty()) Color.Red else Color.Black,
                    fontSize = 16.sp
                )
                Text(//Displaying Placeholder if data is null or empty
                    text = if (schoolItem.state_code.isNullOrEmpty()) "N/A" else " ${schoolItem.state_code}",
                    color = if (schoolItem.state_code.isNullOrEmpty()) Color.Red else Color.Black,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun SetIcon(imgVector: ImageVector, description: String) {
    Icon(
        imageVector = imgVector,
        contentDescription = description,
        tint = Color.Gray
    )
}