package schemas

import Grid
import InitialConditions
import Suggester
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.cos
import kotlin.math.exp

class SecondSchema : AbstractSchema() {
    private fun function(x: BigDecimal): BigDecimal = (exp(x.toDouble()) * cos(x.toDouble())).toBigDecimal()

    /**
     * Необходимо условие (x_0, y_0). Ожидается, что j >= 1.
     */
    override fun computeImpl(j: Int, grid: Grid, initialConditions: InitialConditions): BigDecimal {
        if (initialConditions.isEmpty() || initialConditions.size == 1) {
            throw IllegalArgumentException("Недостаточно начальных условий для этой схемы. Требуется как минимум 2 начальных условия")
        }
        if (!initialConditions.contains(j - 1)) {
            throw IllegalArgumentException("Каждая итерация y_{j} зависит от прошлой и должна владеть информацией о y_{j-1}")
        }

        val `x_{j-1}`: BigDecimal = grid[j - 1]
        val `y_{j-2}`: BigDecimal = initialConditions[j - 2]!!
        return 2.toBigDecimal().multiply(function(`x_{j-1}`), MathContext.UNLIMITED)
            .multiply(grid.h, MathContext.UNLIMITED).add(`y_{j-2}`, MathContext.UNLIMITED)
    }

    fun getInitialConditionsFromSchema(
        initialConditions: InitialConditions,
        schema: AbstractSchema,
        startInterval: BigDecimal,
        endInterval: BigDecimal,
        gridStep: BigDecimal,
    ): InitialConditions {
        val n = Suggester.suggest(startInterval, endInterval, gridStep)
        val grid = Grid(startInterval, endInterval, n)
        val y1: BigDecimal = schema.compute(1, grid, initialConditions)
        val newConditions = initialConditions.toMutableMap()
        newConditions[1] = y1
        newConditions.toMap()
        return newConditions
    }
}