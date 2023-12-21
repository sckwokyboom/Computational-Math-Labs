import schemas.AbstractSchema
import schemas.ExactSolutionSchema
import java.math.BigDecimal
import java.math.MathContext.DECIMAL128
import java.math.MathContext.UNLIMITED
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.math.log

class GridSchemeEvaluator(
    a: BigDecimal,
    b: BigDecimal,
    h: BigDecimal,
    private val exactSchema: ExactSolutionSchema,
    private val approxSchema: AbstractSchema,
    private val initialConditionsForMainGrid: InitialConditions,
    private val initialConditionsForDenseGrid: InitialConditions,

    ) : Iterable<TableRow> {
    private val grid1: Grid
    private val grid2: Grid

    private var evaluated: Array<TableRow>? = null

    init {
        val n1 = Suggester.suggest(a, b, h)
        val n2 = n1 * 3
        grid1 = Grid(a, b, n1)
        grid2 = Grid(a, b, n2)
    }

//    fun compute(): Array<TableRow> {
//        val computeFirstGrid = approxSchema.stream(grid1, initialConditionsForMainGrid).collect(
//            Collectors.toMap(
//                { pair: Pair<Int, BigDecimal> -> pair.first },
//                { pair: Pair<Int, BigDecimal> -> pair.second }
//            )
//        )
//        val computeSecondGrid = approxSchema.stream(grid2, initialConditionsForDenseGrid).collect(
//            Collectors.toMap(
//                { pair: Pair<Int, BigDecimal> -> pair.first },
//                { pair: Pair<Int, BigDecimal> -> pair.second }
//            )
//        )
//        val computeExact = exactSchema.stream(grid1, emptyMap()).collect(
//            Collectors.toMap(
//                { pair: Pair<Int, BigDecimal> -> pair.first },
//                { pair: Pair<Int, BigDecimal> -> pair.second }
//            )
//        )
//        assert((computeFirstGrid.size == computeExact.size) && (computeFirstGrid.size * 3 == computeSecondGrid.size))
//
//        val array: MutableList<TableRow> = IntStream.range(0, grid1.size)
//            .mapToObj { i ->
//                if (i == 1) {
//                    println(computeFirstGrid.getValue(i))
//                }
//                val firstLocalErrorValue = abs(computeFirstGrid.getValue(i) - computeExact.getValue(i))
//                val secondLocalErrorValue = abs(computeSecondGrid.getValue(i * 3) - computeExact.getValue(i))
//                val convergenceValue = log(firstLocalErrorValue / secondLocalErrorValue, 3.0)
//                return@mapToObj TableRow(
//                    index = i,
//                    x = grid1[i],
//                    exactSolutionValue = computeExact[i],
//                    firstGridSolutionValue = computeFirstGrid?.get(i),
//                    secondGridSolutionValue = computeSecondGrid?.get(i * 3),
//                    firstLocalErrorValue = firstLocalErrorValue,
//                    secondLocalErrorValue = secondLocalErrorValue,
//                    convergenceValue = convergenceValue
//                )
//            }
//            .filter(Objects::nonNull)
//            .map { it as TableRow }
//            .collect(Collectors.toList())
//
//        return array.toTypedArray()
//    }

    fun computeByNormalWay(): Array<TableRow> {
        val computeFirstGrid = initialConditionsForMainGrid.toMutableMap()
        for ((j, _) in grid1) {
            computeFirstGrid[j] = approxSchema.compute(j, grid1, computeFirstGrid)
        }
        var computeSecondGrid = initialConditionsForDenseGrid.toMutableMap()
        for ((j, _) in grid2) {
            computeSecondGrid[j] = approxSchema.compute(j, grid2, computeSecondGrid)
        }
        computeSecondGrid = computeSecondGrid.filterKeys { it % 3 == 0 }.toMutableMap()

        val array: MutableList<TableRow> = IntStream.range(0, grid1.size)
            .mapToObj { i ->
                val firstLocalErrorValue =
                    computeFirstGrid.getValue(i).subtract(exactSchema.compute(i, grid1, emptyMap()), UNLIMITED)
                        .abs(UNLIMITED)
                val secondLocalErrorValue =
                    computeSecondGrid.getValue(i * 3).subtract(
                        exactSchema.compute(
                            i,
                            grid1,
                            emptyMap()
                        ), UNLIMITED
                    ).abs(DECIMAL128)
                val convergenceValue = if (secondLocalErrorValue.toDouble() == 0.0) {
                    BigDecimal.ZERO
                } else {
                    log(
                        firstLocalErrorValue.divide(secondLocalErrorValue, DECIMAL128).toDouble(),
                        3.0
                    ).toBigDecimal(UNLIMITED)
                }
                return@mapToObj TableRow(
                    index = i,
                    x = grid1[i],
                    exactSolutionValue = exactSchema.compute(i, grid1, emptyMap()),
                    firstGridSolutionValue = computeFirstGrid.getValue(i),
                    secondGridSolutionValue = computeSecondGrid.getValue(i * 3),
                    firstLocalErrorValue = firstLocalErrorValue,
                    secondLocalErrorValue = secondLocalErrorValue,
                    convergenceValue = convergenceValue
                )
            }
            .map { it as TableRow }
            .collect(Collectors.toList())

        return array.toTypedArray()
    }

    override fun iterator(): Iterator<TableRow> {
        if (evaluated == null) {
//            evaluated = compute()
        }

        return evaluated!!.iterator()
    }

    fun get(): Stream<TableRow> =
        StreamSupport.stream(
            { Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED) },
            Spliterator.ORDERED,
            false
        )
}