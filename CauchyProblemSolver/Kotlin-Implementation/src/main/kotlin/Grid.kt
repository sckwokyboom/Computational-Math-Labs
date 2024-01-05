import kotlin.math.absoluteValue

data class Grid(val start: Double, val end: Double, val numOfGridNodes: Int) : Iterable<Pair<Int, Double>> {
    val stepLength: Double
    val size: Int = numOfGridNodes

    init {
        if (end <= start) throw IllegalArgumentException("Правая граница сетки должна быть больше левой границы")
        if (numOfGridNodes < 2) throw IllegalArgumentException("Количество точек сетки не должно быть меньше двух")
        stepLength = (end - start).absoluteValue / numOfGridNodes
    }

    override fun iterator(): Iterator<Pair<Int, Double>> {
        return object : Iterator<Pair<Int, Double>> {
            private var j: Int = 0
            private var jEnd: Int = numOfGridNodes

            override fun hasNext(): Boolean = j < jEnd

            override fun next(): Pair<Int, Double> {
                if (!hasNext()) throw NoSuchElementException("Пройдены все точки сетки")

                val res = start + stepLength * j
                val computed = j to res
                j++
                return computed
            }
        }
    }

    operator fun get(indexOfNode: Int): Double {
        if (indexOfNode >= numOfGridNodes || indexOfNode < 0) throw IllegalArgumentException("Выход за пределы сетки")
        return start + stepLength * indexOfNode
    }
}