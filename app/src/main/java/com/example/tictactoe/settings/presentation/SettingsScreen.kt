package com.example.tictactoe.settings.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
// dependencies to make remember/observeAsState work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
// dependencies to make remember/observeAsState work
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.core.domain.AIDifficulty
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.core.domain.DimensionAction
import com.example.tictactoe.core.domain.GameModes
import com.example.tictactoe.ui.theme.Slate20

@Composable
fun SettingsScreen(navController: NavController, coreViewModel: CoreViewModel) {
    var scrollState = rememberScrollState()
    val players by coreViewModel.players.observeAsState(emptyList())
    val gameMode = coreViewModel.gameMode.observeAsState()
    val aiDifficulty by coreViewModel.aiDifficulty.observeAsState()
    val boardDimension by coreViewModel.boardDimensions.observeAsState()
    val errorMsg by coreViewModel.errorMsg.observeAsState()

    gameMode.value.let { Log.i("SettingsScreen", "game mode changed: $gameMode") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            Button(onClick = {
            }) {
                Text("Go to Second Screen")
            }

            Column {
                Text(text = "NoughtsXCrosses", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(text = "Customize your fame experience to your hearts content")
                Spacer(modifier = Modifier.height(20.dp))
                Column(modifier = Modifier.fillMaxWidth(1.0f)) {
                    players.forEachIndexed { idx, plyr ->
                        PlayerProfile(
                            imageResourceId = plyr.avatar,
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
                    onClick = { Log.i("SettingsScreen", "game validation") }) {
                    Text("START", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PlayerProfile(
    imageResourceId: Int,
    name: String,
    isAI: Boolean,
    makeAI: () -> Unit,
    generateRandomName: () -> Unit,
    onNameChangeHandler: (String) -> Unit
) {

    val imagePainter: Painter = painterResource(id = imageResourceId)
    val toastCtx = LocalContext.current
    val maxLength: Int = 20

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(
                    RoundedCornerShape(40.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Player profile image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Slate20
                    )
            )
        }
        Column(modifier = Modifier.fillMaxWidth(0.96f)) {
            TextField(
                value = name,
                onValueChange = { it ->
                    if (it.length >= 20) {
                        Toast.makeText(
                            toastCtx,
                            "Input exceeds $maxLength characters!",
                            Toast.LENGTH_LONG
                        ).show()
                        // prevent further input of text
                        return@TextField
                    }
                    onNameChangeHandler(it)
                },
                label = { Text("Enter text") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isAI,
                        // invoke the lambda
                        onClick = { makeAI.invoke() }
                    )
                    Text("Make AI?")
                }

                Button(
                    // invoke the lambda
                    onClick = { generateRandomName.invoke() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Generate Name", fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}


@Composable
fun SettingsSelectionBox(
    label: String,
    isSelected: Boolean = false,
    onSelectionHandler: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(Color.White)
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (isSelected) {
                    Modifier
                        .background(Slate20)
                        .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                } else {
                    Modifier
                }
            )
            .clickable { onSelectionHandler.invoke() }
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SettingsTitleText(label: String) {
    Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
}

/**
 * Composable rendering GameModes & AI Difficult blocks
 *
 * Used a generic ensuring composable reusability without having to battle with the glorious, all-seeing kotlin compiler 🙃.
 *
 * `T` is the generic, `Enum` base class for all enums and `Class` exclusively only allows passing the Java class of the enum
 * */
@Composable
fun <T> TitledSettingsBlock(
    title: String,
    options: Class<T>,
    selectedOption: T?,
    onSelect: (T) -> Unit
) where T : Enum<T> {
    SettingsTitleText(title)
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        options.enumConstants?.forEach {
            SettingsSelectionBox(
                label = it.name.replace('_', ' '),
                it == selectedOption, onSelectionHandler = { onSelect(it) }
            )
        }
    }
}

@Composable
fun BoardSizeSetter(boardSize: String, onDimensionChangeHandler: (DimensionAction) -> Unit) {
    SettingsTitleText("BOARD SIZE")
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(modifier = Modifier.weight(1f), onClick = {
            onDimensionChangeHandler.invoke(
                DimensionAction.DECREMENT
            )
        }, shape = RoundedCornerShape(5.dp)) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Increase board dimension",
                tint = Slate20
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = boardSize,
            modifier = Modifier.weight(3f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.width(10.dp))
        Button(modifier = Modifier.weight(1f), onClick = {
            onDimensionChangeHandler.invoke(
                DimensionAction.INCREMENT
            )
        }, shape = RoundedCornerShape(5.dp)) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase board dimension",
                tint = Slate20
            )
        }
    }
}