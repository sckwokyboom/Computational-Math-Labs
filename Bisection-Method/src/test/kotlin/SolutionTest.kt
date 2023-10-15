import models.Solution
import org.junit.jupiter.api.Assertions.assertEquals
import models.polynomial.CubicPolynomial
import java.math.BigDecimal
import kotlin.test.Test

class SolutionTest {

    private fun assertSolutionsArrayEqualsWithPrecision(
        expected: Array<Solution>,
        actual: Array<Solution>,
        precision: Double
    ) {
        assertEquals(
            expected.size, actual.size,
            "The sizes of arrays with solutions differ."
        )
        for (i in expected.indices) {
            val expectedValue = expected[i].root
            val actualValue = actual[i].root
            val expectedMultiplicity = expected[i].multiplicity
            val actualMultiplicity = expected[i].multiplicity

            val diff = (expectedValue - actualValue).abs()
            assertEquals(
                true, diff <= precision.toBigDecimal(),
                "Solutions values differ by more than $precision at position [$i]." +
                        " Expected value: $expectedValue, but actual was: $actualValue."
            )
            assertEquals(
                expectedMultiplicity,
                actualMultiplicity,
                "Solutions multiplicity differ at position [$i]." +
                        " Expected value: $expectedMultiplicity, but actual was: $actualMultiplicity."
            )
        }
    }

    @Test
    fun test1() {
        val polynomial = CubicPolynomial(0.0, 0.0, 0.0)
        val solutions = arrayOf(Solution(BigDecimal.ZERO, 3))

        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.1, 1.0), 0.1)
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.01, 1.0), 0.1)
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.1)
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.0001, 1.0), 0.1)
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.00001, 1.0), 0.1)
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.000001, 1.0), 0.1)
    }

    @Test
    fun test2() {
        val polynomial = CubicPolynomial(-3.0, 3.0, -1.0)
        val solutions = arrayOf(Solution(1.0.toBigDecimal(), 3))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.0001, 1.0), 0.0001)
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.00000001, 1.0), 0.00000001)
    }

    @Test
    fun test3() {
        val polynomial = CubicPolynomial(3.27, 3.5643, -1.29503)
        val solutions = arrayOf(Solution(0.2833.toBigDecimal(), 1))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.001)
    }

    @Test
    fun test17() {
        val polynomial = CubicPolynomial(-13.2, 58.04, -85.008)
        val solutions1 =
            arrayOf(
                Solution(4.2.toBigDecimal(), 1),
                Solution(4.4.toBigDecimal(), 1),
                Solution(4.6.toBigDecimal(), 1)
            )
        val solutions2 = arrayOf(Solution(4.4.toBigDecimal(), 3))
        assertSolutionsArrayEqualsWithPrecision(solutions2, polynomial.findRoots(0.1, 0.001), 0.1)
        assertSolutionsArrayEqualsWithPrecision(solutions2, polynomial.findRoots(0.01, 0.001), 0.01)
        assertSolutionsArrayEqualsWithPrecision(solutions1, polynomial.findRoots(0.001, 1.0), 0.01)
    }

    @Test
    fun test4() {
        val polynomial = CubicPolynomial(-3.87, 4.9923, -2.14669)
        val solutions = arrayOf(Solution(1.29.toBigDecimal(), 3))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.01, 1.0), 0.01)
    }

    @Test
    fun test5() {
        val polynomial = CubicPolynomial(-3.0, 5.0, -15.0)
        val solutions = arrayOf(Solution(3.0.toBigDecimal(), 1))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.00001, 1.0), 0.00001)
    }

    @Test
    fun test6() {
        val polynomial = CubicPolynomial(10.0, 5.0, 50.0)
        val solutions = arrayOf(Solution((-10.0).toBigDecimal(), 1))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.00001, 1.0), 0.00001)
    }

    @Test
    fun test7() {
        val polynomial = CubicPolynomial(1.001, 1.0, 1.001)
        val solutions = arrayOf(Solution((-1.000999).toBigDecimal(), 1))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.00001, 1.0), 0.00001)
    }

    @Test
    fun test8() {
        val polynomial = CubicPolynomial(-824.0, 15.0, -12360.0)
        val solutions = arrayOf(Solution(824.0.toBigDecimal(), 1))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.00001, 1.0), 0.00001)
    }

    @Test
    fun test9() {
        val polynomial = CubicPolynomial(-5.0, 8.0, -4.0)
        val expectedSolutions = arrayOf(
            Solution(1.0.toBigDecimal(), 1),
            Solution(2.0.toBigDecimal(), 2)
        )
        val actualSolutions = polynomial.findRoots(0.001, 0.00001)
        assertSolutionsArrayEqualsWithPrecision(expectedSolutions, actualSolutions, 0.001)
    }

    @Test
    fun test10() {
        val polynomial = CubicPolynomial(-4.0, -3.0, 18.0)
        val solutions = arrayOf(
            Solution((-2.0).toBigDecimal(), 1),
            Solution(3.0.toBigDecimal(), 2)
        )
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.0001, 1.0), 0.0001)
    }

    @Test
    fun test11() {
        val polynomial = CubicPolynomial(-2.98, -9.08, -5.1)
        val solutions1 = arrayOf(
            Solution((-1.01).toBigDecimal(), 2),
            Solution(5.0.toBigDecimal(), 1)
        )
        val solutions2 = arrayOf(
            Solution((-1.01977).toBigDecimal(), 1),
            Solution((-1.00023).toBigDecimal(), 1),
            Solution(5.0.toBigDecimal(), 1)
        )
        assertSolutionsArrayEqualsWithPrecision(solutions1, polynomial.findRoots(0.001, 1.0), 0.001)
        assertSolutionsArrayEqualsWithPrecision(solutions2, polynomial.findRoots(0.0001, 1.0), 0.0001)
    }

    @Test
    fun test12() {
        val polynomial = CubicPolynomial(57.027, 0.51318, 27455.5)
        val solutions = arrayOf(Solution((-63.77).toBigDecimal(), 1))
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.001)
    }

    @Test
    fun test13() {
        val polynomial = CubicPolynomial(-8.0, -20.0, 0.0)
        val solutions = arrayOf(
            Solution((-2.0).toBigDecimal(), 1),
            Solution(BigDecimal.ZERO, 1),
            Solution(10.0.toBigDecimal(), 1)
        )
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.001)
    }

    @Test
    fun test14() {
        val polynomial = CubicPolynomial(-2.0, -13.0, -10.0)
        val solutions = arrayOf(
            Solution((-2.0).toBigDecimal(), 1),
            Solution((-1.0).toBigDecimal(), 1),
            Solution(5.0.toBigDecimal(), 1)
        )
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.001)
    }

    @Test
    fun test15() {
        val polynomial = CubicPolynomial(3.0, -10.0, -24.0)
        val solutions = arrayOf(
            Solution((-4.0).toBigDecimal(), 1),
            Solution((-2.0).toBigDecimal(), 1),
            Solution(3.0.toBigDecimal(), 1)
        )
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.001)
    }

    @Test
    fun test16() {
        val polynomial = CubicPolynomial(-1.99, -13.04, -10.05)
        val solutions = arrayOf(
            Solution((-2.01).toBigDecimal(), 1),
            Solution((-1.0).toBigDecimal(), 1),
            Solution(5.0.toBigDecimal(), 1)
        )
        assertSolutionsArrayEqualsWithPrecision(solutions, polynomial.findRoots(0.001, 1.0), 0.001)
    }
}