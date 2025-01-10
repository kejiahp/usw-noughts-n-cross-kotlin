package com.example.tictactoe.settings.presentation.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerProfile(
    imageResourceId: Int,
    showAIRadio: Boolean,
    name: String,
    isAI: Boolean,
    makeAI: () -> Unit,
    generateRandomName: () -> Unit,
    onNameChangeHandler: (String) -> Unit
) {

    val imagePainter: Painter = painterResource(id = imageResourceId)
    val toastCtx = LocalContext.current
    val maxLength: Int = 20

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Box(
            modifier = Modifier.Companion
                .width(80.dp)
                .height(80.dp)
                .clip(
                    RoundedCornerShape(40.dp)
                ), contentAlignment = Alignment.Companion.Center
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Player profile image",
                contentScale = ContentScale.Companion.Fit,
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(
                        Color.White
                    )
            )
        }
        Column(modifier = Modifier.Companion.fillMaxWidth(0.96f)) {
            TextField(
                value = name,
                onValueChange = { it ->
                    if (it.length >= 20) {
                        // Display toast when username hit the max character limit
                        Toast.makeText(
                            toastCtx,
                            "Input exceeds $maxLength characters!",
                            Toast.LENGTH_LONG
                        ).show()
                        // prevent further input of text, this a labelled return as kotlin by default returns to the nearest `fun` declaration
                        return@TextField
                    }
                    onNameChangeHandler(it.lowercase())
                },
                label = { Text("Enter text") },
                singleLine = true,
                modifier = Modifier.Companion.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.Companion.height(5.dp))
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    if (showAIRadio) {
                        RadioButton(
                            selected = isAI,
                            // invoke the lambda
                            onClick = { makeAI.invoke() }
                        )
                        Text("Make AI?")
                    }
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                }

                Button(
                    // invoke the lambda
                    onClick = { generateRandomName.invoke() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp)
                ) {
                    Text("Generate Name", fontSize = 12.sp, textAlign = TextAlign.Companion.Center)
                }
            }

        }
    }
    Spacer(modifier = Modifier.Companion.height(10.dp))
}