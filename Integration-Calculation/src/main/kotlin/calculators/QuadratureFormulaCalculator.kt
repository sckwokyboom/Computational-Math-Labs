package calculators

import java.math.BigDecimal
import java.math.MathContext

class QuadratureFormulaCalculator(precisionScale: Int) : IntegralCalculator(precisionScale) {

    override fun calculateApproxAreaOnSegment(
        leftSegmentBorderInclude: BigDecimal,
        rightSegmentBorderInclude: BigDecimal
    ): BigDecimal {
        assert(leftSegmentBorderInclude < rightSegmentBorderInclude)
        val actualGridSpacing =
            (rightSegmentBorderInclude - leftSegmentBorderInclude).divide(6.toBigDecimal(), MathContext(precisionScale))
        val integrandValueInX0 =
            calculateIntegrand(leftSegmentBorderInclude)
        val integrandValueInX1 =
            calculateIntegrand(leftSegmentBorderInclude + actualGridSpacing.multiply(2.toBigDecimal()))
        val integrandValueInX2 =
            calculateIntegrand(leftSegmentBorderInclude + actualGridSpacing.multiply(4.toBigDecimal()))
        val integrandValueInX3 =
            calculateIntegrand(leftSegmentBorderInclude + actualGridSpacing.multiply(6.toBigDecimal()))

        val tmpSumOfIntegrandValues = integrandValueInX0.multiply(3.toBigDecimal()) +
                integrandValueInX1.multiply(9.toBigDecimal()) +
                integrandValueInX2.multiply(9.toBigDecimal()) +
                integrandValueInX3.multiply(3.toBigDecimal())

        return tmpSumOfIntegrandValues.multiply(actualGridSpacing).divide(4.toBigDecimal(), MathContext(precisionScale))
    }
}