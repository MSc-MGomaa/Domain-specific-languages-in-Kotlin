class Node (pattern1 : List<List<Double>>){
    val pattern = pattern1
    var numberOfVisits : Double = 0.0
    val rewardList = mutableListOf<Double>()
    val children = mutableListOf<Node>()
    var parent : Node? = null
    val alreadyExpandedChildren = mutableListOf<Node>()
}