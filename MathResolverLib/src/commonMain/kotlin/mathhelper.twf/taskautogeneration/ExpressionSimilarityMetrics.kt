package mathhelper.twf.taskautogeneration

import mathhelper.twf.expressiontree.ExpressionComparator
import mathhelper.twf.expressiontree.ExpressionNode

/**
 * General interface for expression similarity metrics (also known as distance function)
 * Like any other metric this functor must meet following properties:
 *
 * - d(x, y) = 0 <=> x = y
 * - d(x, y) = d(y, x)
 * - d(x, y) <= d(x, z) + d(z, y)
 */
interface ExpressionSimilarityMetric {
    operator fun invoke(lhs: ExpressionNode, rhs: ExpressionNode): Double
}

/**
 * Combines multiple metrics by combinator function
 */
class CompoundSimilarityMetric(
    val metrics: List<ExpressionSimilarityMetric>,
    val combinator: (acc: Double, metricValue: Double) -> Double = { acc, metricVal -> acc + metricVal }
) : ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode) =
        metrics.map { it(lhs, rhs) }.fold(.0, combinator)
}

// Число переменных, входящих в выражение слева, но не входящих в выражение справа
val variableOnlyInLeft = object : ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode): Double {
        val lhsVars = lhs.getContainedVariables()
        val rhsVars = rhs.getContainedVariables()

        return (lhsVars - rhsVars).size.toDouble()
    }
}

// Число переменных, входящих в выражение справа, но не входящих в выражение слева
val variableOnlyInRight = object : ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode): Double {
        val lhsVars = lhs.getContainedVariables()
        val rhsVars = rhs.getContainedVariables()

        return (rhsVars - lhsVars).size.toDouble()
    }
}

/**
 * Simple adapter from lambdas to similarity metric
 */
class LambdaExpressionSimilarityMetric(val metric: (l: ExpressionNode, r: ExpressionNode) -> Double) : ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode) = metric(lhs, rhs)
}

// Число функций, входящих в выражение слева, но не входящих в выражение справа
val functionsOnlyInLeft = object : ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode): Double {
        val lhsVars = lhs.getContainedFunctions()
        val rhsVars = rhs.getContainedFunctions()

        return (lhsVars - rhsVars).size.toDouble()
    }
}

// Число функций, входящих в выражение справа, но не входящих в выражение слева
val functionsOnlyInRight = object : ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode): Double {
        val lhsVars = lhs.getContainedFunctions()
        val rhsVars = rhs.getContainedFunctions()

        return (rhsVars - lhsVars).size.toDouble()
    }
}

val simplifiedDisparityMetric = CompoundSimilarityMetric(listOf(
    variableOnlyInLeft,
    variableOnlyInRight,
    functionsOnlyInLeft,
    functionsOnlyInRight
))

val disparityMatrixMetric = DisparityMatrixMetric(1);

class DisparityMatrixMetric(private val minSubtreeDepth: Int = 3): ExpressionSimilarityMetric {
    override fun invoke(lhs: ExpressionNode, rhs: ExpressionNode): Double {
        val normalizedLhs = lhs.cloneWithNormalization(sorted = true).children.first()
        val normalizedRhs = rhs.cloneWithNormalization(sorted = true).children.first()

        val leftSubexpressions = normalizedLhs.getNonLeafSubexpressions().filter { it.getDepth() >= minSubtreeDepth }
        val rightSubexpressions = normalizedRhs.getNonLeafSubexpressions().filter { it.getDepth() >= minSubtreeDepth }

        val comparator = ExpressionComparator()

        val leftScore = leftSubexpressions.filter { l ->
            rightSubexpressions.all { r -> !comparator.compareAsIs(l, r) }
        }.size.toDouble()

        val rightScore = rightSubexpressions.filter { r ->
            leftSubexpressions.all { l -> !comparator.compareAsIs(l, r) }
        }.size.toDouble()

        // количество подвыражений, которым не нашлось сопоставление (брать среднее/минимум или максимум)
        // от левой и правой части

        return (leftScore + rightScore) / 2
    }
}

fun ExpressionNode.isLeafNode() = children.isEmpty()

fun ExpressionNode.getNonLeafSubexpressions(): List<ExpressionNode> {
    if (getDepth() <= 2)
        return listOf(this)

    return listOf(this) + children.flatMap { it.getNonLeafSubexpressions() }
}

fun ExpressionNode.distanceTo(
    expressions: List<ExpressionNode>,
    metric: ExpressionSimilarityMetric = disparityMatrixMetric,
    selector: (distancesToExpressions: List<Double>) -> Double = { it.average() }
): Double {
    return selector(expressions.map { metric(this, it) })
}