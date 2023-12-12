package de.hhn.tictactoe

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import de.hhn.tictactoe.model.Field
import de.hhn.tictactoe.model.GameModel
import de.hhn.tictactoe.model.Status

class TicTacToeViewModel {

    // MutableState for recomposition
    var gameModel: MutableState<GameModel> = mutableStateOf(GameModel())
    var gameField: MutableState<Array<Array<Field>>> = mutableStateOf(initGameField())

    // Function to reset the game
    fun resetGame() {
        gameModel.value = GameModel()
        gameField.value = initGameField()
    }

    // Function to handle field selection
    fun selectField(field: Field) {
        if (field.status == Status.Empty && !gameModel.value.isGameEnding) {
            field.status = gameModel.value.currentPlayer
            gameModel.value = gameModel.value.copy(
                currentPlayer = gameModel.value.currentPlayer.next()
            )
            checkEndingGame()
        }
    }

    // Function to check if the game has ended
    private fun checkEndingGame() {
        val rows = gameField.value
        val cols = Array(3) { i -> Array(3) { j -> gameField.value[j][i] } }
        val diagonals = arrayOf(
            arrayOf(gameField.value[0][0], gameField.value[1][1], gameField.value[2][2]),
            arrayOf(gameField.value[0][2], gameField.value[1][1], gameField.value[2][0])
        )

        for (array in rows + cols + diagonals) {
            if (array.all { it.status == Status.PlayerX }) {
                gameModel.value = gameModel.value.copy(
                    isGameEnding = true,
                    winningPlayer = Status.PlayerX
                )
                return
            } else if (array.all { it.status == Status.PlayerO }) {
                gameModel.value = gameModel.value.copy(
                    isGameEnding = true,
                    winningPlayer = Status.PlayerO
                )
                return
            }
        }

        // Check for a tie
        if (rows.flatten().all { it.status != Status.Empty } && !gameModel.value.isGameEnding) {
            gameModel.value = gameModel.value.copy(isGameEnding = true)
        }
    }

    // Helper function to initialize the game field
    private fun initGameField(): Array<Array<Field>> {
        return Array(3) { i ->
            Array(3) { j ->
                Field(indexRow = i, indexColumn = j)
            }
        }
    }
}
