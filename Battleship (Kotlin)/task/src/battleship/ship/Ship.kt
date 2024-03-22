package battleship.ship

import battleship.game_field.CellStatus
import battleship.game_field.Coordinate

class Ship(val type: Type, val location: Location) {

    var cells: List<Cell> = when (location.isHorizontal) {
        true -> (location.startCol..location.endCol).map { col -> Cell(Coordinate("${location.startRow}$col")) }
        false -> (location.startRow..location.endRow).map { row -> Cell(Coordinate("$row${location.startCol}")) }
    }

    val isHit: Boolean get() = cells.any { it.isHit }
    val isSunk: Boolean get() = cells.filter { it.isHit }.size == cells.size

    fun occupiesCell(coordinate: Coordinate): Boolean = cells.any { it.coordinate.toString() == coordinate.toString() }
    fun takeShot(coordinate: Coordinate): CellStatus {
        val cell: Cell? = cells.find { it.coordinate.toString() == coordinate.toString() }
        cell?.hit()
        return if (cell != null) CellStatus.HIT else CellStatus.MISS
    }
}
