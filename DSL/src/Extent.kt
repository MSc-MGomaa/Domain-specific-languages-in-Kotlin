// content represents the rows of the dataset:
@Suppress("UNCHECKED_CAST")
val content = datasetInfo?.runInfo()?.get(1) as List<List<String>>

class Extent(private val pattern: List<List<Double>>){

    fun extentRun(): MutableList<List<Any>> {

        val extentList = mutableListOf<Double>()
        val coveredLabels = mutableListOf<String>()

        for (row in content){ // row is List<Any>
            var counter : Int = 0

            for (att in 1 until (row.size - 1)){
                if (row[att] as Double in (pattern[att -1][0]..pattern[att -1][1])) counter++
            }
            if (counter == (row.size -2)){
                extentList.add(row[0] as Double)
                coveredLabels.add(row[row.size -1])
            }
        }
        return mutableListOf(extentList, coveredLabels)
    }
}

