package battleship

import battleship.game_field.*

const val ERROR_INVALID_SHIP_LOCATION = "Wrong ship location!"
const val ERROR_INVALID_COORDINATES = "You entered the wrong coordinates!"

fun main() {

    val game = Game()

    Player.values().forEach {
        val gameField = if (it.number == 1) game.player1gameField else game.player2gameField

        println("${it.label}, place your ships on the game field")

        println(gameField.toString())

        game.prepareGameField(gameField)

        println("Press Enter and pass the move to another player")
        readln()
    }

    var currentPlayer = Player.PLAYER_1

    do {
        val playerGameField = if (currentPlayer.number == 1) game.player1gameField else game.player2gameField
        val opponentGameField = if (currentPlayer.number == 1) game.player2gameField else game.player1gameField

        println(opponentGameField.toStringWithFogOfWar().trimEnd())
        println(CharArray(21) { '-' })
        println(playerGameField.toString().trimStart())

        println("${currentPlayer.label}, it's your turn:")

        try {
            val coordinate = readln().trim()
            if (coordinate.isBlank()) throw RuntimeException(ERROR_INVALID_COORDINATES)

            val ship = opponentGameField.takeAShot(Coordinate(coordinate))

            val message =
                when {
                    opponentGameField.hasEnded() -> "You sank the last ship. You won. Congratulations!"
                    ship?.isSunk == true -> "You sank a ship!"
                    ship?.occupiesCell(Coordinate(coordinate)) == true -> "You hit a ship!"
                    else -> "You missed!"
                }

            println(message)
            if (!opponentGameField.hasEnded()) println("Press Enter and pass the move to another player")

            currentPlayer = if (currentPlayer.number == 1) Player.PLAYER_2 else Player.PLAYER_1

        } catch (exception: RuntimeException) {
            println("Error! ${exception.message} Try again:")
        }
    } while (!game.hasEnded())
}