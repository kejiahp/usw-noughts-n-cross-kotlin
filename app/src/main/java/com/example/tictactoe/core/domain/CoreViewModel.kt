package com.example.tictactoe.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tictactoe.R
import kotlin.collections.indexOf

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
    /**Tracks error messages*/
    private val _errorMsg = MutableLiveData<String?>(null)
    val errorMsg: LiveData<String?> = _errorMsg

    /**Controls the opening and closing of dialog the Game Over Dialog*/
    private val _showGameOverDialog = MutableLiveData<Boolean>(false)
    val showGameOverDialog: LiveData<Boolean> = _showGameOverDialog

    /**Controls the opening and closing of dialog the Game Menu Dialog*/
    private val _showGameMenuDialog = MutableLiveData<Boolean>(false)
    val showGameMenuDialog: LiveData<Boolean> = _showGameMenuDialog

    /**Tracks player data e.g. score, name, is player AI?, avatar*/
    private val _players = MutableLiveData<MutableList<PlayerDetails>>(
        mutableListOf<PlayerDetails>(
            PlayerDetails(name = "", avatar = R.drawable.sasuke, score = 0, isAI = false),
            PlayerDetails(name = "", avatar = R.drawable.madara, score = 0, isAI = false),
        )
    )
    val players: LiveData<MutableList<PlayerDetails>> = _players

    /**Tracks the board dimensions e.g. 3X3, 4X4, 5X5*/
    private val _boardDimensions = MutableLiveData<Int>(3)
    val boardDimensions: LiveData<Int> = _boardDimensions

    /**Tracks the actual board state*/
    private val _board =
        MutableLiveData<List<MutableList<String?>>>(List(this._boardDimensions.value!!) {
            MutableList(this._boardDimensions.value!!) { null }
        })
    val board: LiveData<List<MutableList<String?>>> = _board;

    /**Track the win conditions, dynamically created using `generateWinConditions`*/
    private val _winConditions = MutableLiveData<List<List<Pair<Int, Int>>>?>(null);

    // If this isn't set & on board screen, user to be redirected to the settings screen
    /**Track the current player*/
    private val _currentPlayer = MutableLiveData<String?>(null)
    val currentPlayer: LiveData<String?> = _currentPlayer

    /**Track the winner*/
    private val _winner = MutableLiveData<String?>(null)
    val winner: LiveData<String?> = _winner

    /**Track the cell combination that created a winning play*/
    private val _winningCells = MutableLiveData<List<String>>(emptyList<String>())
    val winningCells: LiveData<List<String>> = _winningCells

    // If this isn't set & user is on the board screen, user to be redirected to the settings screen
    /**Track the AI difficulty (`AIDifficulty`)*/
    private val _aiDifficulty = MutableLiveData<AIDifficulty>(AIDifficulty.EASY)
    val aiDifficulty: LiveData<AIDifficulty> = _aiDifficulty


    // If this isn't set & user is on the board screen, user to be redirected to the settings screen
    /**Track the game mode (`GameModes`) being played*/
    private val _gameMode = MutableLiveData<GameModes?>(null)
    val gameMode: LiveData<GameModes?> = _gameMode

    /**Track the number of rounds left based on the `_gameMode`*/
    private val _noRoundsLeft = MutableLiveData<Int?>(null)
    val noRoundsLeft: LiveData<Int?> = _noRoundsLeft

    /**
     * Generate or updates the initial board state
     *
     * This function create a mutable list composed of a mutable list of nullable strings, it looks something like this
     *
     * `[[null, "james", null], [null, "james", null]]`
     * */
    private fun generateBoard(size: Int): MutableList<MutableList<String?>> {
        return MutableList(size) { MutableList(size) { null } }
    }

    /**
     * dynamically generates the win conditions using board size parameter, it looks something like this
     *
     * `[[(0, 0), (0, 1), (0, 2)], [(1, 0), (1, 1), (1, 2)]]`
     *
     * the first pair item is the row, the second is the column
     * */
    private fun generateWinConditions(size: Int): List<List<Pair<Int, Int>>> {
        val winConditions = mutableListOf<List<Pair<Int, Int>>>()

        // Add horizontal win conditions
        for (row in 0 until size) {
            val horizontal = mutableListOf<Pair<Int, Int>>()
            for (col in 0 until size) {
                horizontal.add(row to col)
            }
            winConditions.add(horizontal)
        }

        // Add vertical win conditions
        for (col in 0 until size) {
            val vertical = mutableListOf<Pair<Int, Int>>()
            for (row in 0 until size) {
                vertical.add(row to col)
            }
            winConditions.add(vertical)
        }

        // Add top-left to bottom-right diagonal win condition
        val diagonal1 = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until size) {
            diagonal1.add(i to i)
        }
        winConditions.add(diagonal1)

        // Add top-right to bottom-left diagonal win condition
        val diagonal2 = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until size) {
            diagonal2.add(i to (size - 1 - i))
        }
        winConditions.add(diagonal2)

        return winConditions
    }

    /**
     * Make a play in a certain position
     *
     * Insert `PlayerDetails.name` in specified column within row updating the board state
     * */
    fun makePlayInPosition(row: Int, col: Int) {

        // ensure cell is empty and game is still on going
        if (_board.value!![row][col] != null || _winner.value != null) return

        // make play
        val newBoard = _board.value!!.map { it.toMutableList() }
        newBoard[row][col] = _currentPlayer.value!!
        _board.value = newBoard


        // check for a winner
        val (isWin, cells) = checkWin(
            _board.value!!,
            _winConditions.value!!,
            _currentPlayer.value!!
        )
        if (isWin) {
            // if theres a winner update the winner state
            _winner.value = _currentPlayer.value
            _winningCells.value = cells
            // update the score of the winner
            updatePlayerScore(_currentPlayer.value!!)
            // increase round count, if game mode is not FREE_FOR_ALL
            updateRoundCount()
            // display game over modal or not
            displayGameOverModal()
        }
        // check for draw
        else if (checkDraw(_board.value!!)) {
            _winningCells.value = emptyList()
            // if its a draw update the score of all players
            updateAllPlayersScore()
            // increase round count, if game mode is not FREE_FOR_ALL
            updateRoundCount()
            // display game over modal or not
            displayGameOverModal()
        } else {
            val nextPlayer = getNextPlayer(_currentPlayer.value!!)
            // else change current player
            _currentPlayer.value = nextPlayer.name
            _winningCells.value = emptyList()

            // If next player is AI, Make AI play using selected AI difficulty
            if (nextPlayer.isAI) {
                aiMakePlayInPosition(
                    _aiDifficulty.value!!,
                    _board.value!!
                )
            }
        }
    }
    /**
     * Determine the position the AI will play in considering the state of the board and the AI difficulty
     * */
    fun aiMakePlayInPosition(
        difficulty: AIDifficulty,
        boardState: List<MutableList<String?>>,
    ) {
        when (difficulty) {
            // random plays
            AIDifficulty.EASY -> {
                val freeSpace = getRandomEmptyCell(boardState)
                makePlayInPosition(freeSpace.first, freeSpace.second)
            }

            // heuristics based plays
            AIDifficulty.HARD -> {
                // check board corners
                val (isCorner, cornerPlyPos) = isCornerPlayPossible(
                    _board.value!!,
                    _boardDimensions.value!!
                )

                // check board middle
                val (isMiddle, middlePlyPos) = isMiddlePlayPossible(
                    _board.value!!,
                    _boardDimensions.value!!
                )

                // check for winning plays.
                // This involves a lot of iterations to save compute, I skipped this check if there are less than two plays made
                val groupResult = board.value!!.flatten().groupingBy { it != null }.eachCount() // returns a map that looks something like this `{false=3, true=5}`
                val noOfPlaysMade = groupResult[true] ?: 2

                val (isWin, winPlyPos) = if (noOfPlaysMade >= 2) isWinningPlayPossible(_players.value!!.map { it.name }
                    .toMutableList(), _winConditions.value!!, _board.value!!) else Pair(false, null)

                // make play based on heuristics
                if (isWin && winPlyPos != null) {
                    makePlayInPosition(winPlyPos.first, winPlyPos.second)
                } else if (isMiddle && middlePlyPos != null) {
                    makePlayInPosition(middlePlyPos.first, middlePlyPos.second)
                } else if (isCorner && cornerPlyPos != null) {
                    makePlayInPosition(cornerPlyPos.first, cornerPlyPos.second)
                } else {
                    val freeSpace = getRandomEmptyCell(boardState)
                    makePlayInPosition(freeSpace.first, freeSpace.second)
                }
            }
        }
    }

    /**
     * Get a random empty cell from the board
     */
    private fun getRandomEmptyCell(boardState: List<MutableList<String?>>): Pair<Int, Int> {
        val emptyCellList = mutableListOf<Pair<Int, Int>>()

        for ((rowIdx, row) in boardState.withIndex()) {
            for ((columnIdx, column) in row.withIndex()) {
                if (column == null) {
                    emptyCellList.add(Pair(rowIdx, columnIdx))
                }
            }
        }

        return emptyCellList.random()
    }

    /**
     * Get the next player from the array of players
     *
     * Used in changing player turns if win check fails
     *  */
    private fun getNextPlayer(curPlyr: String): PlayerDetails {
        val current = _players.value?.find { it.name == curPlyr };
        val idx = _players.value!!.indexOf(current)
        return if ((idx + 1) >= _players.value!!.size) _players.value!![0] else _players.value!![idx + 1]
    }

    /**
     * Checks for a winner, provided board state, win conditions and player
     * */
    fun checkWin(
        boardState: List<MutableList<String?>>,
        winCond: List<List<Pair<Int, Int>>>,
        plyr: String
    ): Pair<Boolean, List<String>> {
        for (conditions in winCond) {
            if (conditions.all { (row, column) -> boardState[row][column] == plyr }) return Pair(
                true,
                conditions.map { it -> "${it.second}|${it.first}" })
        }
        return Pair(false, emptyList());
    }

    /**
     * Checks for a draw
     * */
    fun checkDraw(boardState: List<MutableList<String?>>): Boolean {
        return boardState.flatten().all { it != null }
    }

    /**
     * reset board state, winner state and winning cells tracker to default values
     * */
    fun resetBoardState() {
        _board.value = generateBoard(_boardDimensions.value!!)
        _winningCells.value = emptyList()
        // changing `_currentPlayer`, ensuring the player is
        // always a human (The game/round can't be initiated by AI player)
        _currentPlayer.value?.let { curPlyr ->
            _players.value?.let { plyrs ->
                val currentPlyr = plyrs.find { plyr -> plyr.name == curPlyr}
                // if current player is AI, start the next round with human player
                if (currentPlyr != null && currentPlyr.isAI) {
                    _currentPlayer.value = plyrs[0].name
                }
                else {
                    // if current player is human, select the next player that is human
                    // else default to the first player which is always human
                    val nextPlyr = getNextPlayer(curPlyr)
                    if (!nextPlyr.isAI) {
                        _currentPlayer.value = nextPlyr.name
                    }else {
                        _currentPlayer.value =  plyrs[0].name
                    }
                }
            }}
        _winner.value = null
    }

    /**
     * everything done by `resetBoardState` including, player score reset, no of rounds left reset
     * */
    fun extendedReset() {
        resetBoardState()
        //reset all players scores
        updateAllPlayersScore(reset = true)
        // reset the number rounds left
        _gameMode.value.let {
            if (it == GameModes.FREE_FOR_ALL) {
                _noRoundsLeft.value = null
            } else {
                _noRoundsLeft.value = 0

            }
        }
    }

    /**
     * Toggles the game over dialog on or off
     */
    fun toggleGameOverDialog(dialogVal: Boolean) {
        _showGameOverDialog.value = dialogVal
    }

    /**
     * Toggles the game menu dialog on or off
     */
    fun toggleGameMenuDialog(dialogVal: Boolean) {
        _showGameMenuDialog.value = dialogVal
    }

    /**
     * Display activate `showGameOverDialog, if game mode maximum rounds is reached.
     * */
    fun displayGameOverModal() {
        _gameMode.value?.let {
            when (it) {
                GameModes.ROUND_OF_NINE -> {
                    if (_noRoundsLeft.value == 9) toggleGameOverDialog(true)
                }

                GameModes.ROUND_OF_THREE -> {
                    if (_noRoundsLeft.value == 3) toggleGameOverDialog(true)
                }

                GameModes.FREE_FOR_ALL -> {}
            }
        }
    }

    /**
     * conditionally update round count using game mode
     * */
    fun updateRoundCount() {
        _gameMode.value?.let {
            when (it) {
                GameModes.ROUND_OF_NINE -> if (_noRoundsLeft.value!! < 9) _noRoundsLeft.value =
                    _noRoundsLeft.value?.plus(1)

                GameModes.ROUND_OF_THREE -> if (_noRoundsLeft.value!! < 3) _noRoundsLeft.value =
                    _noRoundsLeft.value?.plus(1)

                GameModes.FREE_FOR_ALL -> {}
            }
        }
    }

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
     * Update player score using `plyrName`
     * */
    fun updatePlayerScore(plyrName: String) {
        _players.value = _players.value?.map {
            if (it.name == plyrName) it.copy(score = it.score + 1) else it
        }?.toMutableList()
    }

    /**
     * Update the scores of all players
     * */
    fun updateAllPlayersScore(reset: Boolean = false) {
        _players.value = _players.value?.map {
            if (reset) it.copy(score = 0) else it.copy(score = it.score + 1)
        }?.toMutableList()
    }

    /**
     * Selects a random unique name and assigns it to a player
     * */
    fun setRandomName(plyrIndex: Int) {
        val names = listOf<String>(
            "alex",
            "jordan",
            "taylor",
            "casey",
            "morgan",
            "jamie",
            "riley",
            "cameron",
            "avery",
            "quinn"
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
                // by returning 3 an effect making it appear as you are cycling through numbers from 3-12 is created
            } else {
                3
            }
        }
    }

    /***
     * checking if players have the same name
     */
    private fun namesEqual(names: List<String>): Boolean {
        var n1 = names[0];
        for (name in names) {
            if (n1 != name) {
                return false
            }
        }
        return true
    }

    /**
     * Validates all inputs and triggers a redirect to the game board using the set game configurations
     * */
    fun startGame(): Boolean {
        val plyrNames = _players.value?.let {
            // make player names lowercase
            it.map { plyr -> plyr.name.lowercase() }
        } ?: emptyList()
        if (plyrNames.isEmpty()) {
            _errorMsg.value = "Player names are required"
            // prevent further execution
            return false;
        }
        // Ensure no empty string as names
        if (plyrNames.any { it == "" }) {
            _errorMsg.value = "All player names are required"
            // prevent further execution
            return false;
        }
        // Ensure no names are same
        if (namesEqual(plyrNames)) {
            _errorMsg.value = "Player names must not be the same"
            // prevent further execution
            return false;
        }
        // Ensure a game mode is set
        if (_gameMode.value == null) {
            _errorMsg.value = "A game mode must be selected"
            // prevent further execution
            return false;
        }

        // begin each round with players having a score of zero (0)
        updateAllPlayersScore(reset = true)

        // current player to the first element in the player names list
        _currentPlayer.value = plyrNames[0]

        // update the initial board state to create before game start
        this._boardDimensions.value?.let {
            _board.value = generateBoard(it)
            _winConditions.value = generateWinConditions(it)
        } ?: run {
            _errorMsg.value = "Board dimensions are required"
            // this return is pointing to the `startGame` function not the lambda
            return false;
        }

        _gameMode.value.let {
            if (it == GameModes.FREE_FOR_ALL) {
                _noRoundsLeft.value = null
            } else {
                _noRoundsLeft.value = 0

            }
        }

        return true;
    }

    /**
     * Check if all requirements for game to run properly are set
     * */
    fun isGameReadyToPlay(): Boolean {
        val plyrNames = _players.value?.let {
            it.map { plyr -> plyr.name }
        } ?: emptyList()

        // Player details are required
        if (plyrNames.isEmpty()) {
            // prevent further execution
            return false;
        }
        if (_winConditions.value == null) {
            return false
        }
        if (_currentPlayer.value == null) {
            return false
        }
        if (_gameMode.value == null) {
            return false
        }

        return true
    }

    /**
     * Checks if the corners of the board are free and selects a corner at random, to make a play in
     */
    private fun isCornerPlayPossible(
        boardState: List<MutableList<String?>>,
        size: Int
    ): Pair<Boolean, Pair<Int, Int>?> {
        val corners = listOf<Pair<Int, Int>>(
            Pair(0, 0),
            Pair(0, size - 1),
            Pair(size - 1, 0),
            Pair(size - 1, size - 1)
        )
        val freeCorners = corners.filter { boardState[it.first][it.second] == null }
        return if (freeCorners.isEmpty()) Pair(false, null) else Pair(true, freeCorners.random())
    }

    /**
     * Checks if the board as a middle and if its not empty to make a play in
     */
    private fun isMiddlePlayPossible(
        boardState: List<MutableList<String?>>,
        size: Int
    ): Pair<Boolean, Pair<Int, Int>?> {
        val midIdx = size / 2
        // only board dimensions set with un-even numbers have a center
        if (size % 2 != 0 && boardState[midIdx][midIdx] == null) {
            Pair(true, Pair(midIdx, midIdx))
        }
        return Pair(false, null)
    }

    /**
     * Check for a potential winning play. If the winning play is in the favour of the AI it is made (played) else it is blocked.
     *
     * This works by getting the state of the board (cloning it, avoid potential re-renders), making plays in empty spaces of the board and checking for a win
     *
     * `plyrNames` is to be reversed to start winning play checking for the AI first.
     * initial player list order [realPlyr, realPlyr or AI], reversed order [realPlyr or AI, realPlyr]
     * */
    private fun isWinningPlayPossible(
        plyrNames: MutableList<String>,
        winCond: List<List<Pair<Int, Int>>>,
        boardState: List<MutableList<String?>>,
    ): Pair<Boolean, Pair<Int, Int>?> {
        // clone of the board state
        val newBoardState = boardState.map { it.toMutableList() }
        // list holding index of empty cells
        val emptyCellsList = mutableListOf<Pair<Int, Int>>()
        // put empty cells into the list
        newBoardState.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { columnIdx, column ->
                if (column == null) emptyCellsList.add(
                    Pair(rowIdx, columnIdx)
                )
            }
        }
        // win checks
        // reversing `plyrNames` so AI prioritizes itself for win checks
        for (plyr in plyrNames.reversed()) {
            for (i in emptyCellsList) {
                newBoardState[i.first][i.second] = plyr;
                val (isWin) = checkWin(newBoardState, winCond, plyr)
                if (isWin) {
                    return Pair(true, i)
                }
                // undo changes to board state copy
                newBoardState[i.first][i.second] = null;
            }
        }
        return Pair(false, null)
    }
}