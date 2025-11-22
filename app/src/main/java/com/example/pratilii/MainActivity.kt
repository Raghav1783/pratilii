package com.example.pratilii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.pratilii.presentation.appRoute
import com.example.pratilii.presentation.detail.DetailScreenViewModel
import com.example.pratilii.presentation.detail.MailDetailScreen
import com.example.pratilii.presentation.home.MailListScreen
import com.example.pratilii.ui.theme.PratiliiTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PratiliiTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = appRoute.Home.route
                ) {
                    composable(
                        route = appRoute.Home.route
                    ) {
                        MailListScreen { mailId ->
                            navController.navigate(appRoute.MailDetail.withArgument(mailId.toString()))
                        }
                    }

                    composable(
                        route = appRoute.MailDetail.route,
                        arguments = appRoute.MailDetail.arguments
                    ) {
                        val viewModel: DetailScreenViewModel = hiltViewModel()
                        MailDetailScreen(viewModel)
                    }
                }
            }

        }

    }


}






