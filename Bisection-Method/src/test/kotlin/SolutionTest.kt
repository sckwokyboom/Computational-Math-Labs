import org.junit.jupiter.api.Assertions.assertArrayEquals
import polynomial.CubicPolynomial
import kotlin.test.Test

class SolutionTest {
    @Test
    fun test1() {
        val polynomial = CubicPolynomial(0.0, 0.0, 0.0)
        val solutions = arrayOf(Solution(0.0, 3))
        assertArrayEquals(solutions, polynomial.findSolutions(0.1, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.01, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.001, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.0001, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.00001, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.000001, 1.0))
    }

    @Test
    fun test2() {
        val polynomial = CubicPolynomial(-3.0, 3.0, -1.0)
        val solutions = arrayOf(Solution(1.0, 3))
        assertArrayEquals(solutions, polynomial.findSolutions(0.0001, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.00000001, 1.0))
    }

    @Test
    fun test3() {
        val polynomial = CubicPolynomial(3.27, 3.5643, -1.29503)
        val solutions = arrayOf(Solution(0.2833, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.0001, 1.0))
    }

    @Test
    fun test17() {
        val polynomial = CubicPolynomial(-13.2, 58.04, -85.008)
        val solutions1 = arrayOf(Solution(4.2, 1), Solution(4.4, 1), Solution(4.6, 1))
        val solutions2 = arrayOf(Solution(4.3, 3))
        assertArrayEquals(solutions1, polynomial.findSolutions(0.01, 1.0))
        assertArrayEquals(solutions2, polynomial.findSolutions(0.1, 1.0))
    }

    @Test
    fun test4() {
        val polynomial = CubicPolynomial(-3.87, 4.9923, -2.14669)
        val solutions = arrayOf(Solution(1.29, 3))
        assertArrayEquals(solutions, polynomial.findSolutions(0.01, 1.0))
    }

    @Test
    fun test5() {
        val polynomial = CubicPolynomial(-3.0, 5.0, -15.0)
        val solutions = arrayOf(Solution(3.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.00001, 1.0))
    }

    @Test
    fun test6() {
        val polynomial = CubicPolynomial(10.0, 5.0, 50.0)
        val solutions = arrayOf(Solution(-10.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.00001, 1.0))
    }

    @Test
    fun test7() {
        val polynomial = CubicPolynomial(1.001, 1.0, 1.001)
        val solutions = arrayOf(Solution(-1.001, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.00001, 1.0))
    }

    @Test
    fun test8() {
        val polynomial = CubicPolynomial(-824.0, 15.0, -12360.0)
        val solutions = arrayOf(Solution(824.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.00001, 1.0))
    }

    @Test
    fun test9() {
        val polynomial = CubicPolynomial(-5.0, 8.0, -4.0)
        val solutions = arrayOf(Solution(1.0, 1), Solution(2.0, 2))
        assertArrayEquals(solutions, polynomial.findSolutions(0.0001, 1.0))
        assertArrayEquals(solutions, polynomial.findSolutions(0.1, 1.0))
    }

    @Test
    fun test10() {
        val polynomial = CubicPolynomial(-4.0, -3.0, 18.0)
        val solutions = arrayOf(Solution(-2.0, 1), Solution(3.0, 2))
        assertArrayEquals(solutions, polynomial.findSolutions(0.0001, 1.0))
    }

    @Test
    fun test11() {
        val polynomial = CubicPolynomial(-2.98, -9.08, -5.1)
        val solutions1 = arrayOf(Solution(-1.0, 2), Solution(5.0, 1))
        val solutions2 = arrayOf(Solution(-1.02, 1), Solution(-1.0, 1), Solution(5.0, 1))
        assertArrayEquals(solutions1, polynomial.findSolutions(0.001, 1.0))
        assertArrayEquals(solutions2, polynomial.findSolutions(0.0001, 1.0))
    }

    @Test
    fun test12() {
        val polynomial = CubicPolynomial(57.027, 0.51318, 27455.5)
        val solutions = arrayOf(Solution(-63.77, 1))
//        assertArrayEquals(solutions, polynomial.findSolutions(0.001))
    }

    @Test
    fun test13() {
        val polynomial = CubicPolynomial(-8.0, -20.0, 0.0)
        val solutions = arrayOf(Solution(-2.0, 1), Solution(0.0, 1), Solution(10.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.001, 1.0))
    }

    @Test
    fun test14() {
        val polynomial = CubicPolynomial(-2.0, -13.0, -10.0)
        val solutions = arrayOf(Solution(-2.0, 1), Solution(-1.0, 1), Solution(5.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.001, 1.0))
    }

    @Test
    fun test15() {
        val polynomial = CubicPolynomial(3.0, -10.0, -24.0)
        val solutions = arrayOf(Solution(-4.0, 1), Solution(-2.0, 1), Solution(3.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.001, 1.0))
    }

    @Test
    fun test16() {
        val polynomial = CubicPolynomial(-1.99, -13.04, -10.05)
        val solutions = arrayOf(Solution(-2.01, 1), Solution(-1.0, 1), Solution(5.0, 1))
        assertArrayEquals(solutions, polynomial.findSolutions(0.001, 1.0))
    }
}