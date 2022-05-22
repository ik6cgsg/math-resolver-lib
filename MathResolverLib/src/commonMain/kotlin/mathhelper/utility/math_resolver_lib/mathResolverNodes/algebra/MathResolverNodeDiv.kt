package mathhelper.utility.math_resolver_lib.mathResolverNodes.algebra

import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.utility.math_resolver_lib.*
import kotlin.math.ceil

class MathResolverNodeDiv(
    origin: ExpressionNode,
    needBrackets: Boolean = false,
    op: Operation,
    length: Int = 0, height: Int = 0
) : MathResolverNodeBase(origin, needBrackets, op, length, height) {
    private var symbol = symbolMap[OperationType.DIV] ?: defaultSymbolMap[OperationType.DIV]!!
    private var needExtend = false

    override fun setNodesFromExpression()  {
        super.setNodesFromExpression()
        var maxLen = 0
        for (node in origin.children) {
            val elem = createNode(node, false, style, taskType)
            if (elem is MathResolverNodeDiv) {
                needExtend = true
            }
            elem.setNodesFromExpression()
            children.add(elem)
            height += elem.height + 1
            if (elem.length > maxLen) {
                maxLen = elem.length
                baseLineOffset = if (node != origin.children[origin.children.size - 1]) {
                    height - 1
                } else {
                    height - 2 - elem.height
                }
            }
        }
        height--
        length += maxLen
        if (needExtend) {
            length += 2
        }
    }

    override fun setCoordinates(leftTop: Point) {
        super.setCoordinates(leftTop)
        var currH = leftTop.y
        for (child in children) {
            var currLen = leftTop.x + ceil((length - child.length) / 2f).toInt()
            child.setCoordinates(Point(currLen, currH))
            currH += child.height + 1
        }
    }

    override fun getPlainNode(stringMatrix: ArrayList<String>) {
        var curStr = leftTop.y
        var curInd = leftTop.x
        if (needBrackets) {
            curInd++
            BracketHandler.setBrackets(stringMatrix, leftTop, rightBottom)
        }
        children.forEachIndexed { ind: Int, child: MathResolverNodeBase ->
            child.getPlainNode(stringMatrix)
            curStr += child.height
            if (ind != children.size - 1) {
                val len = if (needBrackets) length - 2 else length
                val replacement = symbol.value.repeat(len)
                stringMatrix[curStr] = stringMatrix[curStr].replaceByIndex(curInd, replacement)
                curStr++
            }
        }
    }
}