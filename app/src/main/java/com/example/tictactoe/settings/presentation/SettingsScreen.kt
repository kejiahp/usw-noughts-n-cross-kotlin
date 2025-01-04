package com.example.tictactoe.settings.presentation

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.core.domain.CoreViewModel
import com.example.tictactoe.R
import com.example.tictactoe.ui.theme.Slate20

@Composable
fun SettingsScreen(navController: NavController, coreViewModel: CoreViewModel) {
    var scrollState = rememberScrollState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column (modifier = Modifier.verticalScroll(scrollState)) {
//            Button(onClick = {
//                navController.navigate(NavRoutes.Board)
//            }) {
//                Text("Go to Second Screen")
//            }

            Column {
                Text(text = "NoughtsXCrosses", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                Text(text = "Customize your fame experience to your hearts content")
                Spacer(modifier = Modifier.height(20.dp))
                Column(modifier = Modifier.fillMaxWidth(1.0f)) {
                    PlayerProfile(R.drawable.sasuke)
                    Spacer(modifier = Modifier.height(20.dp))
                    PlayerProfile(R.drawable.madara)
                }
                Spacer(modifier = Modifier.height(20.dp))
                TitledSettingsBlock("GAME MODE SELECTION")
                Spacer(modifier = Modifier.height(20.dp))
                TitledSettingsBlock("BOARD SIZE")
                Spacer(modifier = Modifier.height(20.dp))
                TitledSettingsBlock("AI DIFFICULTY")
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { Log.i("SettingsScreen", "game validation") }) {
                    Text("START", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PlayerProfile(imageResourceId: Int) {
    var text by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Option 1") }

    val imagePainter: Painter = painterResource(id = imageResourceId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(
                    RoundedCornerShape(40.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = imagePainter,
                contentDescription = "Player profile image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Slate20
                    )
            )
        }
        Column(modifier = Modifier.fillMaxWidth(0.96f)) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter text") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == "Option 1",
                    onClick = { selectedOption = "Option 1" }
                )
                Text("Make artificial intelligence?")
            }

        }
    }
}


@Composable
fun SettingsSelectionBox(label: String, isSelected: Boolean = false) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), clip = false)
            .background(Color.White)
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (isSelected) {
                    Modifier
                        .background(Slate20)
                        .border(0.2.dp, Color.Black, RoundedCornerShape(20.dp))
                } else {
                    Modifier
                }
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SettingsTitleText(label: String) {
    Text(label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
}

@Composable
fun TitledSettingsBlock(title: String) {
    SettingsTitleText(title)
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        SettingsSelectionBox(label = "ROUND OF THREE", true)
        SettingsSelectionBox(label = "ROUND OF NINE")
        SettingsSelectionBox(label = "FREE FOR ALL")
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//
//}