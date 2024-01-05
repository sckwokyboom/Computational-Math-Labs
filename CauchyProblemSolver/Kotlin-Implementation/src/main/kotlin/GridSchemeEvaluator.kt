import com.madgag.gif.fmsware.AnimatedGifEncoder
import org.jetbrains.letsPlot.GGBunch
import org.jetbrains.letsPlot.geom.geomPoint
import org.jetbrains.letsPlot.label.ggtitle
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.scale.scaleXContinuous
import org.jetbrains.letsPlot.scale.scaleYContinuous
import scheme.ExactSolutionSchema
import scheme.GodunovScheme
import scheme.ImplicitScheme
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO

class GridSchemeEvaluator(
    val startSpatial: Double,
    val endSpatial: Double,
    val endTimeCounting: Double,
    val r: Double,
    val spatialGrid: Grid,
    private val exactSolutionSchema: ExactSolutionSchema,
    private val godunovScheme: GodunovScheme,
    private val implicitScheme: ImplicitScheme,
    private val initialConditions: InitialConditions,
) {
    fun computeScheme(): List<Path> {
        val countOfNodesInTimeGrid = Suggester.suggest(0.0, endTimeCounting, r * spatialGrid.stepLength);
        val timeGrid = Grid(0.0, endTimeCounting, countOfNodesInTimeGrid)
        val timeGridIterator = timeGrid.iterator()
        val xValues = listOf<Double>().toMutableList()
        val godunovComputedFunValues = listOf<Double>().toMutableList()
        val previousData = initialConditions.toMutableMap()
        val paths = listOf<Path>().toMutableList()
        while (timeGridIterator.hasNext()) {
            var j = 0
            val t = timeGridIterator.next()
            println(t.first)
            while (j != spatialGrid.size) {
                val x = spatialGrid[j]
                val godunovComputedFunValue = godunovScheme.compute(j, t.first, spatialGrid, previousData)
                println(godunovComputedFunValue)
                previousData[Coord(j, t.first + 1)] = godunovComputedFunValue
                xValues.addLast(x)
                godunovComputedFunValues.addLast(godunovComputedFunValue)
                j++
//            val implicitComputedValue = implicitScheme.compute(j,t.first, spatialGrid, initialConditions)
            }

            val data = mapOf<String, Any>(
                "x" to xValues,
                "u(x, t)" to godunovComputedFunValues
            )
            val p =
                letsPlot(data) { x = "x"; y = "u(x, t)" } + geomPoint(size = 5.0, color = "gray") +
                        scaleXContinuous(limits = Pair(-1.0, 10.0)) +
                        scaleYContinuous(
                            limits = Pair(
                                -1,
                                1
                            )
                        ) + ggtitle("График u(x, t) при t = ${"%.2f".format(t.second)}")
            val bunch = GGBunch()
            bunch.addPlot(p, 0, 0, 1000, 700)
            bunch.show()
//            val dataFrame = dataFrameOf("x", "u(x)")(0, 0)
//            val godunovPoints =
//                dataFrameOf("x", "u(x)")(
//                    xValues.zip(godunovComputedFunValues).flatMap { listOf(it.first, it.second) })
//            val plot = dataFrame.plot(x = "x", y = "u(x)")
//                .geomPoint(data = godunovPoints, color = RColor.cyan)
//                .xLabel("x")
//                .yLabel("u(x)")
//                .ylim(Limits(-1.0, 1.0))
//                .xlim(Limits(-1.0, 10.0))
//                .title("График функции u(x) при t=${t.second}")

            val folder = Path.of("plots").toFile()
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val fullPath = File(folder, "plot${t.first}.png").toPath()
//            plot.save(fullPath)
            paths.add(fullPath)
        }
        return paths
    }

    fun createGifFromPngs(pngPaths: List<Path>, delay: Int) {
        try {
            val encoder = AnimatedGifEncoder()
            val outputGif = Path.of("plot.gif").toFile()

            encoder.start(outputGif.toString())
            encoder.setDelay(delay)
            encoder.setRepeat(0)

            for (pngPath in pngPaths) {
                val image: BufferedImage = ImageIO.read(pngPath.toFile())
                encoder.addFrame(image)
            }

            encoder.finish()
        } catch (e: Exception) {
            println("Something went wrong...")
        }
    }
}