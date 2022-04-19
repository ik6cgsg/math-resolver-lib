package mathhelper.twf.taskautogeneration

import mathhelper.twf.api.expressionSubstitutionFromRuleITR
import mathhelper.twf.api.structureStringToExpression
import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR
import mathhelper.twf.defaultcontent.defaultrulepacks.autogeneration.RuleTag
import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.twf.expressiontree.ExpressionSubstitution
import mathhelper.twf.expressiontree.ExpressionTreeParser
import mathhelper.twf.expressiontree.NodeType

class ExpressionUtils {

    companion object {

        fun structureStringToGeneratedExpression(structureString: String): GeneratedExpression {
            val node = structureStringToExpression(structureString)
            val generatedExpression = GeneratedExpression(
                    ExpressionNode(NodeType.FUNCTION, ""),
                    "", "", "", "", "", "", "",
                    "standard_math",
                    mutableSetOf()
            )
            generatedExpression.expressionNode = node
            return generatedExpression
        }

        fun stringToGeneratedExpression(stringExpression: String): GeneratedExpression {
            val expressionTreeParser = ExpressionTreeParser(stringExpression, true)
            expressionTreeParser.parse()
            val node = expressionTreeParser.root
            val generatedExpression = GeneratedExpression(
                ExpressionNode(NodeType.FUNCTION, ""),
                "", "", "", "", "", "", "",
                "standard_math",
                mutableSetOf()
            )
            generatedExpression.expressionNode = node
            return generatedExpression
        }

        fun toExpressionSubstitutions(rulePackList: List<RulePackITR>, tags: Array<RuleTag> = RuleTag.values()): List<ExpressionSubstitution> {
            val substitutions = mutableListOf<ExpressionSubstitution>()
            rulePackList.forEach { substitutions.addAll(toExpressionSubstitutions(it, tags)) }
            return substitutions.filter {
                it.weightInTaskAutoGeneration != 0.0
            }
        }

        private fun toExpressionSubstitutions(rulePack: RulePackITR, tags: Array<RuleTag> = RuleTag.values()): List<ExpressionSubstitution> {
            val substitutions = mutableListOf<ExpressionSubstitution>()
            rulePack.rules
                    ?.filter { tags.toList().containsAll(it.tagsForTaskGenerator)  }
                    ?.forEach { substitutions.add(toExpressionSubstitution(it)) }
            return substitutions
        }

        private fun toExpressionSubstitution(rule: RuleITR): ExpressionSubstitution {
            return expressionSubstitutionFromRuleITR(rule)
        }
    }
}