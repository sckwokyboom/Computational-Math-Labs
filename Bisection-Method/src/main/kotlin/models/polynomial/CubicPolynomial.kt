package models.polynomial

import models.NegativeInfiniteSegment
import models.PositiveInfiniteSegment
import models.Segment
import models.Solution
import exception.NoSolutionsException
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
        val parabolaVertexY = -discriminant / 3
        if (parabolaVertexY > epsilon) {
            return emptyArray()
        }
        if (discriminant.absoluteValue <= epsilon) {
            if (b.absoluteValue <= epsilon) {
                return arrayOf(0.0)
            }
            return arrayOf(-b / 3)
        }
        val extreme1 = (-b - sqrt(discriminant)) / 3
        val extreme2 = (-b + sqrt(discriminant)) / 3

        return arrayOf(extreme1, extreme2)
    }


    override fun findRoots(epsilon: Double, step: Double): Array<Solution> {
        return findUnsortedRoots(epsilon, step)
            .sortedBy { it.value }
            .toTypedArray()
    }

    private fun findUnsortedRoots(epsilon: Double, step: Double): Array<Solution> {
        val extremes = findExtremes(epsilon)

        if (extremes.isEmpty()) {
            val funValueInZero = compute(0.0)
            return if (funValueInZero.absoluteValue <= epsilon) {
                arrayOf(Solution(BigDecimal.ZERO, getMultiplicity(0.0, epsilon)))
            } else if (funValueInZero > epsilon) {
                arrayOf(findRoot(epsilon, step, NegativeInfiniteSegment(0.0), 1))
            } else {
                arrayOf(findRoot(epsilon, step, PositiveInfiniteSegment(0.0), 1))
            }
        }

        if (extremes.size == 1) {
            val extremumPoint = extremes[0]
            val funValueInExtremum = compute(extremumPoint)
            return if (funValueInExtremum.absoluteValue <= epsilon) {
                arrayOf(
                    Solution(
                        normalizeNumberWithPrecision(extremumPoint, epsilon),
                        getMultiplicity(extremumPoint, epsilon)
                    )
                )
            } else if (funValueInExtremum > epsilon) {
                arrayOf(findRoot(epsilon, step, NegativeInfiniteSegment(extremumPoint), 1))
            } else {
                arrayOf(findRoot(epsilon, step, PositiveInfiniteSegment(extremumPoint), 1))
            }
        }

        if (extremes.size == 2) {
            val sortedExtremes = extremes.sortedBy { compute(it) }
            val minExtremumPoint = sortedExtremes[0]
            val maxExtremumPoint = sortedExtremes[1]
            val minFunValue = compute(minExtremumPoint)
            val maxFunValue = compute(maxExtremumPoint)
            if (maxFunValue < -epsilon && minFunValue < -epsilon) {
                return arrayOf(
                    findRoot(
                        epsilon,
                        step,
                        PositiveInfiniteSegment(minExtremumPoint),
                        1
                    )
                )
            } else if (minFunValue > epsilon && maxFunValue > epsilon) {
                return arrayOf(
                    findRoot(
                        epsilon,
                        step,
                        NegativeInfiniteSegment(maxExtremumPoint),
                        1
                    )
                )
            } else if (maxFunValue > epsilon && minFunValue < -epsilon) {
                return arrayOf(
                    findRoot(epsilon, step, NegativeInfiniteSegment(maxExtremumPoint), 1),
                    findRoot(epsilon, step, Segment(maxExtremumPoint, minExtremumPoint), 1),
                    findRoot(epsilon, step, PositiveInfiniteSegment(minExtremumPoint), 1)
                )
            } else if (maxFunValue > epsilon && minFunValue.absoluteValue <= epsilon) {
                return arrayOf(
                    Solution(
                        normalizeNumberWithPrecision(minExtremumPoint, epsilon),
                        getMultiplicity(minExtremumPoint, epsilon)
                    ),
                    findRoot(epsilon, step, NegativeInfiniteSegment(maxExtremumPoint), 1)
                )
            } else if (maxFunValue.absoluteValue <= epsilon && minFunValue < -epsilon) {
                return arrayOf(
                    Solution(
                        normalizeNumberWithPrecision(maxExtremumPoint, epsilon),
                        getMultiplicity(maxExtremumPoint, epsilon)
                    ),
                    findRoot(epsilon, step, PositiveInfiniteSegment(minExtremumPoint), 1)
                )
            } else if (maxFunValue.absoluteValue <= epsilon && minFunValue.absoluteValue <= epsilon) {
                return arrayOf(
                    findRoot(
                        epsilon,
                        step,
                        Segment(maxExtremumPoint, minExtremumPoint),
                        3
                    )
                )
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
