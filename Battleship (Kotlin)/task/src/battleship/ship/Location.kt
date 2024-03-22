package battleship.ship

import battleship.ERROR_INVALID_SHIP_LOCATION
import battleship.game_field.Coordinate

class Location(
    private val startCoordinate: Coordinate,
    private val endCoordinate: Coordinate
) {
    init {
        if (!isValid()) throw RuntimeException(ERROR_INVALID_SHIP_LOCATION)
    }

    val startRow: Char = listOf(startCoordinate.row, endCoordinate.row).minOf { it }
    val endRow: Char = listOf(startCoordinate.row, endCoordinate.row).maxOf { it }

    val startCol: Int = listOf(startCoordinate.col, endCoordinate.col).minOf { it }
    val endCol: Int = listOf(startCoordinate.col, endCoordinate.col).maxOf { it }

    val isHorizontal = startRow == endRow

    val size: Int = when(isHorizontal) {
        true -> {
            val startEnd = listOf(startCol,endCol).sorted()
            (startEnd.first()..startEnd.last()).toList().size
        }
        false -> {
            val startEnd = listOf(startRow,endRow).sorted()
            (startEnd.first()..startEnd.last()).toList().size
        }
    }

    private fun isValid(): Boolean = startCoordinate.row == endCoordinate.row || startCoordinate.col == endCoordinate.col
}
