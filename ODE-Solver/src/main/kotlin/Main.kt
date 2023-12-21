import schemas.ExactSolutionSchema
import schemas.FirstSchema
import schemas.SecondSchema
import java.math.MathContext.UNLIMITED

fun main() {
    val (a, b, h) = listOf(0.0.toBigDecimal(UNLIMITED), 3.0.toBigDecimal(UNLIMITED), 0.003.toBigDecimal(UNLIMITED))

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
            h.divide(3.toBigDecimal(UNLIMITED), UNLIMITED)
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