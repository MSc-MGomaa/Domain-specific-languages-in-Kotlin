@Suppress("UNCHECKED_CAST")
val columns = datasetInfo?.runInfo()?.get(2) as List<List<String>>

class DirectChildren (private val parent : List<List<Double>>, private val minimumSupport : Int)

{
    fun generateDirectChildren() : List<List<List<Double>>>{
        val resultedDC = mutableListOf<List<List<Double>>>()

        for (k in 1 until (columns.size - 1)) {

            val left = columns[k].map { a -> a.toDouble() }.sorted().indexOf(parent[k - 1][0])
            val right = columns[k].map { a -> a.toDouble() }.sorted().indexOf(parent[k - 1][1])

            if (right - left > 1){
                val interval1 = mutableListOf(parent[k - 1][0], columns[k].map { a -> a.toDouble() }[(right + left) / 2])
                val interval2 = mutableListOf(columns[k].map { a -> a.toDouble() }[(right + left) / 2], parent[k - 1][1])

                val child1 = mutableListOf<List<Double>>()
                val child2 = mutableListOf<List<Double>>()

                for ( i in parent.indices){
                    if ((i + 1) == k) child1.add(interval1) else child1.add(parent[i])
                    if ((i + 1) == k) child2.add(interval2) else child2.add(parent[i])
                }

                val obj1 = Extent(pattern = child1)
                val child1Extent = obj1.extentRun()[0].size

                val obj2 = Extent(pattern = child2)
                val child2Extent = obj2.extentRun()[0].size

                if (child1Extent > minimumSupport) resultedDC.add(child1)
                if (child2Extent > minimumSupport) resultedDC.add(child2)

            }
        }
        return resultedDC

    }

    // In the simulation, we will allow the Infrequent patterns:

    fun simulationDirectChildren() : List<List<List<Double>>>{
        val resultedDC = mutableListOf<List<List<Double>>>()

        for (k in 1 until (columns.size - 1)) {

            val left = columns[k].map { a -> a.toDouble() }.sorted().indexOf(parent[k - 1][0])
            val right = columns[k].map { a -> a.toDouble() }.sorted().indexOf(parent[k - 1][1])

            if (right - left > 1){

                val interval1 = mutableListOf(parent[k - 1][0], columns[k].map { a -> a.toDouble() }[(right + left) / 2])
                val interval2 = mutableListOf(columns[k].map { a -> a.toDouble() }[(right + left) / 2], parent[k - 1][1])

                val child1 = mutableListOf<List<Double>>()
                val child2 = mutableListOf<List<Double>>()

                for ( i in parent.indices){
                    if ((i + 1) == k) child1.add(interval1) else child1.add(parent[i])
                    if ((i + 1) == k) child2.add(interval2) else child2.add(parent[i])
                }
                resultedDC.add(child1)
                resultedDC.add(child2)
            }
        }
        return resultedDC

    }
}