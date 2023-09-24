package models.polynomial

import models.Solution
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

class QuadraticPolynomial(
    private val firstCoeff: Double,
    val secondCoeff: Double,
    val thirdCoeff: Double
) : Polynomial() {
    override fun compute(x: Double): Double = x.pow(2) * firstCoeff + x * secondCoeff + thirdCoeff

    override fun findRoots(epsilon: Double, step: Double): Array<Solution> {
        val discriminant = secondCoeff.pow(2) - 4 * firstCoeff * thirdCoeff
        if (discriminant < -epsilon) {
            return emptyArray()
        }
        if (discriminant.absoluteValue <= epsilon) {
            val root = (-secondCoeff) / 2 * thirdCoeff
            return arrayOf(Solution(root.toBigDecimal(), 2))
        }
        val root1 = (-secondCoeff - sqrt(discriminant)) / 2 * thirdCoeff
        val root2 = (-secondCoeff + sqrt(discriminant)) / 2 * thirdCoeff
        return arrayOf(Solution(root1.toBigDecimal(), 1), Solution(root2.toBigDecimal(), 1))
    }

}
