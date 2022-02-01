package mathhelper.twf.expressiondomain

import mathhelper.twf.config.ComparisonType
import mathhelper.twf.expressiontree.*

fun ExpressionNode.containsUncertainties(cmp: ExpressionComparator): Boolean {

    // check log/ln conditions (base > 0, base != 1, arg > 0)
    if (value == "log" || value == "ln") {
        val baseSatisfiesConditions = if (value == "log") {
            val baseNode = this.children[1].cloneAndWrap()

            val baseEqualToOne = cmp.fastProbabilityEquals(baseNode, one)
            // баги с проверкой log(a, b)
            val baseGreaterThanZero = true // cmp.fastProbabilityCheckOnIncorrectTransformation(baseNode, zero, ComparisonType.LEFT_MORE)

            !baseEqualToOne && baseGreaterThanZero
        } else {
            true
        }

        val argNode = this.children[0].cloneAndWrap()
        //val argGreaterThanZero = cmp.fastProbabilityCheckOnIncorrectTransformation(argNode, zero, ComparisonType.LEFT_MORE)
        val argLessOrEqualToZero = cmp.fastProbabilityCheckOnIncorrectTransformation(argNode, zero, ComparisonType.LEFT_LESS_OR_EQUAL)

        if (argLessOrEqualToZero/*!argGreaterThanZero*/ || !baseSatisfiesConditions) return true
    }

    // x / 0
    if (value == "/") {
        val denominatorNode = this.children[1].cloneAndWrap()

        return cmp.fastProbabilityCheckOnZero(denominatorNode) || denominatorNode.containsUncertainties(cmp)
    }

    // 0 ^ 0, 0 ^ inf
    if (value == "^" && cmp.fastProbabilityEquals(children.first(), zero)) {
        val containsInfinityOrZero = children.drop(1).any {
            val child = it.cloneAndWrap()
            child.containsUncertainties(cmp) || cmp.fastProbabilityCheckOnZero(child)
        }

        if (containsInfinityOrZero) return true
    }

    // 0 * 0, x * inf
    if (value == "*") {
        val containsInfinity = children.any { it.containsUncertainties(cmp) }
        val childrenZerosCount = children.count {
            val child = it.cloneAndWrap()
            cmp.fastProbabilityCheckOnZero(child)
        }
        if (containsInfinity || childrenZerosCount > 1) return true
    }

    return children.any { it.containsUncertainties(cmp) }
}
