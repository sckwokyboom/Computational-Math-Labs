package calculators

import java.math.BigDecimal
import java.math.MathContext

class SimpsonCalculator(precisionScale: Int) : IntegralCalculator(precisionScale) {
    override fun calculateIntegral(numOfElementarySegments: Int): BigDecimal {
        val gridSpacing = GLOBAL_SEGMENT_LENGTH.toBigDecimal()
            .divide(numOfElementarySegments.toBigDecimal(), MathContext(precisionScale))
        var integral = BigDecimal.ZERO
        var currentXCoord = LEFT_BORDER_OF_GLOBAL_SEGMENT.toBigDecimal()
        for (i in 0..<numOfElementarySegments) {
            integral += calculateApproxAreaOnSegment(currentXCoord, currentXCoord + gridSpacing)
            currentXCoord += gridSpacing
        }
        return integral
    }

    override fun calculateApproxAreaOnSegment(
        leftSegmentBorderInclude: BigDecimal,
        rightSegmentBorderInclude: BigDecimal
    ): BigDecimal {
        assert(leftSegmentBorderInclude < rightSegmentBorderInclude)
        val integrandValueInLeftBorder = calculateIntegrand(leftSegmentBorderInclude)
        val actualGridSpacing = rightSegmentBorderInclude - leftSegmentBorderInclude
        assert(actualGridSpacing > BigDecimal.ZERO)
        val integrandValueInMiddle =
            calculateIntegrand(
                leftSegmentBorderInclude + actualGridSpacing.divide(
                    2.toBigDecimal(),
                    MathContext(precisionScale)
                )
            )
        val integrandValueInRightBorder = calculateIntegrand(rightSegmentBorderInclude)
        val integratedParabolaExpression =
            integrandValueInLeftBorder + integrandValueInMiddle.multiply(4.toBigDecimal()) + integrandValueInRightBorder
        return actualGridSpacing
            .multiply(integratedParabolaExpression)
            .divide(6.toBigDecimal(), MathContext(precisionScale))
    }
}