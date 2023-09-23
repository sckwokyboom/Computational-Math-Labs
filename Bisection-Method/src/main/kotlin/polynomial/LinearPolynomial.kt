package polynomial

import Solution

class LinearPolynomial(private val firstCoeff: Double, private val secondCoeff: Double) : Polynomial() {
    override fun compute(x: Double): Double = x * firstCoeff + secondCoeff

    override fun findSolutions(epsilon: Double, step: Double): Array<Solution> {
        return arrayOf(Solution((-secondCoeff / firstCoeff).toBigDecimal(), 1))
    }

}
