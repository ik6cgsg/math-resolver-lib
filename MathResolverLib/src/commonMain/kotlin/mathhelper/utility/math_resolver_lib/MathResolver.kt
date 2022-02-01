package mathhelper.utility.math_resolver_lib

import mathhelper.twf.api.stringToExpression
import mathhelper.twf.api.structureStringToExpression
import mathhelper.twf.expressiontree.ExpressionNode
import kotlin.native.concurrent.ThreadLocal


fun String.replaceByIndex(i: Int, replacement: String): String {
    return this.substring(0, i) + replacement + this.substring(i + replacement.length)
}

enum class VariableStyle {
    DEFAULT,
    GREEK
}

enum class TaskType(val str: String) {
    DEFAULT(""),
    SET("setTheory")
}

class MathResolver {
    @ThreadLocal
    companion object {
        private lateinit var stringMatrix: ArrayList<String>
        private var baseString = 0
        private lateinit var currentViewTree: MathResolverNodeBase
        private var ruleDelim = " ↬ "

        fun resolveToPlain(
            expression: ExpressionNode, style: VariableStyle = VariableStyle.DEFAULT,
            taskType: TaskType = TaskType.DEFAULT, customSymbolMap: HashMap<OperationType, String>? = null
        ): MathResolverPair {
            if (expression.toString() == "()") {
                Logger.e("MathResolver", "TWF parsing failed")
                return MathResolverPair(null, arrayListOf("parsing error"))
            }
            customSymbolMap?.let { MathResolverNodeBase.symbolMap = it }
            currentViewTree = MathResolverNodeBase.getTree(expression, style, taskType)
                ?: return MathResolverPair(null,arrayListOf("parsing error"))
            return MathResolverPair(currentViewTree, getPlainString())
        }

        fun resolveToPlain(
            expression: String, style: VariableStyle = VariableStyle.DEFAULT,
            taskType: TaskType = TaskType.DEFAULT, structureString: Boolean = false,
            customSymbolMap: HashMap<OperationType, String>? = null
        ): MathResolverPair {
            val realExpression = if (!structureString) {
                stringToExpression(expression)
            } else structureStringToExpression(expression)
            return resolveToPlain(realExpression, style, taskType, customSymbolMap)
        }

        fun getRule(left: ExpressionNode, right: ExpressionNode,
                    style: VariableStyle = VariableStyle.DEFAULT,
                    taskType: String? = null, delim: String = ruleDelim
        ): String {
            ruleDelim = delim
            val from = when (taskType) {
                TaskType.SET.str -> resolveToPlain(left, style, TaskType.SET)
                else -> resolveToPlain(left, style)
            }
            val to = when (taskType) {
                TaskType.SET.str -> resolveToPlain(right, style, TaskType.SET)
                else -> resolveToPlain(right, style)
            }
            return getRule(from, to)
        }

        private fun getRule(left: MathResolverPair, right: MathResolverPair): String {
            val matrixLeft = left.matrix
            val matrixRight = right.matrix
            val leadingTree: MathResolverNodeBase
            var leftCorr = 0
            var rightCorr = 0
            if (left.tree!!.baseLineOffset > right.tree!!.baseLineOffset) {
                leadingTree = left.tree
                rightCorr = correctMatrixByBaseLine(matrixRight, left.tree, right.tree)
                right.tree.height += rightCorr
            } else {
                leadingTree = right.tree
                leftCorr = correctMatrixByBaseLine(matrixLeft, right.tree, left.tree)
                left.tree.height += leftCorr
            }
            if (left.tree.height > right.tree.height) {
                correctMatrixByHeight(matrixRight, left.tree, right.tree)
            } else {
                correctMatrixByHeight(matrixLeft, right.tree, left.tree)
            }
            return mergeMatrices(matrixLeft, matrixRight, leadingTree.baseLineOffset)
        }


        private fun correctMatrixByBaseLine (matrix: ArrayList<String>, leadingTree: MathResolverNodeBase,
                                             secTree: MathResolverNodeBase
        ): Int {
            val diff = leadingTree.baseLineOffset - secTree.baseLineOffset
            for (i in 0 until diff) {
                matrix.add(0, " ".repeat(secTree.length))
            }
            return diff
        }

        private fun correctMatrixByHeight (matrix: ArrayList<String>, leadingTree: MathResolverNodeBase,
                                           secTree: MathResolverNodeBase
        ): Int {
            val diff = leadingTree.height - secTree.height
            for (i in 0 until diff) {
                matrix.add(" ".repeat(secTree.length))
            }
            return diff
        }

        private fun mergeMatrices(left: ArrayList<String>, right: ArrayList<String>, baseLine: Int): String {
            var res = ""
            val linesNum = left.size
            for (i in 0 until linesNum) {
                res += left[i]
                res += if (i == baseLine) {
                    ruleDelim
                } else {
                    " ".repeat(ruleDelim.length)
                }
                res += right[i]
                if (i != linesNum - 1) {
                    res += "\n"
                }
            }
            return res
        }

        private fun getPlainString(): ArrayList<String> {
            stringMatrix = ArrayList()
            for (i in 0 until currentViewTree.height) {
                stringMatrix.add(" ".repeat(currentViewTree.length))
            }
            baseString = currentViewTree.height / 2
            currentViewTree.getPlainNode(stringMatrix)
            return stringMatrix
        }
    }
}