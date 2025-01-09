package com.example.tictactoe.board.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// dependencies to make remember/observeAsState work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
// dependencies to make remember/observeAsState work
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.R
import com.example.tictactoe.core.domain.GameModes
import com.example.tictactoe.core.domain.PlayerDetails
import com.example.tictactoe.core.navigation.NavRoutes
import com.example.tictactoe.core.utils.Utils
import com.example.tictactoe.ui.theme.Green40
import com.example.tictactoe.ui.theme.Slate20


@Composable
fun BoardScreen(navController: NavController, coreViewModel: CoreViewModel) {
    val isGameReadyToPlay = coreViewModel.isGameReadyToPlay()
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

    coreViewModel.winningCells.observeAsState().value?.let {
        Utils.printDebugger("winningCells", it)
    }

    val localCtx = LocalContext.current;

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
                Toast.LENGTH_LONG
            ).show()
            navController.navigate(NavRoutes.Settings)
        }
    }

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
                        RoundTracker(gameMode!!, noRoundsLeft!!)
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
            // if its a draw or there's a winner
            if(coreViewModel.checkDraw(board) || winner != null) {
                Button(
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

@Composable
fun PlayerScoreBoard(
    imageResourceId: Int,
    playerName: String,
    score: Int,
    isAI: Boolean,
    currentPlyr: Boolean
) {
    val imagePainter: Painter = painterResource(imageResourceId)
    Column(
        modifier = Modifier
            .width(150.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(Color.White)
            .then(
                if (currentPlyr) {
                    Modifier.border(2.dp, color = Color.Black, shape = RoundedCornerShape(20.dp))
                } else {
                    Modifier
                }
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(1.dp, shape = RoundedCornerShape(40.dp), color = Color.Black)
                .clip(RoundedCornerShape(40.dp))
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Player Score Board Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Slate20)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            playerName,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            // Preventing character name from exceeding a single line
            // If a single line is exceeded then the text is ellipsis
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isAI) {
                Text("AI")
                VerticalDivider(
                    modifier = Modifier
                        .padding(
                            vertical = 12.dp,
                            horizontal = 8.dp,
                        )
                        .height(14.dp),
                    thickness = 2.dp
                )
            }
            Text("Score: ", fontSize = 12.sp)
            Text(score.toString(), fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        }
    }
}

@Composable
fun RoundTracker(gameType: GameModes, roundsLeft: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (gameType) {
            GameModes.ROUND_OF_THREE -> Text(
                "$roundsLeft/3",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            GameModes.ROUND_OF_NINE -> Text(
                "$roundsLeft/9",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            else -> Text(
                "∞",
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BoardCell(
    player: PlayerDetails?,
    column: Int,
    row: Int,
    boardDimension: Int,
    inWinningCell: Boolean,
    makePlayHandler: (Int, Int) -> Unit
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
        .then(
            if (inWinningCell) {
                Modifier.background(Green40)
            } else {
                Modifier.background(Slate20)
            }
        )
        .clickable {
            Log.i("BoardCellBtn", "column: $column | row: $row")
            makePlayHandler.invoke(row, column)
        }
        .drawBehind {
            drawLine(
                Color.White,
                Offset(size.width, 15f),
                Offset(size.width, size.height - 15),
                7f,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(20f, 10f), 0f
                )
            )
            drawLine(
                Color.White,
                Offset(15f, size.height),
                Offset(size.width - 15, size.height),
                7f,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(20f, 10f), 0f
                )
            )
        }
        .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        val animDuration = 500
        Column {
            AnimatedVisibility(
                // hide the component if player is null
                visible = player != null,
                enter = scaleIn(tween(animDuration)) + fadeIn(
                    tween(animDuration)
                ),
                exit = scaleOut(tween(animDuration)) + fadeOut(
                    tween(animDuration)
                )
            ) {
                val paintRes = painterResource(id = player?.avatar ?: R.drawable.nothing)
                Image(
                    painter = paintRes,
                    contentDescription = null,
                    modifier = Modifier.size(if (boardDimension >= 7) 20.dp else 30.dp),
                )
            }
        }
    }
}

@Composable
fun ActualBoard(
    players: List<PlayerDetails>,
    boardDimension: Int,
    flattenedBoard: List<String?>,
    winningCells: List<String>,
    makePlay: (Int, Int) -> Unit
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .aspectRatio(1f)
            .drawBehind {
                drawRoundRect(
                    color = Color.Black,
                    style = Stroke(
                        width = 10f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 10f), 0f
                        ),
                    ),
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Gray),
            columns = GridCells.Fixed(boardDimension)
        ) {
            itemsIndexed(flattenedBoard) { index, item ->
                // Calculate row and column
                val row = index / boardDimension
                val column = index % boardDimension
                val player = players.find { it.name === item }
                BoardCell(
                    player = player,
                    column = column,
                    row = row,
                    boardDimension = boardDimension,
                    inWinningCell = winningCells.contains("$column|$row"),
                    makePlayHandler = makePlay
                )
            }
        }
    }

}