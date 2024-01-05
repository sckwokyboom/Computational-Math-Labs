package scheme

import Coord
import Grid
import InitialConditions

abstract class AbstractScheme {

    /**
     * Вычислить значение в точке x.
     * @param j
     *       Номер текущей точки на сетке.
     *       То есть для нахождения y_{j} = y_{j - 1} + g_{j - 1} * h необходимо передать j.
     * @param spatialGrid
     *       Сетка, на которой происходит вычисление функции.
     * @param initialConditions
     *       Начальные условия для схемы аппроксимации. Допустим, (0, y_0) для схемы "разность вперед" 1 порядка.
     *
     * @return Отображение x_{j} -> y_{j}.
     */
    fun compute(j: Int, t: Int, spatialGrid: Grid, initialConditions: InitialConditions): Double {
        if (j == 0) {
//            for (el in initialConditions) {
//                println(el)
//            }
            return initialConditions.getValue(Coord(j, t))
        } else if (j >= spatialGrid.size) {
            throw IllegalArgumentException("Не достаточно точек на сетке для совершения операции")
        }

        return computeImpl(j, t, spatialGrid, initialConditions)
    }

    /**
     * При реализации необходимо добавить условия, которые обязаны быть для реализации вычисления.
     */
    protected abstract fun computeImpl(j: Int, t: Int, spatialGrid: Grid, initialConditions: InitialConditions): Double

    /**
     * @param spatialGrid
     * сетка, на которой производится вычисление.
     * @param initialConditions
     * уже вычисленные начальные условия задачи. Должны быть совместимы с сеткой.
     */
    private fun iterator(spatialGrid: Grid, t: Int, initialConditions: InitialConditions) =
        object : Iterator<Pair<Int, Double>> {
            private val steps = HashMap(initialConditions)
            private val gridIterator = spatialGrid.iterator()

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

            override fun next(): Pair<Int, Double> {
                if (!hasNext()) {
                    throw NoSuchElementException("Просчитаны все точки на сетке")
                }
                val (j, _) = gridIterator.next()
                val value = compute(j, t, spatialGrid, steps)
                steps[Coord(j, t)] = value
                return j to value
            }
        }
}