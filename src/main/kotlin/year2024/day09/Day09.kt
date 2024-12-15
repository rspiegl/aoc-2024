package year2024.day09

import utils.println
import utils.readInput

fun main() {
    val year = 2024
    val day = 9

    fun part1(input: List<String>): Long {
        val diskMap = input[0].asSequence().map { it.digitToInt() }
        var fileId = 0L
        val blockList = diskMap.mapIndexed { index, digit ->
            val isFile = index % 2 == 0
            if (isFile) {
                FileBlock(fileId++, digit)
            } else {
                FreeBlock(digit)
            }
        }.toMutableList()
        var fileBlock = blockList.findLastFileBlock()
        var freeBlock = blockList.findFirstFreeBlock()

        while (freeBlock.first < fileBlock.first) {
            val freeSpace = freeBlock.second.length
            val fileSpace = fileBlock.second.length
            if (freeSpace > fileSpace) {
                freeBlock.second.reduceBy(fileSpace)
                blockList.removeAt(fileBlock.first)
                blockList.add(freeBlock.first, fileBlock.second)
            } else if (freeSpace == fileSpace) {
                blockList.removeAt(fileBlock.first)
                blockList.removeAt(freeBlock.first)
                blockList.add(freeBlock.first, fileBlock.second)
            } else {
                blockList.removeAt(freeBlock.first)
                blockList.add(freeBlock.first, fileBlock.second.copy(newLength = freeSpace))
                fileBlock.second.reduceBy(freeSpace)
            }

            fileBlock = blockList.findLastFileBlock()
            freeBlock = blockList.findFirstFreeBlock()
        }
        var position = 0L
        var sum = 0L
        blockList.forEach { block ->
            (block as? FileBlock)?.toChecksum()?.forEach { id ->
                sum += id * position++
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val diskMap = input[0].asSequence().map { it.digitToInt() }
        var fileId = 0L
        val blockList = diskMap.mapIndexed { index, digit ->
            val isFile = index % 2 == 0
            if (isFile) {
                FileBlock(fileId++, digit)
            } else {
                FreeBlock(digit)
            }
        }.toMutableList()
        var fileBlock = blockList.findLastFileBlock()
        var freeBlock = blockList.findFirstFreeBlock()
        var lastFileBlockIndex: Int
        while (freeBlock.first < fileBlock.first) {
            lastFileBlockIndex = fileBlock.first
            var lastFreeBlockIndex: Int
            while (freeBlock.first < fileBlock.first) {
                lastFreeBlockIndex = freeBlock.first
                val freeSpace = freeBlock.second.length
                val fileSpace = fileBlock.second.length
                if (freeSpace > fileSpace) {
                    freeBlock.second.reduceBy(fileSpace)
                    blockList.removeAt(fileBlock.first)
                    blockList.add(fileBlock.first, FreeBlock(fileSpace))
                    blockList.add(freeBlock.first, fileBlock.second)
                    break
                } else if (freeSpace == fileSpace) {
                    blockList.removeAt(fileBlock.first)
                    blockList.add(fileBlock.first, FreeBlock(fileSpace))
                    blockList.removeAt(freeBlock.first)
                    blockList.add(freeBlock.first, fileBlock.second)
                    break
                }
                freeBlock = blockList.findFirstFreeBlockLaterThan(lastFreeBlockIndex)
            }
            fileBlock = blockList.findLastFileBlockEarlierThan(lastFileBlockIndex)
            freeBlock = blockList.findFirstFreeBlock()
        }
        var position = 0L
        var sum = 0L
        blockList.forEach { block ->
            block.toChecksum().forEach { id ->
                sum += id * position++
            }
        }
        return sum
    }

    val testInput = readInput(year, day, suffix = "_test")
    check(part1(testInput) == 1928L)
    val input = readInput(year, day)
    part1(input).println()

    check(part2(testInput) == 2858L)
    part2(input).println()
}

fun MutableList<Block>.findLastFileBlock(): Pair<Int, FileBlock> {
    for (i in indices.reversed()) {
        if (this[i] is FileBlock) {
            return i to (this[i] as FileBlock)
        }
    }
    throw RuntimeException("No file blocks found.")
}

fun MutableList<Block>.findLastFileBlockEarlierThan(index: Int): Pair<Int, FileBlock> {
    for (i in (index - 1) downTo 0) {
        if (this[i] is FileBlock) {
            return i to (this[i] as FileBlock)
        }
    }
    throw RuntimeException("No file blocks found.")
}

fun MutableList<Block>.findFirstFreeBlock(): Pair<Int, FreeBlock> {
    for (i in indices) {
        if (this[i] is FreeBlock) {
            return i to (this[i] as FreeBlock)
        }
    }
    throw RuntimeException("No free blocks found.")
}

fun MutableList<Block>.findFirstFreeBlockLaterThan(index: Int): Pair<Int, FreeBlock> {
    for (i in (index + 1) until size) {
        if (this[i] is FreeBlock) {
            return i to (this[i] as FreeBlock)
        }
    }
    throw RuntimeException("No free blocks found.")
}

interface Block {
    fun toChecksum(): List<Long>
}

class FileBlock(val id: Long, var length: Int) : Block {

    fun reduceBy(amount: Int) {
        length -= amount
    }

    override fun toChecksum(): List<Long> {
        return List(length) { id }
    }

    override fun toString(): String {
        return "day09.FileBlock(id=$id, length=$length)"
    }

    fun copy(newLength: Int): Block {
        return FileBlock(id, newLength)
    }
}

class FreeBlock(var length: Int) : Block {

    override fun toChecksum(): List<Long> {
        return List(length) { 0L }
    }

    override fun toString(): String {
        return "day09.FreeBlock(length=$length)"
    }

    fun reduceBy(amount: Int) {
        length -= amount
    }
}
