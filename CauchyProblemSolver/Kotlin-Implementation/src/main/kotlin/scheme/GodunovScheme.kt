package scheme

import Coord
import Grid
import InitialConditions

class GodunovScheme(
    private val r: Double,
) : AbstractScheme() {
    override fun computeImpl(j: Int, t: Int, spatialGrid: Grid, initialConditions: InitialConditions): Double {
        val `u_j^n+1` = initialConditions.getValue(Coord(j, t)) -
                r * (initialConditions.getValue(Coord(j, t)) - initialConditions.getValue(Coord(j - 1, t)))

        println("")
        println("j = ${j}, t = ${t + 1}, u = ${`u_j^n+1`}, x = ${spatialGrid[j]}")
        return `u_j^n+1`
    }

}