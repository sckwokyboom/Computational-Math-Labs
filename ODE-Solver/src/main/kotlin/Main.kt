import schemas.ExactSolutionSchema
import schemas.FirstSchema
import schemas.SecondSchema
import java.math.BigDecimal
import java.math.MathContext.DECIMAL128
import java.math.MathContext.UNLIMITED
import java.util.*

fun main(args: Array<String>) {
    val a = BigDecimal.ZERO
    val b: BigDecimal
    val h: BigDecimal
//    val (a, b, h) = listOf(0.0.toBigDecimal(UNLIMITED), 3.0.toBigDecimal(UNLIMITED), 0.003.toBigDecimal(UNLIMITED))
    when (args.size) {
        2 -> {
            b = args[0].toBigDecimal()
            h = args[1].toBigDecimal()
        }

        else -> {
            val scanner = Scanner(System.`in`)
            println("Enter b:")
            b = scanner.nextDouble().toBigDecimal()
            println("Enter h:")
            h = scanner.nextDouble().toBigDecimal()
        }
    }
    val exactSolutionSchema = ExactSolutionSchema()
    val firstSchema = FirstSchema()
    val secondSchema = SecondSchema()

    val y_0 = 0.0.toBigDecimal(UNLIMITED)

    val conditionsForFirstSchema = mapOf(0 to y_0)
    val conditionsForSecondSchemaForMainGrid =
        secondSchema.getInitialConditionsFromSchema(conditionsForFirstSchema, firstSchema, a, b, h)
    val conditionsForSecondSchemaForDenseGrid =
        secondSchema.getInitialConditionsFromSchema(
            conditionsForFirstSchema,
            firstSchema,
            a,
            b,
            h.divide(3.toBigDecimal(UNLIMITED), DECIMAL128)
        )

    val firstSchemaEvaluation = GridSchemeEvaluator(
        a,
        b,
        h,
        exactSolutionSchema,
        firstSchema,
        conditionsForFirstSchema,
        conditionsForFirstSchema
    )

    val secondSchemaEvaluation = GridSchemeEvaluator(
        a,
        b,
        h,
        exactSolutionSchema,
        secondSchema,
        conditionsForSecondSchemaForMainGrid,
        conditionsForSecondSchemaForDenseGrid
    )

    val table = Table()
    table.saveSheet(firstSchemaEvaluation.computeByNormalWay(), "FirstSchema")
    table.saveSheet(secondSchemaEvaluation.computeByNormalWay(), "SecondSchema")
    table.saveWorkBookToDisk()

//    firstSchemaEvaluation.get().forEach(System.out::println)
}