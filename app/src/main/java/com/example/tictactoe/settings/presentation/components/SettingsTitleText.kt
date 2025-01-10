package com.example.tictactoe.settings.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SettingsTitleText(label: String) {
    Text(label, fontSize = 16.sp, fontWeight = FontWeight.Companion.Medium)
}