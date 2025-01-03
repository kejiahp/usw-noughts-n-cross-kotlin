package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe.ui.theme.TictactoeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TictactoeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class PlayerDetails(val name: String, val avatar: String, val score: String)
enum class GameTypes {
    ROUND_OF_THREE, FREE_FOR_ALL
}

class GameState (val players: List<PlayerDetails>, val rows: Int, val columns: Int){
    var boardState: MutableList<String?>;
    var winConditions: MutableList<MutableList<Int>>;

    init {
        winConditions = generateWinConditions();
        boardState = generateInitialBoardState();
    }

    private fun generateWinConditions(): MutableList<MutableList<Int>> {
    }

    private fun generateInitialBoardState(): MutableList<String?> {
    }

    fun playPosition(): Unit {}

    fun checkWin(): Boolean {}

    fun resetBoardState(): Unit {}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TictactoeTheme {
        Greeting("Android")
    }
}