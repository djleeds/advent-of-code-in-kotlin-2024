package day02

import solve
import kotlin.math.absoluteValue

fun main() {
    val tolerance = (1..3)

    fun parse(input: List<String>) = input.map { line -> line.split(" ").map { it.toInt() } }
    fun isAllIncreasing(levels: List<Int>) = levels.zipWithNext().all { it.first < it.second }
    fun isAllDecreasing(levels: List<Int>) = levels.zipWithNext().all { it.first > it.second }

    fun isSafeDifference(levels: List<Int>) =
        levels.zipWithNext().all { (it.first - it.second).absoluteValue in tolerance }

    fun part1(input: List<String>): Int {
        fun isSafe(levels: List<Int>) = isSafeDifference(levels) && (isAllIncreasing(levels) || isAllDecreasing(levels))
        return parse(input).count { isSafe(it) }
    }

    fun part2(input: List<String>): Int {
        fun isSafe(levels: List<Int>): Boolean {
            val permutations: MutableList<List<Int>> = mutableListOf(levels)
            for (index in levels.indices) {
                permutations.add(levels.toMutableList().apply { removeAt(index) })
            }

            return permutations.any { isSafeDifference(it) && (isAllIncreasing(it) || isAllDecreasing(it)) }
        }


        return parse(input).count { isSafe(it) }
    }

    solve(::part1, withInput = "day02/test", andAssert = 2)
    solve(::part1, withInput = "day02/input", andAssert = 242)

    solve(::part2, withInput = "day02/test", andAssert = 4)
    solve(::part2, withInput = "day02/input", andAssert = 311)
}
