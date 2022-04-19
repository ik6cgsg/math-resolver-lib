package mathhelper.twf.expressiontree

import mathhelper.twf.baseoperations.*
import mathhelper.twf.config.CheckingKeyWords.Companion.comparisonTypesConflict
import mathhelper.twf.config.ComparisonType
import mathhelper.twf.config.CompiledConfiguration
import mathhelper.twf.config.reverse
import mathhelper.twf.config.strictComparison
import mathhelper.twf.factstransformations.SubstitutionDirection
import mathhelper.twf.logs.MessageType
import mathhelper.twf.logs.log
import mathhelper.twf.numbers.Complex
import mathhelper.twf.numbers.NumberIntervalType
import mathhelper.twf.optimizerutils.DomainPointGenerator
import mathhelper.twf.platformdependent.lazyPrintln
import kotlin.math.abs
import kotlin.math.max

class ExpressionComparator(
        val baseOperationsDefinitions: BaseOperationsDefinitions = BaseOperationsDefinitions(),
        val baseOperationsComputationDouble: BaseOperationsComputation = BaseOperationsComputation(ComputationType.DOUBLE),
        val baseOperationsComputationComplex: BaseOperationsComputation = BaseOperationsComputation(ComputationType.COMPLEX),
        var debugMode: Boolean = false
) {
    lateinit var compiledConfiguration: CompiledConfiguration
    lateinit var definedFunctionNameNumberOfArgsSet: MutableSet<String>

    fun init(compiledConfiguration: CompiledConfiguration) {
        this.compiledConfiguration = compiledConfiguration
        definedFunctionNameNumberOfArgsSet = compiledConfiguration.definedFunctionNameNumberOfArgsSet
        if (compiledConfiguration.debugOutputMessages.expressionProbabilityComparisonFalseDetailsPrintln) {
            debugMode = true
        }
    }

    fun compareAsIs(left: ExpressionNode, right: ExpressionNode, nameArgsMap: MutableMap<String, String> = mutableMapOf(),
                    withBracketUnification: Boolean = false): Boolean {
        val normilized = normalizeExpressionsForComparison(left, right)
        if (normilized.first.isNodeSubtreeEquals(normilized.second, nameArgsMap)) {
            return true
        } else if (!withBracketUnification) {
            return false
        }
        val lUnified = normilized.first
        lUnified.dropBracketNodesIfOperationsSame()
        val rUnified = normilized.second
        rUnified.dropBracketNodesIfOperationsSame()
        if (lUnified.isNodeSubtreeEquals(rUnified, nameArgsMap)) {
            return true
        }
        lUnified.normalizeSubTree(sorted = true)
        rUnified.normalizeSubTree(sorted = true)
        return lUnified.isNodeSubtreeEquals(rUnified, nameArgsMap)
    }

    fun logicFullSearchComparison(leftOrigin: ExpressionNode, rightOrigin: ExpressionNode,
                                  comparisonType: ComparisonType = compiledConfiguration.comparisonSettings.defaultComparisonType,
                                  maxBustCount: Int = compiledConfiguration.comparisonSettings.maxExpressionBustCount,
                                  resultIfVariablesCountMoreMaxBust: Boolean = false): Boolean {
        val normalized = normalizeExpressionsForComparison(leftOrigin, rightOrigin)
        val left = normalized.first
        val right = normalized.second
        if (compareAsIs(left, right)) {
            return !strictComparison(comparisonType)
        }
        if (right.children.isEmpty() || left.children.isEmpty()) {
            return false
        }
        baseOperationsDefinitions.computeExpressionTree(left.children[0])
        baseOperationsDefinitions.computeExpressionTree(right.children[0])

        val variablesNamesSet = mutableSetOf<String>()
        variablesNamesSet.addAll(left.getVariableNames())
        variablesNamesSet.addAll(right.getVariableNames())

        val variables = variablesNamesSet.toList()
        if (1 shl variables.size > maxBustCount) {
            return resultIfVariablesCountMoreMaxBust
        }
        val variableValues = variables.map { Pair(it, "0") }.toMap().toMutableMap()

        return logicFullSearchComparisonRecursive(variables, variableValues, left, right, comparisonType, 0)
    }

    fun printDetailInfoIfDebug (variableValues: MutableMap<String, String>, left: ExpressionNode, right: ExpressionNode, l: Double, r: Double) {
        if (debugMode) {
            println("'${left}' = '$l' != '$r' = '${right}' if ${variableValues}")
        }
    }

    fun logicFullSearchComparisonRecursive(variables: List<String>, variableValues: MutableMap<String, String>, left: ExpressionNode, right: ExpressionNode, comparisonType: ComparisonType, currentIndex: Int): Boolean {
        if (currentIndex == variables.size) {
            val l = baseOperationsComputationDouble.compute(
                    left.cloneWithNormalization(variableValues, sorted = false)) as Double
            val r = baseOperationsComputationDouble.compute(
                    right.cloneWithNormalization(variableValues, sorted = false)) as Double
            when (comparisonType) {
                ComparisonType.LEFT_MORE_OR_EQUAL -> if (l < r && !baseOperationsDefinitions.additivelyEqual(l, r)) {
                    printDetailInfoIfDebug(variableValues, left, right, l, r)
                    return false
                }
                ComparisonType.LEFT_MORE -> if (l <= r || baseOperationsDefinitions.additivelyEqual(l, r)) {
                    printDetailInfoIfDebug(variableValues, left, right, l, r)
                    return false
                }
                ComparisonType.LEFT_LESS_OR_EQUAL -> if (l > r && !baseOperationsDefinitions.additivelyEqual(l, r)) {
                    printDetailInfoIfDebug(variableValues, left, right, l, r)
                    return false
                }
                ComparisonType.LEFT_LESS -> if (l >= r || baseOperationsDefinitions.additivelyEqual(l, r)) {
                    printDetailInfoIfDebug(variableValues, left, right, l, r)
                    return false
                }
                else -> if (!baseOperationsDefinitions.additivelyEqual(l, r)) {
                    printDetailInfoIfDebug(variableValues, left, right, l, r)
                    return false
                }
            }
        } else {
            variableValues[variables[currentIndex]] = "0"
            if (!logicFullSearchComparisonRecursive(variables, variableValues, left, right, comparisonType, currentIndex + 1)) {
                return false
            }

            variableValues[variables[currentIndex]] = "1"
            if (!logicFullSearchComparisonRecursive(variables, variableValues, left, right, comparisonType, currentIndex + 1)) {
                return false
            }
        }
        return true
    }

    fun probabilityTestComparison(leftOrigin: ExpressionNode, rightOrigin: ExpressionNode,
                                  comparisonType: ComparisonType = compiledConfiguration.comparisonSettings.defaultComparisonType,
                                  justInDomainsIntersection: Boolean = compiledConfiguration.comparisonSettings.justInDomainsIntersection,

                                  maxMinNumberOfPointsForEquality: Int = compiledConfiguration.comparisonSettings.minNumberOfPointsForEquality,
                                  allowedPartOfErrorTests: Double = compiledConfiguration.comparisonSettings.allowedPartOfErrorTests,
                                  testWithUndefinedResultIncreasingCoef: Double = compiledConfiguration.comparisonSettings.testWithUndefinedResultIncreasingCoef,
                                  useGradientDescentComparison: Boolean = false,
                                  numberIntervalType: NumberIntervalType = NumberIntervalType.REAL): Boolean {
        val normalized = normalizeExpressionsForComparison(leftOrigin, rightOrigin)
        val left = normalized.first
        val right = normalized.second
        if (compareAsIs(left, right)) {
            return !strictComparison(comparisonType)
        }
        if (right.children.isEmpty() || left.children.isEmpty()) {
            return false
        }
        baseOperationsDefinitions.computeExpressionTree(left.children[0])
        baseOperationsDefinitions.computeExpressionTree(right.children[0])
        var numberOfRemainingTests = (left.getCountOfNodes() + right.getCountOfNodes()).toDouble()
        if (comparisonType == ComparisonType.EQUAL) {
            val domain = DomainPointGenerator(arrayListOf(left, right), baseOperationsDefinitions, true, numberIntervalType)
            val totalTests = numberOfRemainingTests
            var passedTests = 0.0
            val minNumberOfPointsForEquality = max(max(left.getMaxMinNumberOfPointsForEquality(), right.getMaxMinNumberOfPointsForEquality()), maxMinNumberOfPointsForEquality)
            val isHaveComplexNode = left.haveComplexNode() || right.haveComplexNode()
            if (domain.variablesNamesSet.isEmpty()) {
                if (isHaveComplexNode) {
                    return (baseOperationsComputationComplex.compute(left) as Complex).equals(baseOperationsComputationComplex.compute(right) as Complex)
                } else {
                    return baseOperationsDefinitions.additivelyEqual(baseOperationsComputationDouble.compute(left) as Double, baseOperationsComputationDouble.compute(right) as Double)
                }
            }
            while (numberOfRemainingTests-- > 0) {
                val pointI = domain.generateNewPoint()
                val leftInPoint = left.cloneWithNormalization(pointI, sorted = false)
                val rightInPoint = right.cloneWithNormalization(pointI, sorted = false)
                if (isHaveComplexNode) {
                    val lComplex = baseOperationsComputationComplex.compute(leftInPoint) as Complex
                    val rComplex = baseOperationsComputationComplex.compute(rightInPoint) as Complex
                    if (lComplex.equals(rComplex)) {
                        passedTests++
                    }
                } else {
                    val lDouble = baseOperationsComputationDouble.compute(leftInPoint) as Double
                    val rDouble = baseOperationsComputationDouble.compute(rightInPoint) as Double
                    if (lDouble.isNaN() || rDouble.isNaN()) {
                        if ((lDouble.isNaN() != rDouble.isNaN()) && justInDomainsIntersection) {
                            lazyPrintln(compiledConfiguration.debugOutputMessages.expressionProbabilityComparisonFalseDetailsPrintln, {
                                "'(lDouble.isNaN() != rDouble.isNaN()) && justInDomainsIntersection': " +
                                        "pointI='${pointI.toList().joinToString(",") { "${it.first}=${it.second}" }}' lDouble='$lDouble' rDouble='$rDouble'"
                            })
                            return false
                        } else {
                            val lComplex = baseOperationsComputationComplex.compute(leftInPoint) as Complex
                            val rComplex = baseOperationsComputationComplex.compute(rightInPoint) as Complex
                            if (lComplex.equals(rComplex)) {
                                passedTests++
                            } else {
                                lazyPrintln(compiledConfiguration.debugOutputMessages.expressionProbabilityComparisonFalseDetailsPrintln, {
                                    "'lComplex != rComplex': " +
                                            "pointI='${pointI.toList().joinToString(",") { "${it.first}=${it.second}" }}' lComplex='$lComplex' rComplex='$rComplex'"
                                })
                            }
                        }
                    } else {
                        if (!lDouble.isFinite() || !rDouble.isFinite()) {
                            numberOfRemainingTests += testWithUndefinedResultIncreasingCoef
                        } else if (baseOperationsDefinitions.additivelyEqual(lDouble, rDouble)) {
                            passedTests++
                        } else {
                            lazyPrintln(compiledConfiguration.debugOutputMessages.expressionProbabilityComparisonFalseDetailsPrintln, {
                                "'lDouble != rDouble': " +
                                        "pointI='${pointI.toList().joinToString(",") { "${it.first}=${it.second}" }}' lDouble='$lDouble' rDouble='$rDouble'"
                            })
                        }
                    }
                }
                if (passedTests >= minNumberOfPointsForEquality) {
                    return true
                }
            }
            if ((passedTests >= totalTests * (1 - allowedPartOfErrorTests) && passedTests >= minNumberOfPointsForEquality) || (passedTests >= totalTests)) {
                return true
            } else {
                lazyPrintln(compiledConfiguration.debugOutputMessages.expressionProbabilityComparisonFalseDetailsPrintln, {
                    "(passedTests='$passedTests' >= totalTests='$totalTests' * (1 - allowedPartOfErrorTests='$allowedPartOfErrorTests') && " +
                            "passedTests='$passedTests' >= minNumberOfPointsForEquality='$minNumberOfPointsForEquality') || " +
                            "(passedTests='$passedTests' >= totalTests='$totalTests') failed"
                })
                return false
            }
        } else {
            val domain = DomainPointGenerator(arrayListOf(left, right), baseOperationsDefinitions, false, numberIntervalType)
            if (!domain.hasVariables()) {
                return compareInequalityInPoint(left, mutableMapOf(), right, justInDomainsIntersection, comparisonType)
            }
            val points = domain.generateSimplePoints()
            for (pointI in points) {
                if (!compareInequalityInPoint(left, pointI, right, justInDomainsIntersection, comparisonType)) {
                    return false
                }
            }
            while (numberOfRemainingTests-- > 0) {
                val pointI = domain.generateNewPointInDomain()
                if (!compareInequalityInPoint(left, pointI, right, justInDomainsIntersection, comparisonType)) {
                    return false
                }
            }
            if (useGradientDescentComparison) {
                return gradientDescentComparison(left, right, compiledConfiguration, comparisonType, domain)
            } else {
                return true
            }
        }
    }

    private fun compareInequalityInPoint(left: ExpressionNode, pointI: MutableMap<String, String>, right: ExpressionNode, justInDomainsIntersection: Boolean, comparisonType: ComparisonType): Boolean {
        val strongInequalityComparisonEpsilon: Double = 11.9e-10 // TODO: solve such case more flexible and move settigns to CompiledConfiguration
        val l = baseOperationsComputationDouble.compute(
                left.cloneWithNormalization(pointI, sorted = false)) as Double
        val r = baseOperationsComputationDouble.compute(
                right.cloneWithNormalization(pointI, sorted = false)) as Double
        if (justInDomainsIntersection && (l.isNaN() != r.isNaN())) {
            return false
        } else if (l.isInfinite() && r.isInfinite()) {
            return true // to avoid mistake of considering correct as incorrect
        } else when (comparisonType) {
            ComparisonType.LEFT_MORE_OR_EQUAL -> if (l < r && !baseOperationsDefinitions.additivelyEqual(l, r)) {
                return false
            }
            ComparisonType.LEFT_MORE -> if (l <= r || baseOperationsDefinitions.additivelyEqual(l, r, strongInequalityComparisonEpsilon)) {
                return false
            }
            ComparisonType.LEFT_LESS_OR_EQUAL -> if (l > r && !baseOperationsDefinitions.additivelyEqual(l, r)) {
                return false
            }
            ComparisonType.LEFT_LESS -> if (l >= r || baseOperationsDefinitions.additivelyEqual(l, r, strongInequalityComparisonEpsilon)) {
                return false
            }
        }
        return true
    }

    fun compareWithoutSubstitutions(l: ExpressionNode, r: ExpressionNode,
                                    comparisonType: ComparisonType = compiledConfiguration.comparisonSettings.defaultComparisonType,
                                    definedFunctionNameNumberOfArgs: Set<String> = definedFunctionNameNumberOfArgsSet,
                                    justInDomainsIntersection: Boolean = compiledConfiguration.comparisonSettings.justInDomainsIntersection,
                                    maxBustCount: Int = compiledConfiguration.comparisonSettings.maxExpressionBustCount): Boolean {
        if (compareAsIs(l, r, withBracketUnification = false))
            return true
        val left = l.clone()
        val right = r.clone()
        left.applyAllImmediateSubstitutions(compiledConfiguration)
        right.applyAllImmediateSubstitutions(compiledConfiguration)
        left.applyAllSubstitutions(compiledConfiguration.compiledFunctionDefinitions)
        right.applyAllSubstitutions(compiledConfiguration.compiledFunctionDefinitions)
        left.variableReplacement(compiledConfiguration.variableConfiguration.variableImmediateReplacementMap)
        right.variableReplacement(compiledConfiguration.variableConfiguration.variableImmediateReplacementMap)
        left.normalizeSubTree(sorted = true)
        right.normalizeSubTree(sorted = true)
        if (compareAsIs(left.cloneAndSimplifyByCommutativeNormalizeAndComputeSimplePlaces(compiledConfiguration), right.cloneAndSimplifyByCommutativeNormalizeAndComputeSimplePlaces(compiledConfiguration), withBracketUnification = true)) {
            return true
        }
        if (compiledConfiguration.comparisonSettings.compareExpressionsWithProbabilityRulesWhenComparingExpressions) {
            val needBooleanComparison = left.isBoolExpression(compiledConfiguration.functionConfiguration.boolFunctions) || right.isBoolExpression(compiledConfiguration.functionConfiguration.boolFunctions)
            val functionIdentifierToVariableMap = mutableMapOf<ExpressionNode, String>()
            left.replaceNotDefinedFunctionsOnVariables(functionIdentifierToVariableMap, definedFunctionNameNumberOfArgs, this, hasBoolFunctions = needBooleanComparison)
            right.replaceNotDefinedFunctionsOnVariables(functionIdentifierToVariableMap, definedFunctionNameNumberOfArgs, this, hasBoolFunctions = needBooleanComparison)
            if (!needBooleanComparison) {
                return probabilityTestComparison(left, right, comparisonType, justInDomainsIntersection = justInDomainsIntersection)
            } else {
                return logicFullSearchComparison(left, right, comparisonType, maxBustCount - 1, false)
            }
        }
        return false
    }

    fun checkOpeningBracketsSubstitutions(expressionToTransform: ExpressionNode, otherExpression: ExpressionNode, expressionChainComparisonType: ComparisonType): Boolean {
        val openingBracketsTransformationResults = expressionToTransform.computeResultsOfOpeningBracketsSubstitutions(compiledConfiguration)
        for (expression in openingBracketsTransformationResults) {
            if (compareWithoutSubstitutions(expression, otherExpression, expressionChainComparisonType)) {
                return true
            }
        }
        return false
    }

    companion object {
        val algebraAutoCheckingFunctionsSet = setOf("_0", "_1", "+_-1", "-_-1", "*_-1", "/_-1", "^_-1", "sin_1", "cos_1", "sh_1", "ch_1", "th_1", "tg_1", "asin_1", "acos_1", "atg_1", "exp_1", "ln_1", "abs_1")
        val setAutoCheckingFunctionsSet = setOf("_0", "_1", "and_-1", "or_-1", "xor_-1", "alleq_-1", "not_1")
        val combinatoricsAutoCheckingFunctionsSet = setOf("_0", "_1", "+_-1", "-_-1", "*_-1", "/_-1", "^_-1", "sin_1", "cos_1", "sh_1", "ch_1", "th_1", "tg_1", "asin_1", "acos_1", "atg_1", "exp_1", "ln_1", "abs_1", "S_4", "P_4")
        val trigonometricAutoCheckingFunctionsSet = setOf("sin", "cos", "tg", "ctg", "asin", "acos", "atg", "actg", "sh", "ch", "th", "cth")
    }

    fun fastProbabilityCheckOnIncorrectTransformation(l: ExpressionNode, r: ExpressionNode,
                                                      comparisonType: ComparisonType = compiledConfiguration.comparisonSettings.defaultComparisonType,
                                                      maxBustCount: Int = compiledConfiguration.comparisonSettings.maxExpressionBustCount): Boolean {
        val left = l.clone()
        val right = r.clone()
        left.applyAllImmediateSubstitutions(compiledConfiguration)
        right.applyAllImmediateSubstitutions(compiledConfiguration)
        left.applyAllSubstitutions(compiledConfiguration.compiledFunctionDefinitions)
        right.applyAllSubstitutions(compiledConfiguration.compiledFunctionDefinitions)
        left.variableReplacement(compiledConfiguration.variableConfiguration.variableImmediateReplacementMap)
        right.variableReplacement(compiledConfiguration.variableConfiguration.variableImmediateReplacementMap)
        if (!left.containsFunctionBesides(algebraAutoCheckingFunctionsSet) && !right.containsFunctionBesides(algebraAutoCheckingFunctionsSet)) {
            if (!probabilityTestComparison(left, right, comparisonType, compiledConfiguration.comparisonSettings.justInDomainsIntersection)) {
                return false
            }
        } else if (!left.containsFunctionBesides(setAutoCheckingFunctionsSet) && !right.containsFunctionBesides(setAutoCheckingFunctionsSet)) {
            if (!logicFullSearchComparison(left, right, comparisonType, maxBustCount, true)) {
                return false
            }
        } else if (compiledConfiguration.subjectType == "combinatorics" && !left.containsFunctionBesides(combinatoricsAutoCheckingFunctionsSet) && !right.containsFunctionBesides(combinatoricsAutoCheckingFunctionsSet)) {
            if (!probabilityTestComparison(left, right, comparisonType, numberIntervalType = NumberIntervalType.SMALL_NATURAL)) {
                return false
            }
        }
        return true
    }

    fun fastProbabilityCheckOnZero(expression: ExpressionNode) = fastProbabilityCheckOnIncorrectTransformation(expression, zero, ComparisonType.EQUAL)

    fun fastProbabilityEquals(lhs: ExpressionNode, rhs: ExpressionNode) = fastProbabilityCheckOnIncorrectTransformation(lhs, rhs, ComparisonType.EQUAL)

    fun compareWithTreeTransformationRules(leftOriginal: ExpressionNode, rightOriginal: ExpressionNode, transformations: Collection<ExpressionSubstitution>,
                                           maxTransformationWeight: Double = compiledConfiguration.comparisonSettings.maxExpressionTransformationWeight,
                                           maxBustCount: Int = compiledConfiguration.comparisonSettings.maxExpressionBustCount,
                                           minTransformationWeight: Double = transformations.minByOrNull { it.weight }?.weight
                                                   ?: 1.0,
                                           expressionChainComparisonType: ComparisonType = ComparisonType.EQUAL,
                                           maxDistBetweenDiffSteps: Double = 1.0): Boolean {
        if (transformations.all { !it.basedOnTaskContext } &&
                (expressionChainComparisonType != ComparisonType.EQUAL || !compareAsIs(leftOriginal, rightOriginal)) &&
                !fastProbabilityCheckOnIncorrectTransformation(leftOriginal, rightOriginal, expressionChainComparisonType, maxBustCount)) { // incorrect comparisons take much more time to check it becouse of importance of all rules combination searching. Here we try to avoid such
            return false
        }

        val resultForOperandsInOriginalOrder = compareWithTreeTransformationRulesInternal(leftOriginal, rightOriginal, transformations, maxTransformationWeight,
                maxBustCount, minTransformationWeight, expressionChainComparisonType, false, maxDistBetweenDiffSteps = maxDistBetweenDiffSteps)
        if (resultForOperandsInOriginalOrder) {
            return true
        } else {
            val resultForOperandsInSortedOrder = compareWithTreeTransformationRulesInternal(leftOriginal, rightOriginal, transformations, maxTransformationWeight,
                    maxBustCount, minTransformationWeight, expressionChainComparisonType, true, maxDistBetweenDiffSteps = maxDistBetweenDiffSteps)
            return resultForOperandsInSortedOrder
        }

    }

    fun compareWithTreeTransformationRulesInternal(leftOriginal: ExpressionNode, rightOriginal: ExpressionNode, transformations: Collection<ExpressionSubstitution>,
                                                   maxTransformationWeight: Double = compiledConfiguration.comparisonSettings.maxExpressionTransformationWeight,
                                                   maxBustCount: Int = compiledConfiguration.comparisonSettings.maxExpressionBustCount,
                                                   minTransformationWeight: Double = transformations.minByOrNull { it.weight }?.weight
                                                           ?: 1.0,
                                                   expressionChainComparisonType: ComparisonType = ComparisonType.EQUAL,
                                                   sortOperands: Boolean = false,
                                                   maxDistBetweenDiffSteps: Double = 1.0): Boolean {
        val normFormsOfExpressionNode = mutableMapOf<ExpressionSubstitutionNormType, Pair<ExpressionNode, ExpressionNode>>()
        val left = if (sortOperands) leftOriginal.cloneWithSortingChildrenForExpressionSubstitutionComparison() else leftOriginal.clone()
        val right = if (sortOperands) rightOriginal.cloneWithSortingChildrenForExpressionSubstitutionComparison() else rightOriginal.clone()
        left.applyAllImmediateSubstitutions(compiledConfiguration)
        right.applyAllImmediateSubstitutions(compiledConfiguration)
        definedFunctionNameNumberOfArgsSet = if (maxTransformationWeight < 0.5) {
            compiledConfiguration.noTransformationDefinedFunctionNameNumberOfArgsSet
        } else {
            compiledConfiguration.definedFunctionNameNumberOfArgsSet
        } //set as global class instance parameter because it also mast be used for function arguments comparison
        if (compareWithoutSubstitutions(left, right, expressionChainComparisonType)) return true //we can have situation, when this true after first sorted, but we require sort with only substitution
        if (maxTransformationWeight < minTransformationWeight) return false
        if (left.containsDifferentiation() || right.containsDifferentiation()) {
            var leftDiffWeight = (listOf(0.0) + if (maxDistBetweenDiffSteps > unlimitedWeight) listOf(maxDistBetweenDiffSteps) else emptyList()).toMutableList()
            var rightDiffWeight = (listOf(0.0) + if (maxDistBetweenDiffSteps > unlimitedWeight) listOf(maxDistBetweenDiffSteps) else emptyList()).toMutableList()
            val leftDiff = left.diff(leftDiffWeight, compiledConfiguration)
            val rightDiff = right.diff(rightDiffWeight, compiledConfiguration)
            if (abs(leftDiffWeight[0] - rightDiffWeight[0]) < maxDistBetweenDiffSteps &&
                    compareWithoutSubstitutions(leftDiff, rightDiff, expressionChainComparisonType)) {
                return true
            }
        }
        if (checkOpeningBracketsSubstitutions(left, right, expressionChainComparisonType) ||
                checkOpeningBracketsSubstitutions(right, left, expressionChainComparisonType.reverse())) {
            return true
        }
        val functionsInLeftExpression = left.getContainedFunctions()
        val functionsInRightExpression = right.getContainedFunctions()
        val functionsInExpression = functionsInLeftExpression + functionsInRightExpression
        val filteredTransformations = mutableListOf<Pair<ExpressionSubstitution, Double>>()
        for (transformation in transformations) {
            if (transformation.weight > maxTransformationWeight) {
                continue;
            }
            val transformationLeftIntersectLeftExpression = functionsInLeftExpression.intersect(transformation.leftFunctions)
            val transformationLeftIntersectRightExpression = functionsInRightExpression.intersect(transformation.leftFunctions)
            if (transformation.leftFunctions.isNotEmpty() && transformationLeftIntersectLeftExpression.isEmpty() && transformationLeftIntersectRightExpression.isEmpty()) {
                continue
            }
            var score = 1.0
            val transformationRightIntersectLeftExpression = functionsInLeftExpression.intersect(transformation.rightFunctions)
            val transformationRightIntersectRightExpression = functionsInRightExpression.intersect(transformation.rightFunctions)
            if (transformation.rightFunctions.isNotEmpty() && transformation.leftFunctions.isNotEmpty()) {
                if (transformationLeftIntersectLeftExpression.isEmpty() && transformationRightIntersectLeftExpression.isEmpty()) {
                    score = 0.0 //left expression can not be achieved with this transformation
                } else if (transformationLeftIntersectRightExpression.isEmpty() && transformationRightIntersectRightExpression.isEmpty()) {
                    score = 0.0 //right expression can not be achieved with this transformation
                }
            }
            filteredTransformations.add(Pair(transformation, score))
        }
        if (compiledConfiguration.comparisonSettings.useTransformationsSortingInExpressionComparison) {
            filteredTransformations.sortByDescending { it.second } // sort by score decreasing
        }
        for (filteredTransformation in filteredTransformations) {
            val originalTransformation = filteredTransformation.first
            if (//!originalTransformation.basedOnTaskContext && //taskContextRules contains quite small count of rules and its applicability depends not only on functions
                    originalTransformation.leftFunctions.isNotEmpty() &&
                    functionsInExpression.intersect(originalTransformation.leftFunctions).isEmpty()) { //fast check to indicate that rule is not applicable
                continue
            }
            val transformation = if (sortOperands) {
                ExpressionSubstitution(originalTransformation.left.cloneWithSortingChildrenForExpressionSubstitutionComparison(),
                        originalTransformation.right.cloneWithSortingChildrenForExpressionSubstitutionComparison(),
                        originalTransformation.weight,
                        originalTransformation.basedOnTaskContext,
                        originalTransformation.code,
                        originalTransformation.nameEn,
                        originalTransformation.nameRu,
                        originalTransformation.comparisonType,
                        originalTransformation.leftFunctions,
                        originalTransformation.rightFunctions,
                        originalTransformation.matchJumbledAndNested,
                        originalTransformation.priority,
                        originalTransformation.changeOnlyOrder,
                        originalTransformation.simpleAdditional,
                        originalTransformation.isExtending,
                        originalTransformation.normalizationType
                )
            } else {
                originalTransformation
            }
            if (!normFormsOfExpressionNode.containsKey(transformation.normalizationType)) {
                val l = if (sortOperands) left.cloneWithSortingChildrenForExpressionSubstitutionComparison() else left.clone()
                val r = if (sortOperands) right.cloneWithSortingChildrenForExpressionSubstitutionComparison() else right.clone()
                when (transformation.normalizationType) {
                    ExpressionSubstitutionNormType.ORIGINAL -> normFormsOfExpressionNode[transformation.normalizationType] = Pair(l, r)
                    ExpressionSubstitutionNormType.SORTED -> normFormsOfExpressionNode[transformation.normalizationType] = Pair(left.cloneWithSortingChildrenForExpressionSubstitutionComparison(), right.cloneWithSortingChildrenForExpressionSubstitutionComparison())
                    ExpressionSubstitutionNormType.I_MULTIPLICATED -> normFormsOfExpressionNode[transformation.normalizationType] = Pair(left.cloneWithIMultipleNorm(), right.clone())//for right not need normalization because all substitutions in normal forms
                    ExpressionSubstitutionNormType.SORTED_AND_I_MULTIPLICATED -> normFormsOfExpressionNode[transformation.normalizationType] = Pair(left.cloneWithSortingChildrenForExpressionSubstitutionComparison().iMultiplicativeNormForm(), right.cloneWithSortingChildrenForExpressionSubstitutionComparison())
                }
            }
            val l = normFormsOfExpressionNode[transformation.normalizationType]!!.first.clone()
            val r = normFormsOfExpressionNode[transformation.normalizationType]!!.second.clone()
            val direction = getComparingDirection(expressionChainComparisonType, transformation.comparisonType)
            if (direction == null) {
                log.add(expressionChainComparisonType.string, transformation.comparisonType.string, {
                    "$comparisonTypesConflict '"
                }, { "' in expression vs '" }, { "' in rule. MSG_CODE_" }, messageType = MessageType.USER)
            }
            val substitutionPlaces = if (direction != SubstitutionDirection.RIGHT_TO_LEFT) {
                transformation.findAllPossibleSubstitutionPlaces(l, this)
            } else {
                listOf<SubstitutionPlace>()
            } +
                    if (direction != SubstitutionDirection.LEFT_TO_RIGHT) {
                        transformation.findAllPossibleSubstitutionPlaces(r, this)
                    } else {
                        listOf<SubstitutionPlace>()
                    }
            val bitMaskCount = 1 shl substitutionPlaces.size
            if (substitutionPlaces.size * 2 > Int.SIZE_BITS || bitMaskCount > maxBustCount || // to avoid overflow
                    bitMaskCount * transformations.size > maxBustCount) {
                transformation.applySubstitution(substitutionPlaces, this)
                if (compareWithTreeTransformationRulesInternal(l, r, transformations, maxTransformationWeight - transformation.weight,
                                maxBustCount, minTransformationWeight, expressionChainComparisonType, sortOperands)) {
                    return true
                }
                if (substitutionPlaces.size > 1) {
                    var bitMask = 1
                    while (bitMask < bitMaskCount) {
                        transformation.applySubstitutionByBitMask(substitutionPlaces, bitMask)
                        if (compareWithTreeTransformationRulesInternal(l.clone(), r.clone(), transformations, maxTransformationWeight - transformation.weight,
                                        maxBustCount, minTransformationWeight, expressionChainComparisonType, sortOperands)) {
                            return true
                        }
                        bitMask = bitMask shl 1
                    }
                }
            } else {
                for (bitMask in 1 until bitMaskCount) {
                    transformation.applySubstitutionByBitMask(substitutionPlaces, bitMask)
                    if (compareWithTreeTransformationRulesInternal(l.clone(), r.clone(), transformations, maxTransformationWeight - transformation.weight,
                                    maxBustCount, minTransformationWeight, expressionChainComparisonType, sortOperands)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun fullExpressionsCompare(left: ExpressionNode, right: ExpressionNode,
                               expressionChainComparisonType: ComparisonType = ComparisonType.EQUAL): Boolean {
        left.applyAllSubstitutions(compiledConfiguration.compiledImmediateTreeTransformationRules)
        right.applyAllSubstitutions(compiledConfiguration.compiledImmediateTreeTransformationRules)
        if (compiledConfiguration.comparisonSettings.isComparisonWithRules) {
            if (compareWithTreeTransformationRulesInternal(left, right, compiledConfiguration.compiledExpressionTreeTransformationRules,
                            compiledConfiguration.comparisonSettings.maxTransformationWeight, compiledConfiguration.comparisonSettings.maxBustCount, expressionChainComparisonType = expressionChainComparisonType)) return true
            baseOperationsDefinitions.computeExpressionTree(left.children[0])
            baseOperationsDefinitions.computeExpressionTree(right.children[0])
            if (compareWithTreeTransformationRulesInternal(left, right, compiledConfiguration.compiledExpressionTreeTransformationRules,
                            compiledConfiguration.comparisonSettings.maxTransformationWeight, compiledConfiguration.comparisonSettings.maxBustCount, expressionChainComparisonType = expressionChainComparisonType)) return true
        } else {
            if (compareWithoutSubstitutions(left, right)) return true
        }
        return false
    }
}

fun getComparingDirection(expressionChainComparisonType: ComparisonType, transformationComparisonType: ComparisonType): SubstitutionDirection? {
    return when (expressionChainComparisonType) {
        ComparisonType.EQUAL -> if (transformationComparisonType == ComparisonType.EQUAL) SubstitutionDirection.ALL_TO_ALL else null
        ComparisonType.LEFT_MORE_OR_EQUAL -> when (transformationComparisonType) {
            ComparisonType.EQUAL -> SubstitutionDirection.ALL_TO_ALL
            ComparisonType.LEFT_MORE_OR_EQUAL, ComparisonType.LEFT_MORE -> SubstitutionDirection.LEFT_TO_RIGHT
            ComparisonType.LEFT_LESS_OR_EQUAL, ComparisonType.LEFT_LESS -> SubstitutionDirection.RIGHT_TO_LEFT
        }
        ComparisonType.LEFT_LESS_OR_EQUAL -> when (transformationComparisonType) {
            ComparisonType.EQUAL -> SubstitutionDirection.ALL_TO_ALL
            ComparisonType.LEFT_MORE_OR_EQUAL, ComparisonType.LEFT_MORE -> SubstitutionDirection.RIGHT_TO_LEFT
            ComparisonType.LEFT_LESS_OR_EQUAL, ComparisonType.LEFT_LESS -> SubstitutionDirection.LEFT_TO_RIGHT
        }
        ComparisonType.LEFT_MORE -> when (transformationComparisonType) {
            ComparisonType.LEFT_MORE -> SubstitutionDirection.LEFT_TO_RIGHT
            ComparisonType.LEFT_LESS -> SubstitutionDirection.RIGHT_TO_LEFT
            else -> null
        }
        ComparisonType.LEFT_LESS -> when (transformationComparisonType) {
            ComparisonType.LEFT_MORE -> SubstitutionDirection.RIGHT_TO_LEFT
            ComparisonType.LEFT_LESS -> SubstitutionDirection.LEFT_TO_RIGHT
            else -> null
        }
    }
}