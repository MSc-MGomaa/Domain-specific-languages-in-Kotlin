
class MEstimate(
    pattern: List<List<Double>>,
    private val targetLabel : String,
    private val mValue:Int
    )
{
    private val extentObj = Extent(pattern = pattern)
    private val support = extentObj.extentRun()[0].size
    private val coveredLabels = extentObj.extentRun()[1]

    // The correctly classified, within the covered labels:
    fun runMEstimate(): Double {
        var p = 0
        for (label in coveredLabels) {
            if (label == targetLabel) p++
        }
        // the label count, in the dataset itself:
        var counter = 0
        for (element in content) {
            if (element[element.size - 1] == targetLabel) counter++
        }

        val numSamplesWithinDS: Int = counter / content.size
        return (p + (mValue * numSamplesWithinDS)) / (support + mValue).toDouble()

    }
}

