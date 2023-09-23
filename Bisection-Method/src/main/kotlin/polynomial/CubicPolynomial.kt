package polynomial

import NegativeInfiniteSegment
import PositiveInfiniteSegment
import Segment
import Solution
import exception.NoSolutionsException
import normalizeNumberWithAccuracy
import java.math.BigDecimal
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

class CubicPolynomial(private val secondCoeff: Double, private val thirdCoeff: Double, private val forthCoeff: Double) :
    Polynomial() {
    private val firstDerivative = QuadraticPolynomial(3.0, 2 * secondCoeff, thirdCoeff)
    private val secondDerivative = LinearPolynomial(6.0, 2 * secondCoeff)


    override fun compute(x: Double): Double = x.pow(3) + secondCoeff * x.pow(2) + x * thirdCoeff + forthCoeff

    private fun findExtremes(epsilon: Double): Array<Double> {
        val b = firstDerivative.secondCoeff / 2
        val discriminant = b.pow(2) - 3 * firstDerivative.thirdCoeff
//        val parabolaVertexX = -b / 3
        val parabolaVertexY = -discriminant / 3
        if (parabolaVertexY > epsilon) {
            return emptyArray()
        }
        if (discriminant == 0.0) {
            if (b == 0.0) {
                return arrayOf(0.0)
            }
            return arrayOf(-b / 3)
        }
        val extreme1 = (-b - sqrt(discriminant)) / 3
        val extreme2 = (-b + sqrt(discriminant)) / 3

        return arrayOf(extreme1, extreme2)
    }

    override fun findSolutions(epsilon: Double, step: Double): Array<Solution> {
        val extremes = findExtremes(epsilon)

        if (extremes.isEmpty()) {
            val funValueInZero = compute(0.0)
            return if (funValueInZero.absoluteValue <= epsilon) {
                arrayOf(Solution(BigDecimal.ZERO, getMultiplicity(0.0, epsilon))).sortedBy { it.value }.toTypedArray()
            } else if (funValueInZero > epsilon) {
                arrayOf(findSolution(epsilon, step, NegativeInfiniteSegment(0.0), 1)).sortedBy { it.value }
                    .toTypedArray()
            } else {
                arrayOf(findSolution(epsilon, step, PositiveInfiniteSegment(0.0), 1)).sortedBy { it.value }
                    .toTypedArray()
            }
        }

        if (extremes.size == 1) {
            val funValueInExtremum = compute(extremes[0])
            return if (funValueInExtremum.absoluteValue <= epsilon) {
                arrayOf(
                    Solution(
                        normalizeNumberWithAccuracy(extremes[0], epsilon),
                        getMultiplicity(extremes[0], epsilon)
                    )
                ).sortedBy { it.value }.toTypedArray()
            } else if (funValueInExtremum > epsilon) {
                arrayOf(findSolution(epsilon, step, NegativeInfiniteSegment(extremes[0]), 1)).sortedBy { it.value }
                    .toTypedArray()
            } else {
                arrayOf(findSolution(epsilon, step, PositiveInfiniteSegment(extremes[0]), 1)).sortedBy { it.value }
                    .toTypedArray()
            }
        }

        if (extremes.size == 2) {
            val sortedExtremes = extremes.sortedBy { compute(it) }
            val minFunValue = compute(sortedExtremes[0])
            val maxFunValue = compute(sortedExtremes[1])
            if (maxFunValue < -epsilon && minFunValue < -epsilon) {
                return arrayOf(
                    findSolution(
                        epsilon,
                        step,
                        PositiveInfiniteSegment(sortedExtremes[0]),
                        1
                    )
                ).sortedBy { it.value }.toTypedArray()
            } else if (minFunValue > epsilon && maxFunValue > epsilon) {
                return arrayOf(
                    findSolution(
                        epsilon,
                        step,
                        NegativeInfiniteSegment(sortedExtremes[1]),
                        1
                    )
                ).sortedBy { it.value }.toTypedArray()
            } else if (maxFunValue > epsilon && minFunValue < -epsilon) {
                return arrayOf(
                    findSolution(epsilon, step, NegativeInfiniteSegment(sortedExtremes[1]), 1),
                    findSolution(epsilon, step, Segment(sortedExtremes[1], sortedExtremes[0]), 1),
                    findSolution(epsilon, step, PositiveInfiniteSegment(sortedExtremes[0]), 1)
                ).sortedBy { it.value }.toTypedArray()
            } else if (maxFunValue > epsilon && minFunValue.absoluteValue <= epsilon) {
                return arrayOf(
                    Solution(normalizeNumberWithAccuracy(sortedExtremes[0], epsilon), 2),
                    findSolution(epsilon, step, NegativeInfiniteSegment(sortedExtremes[1]), 1)
                ).sortedBy { it.value }.toTypedArray()
            } else if (maxFunValue.absoluteValue <= epsilon && minFunValue < -epsilon) {
                return arrayOf(
                    Solution(normalizeNumberWithAccuracy(sortedExtremes[1], epsilon), 2),
                    findSolution(epsilon, step, PositiveInfiniteSegment(sortedExtremes[0]), 1)
                ).sortedBy { it.value }.toTypedArray()
            } else if (maxFunValue.absoluteValue <= epsilon && minFunValue.absoluteValue <= epsilon) {
                return arrayOf(
                    findSolution(
                        epsilon,
                        step,
                        Segment(sortedExtremes[1], sortedExtremes[0]),
                        3
                    )
                ).sortedBy { it.value }.toTypedArray()
            }
        }

        throw NoSolutionsException("No solutions")
    }

    private fun getMultiplicity(root: Double, epsilon: Double): Int {
        if ((firstDerivative.compute(root) - compute(root)).absoluteValue <= epsilon) {
            if ((secondDerivative.compute(root) - compute(root)).absoluteValue <= epsilon) {
                return 3
            }
            return 2
        }
        return 1
    }
}
