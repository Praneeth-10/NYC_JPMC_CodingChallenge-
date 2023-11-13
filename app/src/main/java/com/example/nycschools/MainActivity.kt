package com.example.nycschools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nycschools.ui.theme.NYCSchoolsTheme
import com.example.nycschools.utils.Utils
import com.example.nycschools.viewmodel.SchoolsViewModel
import com.example.nycschools.views.HomeScreen
import com.example.nycschools.views.IndeterminateCircularIndicator
import com.example.nycschools.views.SchoolDetailsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Setting up the main UI for the app
            NYCSchoolsTheme {
                // Creating a surface with the background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NYCNavigator()
                }
            }
        }
    }
}

@Composable
fun NYCNavigator(navController: NavHostController = rememberNavController(),schoolsViewModel: SchoolsViewModel = hiltViewModel()) {

    // Defining navigation destinations and their associated composables
    NavHost(navController = navController, startDestination = Utils.ScreenUtils.HOME_SCREEN) {
        // Composable for the Home Screen
        composable(Utils.ScreenUtils.HOME_SCREEN) {
            HomeScreen(navController)
        }
        // Composable for the School Details Screen
        composable(Utils.ScreenUtils.SCHOOL_INFO) {
            SchoolDetailsScreen(navHostController = navController)
        }
    }
}