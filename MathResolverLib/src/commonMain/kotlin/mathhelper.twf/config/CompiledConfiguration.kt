package mathhelper.twf.config

import mathhelper.twf.expressiontree.*
import mathhelper.twf.factstransformations.FactComparator
import mathhelper.twf.factstransformations.FactSubstitution
import mathhelper.twf.factstransformations.parseFromFactIdentifier
import mathhelper.twf.platformdependent.toShortString
import mathhelper.twf.standartlibextensions.isLetter
import mathhelper.twf.taskautogeneration.ExpressionSimilarityMetric
import mathhelper.twf.taskautogeneration.LambdaExpressionSimilarityMetric

data class DebugOutputMessages(
        val expressionProbabilityComparisonFalseDetailsPrintln: Boolean = false
)

class CompiledConfiguration(
        val variableConfiguration: VariableConfiguration = VariableConfiguration(),
        val functionConfiguration: FunctionConfiguration = FunctionConfiguration(setOf("", "subfactorial")),
        val comparisonSettings: ComparisonSettings = ComparisonSettings(),
        val checkedFactAccentuation: CheckedFactAccentuation = CheckedFactAccentuation(),
        val factsLogicConfiguration: FactsLogicConfiguration = FactsLogicConfiguration(),
        val gradientDescentComparisonConfiguration: GradientDescentComparisonConfiguration = GradientDescentComparisonConfiguration(),
        val additionalParamsMap: Map<String, String> = mapOf(),
        val simpleComputationRuleCodesCandidates: Set<String> = setOf(),
        val simpleComputationRuleParams: SimpleComputationRuleParams = SimpleComputationRuleParams(simpleComputationRuleCodesCandidates, true),
        val debugOutputMessages: DebugOutputMessages = DebugOutputMessages(),
        var subjectType: String = "",
        val similarityMetric: ExpressionSimilarityMetric = LambdaExpressionSimilarityMetric { _, r -> r.getContainedFunctions().size.toDouble() }
) {
    val compiledImmediateVariableReplacements = mapOf<String, String>(*(variableConfiguration.variableImmediateReplacementRules.map { Pair(it.left, it.right) }.toTypedArray()))
    val compiledExpressionTreeTransformationRules = mutableListOf<ExpressionSubstitution>()
    val compiledExpressionSimpleAdditionalTreeTransformationRules = mutableListOf<ExpressionSubstitution>()
    val expressionTreeAutogeneratedTransformationRuleIdentifiers = mutableMapOf<String, ExpressionSubstitution>()
    val compiledFactTreeTransformationRules = mutableListOf<FactSubstitution>()
    val compiledImmediateTreeTransformationRules = mutableListOf<ExpressionSubstitution>()
    val compiledFunctionDefinitions = mutableListOf<ExpressionSubstitution>()
    val definedFunctionNameNumberOfArgsSet = mutableSetOf<String>()
    val noTransformationDefinedFunctionNameNumberOfArgsSet = mutableSetOf<String>()
    var configurationErrors = mutableListOf<ConfigurationError>()

    val factComparator: FactComparator

    fun parseStringExpression(expression: String, nameForRuleDesignationsPossible: Boolean = false): ExpressionNode? {
        val expressionTreeParser = ExpressionTreeParser(expression, nameForRuleDesignationsPossible, functionConfiguration, compiledImmediateVariableReplacements)
        val error = expressionTreeParser.parse()
        if (error != null) {
            configurationErrors.add(ConfigurationError(error.description, "TreeTransformationRule", expression, error.position))
            return null
        } else {
            return expressionTreeParser.root
        }
    }


    init {
        factComparator = FactComparator()
        factComparator.init(this)

        if (debugOutputMessages.expressionProbabilityComparisonFalseDetailsPrintln) {
            factComparator.expressionComparator.debugMode = true
        }

        functionConfiguration.notChangesOnVariablesInComparisonFunction
                .forEach { definedFunctionNameNumberOfArgsSet.add(it.getIdentifier()) }

        functionConfiguration.notChangesOnVariablesInComparisonFunctionWithoutTransformations
                .forEach { noTransformationDefinedFunctionNameNumberOfArgsSet.add(it.getIdentifier()) }

        val compiledSubstitutions = mutableMapOf<String, ExpressionSubstitution>()
        for (functionDefinition in functionConfiguration.functionDefinitions) {
            val leftTree = parseStringExpression(functionDefinition.definitionLeftExpression, true)
            val rightTree = parseStringExpression(functionDefinition.definitionRightExpression, true)
            if (leftTree != null && rightTree != null) {
                if (leftTree.children.isEmpty() || rightTree.children.isEmpty()) {
                    configurationErrors.add(ConfigurationError("function definition rule is empty", "TreeTransformationRule",
                            "values: '" + functionDefinition.definitionLeftExpression + "' and '" + functionDefinition.definitionRightExpression + "'", -1))
                } else {
                    leftTree.variableReplacement(compiledImmediateVariableReplacements)
                    rightTree.variableReplacement(compiledImmediateVariableReplacements)
                    rightTree.applyAllFunctionSubstitutions(compiledSubstitutions)
                    val newSubstitution = ExpressionSubstitution(leftTree, rightTree)
                    compiledFunctionDefinitions.add(newSubstitution)
                    val definitionIdentifier = leftTree.children[0].value + "_" + leftTree.children[0].children.size
                    compiledSubstitutions.put(definitionIdentifier, newSubstitution)
                }
            }
        }

        for (treeTransformationRule in functionConfiguration.treeTransformationRules) {
            val leftTree = parseStringExpression(treeTransformationRule.definitionLeftExpression, true)
            val rightTree = parseStringExpression(treeTransformationRule.definitionRightExpression, true)
            if (leftTree != null && rightTree != null) {
                leftTree.variableReplacement(compiledImmediateVariableReplacements)
                rightTree.variableReplacement(compiledImmediateVariableReplacements)
                val newSubstitution = ExpressionSubstitution(leftTree, rightTree, treeTransformationRule.weight)
                if (treeTransformationRule.isImmediate) compiledImmediateTreeTransformationRules.add(newSubstitution)
                else {
                    compiledExpressionTreeTransformationRules.add(newSubstitution)
                    if (newSubstitution.simpleAdditional) {
                        compiledExpressionSimpleAdditionalTreeTransformationRules.add(newSubstitution)
                    }
                }
            }
        }

        for (treeTransformationRule in functionConfiguration.taskContextTreeTransformationRules) {
            val leftTree = parseStringExpression(treeTransformationRule.definitionLeftExpression, true)
            val rightTree = parseStringExpression(treeTransformationRule.definitionRightExpression, true)
            if (leftTree != null && rightTree != null) {
                leftTree.variableReplacement(compiledImmediateVariableReplacements)
                rightTree.variableReplacement(compiledImmediateVariableReplacements)
                val newSubstitution = ExpressionSubstitution(leftTree, rightTree, treeTransformationRule.weight, basedOnTaskContext = true)
                if (treeTransformationRule.isImmediate) compiledImmediateTreeTransformationRules.add(newSubstitution)
                else {
                    compiledExpressionTreeTransformationRules.add(newSubstitution)
                    if (newSubstitution.simpleAdditional) {
                        compiledExpressionSimpleAdditionalTreeTransformationRules.add(newSubstitution)
                    }
                }
            }
        }

        for (factTransformation in factsLogicConfiguration.factsTransformationRules) {
            val leftTree = parseFromFactIdentifier(factTransformation.definitionLeftFactTree, functionConfiguration = functionConfiguration)
            val rightTree = parseFromFactIdentifier(factTransformation.definitionRightFactTree, functionConfiguration = functionConfiguration)
            if (leftTree != null && rightTree != null) {
                leftTree.variableReplacement(compiledImmediateVariableReplacements)
                rightTree.variableReplacement(compiledImmediateVariableReplacements)
                compiledFactTreeTransformationRules.add(
                        FactSubstitution(leftTree, rightTree, factTransformation.weight, direction = factTransformation.direction, factComparator = factComparator)
                )
                if (!factTransformation.isOneDirection) {
                    compiledFactTreeTransformationRules.add(
                            FactSubstitution(rightTree, leftTree, factTransformation.weight, direction = factTransformation.direction, factComparator = factComparator)
                    )
                }
            }
        }

        setInfoFromAdditionalParams(additionalParamsMap)
    }

    fun setInfoFromAdditionalParams (additionalParamsMap: Map<String, Any>? = null) {
        mathhelper.twf.logs.log.addMessage({ "additional params handing" }, level = 1)
        if (additionalParamsMap != null && additionalParamsMap.isNotEmpty()) {
            val simpleComputationRuleParamsMaxCalcComplexity = (additionalParamsMap["simpleComputationRuleParamsMaxCalcComplexity"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxCalcComplexity != null) {
                simpleComputationRuleParams.maxCalcComplexity = simpleComputationRuleParamsMaxCalcComplexity
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxCalcComplexity: ${simpleComputationRuleParamsMaxCalcComplexity}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxTenPowIterations = (additionalParamsMap["simpleComputationRuleParamsMaxTenPowIterations"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxTenPowIterations != null) {
                simpleComputationRuleParams.maxTenPowIterations = simpleComputationRuleParamsMaxTenPowIterations
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxTenPowIterations: ${simpleComputationRuleParamsMaxTenPowIterations}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxPlusArgRounded = (additionalParamsMap["simpleComputationRuleParamsMaxPlusArgRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxPlusArgRounded != null) {
                simpleComputationRuleParams.maxPlusArgRounded = simpleComputationRuleParamsMaxPlusArgRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxPlusArgRounded: ${simpleComputationRuleParamsMaxPlusArgRounded}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxMulArgRounded = (additionalParamsMap["simpleComputationRuleParamsMaxMulArgRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxMulArgRounded != null) {
                simpleComputationRuleParams.maxMulArgRounded = simpleComputationRuleParamsMaxMulArgRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxMulArgRounded: ${simpleComputationRuleParamsMaxMulArgRounded}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxDivBaseRounded = (additionalParamsMap["simpleComputationRuleParamsMaxDivBaseRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxDivBaseRounded != null) {
                simpleComputationRuleParams.maxDivBaseRounded = simpleComputationRuleParamsMaxDivBaseRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxDivBaseRounded: ${simpleComputationRuleParamsMaxDivBaseRounded}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxPowBaseRounded = (additionalParamsMap["simpleComputationRuleParamsMaxPowBaseRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxPowBaseRounded != null) {
                simpleComputationRuleParams.maxPowBaseRounded = simpleComputationRuleParamsMaxPowBaseRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxPowBaseRounded: ${simpleComputationRuleParamsMaxPowBaseRounded}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxPowDegRounded = (additionalParamsMap["simpleComputationRuleParamsMaxPowDegRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxPowDegRounded != null) {
                simpleComputationRuleParams.maxPowDegRounded = simpleComputationRuleParamsMaxPowDegRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxPowDegRounded: ${simpleComputationRuleParamsMaxPowDegRounded}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxLogBaseRounded = (additionalParamsMap["simpleComputationRuleParamsMaxLogBaseRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxLogBaseRounded != null) {
                simpleComputationRuleParams.maxLogBaseRounded = simpleComputationRuleParamsMaxLogBaseRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxLogBaseRounded: ${simpleComputationRuleParamsMaxLogBaseRounded}" }, level = 2)
            }

            val simpleComputationRuleParamsMaxResRounded = (additionalParamsMap["simpleComputationRuleParamsMaxResRounded"] as String?)?.toIntOrNull()
            if (simpleComputationRuleParamsMaxResRounded != null) {
                simpleComputationRuleParams.maxResRounded = simpleComputationRuleParamsMaxResRounded
                mathhelper.twf.logs.log.addMessage({ "simpleComputationRuleParams.maxResRounded: ${simpleComputationRuleParamsMaxResRounded}" }, level = 2)
            }



            val maxExpressionTransformationWeight = (additionalParamsMap["maxExpressionTransformationWeightString"] as String?)?.toDoubleOrNull()
            if (maxExpressionTransformationWeight != null) {
                comparisonSettings.maxExpressionTransformationWeight = maxExpressionTransformationWeight
                mathhelper.twf.logs.log.addMessage({ "comparisonSettings.maxExpressionTransformationWeight: ${maxExpressionTransformationWeight}" }, level = 2)
            }
            val maxDistBetweenDiffSteps = (additionalParamsMap["maxDistBetweenDiffStepsString"] as String?)?.toDoubleOrNull()
            if (maxDistBetweenDiffSteps != null) {
                comparisonSettings.maxDistBetweenDiffSteps = maxDistBetweenDiffSteps
                mathhelper.twf.logs.log.addMessage({ "comparisonSettings.maxDistBetweenDiffSteps: ${maxDistBetweenDiffSteps}" }, level = 2)
            }
        }
    }

    fun checkAndAddNewVariableReplacement(variableName: String, variableValue: ExpressionNode, expression: ExpressionNode): GeneralError? {
        val variableNameSet = setOf(variableName)
        if (expression.containsVariables(variableNameSet)) {
            return GeneralError(code = "EXISTING_VARIABLE", description = "in expression")
        }
        if (variableValue.containsVariables(variableNameSet)) {
            return GeneralError(code = "EXISTING_VARIABLE", description = "in variable value")
        }
        if (compiledExpressionTreeTransformationRules
                        .filter { it.basedOnTaskContext }
                        .any { it.left.containsVariables(variableNameSet) || it.right.containsVariables(variableNameSet) }) {
            return GeneralError(code = "EXISTING_VARIABLE", description = "in task context rules")
        }
        if (variableName.firstOrNull()?.isLetter() != true) {
            return GeneralError(code = "INCORRECT_VARIABLE_NAME", description = variableName.firstOrNull()?.toString()
                    ?: "")
        }
        if (factComparator.expressionComparator.fastProbabilityCheckOnZero(variableValue)) {
            return GeneralError(code = "VARIABLE_VALUE_IS_ZERO", description = "")
        }
        val left = ExpressionNode(NodeType.FUNCTION, "").apply { addChild(ExpressionNode(NodeType.VARIABLE, variableName)) }
        val right = variableValue.clone()
        val rightAfterImmediateSubstitutions = right.clone()
        rightAfterImmediateSubstitutions.applyAllImmediateSubstitutions(this)
        compiledImmediateTreeTransformationRules.add(ExpressionSubstitution(left, rightAfterImmediateSubstitutions, basedOnTaskContext = true))
        compiledExpressionTreeTransformationRules.add(ExpressionSubstitution(left, right, basedOnTaskContext = true))
        compiledExpressionTreeTransformationRules.add(ExpressionSubstitution(right, left, basedOnTaskContext = true))
        return null
    }
    // TODO: add function to add replacements for integral udv replacements and other
    // automatic application of substitutions is not done because of unclear cases, whether transformation should be applied or not:
    // like 't=x+8' in '2*x+15'

    fun createExpressionFunctionNode(function: String, numberOfArgs: Int, children: List<ExpressionNode> = listOf()) = ExpressionNode(NodeType.FUNCTION, function, functionStringDefinition = functionConfiguration.fastFindStringDefinitionByNameAndNumberOfArguments(function, numberOfArgs)).apply {
        for (child in children) {
            addChild(child)
        }
    }

    fun createExpressionVariableNode(value: Double) = if (value >= 0) {
        ExpressionNode(NodeType.VARIABLE, value.toShortString())
    } else {
        val plusNode = createExpressionFunctionNode("+", -1)
        plusNode.addChild(createExpressionFunctionNode("-", -1))
        plusNode.children.last().addChild(ExpressionNode(NodeType.VARIABLE, (-value).toShortString()))
        plusNode
    }

    fun createExpressionVariableNode(value: String) = ExpressionNode(NodeType.VARIABLE, value)
}
