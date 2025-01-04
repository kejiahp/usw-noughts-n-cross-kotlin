package com.example.tictactoe.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class PlayerDetails(val name: String, val avatar: String, val score: String, val isAI: Boolean)

enum class GameTypes {
    ROUND_OF_THREE, ROUND_OF_NINE, FREE_FOR_ALL
}

enum class AIDifficulty {
    EASY, HARD
}

data class BoardDimensions(val rows: Int = 3, val columns: Int = 3)

class CoreViewModel: ViewModel() {
    private val _players = MutableLiveData<MutableList<PlayerDetails>>()
    val players: LiveData<MutableList<PlayerDetails>> = _players

    private val _board = MutableLiveData<MutableList<MutableList<String?>>>()
    val board: LiveData<MutableList<MutableList<String?>>> = _board;

    private val _boardDimensions = MutableLiveData<BoardDimensions>()
    val boardDimensions: LiveData<BoardDimensions> = _boardDimensions

    private val _currentPlayer = MutableLiveData<String>()
    val currentPlayer: LiveData<String> = _currentPlayer

    private val _winner = MutableLiveData<String?>(null)
    val winner: LiveData<String?> = _winner

    private val _aiDifficulty = MutableLiveData<AIDifficulty>(AIDifficulty.EASY)
    val aiDifficulty: LiveData<AIDifficulty> = _aiDifficulty

    private fun getWinConditions() {
    }

    fun playPosition(): Unit {}

    fun checkWin(): Boolean {
        return false;
    }

    fun resetBoardState(): Unit {}
}