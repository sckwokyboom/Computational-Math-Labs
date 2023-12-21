import java.math.BigDecimal

data class TableRow(
    val index: Int?,
    val x: BigDecimal?,
    val exactSolutionValue: BigDecimal?,
    val firstGridSolutionValue: BigDecimal?,
    val secondGridSolutionValue: BigDecimal?,
    val firstLocalErrorValue: BigDecimal?,
    val secondLocalErrorValue: BigDecimal?,
    val convergenceValue: BigDecimal?,
)