package com.akshay.adventofcode

import java.io.File

data class Move(val quantity: Int, val source: Int, val target: Int) {

    companion object {
        fun of(line: String): Move {
            return line
                .split(" ")
                .filterIndexed { index, _ -> index % 2 == 1 }
                .map { it.toInt() }
                .let { Move(it[0], it[1] - 1, it[2] - 1) }
        }
    }
}

/**
 * Advent of Code 2022 - Day 5 (Supply Stacks)
 * http://adventofcode.com/2022/day/5
 */
class Day05 {

    fun partOne(stacks: List<ArrayDeque<Char>>, moves: List<Move>): String {
        moves.forEach { move -> repeat(move.quantity) { stacks[move.target].addFirst(stacks[move.source].removeFirst()) } }
        return stacks.map { it.first() }.joinToString(separator = "")
    }

    fun partTwo(stacks: List<ArrayDeque<Char>>, moves: List<Move>): String {
        moves.forEach { step ->
            stacks[step.source]
                .subList(0, step.quantity)
                .asReversed()
                .map { stacks[step.target].addFirst(it) }
                .map { stacks[step.source].removeFirst() }
        }
        return stacks.map { it.first() }.joinToString(separator = "")
    }

    fun populateStacks(lines: List<String>, onCharacterFound: (Int, Char) -> Unit) {
        lines
            .filter { it.contains("[") }
            .forEach { line ->
                line.mapIndexed { index, char ->
                    if (char.isLetter()) {
                        val stackNumber = index / 4
                        val value = line[index]
                        onCharacterFound(stackNumber, value)
                    }
                }
            }
    }
}

fun main() {
    val lines = File("input.txt").readLines()
    val day05 = Day05()

    // Calculate number of stacks needed
    val stacks = List(9) { ArrayDeque<Char>() }

    // Fill the stacks
    day05.populateStacks(lines) { stackNumber, value ->
        stacks[stackNumber].addLast(value)
    }

    // Get the moves
    val moves = lines.filter { it.contains("move") }.map { Move.of(it) }

    // Perform the moves
    println(day05.partOne(stacks.map { ArrayDeque(it) }.toList(), moves))
    println(day05.partTwo(stacks, moves))
}