package calculators

import java.math.BigDecimal
import java.math.MathContext

class TrapezoidalCalculator(precisionScale: Int) : IntegralCalculator(precisionScale) {
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