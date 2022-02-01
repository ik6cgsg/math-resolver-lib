package mathhelper.utility.math_resolver_lib.mathResolverNodes

import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.twf.expressiontree.NodeType
import mathhelper.utility.math_resolver_lib.*

class MathResolverSetNodeNot(
    origin: ExpressionNode,
    needBrackets: Boolean = false,
    op: Operation,
    length: Int = 0, height: Int = 0
) : MathResolverNodeBase(origin, needBrackets, op, length, height) {
    private val symbol = symbolMap[OperationType.SET_NOT] ?: defaultSymbolMap[OperationType.SET_NOT]!!

    override fun setNodesFromExpression()  {
        needBrackets = origin.children[0].nodeType == NodeType.FUNCTION
        super.setNodesFromExpression()
        val elem = createNode(origin.children[0], false, style, taskType)
        elem.setNodesFromExpression()
        children.add(elem)
        length += elem.length
        height = elem.height + 1
        baseLineOffset = height - 1
    }

    override fun setCoordinates(leftTop: Point) {
        super.setCoordinates(leftTop)
        val currLen = if (!needBrackets) leftTop.x else leftTop.x + 1
        val child = children[0]
        child.setCoordinates(Point(currLen, leftTop.y + 1))
    }

    override fun getPlainNode(stringMatrix: ArrayList<String>) {
        if (needBrackets) {
            BracketHandler.setBrackets(stringMatrix, Point(leftTop.x, leftTop.y + 1), rightBottom)
        }
        val child = children[0]
        val replacement = symbol.repeat(length)
        stringMatrix[leftTop.y] = stringMatrix[leftTop.y].replaceByIndex(leftTop.x, replacement)
        child.getPlainNode(stringMatrix)
    }
}