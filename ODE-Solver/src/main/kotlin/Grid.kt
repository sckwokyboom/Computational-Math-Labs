import java.math.BigDecimal
import java.math.MathContext.DECIMAL128
import java.math.MathContext.UNLIMITED

data class Grid(val a: BigDecimal, val b: BigDecimal, val n: Int) : Iterable<Pair<Int, BigDecimal>> {
    val h: BigDecimal
    val size: Int = n

    init {
        if (b <= a) throw IllegalArgumentException("Правая граница сетки должна быть больше левой границы")
        if (n < 2) throw IllegalArgumentException("Количество точек сетки не должно быть меньше двух")
        h = (b.subtract(a, UNLIMITED)).divide((n).toBigDecimal(UNLIMITED), DECIMAL128)

    }

    override fun iterator(): Iterator<Pair<Int, BigDecimal>> {
        return object : Iterator<Pair<Int, BigDecimal>> {
            private var j: Int = 0
            private var jEnd: Int = n

            override fun hasNext(): Boolean = j < jEnd

            override fun next(): Pair<Int, BigDecimal> {
                if (!hasNext()) throw NoSuchElementException("Пройдены все точки сетки")

                val res = a.add(h.multiply(j.toBigDecimal(UNLIMITED), UNLIMITED), UNLIMITED)
                val computed = j to res
                j++
                return computed
            }
        }
    }

    operator fun get(j: Int): BigDecimal {
        if (j >= n || j < 0) throw IllegalArgumentException("Выход за пределы сетки")
        return a.add(h.multiply(j.toBigDecimal(UNLIMITED), UNLIMITED), UNLIMITED)
    }
}