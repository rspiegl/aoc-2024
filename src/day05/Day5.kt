package day05

import utils.println
import utils.readInput

fun main() {
    val latestDay = 5

    fun breaksARule(update: List<Int>, beforeRules: Map<Int, List<Rule>>, afterRules: Map<Int, List<Rule>>): Rule? {
        update.forEachIndexed { index, page ->
            beforeRules[page]?.forEach { rule ->
                val afterPosition = update.indexOf(rule.after)
                val invalidRule = afterPosition >= 0 && index > afterPosition
                if (invalidRule)
                    return rule
            }
            afterRules[page]?.forEach { rule ->
                val beforePosition = update.indexOf(rule.before)
                val invalidRule = beforePosition >= 0 && index < beforePosition
                if (invalidRule)
                    return rule
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        val rules = input.filter { "|" in it }.map { Rule.from(it) }
        val beforeRules = rules.groupBy { it.before }
        val afterRules = rules.groupBy { it.after }

        val updates = input.filter {"," in it }.map {it.split(",").map { page -> page.toInt() }}
        return updates.sumOf { update ->
            val brokenRule = breaksARule(update, beforeRules, afterRules)
            if (brokenRule == null) update[update.size/2] else 0
        }
    }

    fun MutableList<Int>.swapElements(index1: Int, index2: Int) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }

    fun MutableList<Int>.swapElementsByValue(value1: Int, value2: Int) {
        val index1 = this.indexOf(value1)
        val index2 = this.indexOf(value2)
        swapElements(index1, index2)
    }

    fun part2(input: List<String>): Int {
        val rules = input.filter { "|" in it }.map { Rule.from(it) }
        val beforeRules = rules.groupBy { it.before }
        val afterRules = rules.groupBy { it.after }

        val updates = input.filter {"," in it }.map {it.split(",").map { page -> page.toInt() }}
        return updates.sumOf { update ->
            var brokenRule = breaksARule(update, beforeRules, afterRules)
            if (brokenRule != null) {
                val toFixUpdate = update.toMutableList()
                do {
                    toFixUpdate.swapElementsByValue(brokenRule!!.before, brokenRule.after)
                    brokenRule = breaksARule(toFixUpdate, beforeRules, afterRules)
                } while (brokenRule != null)
                toFixUpdate[toFixUpdate.size/2]
            } else {
                0
            }
        }
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 143)
    val input = readInput("$latestDay")
    part1(input).println()

    check(part2(testInput) == 123)
    part2(input).println()
}

data class Rule(val before: Int, val after: Int) {
    companion object {
        fun from(line: String): Rule {
            val (before, after) = line.split("|").map { it.toInt() }
            return Rule(before, after)
        }
    }
}
