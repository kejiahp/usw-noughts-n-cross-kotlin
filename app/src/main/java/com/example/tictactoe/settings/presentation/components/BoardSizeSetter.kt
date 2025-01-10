package com.example.tictactoe.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.core.domain.DimensionAction

@Composable
fun BoardSizeSetter(boardSize: String, onDimensionChangeHandler: (DimensionAction) -> Unit) {
    SettingsTitleText("BOARD SIZE")
    Spacer(modifier = Modifier.Companion.height(10.dp))
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Button(modifier = Modifier.Companion.weight(1f), onClick = {
            onDimensionChangeHandler.invoke(
                DimensionAction.DECREMENT
            )
        }, shape = RoundedCornerShape(5.dp)) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Decrease board dimension",
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.Companion.width(10.dp))
        Text(
            text = boardSize,
            modifier = Modifier.Companion.weight(3f),
            fontSize = 20.sp,
            fontWeight = FontWeight.Companion.Medium,
            textAlign = TextAlign.Companion.Center
        )
        Spacer(modifier = Modifier.Companion.width(10.dp))
        Button(modifier = Modifier.Companion.weight(1f), onClick = {
            onDimensionChangeHandler.invoke(
                DimensionAction.INCREMENT
            )
        }, shape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp)) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Increase board dimension",
                tint = Color.Unspecified
            )
        }
    }
}