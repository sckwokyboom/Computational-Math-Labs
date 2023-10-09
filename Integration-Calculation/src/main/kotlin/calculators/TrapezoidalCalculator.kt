package calculators

import java.math.BigDecimal
import java.math.MathContext

class TrapezoidalCalculator(precisionScale: Int) : IntegralCalculator(precisionScale) {
    override fun calculateIntegral(numOfElementarySegments: Int): BigDecimal {
        val gridSpacing =
            GLOBAL_SEGMENT_LENGTH
                .toBigDecimal()
                .divide(numOfElementarySegments.toBigDecimal(), MathContext(precisionScale))
        println("Your uniform grid step: $gridSpacing")
        var integralValue = BigDecimal.ZERO
        var currentXCoord = LEFT_BORDER_OF_GLOBAL_SEGMENT.toBigDecimal()
        for (i in 0..<numOfElementarySegments) {
            assert(currentXCoord < RIGHT_BORDER_OF_GLOBAL_SEGMENT.toBigDecimal())
            integralValue += calculateApproxAreaOnSegment(currentXCoord, currentXCoord + gridSpacing)
            currentXCoord += gridSpacing
        }
        return integralValue
    }

    override fun calculateApproxAreaOnSegment(
        leftSegmentBorderInclude: BigDecimal,
        rightSegmentBorderInclude: BigDecimal
    ): BigDecimal {
        assert(leftSegmentBorderInclude < rightSegmentBorderInclude)
        val gridSpacing =
            (leftSegmentBorderInclude - rightSegmentBorderInclude).abs()
        return (calculateIntegrand(leftSegmentBorderInclude) + calculateIntegrand(rightSegmentBorderInclude))
            .multiply(gridSpacing)
            .divide(2.toBigDecimal(), MathContext(precisionScale))
    }
}