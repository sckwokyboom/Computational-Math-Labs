import scheme.ExactSolutionSchema
import scheme.GodunovScheme
import scheme.ImplicitScheme
import java.util.*
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    val spatialStep: Double
    val r: Double
    val countingEndTime: Double
    if (args.size == 3) {
        spatialStep = args[0].toDouble()
        r = args[1].toDouble()
        countingEndTime = args[2].toDouble()
    } else {
        val scanner = Scanner(System.`in`)
        println("Enter spatial step (h):")
        spatialStep = scanner.nextDouble()
        println("Enter r = h / tau:")
        r = scanner.nextDouble()
        println("Enter counting end time (T):")
        countingEndTime = scanner.nextDouble()
    }
    val startOfSegment = -1.0
    val endOfSegment = 10.0
    val tau = r * spatialStep
    val n = Suggester.suggest(startOfSegment, endOfSegment, spatialStep)
    val x_0 = startOfSegment
    val x_N = endOfSegment
    val exactSolutionSchema = ExactSolutionSchema()
    val initialConditions = emptyMap<Coord, Double>().toMutableMap()
    var counterOfTime = 0
    val exactValueInZero = 1.0
    val exactValueInEnd = 0.0
    while (counterOfTime * tau < countingEndTime) {
        initialConditions[Coord(0, counterOfTime)] = exactValueInZero
        initialConditions[Coord(n, counterOfTime)] = exactValueInEnd
        counterOfTime += 1
    }
    var counterOfSpatial = 0
    while (counterOfSpatial * spatialStep < (startOfSegment - endOfSegment).absoluteValue) {
        if (counterOfSpatial * spatialStep < 1) {
            initialConditions[Coord(counterOfSpatial, 0)] = 1.0
        } else {
            initialConditions[Coord(counterOfSpatial, 0)] = 0.0
        }
        counterOfSpatial++
    }

    val spatialGrid = Grid(startOfSegment, endOfSegment, n)
    val godunovScheme = GodunovScheme(r)
    val implicitScheme = ImplicitScheme()

    val gridSchemeEvaluator =
        GridSchemeEvaluator(
            startOfSegment,
            endOfSegment,
            countingEndTime,
            r,
            spatialGrid,
            exactSolutionSchema,
            godunovScheme,
            implicitScheme,
            initialConditions
        )
    val paths = gridSchemeEvaluator.computeScheme()
    gridSchemeEvaluator.createGifFromPngs(paths, 200)

//
//    val xValues = listOf(1, 4, 12, 15)
//    val godunovComputedFunValues = listOf(2, 5, 13, 16)
//    val dataFrame = dataFrameOf("x", "u(x)")(listOf(0, 0))
//    println(xValues.zip(godunovComputedFunValues).flatMap { listOf(it.first, it.second) })
//    val specialPoints =
//        dataFrameOf("x", "u(x)")(xValues.zip(godunovComputedFunValues).flatMap { listOf(it.first, it.second) })
//    val plot = dataFrame.plot(x = "x", y = "u(x)")
//        .geomPoint(data = specialPoints, color = RColor.cyan)
//        .xLabel("x")
//        .yLabel("u(x)")
//        .title("График функции u(x)")
//    plot.save(Path.of("plot2.png"))

}