package com.example.tictactoe.board.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**Custom Dialog wrapper component*/
@Composable
fun GameAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    confirmBtnTxt: String,
    dismissBtnTxt: String
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = dialogText,
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                // creating a copy of the object returned from `textButtonColors` with a custom `contentColor` value
                colors = ButtonDefaults.textButtonColors().copy(contentColor = Color.Unspecified),
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(confirmBtnTxt, fontWeight = FontWeight.Light)
            }
        },
        dismissButton = {
            OutlinedButton(
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(dismissBtnTxt)
            }
        }
    )
}