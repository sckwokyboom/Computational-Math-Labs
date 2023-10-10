package calculators

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.exp

abstract class IntegralCalculator(protected val precisionScale: Int) {
    companion object {
        const val LEFT_BORDER_OF_GLOBAL_SEGMENT = 5
        const val RIGHT_BORDER_OF_GLOBAL_SEGMENT = 7
        const val GLOBAL_SEGMENT_LENGTH = RIGHT_BORDER_OF_GLOBAL_SEGMENT - LEFT_BORDER_OF_GLOBAL_SEGMENT

    }

    protected fun calculateIntegrand(x: BigDecimal): BigDecimal {
        val cosValue = cos(x.toDouble()).toBigDecimal()
        val exponentValue = exp(x.toDouble()).toBigDecimal()
        return exponentValue.multiply(cosValue)
    }

    fun calculateIntegral(numOfElementarySegments: Int): BigDecimal {
        val gridSpacing = GLOBAL_SEGMENT_LENGTH
            .toBigDecimal()
            .divide(numOfElementarySegments.toBigDecimal(), MathContext(precisionScale))
        println("Your uniform grid step: $gridSpacing")
        var integral = BigDecimal.ZERO
        var currentXCoord = LEFT_BORDER_OF_GLOBAL_SEGMENT.toBigDecimal()
        for (i in 0..<numOfElementarySegments) {
            assert(currentXCoord <= RIGHT_BORDER_OF_GLOBAL_SEGMENT.toBigDecimal())
            integral += calculateApproxAreaOnSegment(currentXCoord, currentXCoord + gridSpacing)
            currentXCoord += gridSpacing
        }
        return integral.setScale(precisionScale, RoundingMode.HALF_UP)
    }

    protected abstract fun calculateApproxAreaOnSegment(
        leftSegmentBorderInclude: BigDecimal,
        rightSegmentBorderInclude: BigDecimal
    ): BigDecimal
}