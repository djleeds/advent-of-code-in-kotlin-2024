package day03

import solve

fun main() {
    val regexMul = Regex("mul\\((\\d+),(\\d+)\\)")

    fun part1(input: List<String>): Int {
        val result = input.sumOf { line ->
            regexMul.findAll(line).sumOf { match ->
                val (_, a, b) = match.groupValues
                a.toInt() * b.toInt()
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val regexDo = Regex("do\\(\\)")
        val regexDont = Regex("don\'t\\(\\)")
        val joined = input.joinToString("|")

        val muls = regexMul.findAll(joined)
        val dos = listOf(true to 0) + regexDo.findAll(joined).map { true to it.range.last }
        val donts = regexDont.findAll(joined).map { false to it.range.last } + (false to joined.length)
        val enablement = (dos + donts)
            .sortedBy { it.second }
            .zipWithNext()
            .map { (p1, p2) -> p1.first to p1.second..<p2.second }

        println(enablement)

        val filtered = muls.filter { mul -> enablement.first { mul.range.first in it.second }.first }
        val result = filtered.sumOf { match ->
            val (_, a, b) = match.groupValues
            a.toInt() * b.toInt()
        }
        return result
    }

    solve(::part1, withInput = "day03/test", andAssert = 161)
    solve(::part1, withInput = "day03/input", andAssert = 174103751)

    solve(::part2, withInput = "day03/test2", andAssert = 48)
    solve(::part2, withInput = "day03/input", andAssert = 100411201)
}
