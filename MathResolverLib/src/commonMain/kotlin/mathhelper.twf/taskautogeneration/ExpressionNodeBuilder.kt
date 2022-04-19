package mathhelper.twf.taskautogeneration

import mathhelper.twf.config.CompiledConfiguration
import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.twf.expressiontree.NodeType

class ExpressionNodeBuilder {

    companion object {
        fun buildNodeFromConstant(value: Number, parent: ExpressionNode? = null) : ExpressionNode {
            return ExpressionNode(NodeType.VARIABLE, value.toString(), identifier = value.toString(), parent = parent)
        }

        fun buildNodeFromMultipliers(
            multipliers: List<ExpressionNode>,
            compiledConfiguration: CompiledConfiguration
        ): ExpressionNode {
            if (multipliers.size == 1) {
                return multipliers[0]
            }
            return compiledConfiguration.createExpressionFunctionNode("*", -1, children = multipliers)
        }

        fun buildNodeFromTerms(
            terms: MutableList<ExpressionNode>,
            compiledConfiguration: CompiledConfiguration
        ): ExpressionNode {
            return compiledConfiguration.createExpressionFunctionNode("+", -1, children = terms.map { it })
        }

        fun buildNodeFromDividendAndDivisor(
            nominator: ExpressionNode,
            denominator: ExpressionNode,
            compiledConfiguration: CompiledConfiguration
        ): ExpressionNode {
            return compiledConfiguration.createExpressionFunctionNode("/", -1, children = listOf(nominator, denominator))
        }
    }
}
