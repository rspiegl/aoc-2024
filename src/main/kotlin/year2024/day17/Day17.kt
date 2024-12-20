package year2024.day17

import utils.getAllNumbers
import utils.println
import utils.readInput
import kotlin.math.pow

fun main() {
    val year = 2024
    val day = 17

    fun runProgram(
        instructions: List<Long>,
        startA: Long
    ): MutableList<Long> {
        var regA = startA
        var regB = 0L
        var regC = 0L
        fun getLiteralOperand(operand: Long): Long = operand
        fun getComboOperand(operand: Long): Long = when (operand) {
            0L, 1L, 2L, 3L -> operand
            4L -> regA
            5L -> regB
            6L -> regC
            7L -> error("Reserved operand: $operand")
            else -> error("Unknown operand $operand")
        }

        val output = mutableListOf<Long>()
        var instructionPointer = 0
        while (instructionPointer < instructions.lastIndex) {
            val opcode = instructions[instructionPointer]
            val operand = instructions[instructionPointer + 1]

            when (opcode) {
                0L -> { //adv
                    val denominator = 2.0.pow(getComboOperand(operand).toDouble())
                    regA = (regA / denominator).toLong()
                    instructionPointer += 2
                }

                1L -> { //bxl
                    regB = regB xor getLiteralOperand(operand)
                    instructionPointer += 2
                }

                2L -> { //bst
                    regB = getComboOperand(operand) % 8
                    instructionPointer += 2
                }

                3L -> { //jnz
                    if (regA != 0L) {
                        instructionPointer = getLiteralOperand(operand).toInt()
                    } else {
                        instructionPointer += 2
                    }
                }

                4L -> { //bxc
                    regB = regB xor regC
                    instructionPointer += 2
                }

                5L -> { //out
                    val out = getComboOperand(operand) % 8
                    output.add(out)
                    instructionPointer += 2
                }

                6L -> { //bdv
                    val denominator = 2.0.pow(getComboOperand(operand).toDouble())
                    regB = (regA / denominator).toLong()
                    instructionPointer += 2
                }

                7L -> { //cdv
                    val denominator = 2.0.pow(getComboOperand(operand).toDouble())
                    regC = (regA / denominator).toLong()
                    instructionPointer += 2
                }
            }
        }
        return output
    }

    fun part1(input: List<String>): String {
        val regA = input[0].getAllNumbers()[0].toLong()
        val instructions = input[4].removePrefix("Program: ").split(",").map { it.toLong() }

        val output = runProgram(instructions, regA)

        return output.joinToString(",")
    }

    fun simulateUntilMatch(instructions: List<Long>, match: List<Long>): Long {
        var regA = if (match.size == 1) {
            0
        } else {
            8 * simulateUntilMatch(instructions, match.drop(1))
        }

        while (runProgram(instructions, regA) != match) {
            regA++
        }

        return regA
    }

    fun part2(input: List<String>): Long {
        val instructions = input[4].removePrefix("Program: ").split(",").map { it.toLong() }

        // credit goes to u/seekerdarksteel
        val aForMatch = simulateUntilMatch(instructions, instructions)

        return aForMatch
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    val input = readInput(year, day)
    part1(input).println()

    val testInput2 = readInput(year, day, suffix = "_test_2")
    check(part2(testInput2) == 117440L)
    part2(input).println()
}
