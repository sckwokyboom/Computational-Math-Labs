import java.math.BigDecimal
import java.math.RoundingMode

fun normalizeNumberWithAccuracy(value: Double, accuracy: Double): BigDecimal {
    println("$value $accuracy")
    return value.toBigDecimal().setScale(accuracy.toBigDecimal().scale(), RoundingMode.HALF_UP)
}