package battleship.game_field

import battleship.ship.Ship
import battleship.ship.Type as ShipType

private const val ERROR_WRONG_LENGTH_OF_S = "Wrong length of the %s!"
private const val ERROR_SHIP_TOO_CLOSE_TO_ANOTHER = "You placed it too close to another one."

private const val ERROR_ALREADY_ADDED_S = "Already added %s"

class GameField {

    private val rowLabels = ('A'..'J').toList()
    private val cols = (1..10).map { it.toString() }
    private val rows = (1..10).map { cols.map { CellStatus.FOG }.toMutableList() }.toMutableList()

    private val ships = emptyMap<ShipType, Ship>().toMutableMap()

    fun getShipTypesToAdd(): List<ShipType> = ShipType.values().filter { !ships.contains(it) }
    fun addShip(ship: Ship) {

        assertShipNotAdded(ship)
        if (ship.location.size != ship.type.size) throw RuntimeException(ERROR_WRONG_LENGTH_OF_S.format(ship.type.label))
        if (isTooCloseToAnotherShip(ship)) throw RuntimeException(ERROR_SHIP_TOO_CLOSE_TO_ANOTHER)

        ships[ship.type] = ship
        when (ship.location.isHorizontal) {
            true -> {
                val rowLabel: Char = ship.location.startRow
                val row = getRowFromLabel(rowLabel)
                if (!(1..10).contains(row)) throw RuntimeException()

                (ship.location.startCol..ship.location.endCol)
                    .forEach { col -> plotCellValue(Coordinate("${getLabelFromRow(row)}$col"), CellStatus.SHIP) }
            }

            false -> {
                val col: Int = ship.location.startCol

                (ship.location.startRow..ship.location.endRow)
                    .forEach { row -> plotCellValue(Coordinate("$row$col"), CellStatus.SHIP) }
            }
        }
    }

    private fun assertShipNotAdded(ship: Ship) {
        if (ships.containsKey(ship.type)) throw RuntimeException(ERROR_ALREADY_ADDED_S.format(ship.type.label))
    }

    fun isReady(): Boolean = ShipType.values().size == ships.size
    fun hasEnded(): Boolean = ships.values.filter { it.isSunk }.size == ships.size

    fun takeAShot(coordinate: Coordinate): Ship? {

        val ship = ships.values.firstOrNull { it.occupiesCell(coordinate) }
        ship?.takeShot(coordinate)

        val cellValue = if (ship?.isHit == true) CellStatus.HIT else CellStatus.MISS
        plotCellValue(coordinate, cellValue)

        return ship
    }

    fun getCellValue(coordinate: Coordinate) = rows[getRowFromLabel(coordinate.row) - 1][coordinate.col - 1]

    fun toStringWithFogOfWar(): String = this.toString().replace(CellStatus.SHIP.label, CellStatus.FOG.label)

    override fun toString(): String {
        return """
${getRowString(row = cols)}
${rowLabels.mapIndexed { k, label -> "$label ${rows[k].map { it.label }.joinToString(" ")}" }.joinToString("\n")}
        """
    }

    private fun plotCellValue(coordinate: Coordinate, cellValue: CellStatus) {
        rows[getRowFromLabel(coordinate.row) - 1][coordinate.col - 1] = cellValue
    }

    private fun getRowFromLabel(rowLabel: Char) = rowLabels.indexOf(rowLabel) + 1

    private fun getLabelFromRow(row: Int) = rowLabels.toList()[row - 1]

    private fun isTooCloseToAnotherShip(ship: Ship): Boolean {
        val startCol = 1.coerceAtLeast(ship.location.startCol - 1)
        val endCol = cols.size.coerceAtMost(ship.location.endCol + 1)

        val startRow = 1.coerceAtLeast(rowLabels.indexOf(ship.location.startRow))
        val endRow = rows.size.coerceAtMost(rowLabels.indexOf(ship.location.endRow) + 2)

        (startCol..endCol).forEach { col ->
            (startRow..endRow).forEach { row ->
                if (getCellValue(Coordinate("${getLabelFromRow(row)}$col")) == CellStatus.SHIP) return true
            }
        }

        return false
    }

    private fun getRowString(key: Char = ' ', row: List<String>) = "$key ${row.joinToString(" ").trim()}"
}
