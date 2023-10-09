package calculators

import java.math.BigDecimal
import kotlin.math.cos
import kotlin.math.exp

abstract class IntegralCalculator(protected val precisionScale: Int) {
    companion object {
        const val LEFT_BORDER_OF_GLOBAL_SEGMENT = 5
        const val RIGHT_BORDER_OF_GLOBAL_SEGMENT = 7
        const val GLOBAL_SEGMENT_LENGTH = 2

    }

    fun calculateIntegrand(x: BigDecimal): BigDecimal {
        val cosValue = cos(x.toDouble()).toBigDecimal()
        val exponentValue = exp(x.toDouble()).toBigDecimal()
        return exponentValue.multiply(cosValue)
    }

    abstract fun calculateIntegral(numOfElementarySegments: Int): BigDecimal
    protected abstract fun calculateApproxAreaOnSegment(
        leftSegmentBorderInclude: BigDecimal,
        rightSegmentBorderInclude: BigDecimal
    ): BigDecimal
}