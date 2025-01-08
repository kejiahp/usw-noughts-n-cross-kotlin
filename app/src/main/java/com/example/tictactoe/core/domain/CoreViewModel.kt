package com.example.tictactoe.core.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tictactoe.R

data class PlayerDetails(var name: String, var avatar: Int, var score: Int, var isAI: Boolean)

enum class GameModes {
    ROUND_OF_THREE, ROUND_OF_NINE, FREE_FOR_ALL
}

enum class DimensionAction {
    INCREMENT, DECREMENT
}

enum class AIDifficulty {
    EASY, HARD
}

class CoreViewModel : ViewModel() {
    private val _errorMsg = MutableLiveData<String?>(null)
    val errorMsg: LiveData<String?> = _errorMsg

    private val _players = MutableLiveData<MutableList<PlayerDetails>>(
        mutableListOf<PlayerDetails>(
            PlayerDetails(name = "", avatar = R.drawable.sasuke, score = 0, isAI = false),
            PlayerDetails(name = "", avatar = R.drawable.madara, score = 0, isAI = false),
        )
    )
    val players: LiveData<MutableList<PlayerDetails>> = _players

    private val _boardDimensions = MutableLiveData<Int>(3)
    val boardDimensions: LiveData<Int> = _boardDimensions

    private val _board =
        MutableLiveData<MutableList<MutableList<String?>>>(MutableList(this._boardDimensions.value!!) {
            MutableList(this._boardDimensions.value!!) { null }
        })
    val board: LiveData<MutableList<MutableList<String?>>> = _board;

    // If this isn't set & on board screen, user to be redirected to the settings screen
    private val _currentPlayer = MutableLiveData<String?>(null)
    val currentPlayer: LiveData<String?> = _currentPlayer

    private val _winner = MutableLiveData<String?>(null)
    val winner: LiveData<String?> = _winner

    // If this isn't set & on board screen, user to be redirected to the settings screen
    private val _aiDifficulty = MutableLiveData<AIDifficulty>(AIDifficulty.EASY)
    val aiDifficulty: LiveData<AIDifficulty> = _aiDifficulty

    // If this isn't set & on board screen, user to be redirected to the settings screen
    private val _gameMode = MutableLiveData<GameModes?>(null)
    val gameMode: LiveData<GameModes?> = _gameMode

    private fun getWinConditions() {
    }

    fun playPosition(): Unit {}

    fun checkWin(): Boolean {
        return false;
    }

    fun resetBoardState(): Unit {}

    /**
     * Updates player name using `playerIndex` to get player object
     * */
    fun updatePlayerName(plyrIndex: Int, newName: String) {
        _players.value = _players.value?.mapIndexed { i, person ->
            // use of `.copy` is to ensure immutability and avoid modifying the original object
            if (i == plyrIndex) person.copy(name = newName) else person
        }?.toMutableList() // Create a new list (makes it mutable) to trigger LiveData observers
    }

    /**
     * Selects a random unique name and assigns it to a player
     * */
    fun setRandomName(plyrIndex: Int) {
        val names = listOf<String>(
            "Alex",
            "Jordan",
            "Taylor",
            "Casey",
            "Morgan",
            "Jamie",
            "Riley",
            "Cameron",
            "Avery",
            "Quinn"
        )
        // if _players.value is null, skip the filtering to avoid name duplication and just select any random name
        val randName = _players.value?.let {
            val playerNames = it.map { plyr -> plyr.name }
            val filteredNames = names.filter { !playerNames.contains(it) }
            filteredNames.random()
        } ?: names.random()

        // set the random name
        updatePlayerName(plyrIndex, randName)
    }

    /**
     * Updates players `isAI` attribute
     * */
    fun makeAI(plyrIndex: Int) {
        _players.value = _players.value?.mapIndexed { idx, plyr ->
            if (idx == plyrIndex) plyr.copy(isAI = !plyr.isAI) else plyr
        }?.toMutableList()
    }

    /**
     * Change the game mode
     * */
    fun setGameMode(mode: GameModes) {
        _gameMode.value = mode;
    }

    /**
     * Change the AI difficulty
     * */
    fun setAIDifficulty(diff: AIDifficulty) {
        _aiDifficulty.value = diff;
    }

    /**
     * Increment or decrement board dimensions
     */
    fun changeDimension(action: DimensionAction) {
        _boardDimensions.value = _boardDimensions.value?.let {
            // ensuring 12 is the highest board dimension
            if (action == DimensionAction.INCREMENT && it < 12) {
                _boardDimensions.value?.plus(1)
                // ensuring 3 is the lowest board dimension
            } else if (action == DimensionAction.DECREMENT && it > 3) {
                _boardDimensions.value?.minus(1);
                // create an effect making it appear as you are cycling through number from 3-12
            } else {
                3
            }
        }
    }

    /**
     * Validates all inputs and redirects to the game board using the set game configurations
     * */
    fun startGame() {
        TODO("Start Game Validation")
    }
}