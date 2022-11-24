import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.random.Random


@Suppress("UNCHECKED_CAST")
val labels = datasetInfo?.runInfo()?.get(2)?.get(datasetInfo!!.runInfo()[2].size-1)
val pw = PrintWriter(BufferedWriter(FileWriter("Results.txt", true)))

class MCTS (private val numberOfIterations : Int, private val minSupport: Int, private val mEstimate : Int){

    private val m = mutableListOf<List<List<Double>>>()
    private val alreadyExpandedPatterns = mutableListOf<List<List<Double>>>()
    private var p = mutableListOf<List<List<Double>>>()

    // create generic class if the simulation function calls send 1 if the original send 2.
    private fun generateChildren(node : Node){
        val obj = DirectChildren(parent = node.pattern, minimumSupport = minSupport)
        val children = obj.generateDirectChildren()
        node.children.clear()
        children.map { child -> node.children.add(Node(child))}
    }

    private fun generateChildrenSimulation(node : Node){
        val obj = DirectChildren(parent = node.pattern, minimumSupport = minSupport)
        val children = obj.simulationDirectChildren()
        node.children.clear()
        children.map { child -> node.children.add(Node(child))}
    }

    private fun terminal(node:Node) : Boolean{
        @Suppress("UNCHECKED_CAST")
        val obj = Extent(pattern = node.pattern)
        return obj.extentRun()[0].size < minSupport
    }

    private fun stillUnExpandedChildren(node: Node): List<Node> {
        val childrenAsPatterns = node.children.map { child -> child.pattern }
        val expandedChildrenPatterns = node.alreadyExpandedChildren.map { child -> child.pattern }
        return childrenAsPatterns.filter { it !in expandedChildrenPatterns }.map { child -> Node(child) }
    }

    private fun bestChild(node: Node): Node {
        val evaluationList = mutableListOf<Double>()
        for (child in node.children) {
            val obj = Evaluation(rewardList = child.rewardList, node.numberOfVisits, child.numberOfVisits)
            evaluationList.add(obj.runEvaluation())
        }
        val bestC = evaluationList.indexOf(evaluationList.maxOrNull())
        return node.children[bestC]
    }

    private fun nodeSelection(_node: Node) : Node{
        var node = _node
        while (!terminal(node)){
            if(stillUnExpandedChildren(node).isNotEmpty()) break
            else {
                node = bestChild(node)
                generateChildren(node = node)
            }
        }
        return node
    }

    private fun expansion(node: Node): Node {
        val notExpandedChildren = stillUnExpandedChildren(node)
        val newExpandedNode = notExpandedChildren[Random.nextInt(0, notExpandedChildren.size)]
        newExpandedNode.parent = node
        return newExpandedNode
    }

    private fun rollout(_node: Node, label:String): Double {
        var node = _node
        var reward = 0.0
        val path = mutableListOf<Double>()
        val pathOfPatterns = mutableListOf<List<List<Double>>>()

        while (!terminal(node)){
            generateChildrenSimulation(node)
            val selectedChild = node.children[Random.nextInt(0, node.children.size)]
            val obj = MEstimate(pattern = selectedChild.pattern, targetLabel = label, mValue = mEstimate)
            path.add(obj.runMEstimate())
            pathOfPatterns.add(selectedChild.pattern)
            reward = path.maxOrNull() ?: 0.0
            node = selectedChild
        }
        m.add(pathOfPatterns[path.indexOf(reward)])
        return reward
    }

    private fun update(_node : Node, reward: Double){
        var node = _node
        node.rewardList.add(reward)
        node.numberOfVisits += 1

        while (node.parent != null)
            node = node.parent!!
            node.rewardList.add(reward)
            node.numberOfVisits += 1
    }

    private fun retrieveRoot() : List<List<Double>>{
        val rootPattern = mutableListOf<List<Double>>()

        for (k in 1 until (columns.size - 1)) {
            val subPattern = mutableListOf<Double>()
            (columns[k].map { a -> a.toDouble() }).minOrNull()?.let { subPattern.add(it) }
            (columns[k].map { a -> a.toDouble() }).maxOrNull()?.let { subPattern.add(it) }
            rootPattern.add(subPattern)
        }
        return rootPattern
    }

    fun run(){

        pw.println("************************************************************************************************")
        pw.println("                    Monte Carlo Tree Search Algorithm for Rule Learning                         ")
        pw.println("************************************************************************************************")

        val rootNode = Node(pattern1 = retrieveRoot())
        for (label in labels as List<*>) {
            generateChildren(node = rootNode)

            for (x in 1..numberOfIterations) {
                //if (x % 3 == 0) print("*")
                val selectedNode = nodeSelection(rootNode)
                val newExpandedNode = expansion(selectedNode)
                alreadyExpandedPatterns.add(newExpandedNode.pattern)
                val reward = rollout(newExpandedNode, label.toString())
                update(_node = newExpandedNode, reward = reward)
            }

            // has the advantage of deleting the redundant patterns:
            p = alreadyExpandedPatterns.union(m).toMutableList()
            val patternsEvaluation = mutableListOf<Double>()
            for (pattern in p) {
                val obj = MEstimate(pattern = pattern, targetLabel = label.toString(), mValue = mEstimate)
                patternsEvaluation.add(obj.runMEstimate())
            }
            // the index of the max evaluation:
            val indexMaxEvaluation = patternsEvaluation.indexOf(patternsEvaluation.maxOrNull() ?: 0.0)
            val obj = Extent(pattern = p[indexMaxEvaluation])
            var counter = 0
            for (k in obj.extentRun()[1]) if (k == label) counter ++

            pw.println("")
            pw.println("Class : $label with $counter samples correct and ${obj.extentRun()[1].size - counter} incorrect")
            displayedArea.append("Class : $label with $counter samples correct and ${obj.extentRun()[1].size - counter} incorrect\n")

            val objDisplay = ToRule(
                header = datasetInfo?.runInfo()?.get(0) as List<*> , pattern = p[indexMaxEvaluation],
                root = retrieveRoot())
            objDisplay.run()

            p.clear()
            m.clear()
            alreadyExpandedPatterns.clear()
            pw.println("-------------------------------------------------------------------------------------------------")
            displayedArea.append("-----------------------------------------------------------------------------------\n")

        }
        pw.close()


    }

}