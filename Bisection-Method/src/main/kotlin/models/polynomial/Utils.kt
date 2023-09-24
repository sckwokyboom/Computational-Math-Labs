package models.polynomial

import java.math.BigDecimal
import java.math.RoundingMode

fun normalizeNumberWithPrecision(value: Double, precision: Double): BigDecimal {
    return value.toBigDecimal().setScale(precision.toBigDecimal().scale(), RoundingMode.HALF_EVEN)
}