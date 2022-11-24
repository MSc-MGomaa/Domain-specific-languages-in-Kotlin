import java.io.File

class DatasetInfo(val csvFile: String){

    // a function to retrieve: Header, Rows, Columns:
    fun runInfo() : List<List<Any>> {
        val content = File(csvFile).readLines()
        val header: List<String> = content[0].split(",")
        val rows = mutableListOf<Any>()
        val columns = mutableListOf<Any>()

        // retrieve the data set as a list of rows:
        for (line in 1 until content.size) {
            val row = content[line].split(",")
            val x = mutableListOf<Any>()
            for (value in 0..(row.size - 2)){
                x.add(row[value].toDouble())
            }
            x.add(row[row.size -1])
            rows.add(x)
        }
        // retrieve the columns of the dataset:
        for (i in header.indices) {
            val column = mutableListOf<String>()
            for (line in 1 until content.size) {
                val x = content[line].split(",")
                column.add(x[i])
            }
            columns.add(column.distinct())
        }
        return listOf(header, rows, columns)
    }
}