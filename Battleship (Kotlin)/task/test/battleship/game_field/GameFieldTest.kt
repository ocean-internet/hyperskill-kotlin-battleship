package battleship.game_field

import battleship.ship.Ship
import battleship.ship.Location
import battleship.ship.Type
import org.junit.Assert.*
import org.junit.Test

class GameFieldTest {

    @Test
    fun `it should get a list of all ShipType`() {
        val gameField = GameField()
        assertEquals(
            "[AIRCRAFT_CARRIER, BATTLESHIP, SUBMARINE, CRUISER, DESTROYER]", gameField.getShipTypesToAdd().toString()
        )
    }

    @Test
    fun `it should remove ShipType from list when added to GameField`() {

        val gameField = GameField()

        gameField.addShip(
            Ship(
                Type.CRUISER,
                Location(Coordinate("A1"), Coordinate("A3"))
            )
        )

        assertEquals(
            "[AIRCRAFT_CARRIER, BATTLESHIP, SUBMARINE, DESTROYER]",
            gameField.getShipTypesToAdd().toString()
        )
    }

    @Test
    fun `it should add a ship to the GameField`() {
        val gameField = GameField()

        gameField.addShip(
            Ship(
                Type.AIRCRAFT_CARRIER,
                Location(Coordinate("B4"), Coordinate("F4"))
            )
        )

        assertEquals(
            """
  1 2 3 4 5 6 7 8 9 10
A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
B ~ ~ ~ O ~ ~ ~ ~ ~ ~
C ~ ~ ~ O ~ ~ ~ ~ ~ ~
D ~ ~ ~ O ~ ~ ~ ~ ~ ~
E ~ ~ ~ O ~ ~ ~ ~ ~ ~
F ~ ~ ~ O ~ ~ ~ ~ ~ ~
G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        """,
            gameField.toString()
        )
    }

    @Test
    fun `it should not add the same ShipType twice`() {
        val shipType = Type.DESTROYER
        val exception = assertThrows(RuntimeException::class.java) {
            val gameField = GameField()

            gameField.addShip(Ship(shipType, Location(Coordinate("A1"), Coordinate("B1"))))
            gameField.addShip(Ship(shipType, Location(Coordinate("I10"), Coordinate("J10"))))
        }

        assertEquals("Already added ${shipType.label}", exception.message)
    }

    @Test
    fun `it should return true when all ShipType are added to GameField`() {

        val gameField = GameField()

        assertFalse(gameField.isReady())

        generateShips().forEach { gameField.addShip(it) }

        assertTrue(gameField.isReady())
    }

    @Test
    fun `it should return true when all ships are sunk`() {

        val gameField = GameField()
        val ships = generateShips()

        ships.forEach { gameField.addShip(it) }

        assertFalse(gameField.hasEnded())

        ships.forEach { ship -> ship.cells.forEach { gameField.takeAShot(it.coordinate) } }

        assertTrue(gameField.hasEnded())
    }

    @Test
    fun `it should take a shot and hit`() {

        val gameField = GameField()

        val coordinate = Coordinate("F3")
        generateShips().forEach { gameField.addShip(it) }

        assertEquals(CellStatus.SHIP, gameField.getCellValue(coordinate))

        assertNotNull(gameField.takeAShot(coordinate))
        assertEquals(CellStatus.HIT, gameField.getCellValue(coordinate))

        assertNotNull(gameField.takeAShot(coordinate))
        assertEquals(CellStatus.HIT, gameField.getCellValue(coordinate))
    }

    @Test
    fun `it should take a shot and miss`() {
        val gameField = GameField()

        val coordinate = Coordinate("F2")
        generateShips().forEach { gameField.addShip(it) }

        assertEquals(CellStatus.FOG, gameField.getCellValue(coordinate))

        assertNull(gameField.takeAShot(coordinate))
        assertEquals(CellStatus.MISS, gameField.getCellValue(coordinate))

        assertNull(gameField.takeAShot(coordinate))
        assertEquals(CellStatus.MISS, gameField.getCellValue(coordinate))
    }

    @Test
    fun `it should return game field as string`() {
        assertEquals(
            """
  1 2 3 4 5 6 7 8 9 10
A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        """,
            GameField().toString()
        )
    }

    @Test
    fun `it should return game field as string with fog of war`() {
        val gameField = GameField()

        gameField.addShip(
            Ship(
                Type.AIRCRAFT_CARRIER,
                Location(
                    Coordinate("A1"),
                    Coordinate("A5")
                )
            )
        )

        gameField.takeAShot(Coordinate("A1"))
        gameField.takeAShot(Coordinate("B1"))

        assertEquals(
            """
  1 2 3 4 5 6 7 8 9 10
A X ~ ~ ~ ~ ~ ~ ~ ~ ~
B M ~ ~ ~ ~ ~ ~ ~ ~ ~
C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        """,
            gameField.toStringWithFogOfWar()
        )
    }

    private fun generateShips() = listOf(
        Ship(Type.AIRCRAFT_CARRIER, Location(Coordinate("F3"), Coordinate("F7"))),
        Ship(Type.BATTLESHIP, Location(Coordinate("A1"), Coordinate("D1"))),
        Ship(Type.SUBMARINE, Location(Coordinate("J10"), Coordinate("J8"))),
        Ship(Type.CRUISER, Location(Coordinate("B9"), Coordinate("D9"))),
        Ship(Type.DESTROYER, Location(Coordinate("I2"), Coordinate("J2"))),
    )
}