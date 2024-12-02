package day01

import solve
import kotlin.math.absoluteValue

fun main() {
    fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
        val pairs = input.map {
            val (a, b) = it.split("   ")
            a.toInt() to b.toInt()
        }

        val list1 = pairs.map { it.first }.sorted()
        val list2 = pairs.map { it.second }.sorted()

        return list1 to list2
    }

    fun part1(input: List<String>): Int {
        val (list1, list2) = parse(input)
        check(list1.size == list2.size)

        val result = list1.zip(list2) { left, right -> (left - right).absoluteValue }.sum()
        return result
    }

    fun part2(input: List<String>): Int {
        val (list1, list2) = parse(input)

        val result = list1.sumOf { left ->
            val appearances = list2.count { it == left }
            left * appearances
        }

        return result
    }

    solve(::part1, withInput = "day01/test", andAssert = 11)
    solve(::part1, withInput = "day01/input", andAssert = 1765812)

    solve(::part2, withInput = "day01/test", andAssert = 31)
    solve(::part2, withInput = "day01/input", andAssert = 20520794)
}
