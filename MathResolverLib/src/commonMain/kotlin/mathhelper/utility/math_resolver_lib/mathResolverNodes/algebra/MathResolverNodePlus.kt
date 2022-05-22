package mathhelper.utility.math_resolver_lib.mathResolverNodes.algebra

import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.twf.expressiontree.NodeType
import mathhelper.utility.math_resolver_lib.*

class MathResolverNodePlus(
    origin: ExpressionNode,
    needBrackets: Boolean = false,
    op: Operation,
    length: Int = 0, height: Int = 0
) : MathResolverNodeBase(origin, needBrackets, op, length, height) {
    private var operators: ArrayList<String> = ArrayList()
    private val symbol = symbolMap[OperationType.PLUS] ?: defaultSymbolMap[OperationType.PLUS]!!

    override fun setNodesFromExpression()  {
        super.setNodesFromExpression()
        var maxH = 0
        length += (origin.children.size - 1) * symbol.length
        origin.children.forEachIndexed { i, node ->
            lateinit var elem: MathResolverNodeBase
            if (node.nodeType == NodeType.FUNCTION &&
                    Operation(node.value).type == OperationType.MINUS && i != 0) {
                operators.add((symbolMap[OperationType.MINUS] ?: defaultSymbolMap[OperationType.MINUS]!!).value)
                var brackets = false
                if (node.children[0].nodeType == NodeType.FUNCTION &&
                    Operation(node.children[0].value).type == OperationType.PLUS) {
                    brackets = true
                }
                elem = createNode(node.children[0], brackets, style, taskType)
            } else {
                if (i != 0) {
                    operators.add(symbol.value)
                }
                elem = createNode(node, getNeedBrackets(node), style, taskType)
            }
            elem.setNodesFromExpression()
            if (elem is MathResolverNodeMinus && node != origin.children[0]) {
                elem.length -= elem.op!!.name.length
            }
            children.add(elem)
            length += elem.length
            if (elem.height > maxH) {
                maxH = elem.height
                if (elem.op != null) {
                    baseLineOffset = elem.baseLineOffset
                }
            }
        }
        height = maxH
        if (baseLineOffset < 0) {
            baseLineOffset = height - 1
        }
        super.fixBaseline()
    }

    override fun setCoordinates(leftTop: Point) {
        super.setCoordinates(leftTop)
        var currLen = if (!needBrackets) leftTop.x else leftTop.x + 1
        for (child in children) {
            child.setCoordinates(Point(currLen, leftTop.y + baseLineOffset - child.baseLineOffset))
            currLen += child.length + symbol.length
        }
    }

    override fun getPlainNode(stringMatrix: ArrayList<String>) {
        val curStr = leftTop.y + baseLineOffset
        var curInd = leftTop.x
        if (needBrackets) {
            curInd++
            BracketHandler.setBrackets(stringMatrix, leftTop, rightBottom)
        }
        children.forEachIndexed { ind: Int, child: MathResolverNodeBase ->
            if (ind != 0) {
                    stringMatrix[curStr] = stringMatrix[curStr].replaceByIndex(curInd, operators[0])
                    curInd += operators[0].length
                    operators.removeAt(0)
            }
            child.getPlainNode(stringMatrix)
            curInd += child.length
        }
    }
}