package com.kitchino.vkproj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kitchino.vkproj.model.App
import com.kitchino.vkproj.ui.screens.AppDetailsScreen
import com.kitchino.vkproj.ui.screens.AppListScreen
import com.kitchino.vkproj.ui.theme.VKProjTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedApp by remember { mutableStateOf<App?>(null) }
            VKProjTheme {
                var currentApp = selectedApp
                if (currentApp != null) {
                    AppDetailsScreen(app = currentApp, onBack = {selectedApp = null})
                } else {
                    AppListScreen(onAppClick  = { selectedApp = it})
                }
            }
        }
    }
}