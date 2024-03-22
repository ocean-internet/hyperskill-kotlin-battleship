package battleship

import battleship.game_field.Coordinate
import battleship.ship.Location
import org.junit.Assert.*
import org.junit.Test

class LocationTest {

    @Test
    fun `it should return start row of ShipLocation`() {
        listOf(
            listOf("C1", "D1", 'C'),
            listOf("D1", "C1", 'C'),
            listOf("D1", "D2", 'D'),
            listOf("D2", "D1", 'D')
        ).forEach { args ->
            assertEquals(
                args[2],
                Location(Coordinate(args[0].toString()), Coordinate(args[1].toString())).getStartRow()
            )
        }
    }

    @Test
    fun `it should return end row of ShipLocation`() {
        listOf(
            listOf("C1", "D1", 'D'),
            listOf("D1", "C1", 'D'),
            listOf("C1", "C2", 'C'),
            listOf("C2", "C1", 'C')
        ).forEach { args ->
            assertEquals(args[2], Location(Coordinate(args[0].toString()), Coordinate(args[1].toString())).getEndRow())
        }
    }

    @Test
    fun `it should return start col of ShipLocation`() {
        listOf(
            listOf("C1", "C2", 1),
            listOf("C2", "C1", 1),
            listOf("C2", "D2", 2),
            listOf("D2", "C2", 2)
        ).forEach { args ->
            assertEquals(
                args[2],
                Location(Coordinate(args[0].toString()), Coordinate(args[1].toString())).getStartCol()
            )
        }
    }

    @Test
    fun `it should return end col of ShipLocation`() {
        listOf(
            listOf("C1", "C2", 2),
            listOf("C2", "C1", 2),
            listOf("C1", "D1", 1),
            listOf("D1", "C1", 1)
        ).forEach { args ->
            assertEquals(args[2], Location(Coordinate(args[0].toString()), Coordinate(args[1].toString())).getEndCol())
        }
    }

    @Test
    fun `it should return true if ShipLocation is vertical`() {
        listOf(
            listOf("C1", "D1"),
            listOf("D1", "C1")
        ).forEach { args ->
            assertTrue(Location(Coordinate(args[0]), Coordinate(args[1])).isVertical())
        }
    }

    @Test
    fun `it should return false if ShipLocation is horizontal`() {
        listOf(
            listOf("C1", "C2"),
            listOf("C2", "C1")
        ).forEach { args ->
            assertFalse(Location(Coordinate(args[0]), Coordinate(args[1])).isVertical())
        }
    }

    @Test
    fun `it should return true if ShipLocation is horizontal`() {
        listOf(
            listOf("C1", "C2"),
            listOf("C2", "C1")
        ).forEach { args ->
            assertTrue(Location(Coordinate(args[0]), Coordinate(args[1])).isHorizontal())
        }
    }

    @Test
    fun `it should return false if ShipLocation is vertical`() {
        listOf(
            listOf("C1", "D1"),
            listOf("D1", "C1")
        ).forEach { args ->
            assertFalse(Location(Coordinate(args[0]), Coordinate(args[1])).isHorizontal())
        }
    }

    @Test
    fun `it should return the number of cells the ShipLocation occupies`() {
        listOf(
            listOf("F3", "F7", 5),
            listOf("F7", "F3", 5),
            listOf("A1", "D1", 4),
            listOf("D1", "A1", 4),
            listOf("J10", "J8", 3),
            listOf("I2", "J2", 2),
        ).forEach { args ->
            assertEquals(args[2], Location(Coordinate(args[0].toString()), Coordinate(args[1].toString())).getSize())
        }
    }
}