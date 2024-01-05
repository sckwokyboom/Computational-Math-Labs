package scheme

import Grid
import InitialConditions

class ExactSolutionSchema : AbstractScheme() {

    override fun computeImpl(j: Int, t: Int, spatialGrid: Grid, initialConditions: InitialConditions): Double {
        val value = spatialGrid[j] - t
        return if (value < 0) 1.0 else 0.0
    }
}