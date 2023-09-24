import exception.NoSolutionsException
import models.polynomial.CubicPolynomial
import java.util.*

fun main(args: Array<String>) {
    val b: Double
    val c: Double
    val d: Double
    val epsilon: Double

    if (args.size == 4) {
        b = args[0].toDouble()
        c = args[1].toDouble()
        d = args[2].toDouble()
        epsilon = args[3].toDouble()
        println("$b $c $d $epsilon")
    } else {
        println("x^3 + bx^2 + cx + d")
        val scanner = Scanner(System.`in`)
        println("Enter: second coefficient (b)")
        b = scanner.nextDouble()
        println("Enter: third coefficient (c)")
        c = scanner.nextDouble()
        println("Enter: forth coefficient (d)")
        d = scanner.nextDouble()
        println("Enter: vicinity of accuracy (epsilon)")
        epsilon = scanner.nextDouble()
    }
    val step = 1.0
    val polynomial = CubicPolynomial(b, c, d)
    try {
        polynomial.findRoots(epsilon, step).forEach { println(it.value.toString() + " [" + it.multiplicity + "]") }
    } catch (e: NoSolutionsException) {
        System.err.println(e.message)
    }
}