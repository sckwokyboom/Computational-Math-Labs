import calculators.QuadratureFormulaCalculator
import calculators.SimpsonCalculator
import calculators.TrapezoidalCalculator
import java.util.*

fun main(args: Array<String>) {
    val numOfElementarySegments: Int
    var precisionScale = 10
    when (args.size) {
        1 -> {
            numOfElementarySegments = args[0].toInt()
        }

        2 -> {
            numOfElementarySegments = args[0].toInt()
            precisionScale = args[1].toInt()
        }

        else -> {
            println("Enter the number of elementary segments")
            val scanner = Scanner(System.`in`)
            numOfElementarySegments = scanner.nextInt()
            println("Enter the precision scale")
            precisionScale = scanner.nextInt()
        }
    }
    println("Your number of elementary segments: $numOfElementarySegments.")
    println("Your precision scale: $precisionScale.\n")
    println(
        "Integral value by trapezoidal method: ${
            TrapezoidalCalculator(precisionScale).calculateIntegral(numOfElementarySegments)
        }"
    )
    println(
        "Integral value by Simpson's method: ${
            SimpsonCalculator(precisionScale).calculateIntegral(numOfElementarySegments)
        }"
    )
    println(
        "Integral value by quadrature formula: ${
            QuadratureFormulaCalculator(precisionScale).calculateIntegral(
                numOfElementarySegments
            )
        }"
    )
}