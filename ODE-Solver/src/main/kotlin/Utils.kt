import java.math.BigDecimal
import java.math.MathContext
import java.math.MathContext.DECIMAL128
import java.math.MathContext.UNLIMITED

typealias InitialConditions = Map<Int, BigDecimal>

object Suggester {
    fun suggest(a: BigDecimal, b: BigDecimal, h: BigDecimal): Int {
        val n = ((b - a) / h).toInt()
        val actualH = (b.subtract(a, UNLIMITED)).divide(n.toBigDecimal(UNLIMITED), DECIMAL128)
        return if (actualH < h) {
            n + 1
        } else {
            n
        }
    }
}