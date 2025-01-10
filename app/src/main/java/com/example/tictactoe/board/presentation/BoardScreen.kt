package com.example.tictactoe.board.presentation


import android.media.AudioAttributes
import android.media.SoundPool
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// dependencies to make remember/observeAsState work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
// dependencies to make remember/observeAsState work
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.R
import com.example.tictactoe.board.presentation.components.ActualBoard
import com.example.tictactoe.core.domain.GameModes
import com.example.tictactoe.core.navigation.NavRoutes
import com.example.tictactoe.board.presentation.components.GameAlertDialog
import com.example.tictactoe.board.presentation.components.PlayerScoreBoard
import com.example.tictactoe.board.presentation.components.RoundTracker


@Composable
fun BoardScreen(navController: NavController, coreViewModel: CoreViewModel) {
    val isGameReadyToPlay = coreViewModel.isGameReadyToPlay()
    val localCtx = LocalContext.current
    val scrollState = rememberScrollState()
    // observe livedata and delegate values to variables,
    // also specifying default to protect against nullables which could cause exceptions
    val players by coreViewModel.players.observeAsState(emptyList());
    val boardDimension by coreViewModel.boardDimensions.observeAsState(3);
    val board by coreViewModel.board.observeAsState(emptyList());
    val currentPlayer by coreViewModel.currentPlayer.observeAsState("");
    val gameMode by coreViewModel.gameMode.observeAsState(GameModes.ROUND_OF_THREE)
    val noRoundsLeft by coreViewModel.noRoundsLeft.observeAsState(0)
    val winner by coreViewModel.winner.observeAsState("")
    val winningCells by coreViewModel.winningCells.observeAsState(emptyList())
    val showGameOverDialog by coreViewModel.showGameOverDialog.observeAsState(false)
    val showGameMenuDialog by coreViewModel.showGameMenuDialog.observeAsState(false)
    // Remember SoundPool instance
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(5) // Max simultaneous streams
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            .build()
    }
    // Preload audio resources to memory
    val buttonClickSound = remember { soundPool.load(localCtx, R.raw.buttonclick, 1) }
    val winSound = remember { soundPool.load(localCtx, R.raw.congratulationsdeepvoice, 1) }

    /**
     * Plays cell click audio
     * */
    val playBtnClickAudio = {
        soundPool.play(
            buttonClickSound, // Sound to play
            1f,      // Left volume
            1f,      // Right volume
            1,       // Priority
            0,       // Loop (0 for no loop)
            1f       // Playback rate (1f is normal speed)
        )
    }

    /**
     * Plays audio on winner found
     */
    val playWinAudio = {
        soundPool.play(
            winSound, // Sound to play
            1f,      // Left volume
            1f,      // Right volume
            1,       // Priority
            0,       // Loop (0 for no loop)
            1f       // Playback rate (1f is normal speed)
        )
    }


    /**
     * Navigate to the settings page if game requirements are not set
     *
     * [Uses compose side-effects](https://developer.android.com/develop/ui/compose/side-effects)
     */
    LaunchedEffect(isGameReadyToPlay) {
        if (!isGameReadyToPlay) {
            Toast.makeText(
                localCtx,
                "Game settings are required",
                Toast.LENGTH_SHORT
            ).show()
            navController.navigate(NavRoutes.Settings)
        }
    }

    /**
     * Play `congratulationsdeepvoice.mp3` on winning move
     * */
    winner?.let {
        LaunchedEffect(it) {
            playWinAudio()
        }
    }

    // conditionally render/compose various dialogs
    when {
        // Game Over dialog
        showGameOverDialog -> {
            val displayTxt =
                if (winner != null) "${winner!!.replaceFirstChar { it.uppercase() }} Won" else if (coreViewModel.checkDraw(
                        board
                    )
                ) "It's a draw" else "Wanna give it another go?"
            GameAlertDialog(
                onConfirmation = {
                    // reset everything
                    coreViewModel.extendedReset()
                    // close dialog
                    coreViewModel.toggleGameOverDialog(false)
                    // navigate to settings page
                    navController.navigate(NavRoutes.Settings)
                },
                onDismissRequest = {
                    // reset everything
                    coreViewModel.extendedReset()
                    // close dialog
                    coreViewModel.toggleGameOverDialog(false)
                },
                dialogTitle = "Game over",
                dialogText = displayTxt,
                icon = Icons.Default.Info,
                dismissBtnTxt = "Play again",
                confirmBtnTxt = "Back to settings"
            )
        }
        // Game menu dialog
        showGameMenuDialog -> {
            GameAlertDialog(
                onConfirmation = {
                    // reset everything
                    coreViewModel.extendedReset()
                    // close dialog
                    coreViewModel.toggleGameMenuDialog(false)
                    // navigate to settings page
                    navController.navigate(NavRoutes.Settings)
                },
                onDismissRequest = {
                    // close dialog
                    coreViewModel.toggleGameMenuDialog(false)
                },
                dialogTitle = "Paused",
                dialogText = "Ooh...so you're leaving? 😒",
                icon = Icons.Default.Info,
                confirmBtnTxt = "Back to settings",
                dismissBtnTxt = "Continue playing"
            )
        }
    }

    // conditionally render/compose the game board
    // if game settings are not provided the game will never be rendered
    when {
        isGameReadyToPlay -> {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { coreViewModel.toggleGameMenuDialog(true) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Menu Dialog Icon"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        players.let {
                            if (it.size >= 2) {
                                val (plyr1, plyr2) = it
                                PlayerScoreBoard(
                                    plyr1.avatar,
                                    plyr1.name,
                                    plyr1.score,
                                    plyr1.isAI,
                                    currentPlayer == plyr1.name
                                )
                                RoundTracker(gameMode!!, noRoundsLeft)
                                PlayerScoreBoard(
                                    plyr2.avatar,
                                    plyr2.name,
                                    plyr2.score,
                                    plyr2.isAI,
                                    currentPlayer == plyr2.name
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    ActualBoard(
                        players = players,
                        boardDimension = boardDimension,
                        flattenedBoard = board.flatten(),
                        // `::` callable reference or class reference
                        makePlay = coreViewModel::makePlayInPosition,
                        winningCells = winningCells,
                        btnClickAudioHandler = playBtnClickAudio
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    Box(
                        modifier = Modifier
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(5.dp)
                            )
                            .defaultMinSize(
                                minWidth = ButtonDefaults.MinWidth,
                                minHeight = ButtonDefaults.MinHeight
                            )
                            .padding(ButtonDefaults.ContentPadding)
                    ) {
                        val displayText =
                            if (winner != null) "${winner!!.replaceFirstChar { it.uppercase() }} Won" else if (coreViewModel.checkDraw(
                                    board
                                )
                            ) "It's a draw" else "${currentPlayer!!.replaceFirstChar { it.uppercase() }}'s Turn"
                        Text(
                            displayText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(150.dp),
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                    when {
                        // if its a draw or there is a winner, show the button
                        coreViewModel.checkDraw(board) || winner != null -> Button(
                            shape = RoundedCornerShape(5.dp),
                            onClick = { coreViewModel.resetBoardState() }) {
                            Text(
                                when (gameMode) {
                                    GameModes.FREE_FOR_ALL -> "Play Again"
                                    else -> "Next Round"
                                },
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(150.dp),
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }

                }
            }
        }
        // fallback in the absence of game settings
        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Game settings are required", textAlign = TextAlign.Center, fontSize = 20.sp)
            }
        }
    }


}

