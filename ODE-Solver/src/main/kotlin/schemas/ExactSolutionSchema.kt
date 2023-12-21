package schemas

import Grid
import InitialConditions
import java.math.BigDecimal
import java.math.MathContext.UNLIMITED
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

class ExactSolutionSchema : AbstractSchema() {

    override fun computeImpl(j: Int, grid: Grid, initialConditions: InitialConditions): BigDecimal {
        val x = grid[j]
        return (0.5.toBigDecimal(UNLIMITED).multiply(
            exp(x.toDouble()).toBigDecimal(UNLIMITED),
            UNLIMITED
        )).multiply(
            sin(x.toDouble()).toBigDecimal(UNLIMITED)
                .add(cos(x.toDouble()).toBigDecimal(UNLIMITED), UNLIMITED)
        )
            .subtract(0.5.toBigDecimal(UNLIMITED), UNLIMITED)

    }
}