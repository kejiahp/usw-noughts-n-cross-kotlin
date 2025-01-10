package com.example.tictactoe.board.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.tictactoe.core.domain.PlayerDetails

@Composable
fun ActualBoard(
    btnClickAudioHandler: () -> Int,
    players: List<PlayerDetails>,
    boardDimension: Int,
    flattenedBoard: List<String?>,
    winningCells: List<String>,
    makePlay: (Int, Int) -> Unit
) {
    Box(
        modifier = Modifier.Companion
            .size(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Companion.White)
            .aspectRatio(1f)
            .drawBehind {
                drawRoundRect(
                    color = Color.Companion.Black,
                    style = Stroke(
                        width = 10f,
                        pathEffect = PathEffect.Companion.dashPathEffect(
                            floatArrayOf(20f, 10f), 0f
                        ),
                    ),
                    cornerRadius = CornerRadius(20.dp.toPx())
                )
            }
            .padding(5.dp),
        contentAlignment = Alignment.Companion.Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier.Companion
                .size(300.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                .background(Color.Companion.Gray),
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
                    makePlayHandler = makePlay,
                    btnAudioHandler = btnClickAudioHandler
                )
            }
        }
    }

}