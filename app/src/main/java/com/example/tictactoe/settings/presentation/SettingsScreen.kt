package com.example.tictactoe.settings.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
// dependencies to make remember/observeAsState work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
// dependencies to make remember/observeAsState work
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.core.domain.AIDifficulty
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.core.domain.GameModes
import com.example.tictactoe.core.navigation.NavRoutes
import com.example.tictactoe.settings.presentation.components.BoardSizeSetter
import com.example.tictactoe.settings.presentation.components.PlayerProfile
import com.example.tictactoe.settings.presentation.components.TitledSettingsBlock

@Composable
fun SettingsScreen(navController: NavController, coreViewModel: CoreViewModel) {
    var scrollState = rememberScrollState()
    val players by coreViewModel.players.observeAsState(emptyList())
    val gameMode = coreViewModel.gameMode.observeAsState()
    val aiDifficulty by coreViewModel.aiDifficulty.observeAsState()
    val boardDimension by coreViewModel.boardDimensions.observeAsState()
    val errorMsg by coreViewModel.errorMsg.observeAsState()
    val localCtx = LocalContext.current

    /**
     * This shows a toast whenever errorMsg changes and the value is not null
     *
     * [Uses compose side-effects](https://developer.android.com/develop/ui/compose/side-effects)
     */
    errorMsg?.let {
        LaunchedEffect(it) {
            Toast.makeText(localCtx, it, Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(10.dp)
        ) {
            Column {
                Text(text = "NoughtsXCrosses", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(text = "Customize your fame experience to your hearts content")
                Spacer(modifier = Modifier.height(20.dp))
                Column(modifier = Modifier.fillMaxWidth(1.0f)) {
                    players.forEachIndexed { idx, plyr ->
                        PlayerProfile(
                            imageResourceId = plyr.avatar,
                            // hide make AI radio, the first player must always be human
                            showAIRadio = idx != 0,
                            name = plyr.name,
                            isAI = plyr.isAI,
                            makeAI = { coreViewModel.makeAI(idx) },
                            generateRandomName = { coreViewModel.setRandomName(idx) }) { it ->
                            coreViewModel.updatePlayerName(
                                idx,
                                it
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                // `::` - callable reference or class reference
                TitledSettingsBlock<GameModes>(
                    "GAME MODE SELECTION",
                    GameModes::class.java,
                    gameMode.value,
                    onSelect = coreViewModel::setGameMode
                )

                Spacer(modifier = Modifier.height(20.dp))
                BoardSizeSetter(
                    boardSize = boardDimension.toString(),
                    onDimensionChangeHandler = coreViewModel::changeDimension
                )

                Spacer(modifier = Modifier.height(20.dp))

                // only display AI difficulty options, if at-least a single player is an AI
                if (players.any { it.isAI == true }) {
                    TitledSettingsBlock<AIDifficulty>(
                        "AI DIFFICULTY",
                        AIDifficulty::class.java,
                        aiDifficulty,
                        onSelect = coreViewModel::setAIDifficulty
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    modifier = Modifier.size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        // check if the board configurations are set, if so navigate to the actual board
                        val isGameValid =
                            coreViewModel.startGame(); if (isGameValid) navController.navigate(
                        NavRoutes.Board
                    ) else return@Button
                    }) {
                    Text("START", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


