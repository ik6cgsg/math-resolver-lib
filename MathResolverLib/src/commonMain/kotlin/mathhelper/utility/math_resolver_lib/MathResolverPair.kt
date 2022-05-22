package mathhelper.utility.math_resolver_lib

import mathhelper.twf.expressiontree.ExpressionNode

data class NodeWithStringCoords(
    val node: ExpressionNode,
    val lt: Point,
    val rb: Point
)

class MathResolverPair(val tree: MathResolverNodeBase?, val matrix: ArrayList<String>) {
    var height = matrix.size

    fun getNodeByCoords(x: Int, y: Int): NodeWithStringCoords? {
        if (tree != null ) {
            if (insideBox(x, y, tree.leftTop, tree.rightBottom)) {
                val mathNode = getNode(tree, x, y) ?: tree
                /*val coords = hashMapOf<Int, Pair<Int, Int>>()
                for (i in mathNode.leftTop.y..mathNode.rightBottom.y) {
                    coords[i] = Pair(mathNode.leftTop.x, mathNode.rightBottom.x)
                }*/
                return NodeWithStringCoords(mathNode.origin, mathNode.leftTop, mathNode.rightBottom)
            }
        }
        return null
    }

    fun shrink(symbolMap: HashMap<OperationType, OperatorInfo>) {
        val symToShrink = symbolMap.filter { it.value.length > 1 }
        for (i in 0 until height) {
            symToShrink.forEach {
                matrix[i] = matrix[i].replace(
                    it.value.value + " ".repeat(it.value.length - 1),
                    it.value.value
                )
            }
        }
    }

    private fun insideBox(x: Int, y: Int, lt: Point, rb: Point): Boolean {
        if (x >= lt.x && x <= rb.x && y >= lt.y && y <= rb.y) {
            return true
        }
        return false
    }

    private fun getNode(node: MathResolverNodeBase, x: Int, y: Int): MathResolverNodeBase? {
        var resNode: MathResolverNodeBase? = null
        for (n in node.children) {
            if (insideBox(x, y, n.leftTop, n.rightBottom)) {
                if (n.children.isEmpty()) {
                    resNode = n
                    break
                }
                resNode = getNode(n, x, y) ?: n
            }
        }
        return resNode
    }
    /*
    private fun findNodeByExpression(currentTree: MathResolverNodeBase?, expressionNode: ExpressionNode): MathResolverNodeBase? {
        if (currentTree == null)
            return null
        if (expressionNode.nodeId == currentTree.origin.nodeId)
            return currentTree
        var res: MathResolverNodeBase? = null
        for (child in currentTree.children)
            res = findNodeByExpression(child, expressionNode)?:res
        return res
    }
    */
}