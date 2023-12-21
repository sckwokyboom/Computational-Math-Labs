package schemas

import Grid
import InitialConditions
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.cos
import kotlin.math.exp

/**
 * Разностная схема первого порядка для дифференциального уравнения:
 *
 *              y`(x) = e^x * cos(x)
 *
 *
 * Обозначим:
 *
 *              function(x) = e^x * cos(x)
 *
 * Рекуррентное соотношение:
 *
 *              y_{j} = function_{j} * h + y_{j-1}
 */
class FirstSchema : AbstractSchema() {

    private fun function(x: BigDecimal): BigDecimal = (exp(x.toDouble()) * cos(x.toDouble())).toBigDecimal()

    /**
     * Необходимо условие (x_0, y_0). Ожидается, что j >= 1.
     */
    override fun computeImpl(j: Int, grid: Grid, initialConditions: InitialConditions): BigDecimal {
        if (initialConditions.isEmpty() || !initialConditions.contains(j - 1)) {
            throw IllegalArgumentException("Каждая итерация y_{j} зависит от прошлой и должна владеть информацией о y_{j-1}")
        }

        val `x_{j-1}`: BigDecimal = grid[j - 1]
        val `y_{j-1}`: BigDecimal = initialConditions[j - 1]!!
        return function(`x_{j-1}`).multiply(grid.h, MathContext.UNLIMITED).add(`y_{j-1}`, MathContext.UNLIMITED)
    }

}