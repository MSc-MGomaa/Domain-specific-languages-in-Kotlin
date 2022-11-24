import kotlin.math.ln
import kotlin.math.sqrt

class Evaluation(
    private val rewardList : List<Double>,
    private val numberOfVisitParent : Double,
    private val numberOfVisitCurrent: Double) {

    private fun variance(lst : List<Double>) =
        ((lst.map{n: Double -> (n - lst.average()) * (n - lst.average())}).sum()) / lst.size

    fun runEvaluation(): Double {
        val q = rewardList.sum() / numberOfVisitCurrent
        val numerator = 2 * ln(numberOfVisitParent)
        val secondTerm = .5 * sqrt(numerator / numberOfVisitCurrent)
        // d is constant, I will assume it = .01
        val d = .01
        val rewardVariance = variance(rewardList)
        val inflateTerm = d / numberOfVisitCurrent
        val thirdTerm = sqrt(rewardVariance + inflateTerm)

        return q + secondTerm + thirdTerm
    }
}