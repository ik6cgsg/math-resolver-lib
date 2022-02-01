package mathhelper.utility.math_resolver_lib.mathResolverNodes

import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.utility.math_resolver_lib.MathResolverNodeBase
import mathhelper.utility.math_resolver_lib.Operation
import mathhelper.utility.math_resolver_lib.Point
import mathhelper.utility.math_resolver_lib.replaceByIndex

class MathResolverNodeRightUnary(
    origin: ExpressionNode,
    needBrackets: Boolean = false,
    op: Operation,
    length: Int = 0, height: Int = 0
) : MathResolverNodeBase(origin, needBrackets, op, length, height) {

    override fun setNodesFromExpression()  {
        super.setNodesFromExpression()
        length += op!!.name.length
        var maxH = 0
        for (node in origin.children) {
            val elem = createNode(node, getNeedBrackets(node), style, taskType)
            elem.setNodesFromExpression()
            children.add(elem)
            if (elem.height > maxH) {
                maxH = elem.height
                if (elem.op != null) {
                    baseLineOffset = elem.baseLineOffset
                }
            }
            length += elem.length
        }
        height = maxH
        if (baseLineOffset < 0) {
            baseLineOffset = height - 1
        }
    }

    override fun setCoordinates(leftTop: Point) {
        super.setCoordinates(leftTop)
        children.firstOrNull()?.setCoordinates(Point(leftTop.x, leftTop.y + baseLineOffset - children.first().baseLineOffset))
    }

    override fun getPlainNode(stringMatrix: ArrayList<String>) {
        val curStr = leftTop.y + baseLineOffset
        var curInd = leftTop.x
        children.firstOrNull()?.getPlainNode(stringMatrix) ?: return
        curInd += children.first().length
        stringMatrix[curStr] = stringMatrix[curStr].replaceByIndex(curInd, op!!.name)
        curInd += op!!.name.length + 1
    }
}