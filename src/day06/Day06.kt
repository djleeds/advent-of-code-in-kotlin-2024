package day06

import lib.grid.Coordinates
import lib.grid.Direction
import lib.grid.Grid
import solve

fun main() {
    fun parse(input: List<String>): Grid<Char> {
        return Grid.parseCharacters(input, { it }, '.')
    }

    fun Direction.turnRight() = when (this) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST  -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST  -> Direction.NORTH
        else            -> throw IllegalArgumentException()
    }

    fun part1(input: List<String>): Int {
        val grid: Grid<Char> = parse(input)
        val history = mutableListOf<Coordinates>()
        var guardDirection = Direction.NORTH
        var guardPosition = grid.find { it.item == '^' }.coordinates

        while (grid.bounds.contains(guardPosition)) {
            history.add(guardPosition)
            val nextStep: Coordinates = guardPosition.step(guardDirection)
            if (grid[nextStep] == '#') {
                guardDirection = guardDirection.turnRight()
            } else {
                guardPosition = nextStep
            }
        }

        return history.toSet().count()
    }

    // Correctly solves the sample puzzle, but not the full puzzle input
    fun part2(input: List<String>): Int {
        data class History(val coordinates: Coordinates, val direction: Direction, val step: Int)
        data class ObstacleHit(val coordinatesOfObstacle: Coordinates, val originalDirection: Direction, val resultingDirection: Direction, val step: Int)

        var stepCount = 0

        val grid: Grid<Char> = parse(input)
        val history = mutableListOf<History>()
        val obstaclesHit = mutableListOf<ObstacleHit>()
        var guardDirection = Direction.NORTH
        var guardPosition = grid.find { it.item == '^' }.coordinates
        history.add(History(guardPosition, guardDirection, stepCount))

        // Build out the history
        while (grid.bounds.contains(guardPosition)) {
            val nextStep: Coordinates = guardPosition.step(guardDirection)
            if (grid[nextStep] == '#') {
                obstaclesHit.add(ObstacleHit(nextStep, guardDirection, guardDirection.turnRight(), stepCount))
                guardDirection = guardDirection.turnRight()
            } else {
                guardPosition = nextStep
                stepCount++
                history.add(History(guardPosition, guardDirection, stepCount))
            }
        }

        val obstaclesToAdd = mutableListOf<Coordinates>()

        // For each step in the history, find the first obstacle to the right that was hit in the `right` direction

        // NORTHBOUND
        history
            .filter { it.direction == Direction.NORTH }
            .forEach { historyStep ->
                val obstacle = obstaclesHit
                    .filter {
                        it.originalDirection == Direction.EAST &&
                                it.coordinatesOfObstacle.y == historyStep.coordinates.y &&
                                it.coordinatesOfObstacle.x > historyStep.coordinates.x &&
                                it.step < historyStep.step
                    }.minByOrNull { it.coordinatesOfObstacle.x }
                if (obstacle != null) {
                    obstaclesToAdd.add(historyStep.coordinates.step(Direction.NORTH))
                }
            }

        // SOUTHBOUND
        history
            .filter { it.direction == Direction.SOUTH }
            .forEach { historyStep ->
                val obstacle = obstaclesHit
                    .filter {
                        it.originalDirection == Direction.WEST &&
                                it.coordinatesOfObstacle.y == historyStep.coordinates.y &&
                                it.coordinatesOfObstacle.x < historyStep.coordinates.x &&
                                it.step < historyStep.step
                    }.maxByOrNull { it.coordinatesOfObstacle.x }
                if (obstacle != null) {
                    obstaclesToAdd.add(historyStep.coordinates.step(Direction.SOUTH))
                }
            }

        // EASTBOUND
        history
            .filter { it.direction == Direction.EAST }
            .forEach { historyStep ->
                val obstacle = obstaclesHit
                    .filter {
                        it.originalDirection == Direction.SOUTH &&
                                it.coordinatesOfObstacle.x == historyStep.coordinates.x &&
                                it.coordinatesOfObstacle.y > historyStep.coordinates.y &&
                                it.step < historyStep.step
                    }.minByOrNull { it.coordinatesOfObstacle.y }
                if (obstacle != null) {
                    obstaclesToAdd.add(historyStep.coordinates.step(Direction.EAST))
                }
            }

        // WESTBOUND
        history
            .filter { it.direction == Direction.WEST }
            .forEach { historyStep ->
                val obstacle = obstaclesHit
                    .filter {
                        it.originalDirection == Direction.NORTH &&
                                it.coordinatesOfObstacle.x == historyStep.coordinates.x &&
                                it.coordinatesOfObstacle.y < historyStep.coordinates.y &&
                                it.step < historyStep.step
                    }.maxByOrNull { it.coordinatesOfObstacle.y }
                if (obstacle != null) {
                    obstaclesToAdd.add(historyStep.coordinates.step(Direction.WEST))
                }
            }

        return obstaclesToAdd.count()
    }

    solve(::part1, withInput = "day06/test", andAssert = 41)
    solve(::part1, withInput = "day06/input", andAssert = 4752)

    solve(::part2, withInput = "day06/test", andAssert = 6)
    solve(::part2, withInput = "day06/input")
}
