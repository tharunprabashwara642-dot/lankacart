package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.presentation.navigation.LankaJobsAppNavigation
import com.example.ui.theme.LankaJobsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as LankaJobsApp
        val container = app.container

        setContent {
            LankaJobsAppTheme {
                LankaJobsAppNavigation(container = container)
            }
        }
    }
}
