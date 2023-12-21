import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.math.BigDecimal

class Table {
    private val workbook = XSSFWorkbook()
    private val counter = 0
    fun printHeader() {
        println("j\t" + "x_j\t" + "y_ex(x_j)\t" + "delta_y_h_1(x_j)\t" + "delta_y_h_2(x_j)\t" + "p_j")
    }

    fun printRow() {
        print(counter.toString() + "\t")
    }

    fun saveSheet(data: Array<TableRow>, sheetName: String) {
        val sheet = workbook.createSheet(sheetName)
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("j")
        headerRow.createCell(1).setCellValue("x")
        headerRow.createCell(2).setCellValue("y_ex")
        headerRow.createCell(3).setCellValue("y_h1")
        headerRow.createCell(4).setCellValue("y_h2")
        headerRow.createCell(5).setCellValue("delta_y_h_1")
        headerRow.createCell(6).setCellValue("delta_y_h_2")
        headerRow.createCell(7).setCellValue("p_j")

        for ((rowIndex, rowData) in data.withIndex()) {
            val row = sheet.createRow(rowIndex + 1)
            val rowDataArray = arrayOf(
                rowData.index,
                rowData.x,
                rowData.exactSolutionValue,
                rowData.firstGridSolutionValue,
                rowData.secondGridSolutionValue,
                rowData.firstLocalErrorValue,
                rowData.secondLocalErrorValue,
                rowData.convergenceValue
            )
            for ((columnIndex, cellData) in rowDataArray.withIndex()) {
                val cell = row.createCell(columnIndex)
//                println(cellData)
                when (cellData) {
                    is BigDecimal -> cell.setCellValue(cellData.toDouble())
                    is Int -> cell.setCellValue(cellData.toDouble())
                }
            }
        }
    }

    fun saveWorkBookToDisk() {
        try {
            val fileOut = FileOutputStream("ODE-Results.xlsx")
            fileOut.use {
                workbook.write(it)
            }
        } catch (e: FileNotFoundException) {
            var numberOfCopy = 1
            var newFileName = "ODE-Results(%d).xlsx".format(numberOfCopy)
            while (File(newFileName).exists()) {
                numberOfCopy++
                newFileName = "ODE-Results(%d).xlsx".format(numberOfCopy)
            }
            try {
                val fileOut = FileOutputStream(newFileName)
                fileOut.use {
                    workbook.write(it)
                }
            } catch (e: FileNotFoundException) {
                error("Unable to save table")
            }
        }
    }
}