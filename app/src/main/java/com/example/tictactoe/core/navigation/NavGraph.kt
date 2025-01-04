package com.example.tictactoe.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.tictactoe.board.presentation.BoardScreen
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.settings.presentation.SettingsScreen
import kotlinx.serialization.Serializable

/**
 * object declaration, storing containing all navigation route keys
 */
object NavRoutes {
    @Serializable
    object Settings

    @Serializable
    object Board
}

@Composable
fun NavGraph(modifier: Modifier, navController: NavHostController, coreViewModel: CoreViewModel) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavRoutes.Settings
    ) {
        composable<NavRoutes.Settings> {
            SettingsScreen(navController, coreViewModel)
        }
        composable<NavRoutes.Board> {
            BoardScreen(navController, coreViewModel)
        }
    }
}