package com.example.tictactoe.board.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.tictactoe.core.domain.GameModes

@Composable
fun RoundTracker(gameType: GameModes, roundsLeft: Int?) {
    Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
        when (gameType) {
            GameModes.ROUND_OF_THREE -> Text(
                "$roundsLeft/3",
                fontSize = 30.sp,
                fontWeight = FontWeight.Companion.Medium,
                textAlign = TextAlign.Companion.Center
            )

            GameModes.ROUND_OF_NINE -> Text(
                "$roundsLeft/9",
                fontSize = 30.sp,
                fontWeight = FontWeight.Companion.Medium,
                textAlign = TextAlign.Companion.Center
            )

            else -> Text(
                "∞",
                fontSize = 30.sp,
                fontWeight = FontWeight.Companion.Medium,
                textAlign = TextAlign.Companion.Center
            )
        }
    }
}