package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.core.navigation.NavGraph
import com.example.tictactoe.ui.theme.TictactoeTheme

class MainActivity : ComponentActivity() {
    /**
     *Initializing the view model
     *
     * [kotlin working with livedata](https://developer.android.com/topic/libraries/architecture/livedata#kotlin)
     * */
    private val coreViewModel: CoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TictactoeTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        navController = navController,
                        coreViewModel = coreViewModel
                    )
                }
            }
        }
    }
}