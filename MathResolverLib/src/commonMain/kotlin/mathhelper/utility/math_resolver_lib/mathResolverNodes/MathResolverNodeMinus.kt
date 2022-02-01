package mathhelper.utility.math_resolver_lib.mathResolverNodes

import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.utility.math_resolver_lib.*

class MathResolverNodeMinus(
    origin: ExpressionNode,
    needBrackets: Boolean = false,
    op: Operation,
    length: Int = 0, height: Int = 0
) : MathResolverNodeBase(origin, needBrackets, op, length, height) {
    private val symbol = symbolMap[OperationType.MINUS] ?: defaultSymbolMap[OperationType.MINUS]!!

    override fun setNodesFromExpression()  {
        super.setNodesFromExpression()
        val elem = createNode(origin.children[0], getNeedBrackets(origin.children[0]), style, taskType)
        elem.setNodesFromExpression()
        children.add(elem)
        height = elem.height
        length += elem.length + symbol.length
        baseLineOffset = elem.baseLineOffset
    }

    override fun setCoordinates(leftTop: Point) {
        super.setCoordinates(leftTop)
        var currLen = leftTop.x + symbol.length
        if (needBrackets)
            currLen++
        val child = children[0]
        child.setCoordinates(Point(currLen, leftTop.y + baseLineOffset - child.baseLineOffset))
    }

    override fun getPlainNode(stringMatrix: ArrayList<String>) {
        val curStr = leftTop.y + baseLineOffset
        var curInd = leftTop.x
        if (needBrackets) {
            curInd++
            BracketHandler.setBrackets(stringMatrix, leftTop, rightBottom)
        }
        val child = children[0]
        stringMatrix[curStr] = stringMatrix[curStr].replaceByIndex(curInd, symbol)
        child.getPlainNode(stringMatrix)
    }
}