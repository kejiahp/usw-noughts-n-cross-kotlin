package com.example.tictactoe.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSelectionBox(
    label: String,
    isSelected: Boolean = false,
    onSelectionHandler: () -> Unit
) {
    Box(
        modifier = Modifier.Companion
            .size(90.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(MaterialTheme.colorScheme.surface)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
            .then(
                if (isSelected) {
                    Modifier.Companion
                        .background(MaterialTheme.colorScheme.primary)
                        .border(
                            2.dp,
                            Color.Companion.Black,
                            androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                        )
                } else {
                    Modifier.Companion
                }
            )
            .clickable { onSelectionHandler.invoke() }
            .padding(10.dp),
        contentAlignment = Alignment.Companion.Center,
    ) {
        Text(
            label,
            textAlign = TextAlign.Companion.Center,
            fontWeight = FontWeight.Companion.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.background else Color.Unspecified
        )
    }
}
