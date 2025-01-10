package com.example.tictactoe.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable rendering GameModes & AI Difficult blocks
 *
 * Used a generic ensuring composable reusability without having to battle with the glorious, all-seeing kotlin compiler 🙃.
 *
 * `T` is the generic, `Enum` base class for all enums and `Class` exclusively only allows passing the Java class of the enum
 * */
@Composable
fun <T> TitledSettingsBlock(
    title: String,
    options: Class<T>,
    selectedOption: T?,
    onSelect: (T) -> Unit
) where T : Enum<T> {
    SettingsTitleText(title)
    Spacer(modifier = Modifier.Companion.height(10.dp))
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        options.enumConstants?.forEach {
            SettingsSelectionBox(
                label = it.name.replace('_', ' '),
                it == selectedOption, onSelectionHandler = { onSelect(it) }
            )
        }
    }
}