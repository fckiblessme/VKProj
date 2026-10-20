package com.kitchino.vkproj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kitchino.vkproj.ui.screens.AppListScreen
import com.kitchino.vkproj.ui.theme.VKProjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKProjTheme {
                AppListScreen()
            }
        }
    }
}