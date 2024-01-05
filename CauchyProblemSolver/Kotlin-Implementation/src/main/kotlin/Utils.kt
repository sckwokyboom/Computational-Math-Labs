import kotlin.math.absoluteValue

typealias InitialConditions = Map<Coord, Double>

object Suggester {
    fun suggest(a: Double, b: Double, h: Double): Int {
        val n = ((b - a).absoluteValue / h).toInt()
        val actualH = (b - a).absoluteValue / n
        return if (actualH < h) {
            n + 1
        } else {
            n
        }
    }
}