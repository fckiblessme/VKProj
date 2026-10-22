package com.kitchino.vkproj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kitchino.vkproj.model.AppRepository
import com.kitchino.vkproj.ui.screens.AppDetailScreen
import com.kitchino.vkproj.ui.screens.AppListScreen
import com.kitchino.vkproj.ui.theme.VKProjTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKProjTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "app_list") {
                    composable("app_list") {
                        AppListScreen(onAppClick = { app ->
                            navController.navigate("app_detail/${app.id}")
                        })
                    }
                    composable(
                        route = "app_detail/{appId}"
                    ) { backStackEntry ->
                        val appId = backStackEntry.getString()?.getInt("appId") ?: 1
                        val app = AppRepository.sampleApps.firstOrNull { it.id == appId }
                            ?: AppRepository.sampleApps[0]
                        AppDetailScreen(
                            app = app,
                            onBackClick = { navController.popBackStack() },
                            onShowcaseAppClick = { navController.navigate("app_list") }
                        )
                    }
                }
            }
        }
    }

    private fun String?.getInt(key: String): Int? {
        return this?.split("/")?.lastOrNull()?.toIntOrNull()
    }
}