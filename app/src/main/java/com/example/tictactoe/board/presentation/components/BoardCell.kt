package com.example.tictactoe.board.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.R
import com.example.tictactoe.core.domain.PlayerDetails
import com.example.tictactoe.ui.theme.Green40
import com.example.tictactoe.ui.theme.Slate20

@Composable
fun BoardCell(
    btnAudioHandler: () -> Int,
    player: PlayerDetails?,
    column: Int,
    row: Int,
    boardDimension: Int,
    inWinningCell: Boolean,
    makePlayHandler: (Int, Int) -> Unit
) {
    Box(modifier = Modifier.Companion
        .fillMaxSize()
        .aspectRatio(1f)
        .then(
            if (inWinningCell) {
                Modifier.Companion.background(Green40)
            } else {
                Modifier.Companion.background(Slate20)
            }
        )
        .clickable {
            // Play audio on button click
            btnAudioHandler.invoke()

            // Make play in position
            makePlayHandler.invoke(row, column)
        }
        .drawBehind {
            drawLine(
                Color.Companion.White,
                Offset(size.width, 15f),
                Offset(size.width, size.height - 15),
                7f,
                pathEffect = PathEffect.Companion.dashPathEffect(
                    floatArrayOf(20f, 10f), 0f
                )
            )
            drawLine(
                Color.Companion.White,
                Offset(15f, size.height),
                Offset(size.width - 15, size.height),
                7f,
                pathEffect = PathEffect.Companion.dashPathEffect(
                    floatArrayOf(20f, 10f), 0f
                )
            )
        }
        .padding(10.dp),
        contentAlignment = Alignment.Companion.Center
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
                    modifier = Modifier.Companion.size(if (boardDimension >= 7) 20.dp else 30.dp),
                )
            }
        }
    }
}