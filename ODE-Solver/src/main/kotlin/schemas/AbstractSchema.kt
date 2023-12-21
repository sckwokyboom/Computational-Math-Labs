package schemas

import Grid
import InitialConditions
import java.math.BigDecimal
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport

abstract class AbstractSchema {

    /**
     * Вычислить значение в точке x.
     * @param j
     *       Номер текущей точки на сетке.
     *       То есть для нахождения y_{j} = y_{j - 1} + g_{j - 1} * h необходимо передать j.
     * @param grid
     *       Сетка, на которой происходит вычисление функции.
     * @param initialConditions
     *       Начальные условия для схемы аппроксимации. Допустим, (0, y_0) для схемы "разность вперед" 1 порядка.
     *
     * @return Отображение x_{j} -> y_{j}.
     */
    fun compute(j: Int, grid: Grid, initialConditions: InitialConditions): BigDecimal {
        if (j <= initialConditions.size - 1) {
            return initialConditions.getValue(j)
        } else if (j >= grid.size) {
            throw IllegalArgumentException("Не достаточно точек на сетке для совершения операции")
        }

        return computeImpl(j, grid, initialConditions)
    }

    /**
     * При реализации необходимо добавить условия, которые обязаны быть для реализации вычисления.
     */
    protected abstract fun computeImpl(j: Int, grid: Grid, initialConditions: InitialConditions): BigDecimal

    /**
     * @param grid
     * сетка, на которой производится вычисление.
     * @param initialConditions
     * уже вычисленные начальные условия задачи. Должны быть совместимы с сеткой.
     */
    private fun iterator(grid: Grid, initialConditions: InitialConditions) =
        object : Iterator<Pair<Int, BigDecimal>> {
            private val steps = HashMap(initialConditions)
            private val gridIterator = grid.iterator()

//            init {
//                var i = steps.size
//                while (i > 0 && gridIterator.hasNext()) {
//                    gridIterator.next()
//                    i--
//                }
//                if (i != 0 || !gridIterator.hasNext()) {
//                    throw IllegalArgumentException("Начальные условия должны быть совместимы с сеткой")
//                }
//            }

            override fun hasNext(): Boolean = gridIterator.hasNext()

            override fun next(): Pair<Int, BigDecimal> {
                if (!hasNext()) {
                    throw NoSuchElementException("Просчитаны все точки на сетке")
                }
                val (j, _) = gridIterator.next()
                val value = compute(j, grid, steps)
                steps[j] = value
                return j to value
            }
        }

    fun stream(grid: Grid, initialConditions: InitialConditions): Stream<Pair<Int, BigDecimal>> =
        StreamSupport.stream(
            { Spliterators.spliteratorUnknownSize(iterator(grid, initialConditions), Spliterator.ORDERED) },
            Spliterator.ORDERED,
            false
        )
}

