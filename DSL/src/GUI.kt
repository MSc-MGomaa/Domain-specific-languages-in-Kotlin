import java.awt.Color
import java.awt.EventQueue
import java.awt.Font
import java.awt.SystemColor
import javax.swing.*
import javax.swing.LayoutStyle.ComponentPlacement
import javax.swing.filechooser.FileSystemView


// Global:
var datasetInfo : DatasetInfo? = null
val displayedArea = JTextArea()

class GUI {

    private var mainFrame: JFrame = JFrame()
    private val uploadButton = JButton()

    private val iterations = JLabel()
    private val supportValue = JLabel()
    private val mEstimate = JLabel()
    private val outputLabel = JLabel()


    private val iterationsField = JTextField()
    private val suppValueField = JTextField()
    private val mEstimateField = JTextField()


    private val findButton = JButton()
    private val cancelButton = JButton()

    private val scrollPane = JScrollPane()
    private val groupLayout = GroupLayout(mainFrame.contentPane)


    private fun button(myButton1: JButton, lambda: JButton.() -> Unit): JButton {
        lambda(myButton1)
        return myButton1
    }

    private fun uploadCSV(): DatasetInfo? {

        val jFC = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
        val returnValue = jFC.showOpenDialog(null)

        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            val selectedFile = jFC.selectedFile
            datasetInfo = DatasetInfo(csvFile = selectedFile.absolutePath)
        }
        return datasetInfo
    }

    private fun JButton.fontProperties(){
        this.font = Font("Tahoma", Font.BOLD, 14)
    }

    private fun txtField(textField: JTextField, lambda: JTextField.() -> Unit): JTextField {
        lambda(textField)
        return textField
    }

    private fun mctsPro(): MCTS {
        return MCTS(
            iterationsField.text.toInt(),
            suppValueField.text.toInt(),
            mEstimateField.text.toInt()
        )
    }

    private fun JTextField.commonProperties(){
        this.background = SystemColor.window
        this.columns = 10
    }

    private fun label(myLabel: JLabel, lambda: JLabel.() -> Unit): JLabel {
        lambda(myLabel)
        return myLabel
    }

    private fun JLabel.fontProperties(){
        this.font = Font("Tahoma", Font.BOLD or Font.ITALIC, 12)
    }

    private fun scrolPane(obj: JScrollPane, lambda: JScrollPane.() -> Unit): JScrollPane {
        lambda(obj)
        return obj
    }

    private fun txtarea(obj: JTextArea, lambda: JTextArea.() -> Unit): JTextArea {
        lambda(obj)
        return obj
    }

        private fun initialize() {

        with(mainFrame)
        {
            contentPane.background = Color(238, 232, 170)
            title = "DSL"
            setBounds(100, 100, 415, 510)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            contentPane.layout = groupLayout
            isVisible = true

        }

        button(uploadButton)
        {
            text = "UPLOAD CSV"
            foreground = Color.BLACK
            background = SystemColor.window
            fontProperties()

            addActionListener{
                uploadCSV()
            }
        }

        label(iterations){
            text = "Number Of Iterations"
            fontProperties()
        }

        txtField(iterationsField)
        {
            commonProperties()
        }

        label(supportValue){
            text = "Min Support Value"
            fontProperties()

        }

        txtField(suppValueField)
        {
            commonProperties()
        }


        label(mEstimate){
            text = "M-Estimate Value"
            fontProperties()

        }

        txtField(mEstimateField)
        {
            commonProperties()
        }

        label(outputLabel){
        text = "Output"
        fontProperties()

        }

        button(findButton){
            text = "Find"
            background = Color.WHITE
            fontProperties()

            addActionListener {
                displayedArea.text = "The best rule per class: \n\n"
                // To run the algorithm:
                mctsPro().run()
            }

        }

        button(cancelButton){
            text = "Cancel"
            background = SystemColor.window
            fontProperties()
            addActionListener { mainFrame.dispose() }
        }

        scrolPane(scrollPane){
            toolTipText = ""
            setViewportView(displayedArea)
        }

        txtarea(displayedArea){
            columns = 10
            wrapStyleWord = true
            text = "Here the results will be displayed!"
            lineWrap = true
            foreground = Color.BLACK
            background = SystemColor.control
        }

        //----------------------------------------------------------------------------------------------------------

        groupLayout.setHorizontalGroup(
        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(
                groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(
                                groupLayout.createSequentialGroup()
                                    .addGroup(
                                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(
                                                iterations,
                                                GroupLayout.PREFERRED_SIZE,
                                                139,
                                                GroupLayout.PREFERRED_SIZE
                                            )
                                            .addGroup(
                                                groupLayout.createParallelGroup(
                                                    GroupLayout.Alignment.TRAILING,
                                                    false
                                                )
                                                    .addComponent(
                                                        mEstimate,
                                                        GroupLayout.Alignment.LEADING,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE.toInt()
                                                    )
                                                    .addComponent(
                                                        supportValue,
                                                        GroupLayout.Alignment.LEADING,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        128,
                                                        Short.MAX_VALUE.toInt()
                                                    )
                                            )
                                    )
                                    .addGap(18)
                            )
                            .addGroup(
                                groupLayout.createSequentialGroup()
                                    .addComponent(
                                        uploadButton,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE.toInt()
                                    )
                                    .addGap(34)
                            )
                    )
                    .addGroup(
                        groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(suppValueField, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE.toInt())
                            .addComponent(iterationsField, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE.toInt())
                            .addComponent(mEstimateField, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE.toInt())
                    )
                    .addContainerGap(79, Short.MAX_VALUE.toInt())
            )
            .addGroup(
                groupLayout.createSequentialGroup()
                    .addGap(38)
                    .addComponent(findButton, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE.toInt())
                    .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                    .addGap(77)
            )
            .addGroup(
                groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(outputLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(311, Short.MAX_VALUE.toInt())
            )
            .addGroup(
                groupLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE.toInt())
                    .addContainerGap()
            )
    )

        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    groupLayout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(uploadButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(18)
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(iterationsField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                .addComponent(iterations, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(18)
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(supportValue, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE.toInt())
                                .addComponent(suppValueField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(18)
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(mEstimate, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
                                .addComponent(mEstimateField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGap(18)
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(
                                    findButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    51,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    cancelButton,
                                    GroupLayout.PREFERRED_SIZE,
                                    51,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addGap(18)
                        .addComponent(outputLabel)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap()
                )
        )


    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            EventQueue.invokeLater { GUI()}
        }
    }
    init {
        initialize()
    }
}


