package calculators

import java.math.BigDecimal

class QuadratureFormulaCalculator(precisionScale: Int) : IntegralCalculator(precisionScale) {
    override fun calculateIntegral(numOfElementarySegments: Int): BigDecimal {
        TODO("Not yet implemented")
    }

    override fun calculateApproxAreaOnSegment(
        leftSegmentBorderInclude: BigDecimal,
        rightSegmentBorderInclude: BigDecimal
    ): BigDecimal {
        TODO("Not yet implemented")
    }
}