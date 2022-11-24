
class ToRule(val header: List<*>, val pattern: List<List<Double>>, val root: List<List<Double>>) {

    fun run(){
        var counter = 0
        root.zip(pattern).forEach {pair ->
            if (pair.component1()[0] != pair.component2()[0] && pair.component1()[1] != pair.component2()[1]){
                pw.println("${pair.component2()[0]} < ${header[counter + 1]} < ${pair.component2()[1]}\n")
                displayedArea.append("${pair.component2()[0]} < ${header[counter + 1]} < ${pair.component2()[1]}\n")
            }
            else if (pair.component1()[0] != pair.component2()[0]){
                pw.println("${pair.component2()[0]} < ${header[counter + 1]} \n")
                displayedArea.append("${pair.component2()[0]} < ${header[counter + 1]} \n")
            }
            else if (pair.component1()[1] != pair.component2()[1]){
                pw.println("${header[counter + 1]} < ${pair.component2()[1]}\n")
                displayedArea.append("${header[counter + 1]} < ${pair.component2()[1]}\n")

            }
            counter ++
        }
    }

}