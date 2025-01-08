package com.example.tictactoe.board.presentation

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// dependencies to make remember/observeAsState work
import androidx.compose.runtime.getValue
// dependencies to make remember/observeAsState work
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import com.example.tictactoe.ui.theme.Slate20


@Composable
fun BoardScreen(navController: NavController, coreViewModel: CoreViewModel) {
    val players by coreViewModel.players.observeAsState()
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

    Log.i("BoardScreen", "$players")


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainComposable()
    }
}

@Composable
fun PlayerScoreBoard(imageResourceId: Int, playerName: String, score: Int) {
    val imagePainter: Painter = painterResource(imageResourceId)
    Column(
        modifier = Modifier
            .width(150.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(Color.White)
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
        Row(verticalAlignment = Alignment.CenterVertically) {
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
fun BoardCell(cellItem: String) {
    Box(modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
        .background(Slate20)
        .clickable {
            Log.i("BoardCellBtn", "Board Item: $cellItem was clicked")
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
        Text(cellItem)
    }
}

@Composable
fun ActualBoard() {
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
            columns = GridCells.Fixed(4)
        ) {
            val randomItems = List(3 * 4) { "Dog" }
            itemsIndexed(randomItems) { index, item ->
                BoardCell((index + 1).toString())
            }
        }
    }

}

@Composable
fun MainComposable() {
    Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerScoreBoard(
                R.drawable.madara,
                "Player 1 Here's some extra information too long he he he",
                30
            )
            RoundTracker(GameModes.ROUND_OF_THREE, 1)
            PlayerScoreBoard(
                R.drawable.sasuke,
                "Player 1 Here's some extra information too long he he he",
                30
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        ActualBoard()

        Spacer(modifier = Modifier.height(40.dp))
        Button(shape = RoundedCornerShape(5.dp), onClick = {}) {
            Text(
                "Player 1 Turn",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(150.dp),
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Button(shape = RoundedCornerShape(5.dp), onClick = {}) {
            Text(
                "Next Round ",
                textAlign = TextAlign.Center,
                modifier = Modifier.width(150.dp),
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainComposable()
}