package models.polynomial

import exception.NoSolutionsException
import models.Segment
import models.Solution
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.absoluteValue
import kotlin.math.sign

abstract class Polynomial {
    abstract fun compute(x: Double): Double
    abstract fun findRoots(epsilon: Double, step: Double): Array<Solution>
    protected fun findRoot(
        epsilon: Double,
        step: Double,
        segment: Segment,
        expectedRootMultiplicity: Int
    ): Solution {
        assert(epsilon >= 0)
        if (compute(segment.leftBorder).sign == compute(segment.rightBorder).sign) {
            throw NoSolutionsException("The function has the same sign at the both edges of the segment")
        }
        return findRootByBisectionMethod(epsilon, step, segment, expectedRootMultiplicity)
    }

    private fun findRootByBisectionMethod(
        epsilon: Double,
        step: Double,
        originalSegment: Segment,
        expectedRootMultiplicity: Int
    ): Solution {
        var seg = originalSegment
        var iterationsCounter = 0
        while (true) {
            iterationsCounter++
            if (compute(seg.rightBorder).sign == compute(seg.leftBorder).sign) {
                throw NoSolutionsException(
                    "The edges of the segment have the same sign."
                )
            }

            if (seg.leftBorder == NEGATIVE_INFINITY || seg.rightBorder == POSITIVE_INFINITY) {
                seg = findNewSegment(seg, step)
                return findRootByBisectionMethod(epsilon, step, seg, expectedRootMultiplicity)
            }
            val middle = seg.middle()
            val computed = compute(middle)

            if (computed.absoluteValue <= epsilon) {
                println("The number of iterations it took to find the ${Solution(normalizeNumberWithPrecision(middle, epsilon), expectedRootMultiplicity)}: $iterationsCounter")
                return Solution(normalizeNumberWithPrecision(middle, epsilon), expectedRootMultiplicity)
            }

            if (compute(seg.leftBorder).sign != computed.sign) {
                seg = Segment(seg.leftBorder, middle)
            } else if (computed.sign != compute(seg.rightBorder).sign) {
                seg = Segment(middle, seg.rightBorder)
            }
        }
    }

    private fun findNewSegment(segment: Segment, step: Double): Segment {
        var newSegment = segment
        if (newSegment.leftBorder == NEGATIVE_INFINITY) {
            newSegment = Segment(newSegment.rightBorder, newSegment.rightBorder)
            val rightBorderFunSign = compute(newSegment.rightBorder).sign
            while (rightBorderFunSign == compute(newSegment.leftBorder).sign) {
                newSegment = newSegment.leftBorderStep(-step)
            }
        }

        if (newSegment.rightBorder == POSITIVE_INFINITY) {
            newSegment = Segment(newSegment.leftBorder, newSegment.leftBorder)
            val leftBorderFunSign = compute(newSegment.leftBorder).sign
            while (leftBorderFunSign == compute(newSegment.rightBorder).sign) {
                newSegment = newSegment.rightBorderStep(step)
            }
        }
        return newSegment
    }
}
