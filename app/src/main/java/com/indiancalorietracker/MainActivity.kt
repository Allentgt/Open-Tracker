package com.indiancalorietracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.auth0.android.provider.WebAuthProvider
import com.indiancalorietracker.service.Auth0Service
import com.indiancalorietracker.ui.navigation.AppNavigation
import com.indiancalorietracker.ui.theme.IndianCalorieTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var auth0Service: Auth0Service
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndianCalorieTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle Auth0 callback URL
        intent?.data?.let { uri ->
            if (uri.toString().startsWith("com.indiancalorietracker://")) {
                WebAuthProvider.resume(intent)
                // Refresh auth state after callback
                auth0Service.refreshAuthState()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh auth state when resuming (in case user just logged in)
        auth0Service.refreshAuthState()
    }
}
