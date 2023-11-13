package com.example.nycschools.views


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nycschools.models.schools.SchoolListItem
import com.example.nycschools.models.schools.SchoolScoresItem
import com.example.nycschools.utils.Utils
import com.example.nycschools.viewmodel.SchoolsViewModel
import kotlinx.coroutines.delay

@Composable
fun SchoolDetailsScreen(
    navHostController: NavHostController,
    schoolsViewModel: SchoolsViewModel = hiltViewModel()
) {
    // Get the current context
    val context = LocalContext.current

    // Check internet connectivity and set it as a mutable state
    val checkInternet = remember { mutableStateOf(checkInternetConnectivity(context)) }

    // Create a trigger to recheck internet connectivity
    val retryTrigger = remember { mutableIntStateOf(0) }

    // Fetching Data from Home Screen
    val item = navHostController.previousBackStackEntry?.savedStateHandle?.get<SchoolListItem>(Utils.NavUtils.SCHOOL_DBN)

    // Check internet connectivity when the retryTrigger value changes
    LaunchedEffect(retryTrigger.intValue) {
        checkInternet.value = checkInternetConnectivity(context)

        // If there's internet connection, fetch school details based on DBN
        if (checkInternet.value) {
            schoolsViewModel.setLoading(true)
            schoolsViewModel.getSchoolDetails(item?.dbn ?: "")
        }
        else {
            schoolsViewModel.setLoading(true)
            delay(3000) // Delay of 3 seconds before showing the retry button
            schoolsViewModel.setLoading(false)
        }
    }

    // Checking if there's internet connectivity
    if (checkInternet.value) {
        // If there's an error in the ViewModel, displaying an error message
        if (schoolsViewModel.errorState.value != null) {
            Text(text = "An error occurred: ${schoolsViewModel.errorState.value?.message}")
        } else {
            // Display school details if there are no errors
            Surface(modifier = Modifier.fillMaxSize(1f)) {
                Column {
                    // Display a header
                    Header()
                    IndeterminateCircularIndicator(remember{schoolsViewModel.loadingIndicatorState}) // Display loading indicator while fetching data
                    // Display school information using the schoolScoresItem from the ViewModel
                    if (item != null) {
                        SchoolInfo(schoolScoresItem = schoolsViewModel.schoolDetails.value, schoolListItem = item)
                    }
                }
            }
        }
    } else {
        // Display a message when there's no internet connectivity
        Surface(modifier = Modifier.fillMaxSize(), color = Color.LightGray) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IndeterminateCircularIndicator(remember{schoolsViewModel.loadingIndicatorState}) // Display loading indicator while fetching data
                Text(
                    text = "No Internet connection",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { retryTrigger.intValue++ }) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
fun SchoolInfo(schoolScoresItem: SchoolScoresItem, schoolListItem: SchoolListItem, schoolsViewModel: SchoolsViewModel = hiltViewModel()) {
    // Getting the screen dimensions for customization
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // Defining padding width and height as a fraction of the screen dimensions
    val paddingWidth = screenWidth * 0.07f
    val paddingHeight = screenHeight * 0.1f

    // Context for opening Webpage
    val context = LocalContext.current
    val urlWebsite = schoolListItem.website

    // Creating a Card with custom styling
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingWidth, paddingHeight)
            .background(Color(0xFF8BBCD6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)) {

            // Displaying the school name with styling
            Text(
                text = schoolListItem.school_name,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = urlWebsite,
                color = Color.Blue,
                fontSize = 16.sp,
                style = MaterialTheme.typography.headlineSmall.copy(textDecoration = TextDecoration.Underline),
                modifier = Modifier
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://$urlWebsite"))
                        context.startActivity(intent)
                    }
                    .align(alignment = Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(3.dp) )

            // Adding a divider line with styling
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)

            // Displaying school-related information using TextWithLabel Composable
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = schoolListItem.overview_paragraph,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                    color = if (schoolListItem.overview_paragraph == "No Data Found") Color.Red else MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(8.dp))
                TextWithLabel("Neighborhood :", schoolListItem.neighborhood)
                Spacer(Modifier.height(8.dp))
                TextWithLabel("No Of TestTaker :", schoolScoresItem.num_of_sat_test_takers)
                Spacer(Modifier.height(8.dp))
                TextWithLabel("Avg. Critical Reading :", schoolScoresItem.sat_critical_reading_avg_score)
                Spacer(Modifier.height(8.dp))
                TextWithLabel("Avg. Math :", schoolScoresItem.sat_math_avg_score)
                Spacer(Modifier.height(8.dp))
                TextWithLabel("Avg. Writing :", schoolScoresItem.sat_writing_avg_score)
            }
        }
    }
}

@Composable
fun TextWithLabel(label: String, text: String) {
    // Displaying a row with a label and text
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Displaying the label with styling
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(Modifier.width(8.dp))
        // Displaying the text with styling, with special cases (e.g., "No Data Found")
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
            color = if (text == "No Data Found") Color.Red else MaterialTheme.colorScheme.secondary
        )
    }
}

