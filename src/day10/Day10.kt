fun main() {
    val latestDay = 10

    fun part1(input: List<String>): Int {
        val topography = input.toIntMap()
        val hikingStarts = topography.getLocations(0)
        val hikingEnds = topography.getLocations(9)
        val adjacencyList = topography.buildAdjacencyList()
        val sum = hikingStarts.sumOf { hikingStart ->
            val visited = bfs(adjacencyList, hikingStart)
            val visitedEnds = visited.filter { it in hikingEnds }
            visitedEnds.size
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val topography = input.toIntMap()
        val hikingStarts = topography.getLocations(0)
        val hikingEnds = topography.getLocations(9)
        val adjacencyList = topography.buildAdjacencyList()
        val sum = hikingStarts.sumOf { hikingStart ->
            val pathEnds = bfsPathEnds(adjacencyList, hikingStart)
            val visitedEnds = pathEnds.filter { it in hikingEnds }
            visitedEnds.size
        }
        return sum
    }

    val testInput = readInput("$latestDay", suffix = "_test")
    check(part1(testInput) == 2)
    val input = readInput("$latestDay")
    part1(input).println()

    check(part2(testInput) == 227)
    part2(input).println()
}
