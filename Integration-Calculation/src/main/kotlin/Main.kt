import calculators.SimpsonCalculator
import calculators.TrapezoidalCalculator
import java.util.*

fun main(args: Array<String>) {
    val numOfElementarySegments: Int
    if (args.size == 1) {
        numOfElementarySegments = args[0].toInt()
        println("Your number of elementary segments: $numOfElementarySegments.")
    } else {
        println("Enter the number of elementary segments")
        val scanner = Scanner(System.`in`)
        numOfElementarySegments = scanner.nextInt()
    }
    println(
        "Integral value by trapezoidal method: ${
            TrapezoidalCalculator(10).calculateIntegral(numOfElementarySegments)
        }"
    )
    println(
        "Integral value by Simpson's method: ${
            SimpsonCalculator(10).calculateIntegral(numOfElementarySegments)
        }"
    )
//    println(
//        "Integral value by quadrature formula: ${
//            QuadratureFormulaCalculator().calculateIntegral(
//                numOfElementarySegments,
//                10
//            )
//        }"
//    )
}