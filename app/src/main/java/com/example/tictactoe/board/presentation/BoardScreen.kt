package com.example.tictactoe.board.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.core.navigation.NavRoutes


@Composable
fun BoardScreen(navController: NavController, coreViewModel: CoreViewModel) {
    val players = coreViewModel.players.observeAsState()
    val localCtx = LocalContext.current;
    val scrollState = rememberScrollState()

    /**
     * Navigate to the settings page if the player details are not available
     *
     * Uses compose side-effects
     *
     * [Side effects](https://developer.android.com/develop/ui/compose/side-effects)
     */
//    LaunchedEffect(players.value) {
//        if (players.value.isNullOrEmpty()) {
//            Toast.makeText(
//                localCtx,
//                "Player are required to start the game",
//                Toast.LENGTH_LONG
//            ).show()
//            navController.navigate(NavRoutes.Settings)
//        }
//    }

    Log.i("BoardScreen", "${players.value}")


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .border(1.dp, Color.Gray)
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text("Board Screen ${players.value}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column {
        Text("Some stuff")
    }
}