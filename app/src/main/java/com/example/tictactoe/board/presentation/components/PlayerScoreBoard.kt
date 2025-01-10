package com.example.tictactoe.board.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        modifier = Modifier.Companion
            .width(150.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(Color.Companion.White)
            .then(
                if (currentPlyr) {
                    Modifier.Companion.border(
                        2.dp,
                        color = Color.Companion.Black,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                    )
                } else {
                    Modifier.Companion
                }
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.Companion
                .size(80.dp)
                .border(
                    1.dp,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(40.dp),
                    color = Color.Companion.Black
                )
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(40.dp))
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Player Score Board Image",
                contentScale = ContentScale.Companion.Fit,
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
        Spacer(modifier = Modifier.Companion.height(2.dp))
        Text(
            playerName,
            fontSize = 24.sp,
            textAlign = TextAlign.Companion.Center,
            fontWeight = FontWeight.Companion.Medium,
            overflow = TextOverflow.Companion.Ellipsis,
            color=Color.Black,
            // Preventing character name from exceeding a single line
            // If a single line is exceeded then the text is ellipsis
            maxLines = 1
        )
        Spacer(modifier = Modifier.Companion.height(2.dp))
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            if (isAI) {
                Text("AI", color=Color.Black)
                VerticalDivider(
                    color=Color.Black,
                    modifier = Modifier.Companion
                        .padding(
                            vertical = 12.dp,
                            horizontal = 8.dp,
                        )
                        .height(14.dp),
                    thickness = 2.dp
                )
            }
            Text("Score: ", fontSize = 12.sp, color=Color.Black)
            Text(score.toString(), fontWeight = FontWeight.Companion.SemiBold, fontSize = 18.sp, color=Color.Black)
        }
    }
}