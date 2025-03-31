package com.am.zviryata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.am.zviryata.repository.GameRepository
import com.am.zviryata.services.AdManager
import com.am.zviryata.ui.theme.LevelScreen
import com.am.zviryata.ui.theme.MenuScreen
import com.am.zviryata.ui.theme.ZviryataTheme
import com.am.zviryata.viewmodel.GameViewModel
import com.am.zviryata.viewmodel.GameViewModelFactory
import com.google.android.gms.ads.MobileAds

/**
 * Main entry point of the application.
 * Initializes ads and sets up the Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {} // Initialize Google Mobile Ads

        setContent {
            ZviryataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * Sets up navigation for the app using Jetpack Compose Navigation.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(GameRepository(LocalContext.current), AdManager(context))
    )

    NavHost(navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(navController, gameViewModel)
        }
        composable("level/{levelId}") { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId")?.toIntOrNull() ?: 1
            LevelScreen(
                viewModel = gameViewModel,
                levelId = levelId,
                onBack = { navController.popBackStack() },
                onNext = { nextLevelId ->
                    if (nextLevelId == 0) { // Game completed
                        navController.navigate("menu") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    } else {
                        navController.navigate("level/$nextLevelId") {
                            popUpTo("menu") { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}


//1000
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

        setContent {
            ZviryataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory(GameRepository(LocalContext.current)))

    NavHost(navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(navController, gameViewModel)
        }
        composable("level/{levelId}") { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId")?.toIntOrNull()
                ?: 1
            LevelScreen(
                viewModel = gameViewModel,
                levelId = levelId,
                onBack = { navController.popBackStack() },
                onNext = { nextLevelId ->
                    if (gameViewModel.gameRepository.getLevel(nextLevelId) != null) {
                        navController.navigate("level/$nextLevelId") {
                            popUpTo("menu") { inclusive = false }
                        }
                    } else {
                        navController.navigate("menu") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
*/
