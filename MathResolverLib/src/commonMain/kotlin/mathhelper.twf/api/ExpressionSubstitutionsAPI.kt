package mathhelper.twf.api

import mathhelper.twf.config.CompiledConfiguration
import mathhelper.twf.config.FunctionConfiguration
import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR
import mathhelper.twf.expressiontree.*
import mathhelper.twf.platformdependent.escapeCharacters


fun expressionSubstitutionFromStrings(
        left: String = "",
        right: String = "",
        scope: String = "",
        basedOnTaskContext: Boolean = false,
        matchJumbledAndNested: Boolean = false,
        priority: Int = 50,
        code: String = "",
        nameEn: String = "",
        nameRu: String = "",
        functionConfiguration: FunctionConfiguration = FunctionConfiguration(
                scopeFilter = scope.split(";").filter { it.isNotEmpty() }.toSet()
        ),
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration(functionConfiguration = functionConfiguration)
) = ExpressionSubstitution(
        if (left.isNotBlank()) stringToExpression(left, compiledConfiguration = compiledConfiguration) else ExpressionNode(NodeType.EMPTY, ""),
        if (right.isNotBlank()) stringToExpression(right, compiledConfiguration = compiledConfiguration) else ExpressionNode(NodeType.EMPTY, ""),
        basedOnTaskContext = basedOnTaskContext,
        matchJumbledAndNested = matchJumbledAndNested,
        priority = priority,
        code = if (code.isNotBlank()) code else "'${stringToStructureString(left, compiledConfiguration = compiledConfiguration)}'->'${stringToStructureString(right, compiledConfiguration = compiledConfiguration)}'",
        nameEn = nameEn,
        nameRu = nameRu
)


fun expressionSubstitutionFromStructureStrings(
        leftStructureString: String = "",
        rightStructureString: String = "",
        basedOnTaskContext: Boolean = false,
        matchJumbledAndNested: Boolean = false,
        simpleAdditional: Boolean = false,
        isExtending: Boolean = false,
        priority: Int = 50,
        code: String = "",
        nameEn: String = "",
        nameRu: String = "",
        normalizationType: ExpressionSubstitutionNormType = ExpressionSubstitutionNormType.ORIGINAL,
        weight: Double = 1.0,
        weightInTaskAutoGeneration: Double = 1.0,
        useWhenPostprocessGeneratedExpression: Boolean = false
) = ExpressionSubstitution(
        if (leftStructureString.isNotBlank()) structureStringToExpression(leftStructureString) else ExpressionNode(NodeType.EMPTY, ""),
        if (rightStructureString.isNotBlank()) structureStringToExpression(rightStructureString) else ExpressionNode(NodeType.EMPTY, ""),
        basedOnTaskContext = basedOnTaskContext,
        matchJumbledAndNested = matchJumbledAndNested,
        simpleAdditional = simpleAdditional,
        isExtending = isExtending,
        priority = priority,
        code = if (code.isNotBlank()) code else "'$leftStructureString'->'$rightStructureString'",
        nameEn = nameEn,
        nameRu = nameRu,
        normalizationType = normalizationType,
        weight = weight,
        weightInTaskAutoGeneration = weightInTaskAutoGeneration,
        useWhenPostprocessGeneratedExpression = useWhenPostprocessGeneratedExpression
    )


fun expressionSubstitutionFromRuleITR(ruleITR: RuleITR) = expressionSubstitutionFromStructureStrings(
        ruleITR.leftStructureString ?: "",
        ruleITR.rightStructureString ?: "",
        ruleITR.basedOnTaskContext ?: false,
        ruleITR.matchJumbledAndNested ?: false,
        ruleITR.simpleAdditional ?: false,
        ruleITR.isExtending ?: false,
        ruleITR.priority ?: 50,
        ruleITR.code ?: "",
        ruleITR.nameEn ?: "",
        ruleITR.nameRu ?: "",
        ExpressionSubstitutionNormType.valueOf(ruleITR.normalizationType ?: "ORIGINAL"),
        ruleITR.weight ?: 1.0,
        ruleITR.weightInTaskAutoGeneration ?: 1.0,
        ruleITR.useWhenPostprocessGeneratedExpression
    )


fun expressionSubstitutionsFromRulePackITR(rulePackITR: RulePackITR, rulePacksITR: Map<String,RulePackITR>, includeChildRulePacks: Boolean): MutableList<ExpressionSubstitution> {
    val result = mutableListOf<ExpressionSubstitution>()
    if (rulePackITR.rules != null) {
        for (rule in rulePackITR.rules) {
            result.add(expressionSubstitutionFromRuleITR(rule))
        }
    }
    if (includeChildRulePacks) {
        rulePackITR.rulePacks?.forEach {
            val rulePack = rulePacksITR[it.rulePackCode]
            if (rulePack != null) {
                result.addAll(expressionSubstitutionsFromRulePackITR(rulePack, rulePacksITR, includeChildRulePacks))
            } else {
                throw IllegalArgumentException("No Rule Pack with code '${it.rulePackCode}'")
            }
        }
    }
    return result
}


fun getRulePackCodesFromTree(rulePackITR: RulePackITR, rulePacksITR: Map<String,RulePackITR>): MutableSet<String> {
    val result = mutableSetOf<String>(rulePackITR.code ?: throw IllegalArgumentException("No code in Rule Pack"))
    rulePackITR.rulePacks?.forEach {
        val rulePack = rulePacksITR[it.rulePackCode]
        if (rulePack != null) {
            result.addAll(getRulePackCodesFromTree(rulePack, rulePacksITR))
        } else {
            throw IllegalArgumentException("No Rule Pack with code '${it.rulePackCode}'")
        }
    }
    return result
}


fun findSubstitutionPlacesInExpression(
        expression: ExpressionNode,
        substitution: ExpressionSubstitution,
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration()
): MutableList<SubstitutionPlace> {
    if (!substitution.isAppropriateToFunctions(expression.getContainedFunctions()) &&
            substitution.left.getContainedVariables().intersect(expression.getContainedVariables()).isEmpty()
    ) {
        return mutableListOf()
    }
    var expr = expression
    var result = substitution.findAllPossibleSubstitutionPlaces(expression, compiledConfiguration.factComparator.expressionComparator)
    if (result.isEmpty() && substitution.matchJumbledAndNested && expression.containsNestedSameFunctions()) {
        expr = expression.cloneWithExpandingNestedSameFunctions()
        result = substitution.findAllPossibleSubstitutionPlaces(expr, compiledConfiguration.factComparator.expressionComparator)
    }
    if (result.isEmpty()) {
        expr = expr.cloneAndSimplifyByComputeSimplePlaces()
        result = substitution.findAllPossibleSubstitutionPlaces(expr, compiledConfiguration.factComparator.expressionComparator)
    }
    return result
}


fun applySubstitution(
        expression: ExpressionNode,
        substitution: ExpressionSubstitution,
        substitutionPlaces: List<SubstitutionPlace>, //containsPointersOnExpressionPlaces
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration()
): ExpressionNode {
    substitution.applySubstitution(substitutionPlaces, compiledConfiguration.factComparator.expressionComparator)
    expression.getTopNode().reduceExtraSigns(setOf("+"), setOf("-"))
    expression.getTopNode().normalizeSubtructions(FunctionConfiguration())
    expression.getTopNode().computeNodeIdsAsNumbersInDirectTraversalAndDistancesToRoot()
    expression.normalizeParentLinks()
    return expression
}


fun createCompiledConfigurationFromExpressionSubstitutionsAndParams(
        expressionSubstitutions: Array<ExpressionSubstitution>,
        additionalParamsMap: Map<String, String> = mapOf()
): CompiledConfiguration {
    val simpleComputationRuleCodesCandidates = expressionSubstitutions.map { it.code }.filter { it.isNotBlank() }.toSet()
    return CompiledConfiguration(additionalParamsMap = additionalParamsMap, simpleComputationRuleCodesCandidates = simpleComputationRuleCodesCandidates).apply {
        compiledExpressionTreeTransformationRules.clear()
        compiledExpressionSimpleAdditionalTreeTransformationRules.clear()
        val handledCodesHashSet = hashSetOf<String>()
        for (substitution in expressionSubstitutions) {
            if (handledCodesHashSet.contains(substitution.code))
                continue
            handledCodesHashSet.add(substitution.code)
            if (substitution.left.nodeType == NodeType.EMPTY || substitution.right.nodeType == NodeType.EMPTY) {
                if (substitution.code.isNotEmpty()) {
                    expressionTreeAutogeneratedTransformationRuleIdentifiers.put(substitution.code, substitution)
                }
            } else {
                compiledExpressionTreeTransformationRules.add(substitution)
                if (substitution.simpleAdditional) {
                    compiledExpressionSimpleAdditionalTreeTransformationRules.add(substitution)
                }
            }
        }
    }
}


fun findApplicableSubstitutionsInSelectedPlace(
        expression: ExpressionNode,
        selectedNodeIds: Array<Int>,
        compiledConfiguration: CompiledConfiguration,
        simplifyNotSelectedTopArguments: Boolean = false,
        withReadyApplicationResult: Boolean = true,
        withFullExpressionChangingPart: Boolean = true
) = generateSubstitutionsBySelectedNodes(
        SubstitutionSelectionData(expression, selectedNodeIds, compiledConfiguration),
        withReadyApplicationResult = withReadyApplicationResult,
        withFullExpressionChangingPart = withFullExpressionChangingPart
)


fun applySubstitutionInSelectedPlace(
        expression: ExpressionNode,
        selectedNodeIds: Array<Int>,
        substitution: ExpressionSubstitution,
        compiledConfiguration: CompiledConfiguration,
        simplifyNotSelectedTopArguments: Boolean = false
): ExpressionNode? {
    val substitutionSelectionData = SubstitutionSelectionData(expression, selectedNodeIds, compiledConfiguration)
    fillSubstitutionSelectionData(substitutionSelectionData)
    return applySubstitution(substitutionSelectionData, substitution, simplifyNotSelectedTopArguments)
}


fun checkAndAddNewVariableReplacement(
        variableName: String,
        variableValue: ExpressionNode,
        expression: ExpressionNode,
        compiledConfiguration: CompiledConfiguration
) = compiledConfiguration.checkAndAddNewVariableReplacement(variableName, variableValue, expression)


fun generateSubstitutionsBySelectedNodesAndItsForwardInverseExtension(
        expression: ExpressionNode,
        selectedNodeIds: Array<Int>,
        compiledConfiguration: CompiledConfiguration,
        forwardInverseExtension: ForwardInverseExtension,
        withFullExpressionChangingPart: Boolean = true
): List<SubstitutionApplication> = generateSubstitutionsBySelectedNodesAndItsForwardInverseExtension(
        SubstitutionSelectionData(expression, selectedNodeIds, compiledConfiguration),
        forwardInverseExtension,
        withFullExpressionChangingPart = withFullExpressionChangingPart
)

fun getAllowedForwardInverseExtensionTypes(compiledConfiguration: CompiledConfiguration): List<ForwardInverseExtensionType> {
    val result = mutableListOf<ForwardInverseExtensionType>()
    when (compiledConfiguration.subjectType) { //to avoid extra specifications in rule packs
        "set", "logic" -> {
            if (compiledConfiguration.expressionTreeAutogeneratedTransformationRuleIdentifiers.containsKey("SetComplicatingExtension")){
                result.add(ForwardInverseExtensionType.LOGIC_ABSORPTION)
            }
        }
        else -> {
            if (compiledConfiguration.expressionTreeAutogeneratedTransformationRuleIdentifiers.containsKey("AdditiveComplicatingExtension")) {
                result.add(ForwardInverseExtensionType.ADD_SUBTRACT)
            }
            if (compiledConfiguration.expressionTreeAutogeneratedTransformationRuleIdentifiers.containsKey("MultiplicativeComplicatingExtension")) {
                result.add(ForwardInverseExtensionType.MULTIPLY_DIVIDE)
            }
            if (compiledConfiguration.expressionTreeAutogeneratedTransformationRuleIdentifiers.containsKey("PowRootComplicatingExtension")) {
                result.add(ForwardInverseExtensionType.POW_ROOT)
            }
            if (compiledConfiguration.expressionTreeAutogeneratedTransformationRuleIdentifiers.containsKey("LogExpComplicatingExtension")) {
                result.add(ForwardInverseExtensionType.EXPONENTIATE_LOGARITHM)
            }
        }
    }
    return result
}


fun findLowestSubtreeTopOfSelectedNodesInExpression(
        node: ExpressionNode,
        selectedNodes: List<ExpressionNode>
) = node.findLowestSubtreeTopOfNodes(selectedNodes)


//string API
data class SubstitutionPlaceOfflineData(
        val parentStartPosition: Int,
        val parentEndPosition: Int,
        val startPosition: Int,
        val endPosition: Int
) {
    fun toJSON() = "{" +
            "\"parentStartPosition\":\"$parentStartPosition\"," +
            "\"parentEndPosition\":\"$parentEndPosition\"," +
            "\"startPosition\":\"$startPosition\"," +
            "\"endPosition\":\"$endPosition\"" +
            "}"
}

fun findSubstitutionPlacesCoordinatesInExpressionJSON(
        expression: String,
        substitutionLeft: String,
        substitutionRight: String,
        scope: String = "",
        basedOnTaskContext: Boolean = false,
        matchJumbledAndNested: Boolean = false,
        functionConfiguration: FunctionConfiguration = FunctionConfiguration(
                scopeFilter = scope.split(";").filter { it.isNotEmpty() }.toSet()
        ),
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration(functionConfiguration = functionConfiguration)
): String {
    val substitutionPlaces = findSubstitutionPlacesInExpression(
            stringToExpression(expression, compiledConfiguration = compiledConfiguration),
            expressionSubstitutionFromStrings(
                    substitutionLeft, substitutionRight,
                    basedOnTaskContext = basedOnTaskContext, compiledConfiguration = compiledConfiguration, matchJumbledAndNested = matchJumbledAndNested
            )
    )

    val data = substitutionPlaces.map {
        SubstitutionPlaceOfflineData(
                it.nodeParent.startPosition, it.nodeParent.endPosition,
                it.nodeParent.children[it.nodeChildIndex].startPosition,
                it.nodeParent.children[it.nodeChildIndex].endPosition
        )
    }.joinToString(separator = ",") { it.toJSON() }

    return "{\"substitutionPlaces\":[$data]}"
}

fun findStructureStringsSubstitutionPlacesCoordinatesInExpressionJSON(
        expression: String,
        substitutionLeftStructureString: String,
        substitutionRightStructureString: String,
        scope: String = "",
        basedOnTaskContext: Boolean = false,
        matchJumbledAndNested: Boolean = false,
        functionConfiguration: FunctionConfiguration = FunctionConfiguration(
                scopeFilter = scope.split(";").filter { it.isNotEmpty() }.toSet()
        ),
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration(functionConfiguration = functionConfiguration)
): String {
    val substitutionPlaces = findSubstitutionPlacesInExpression(
            stringToExpression(expression, compiledConfiguration = compiledConfiguration),
            expressionSubstitutionFromStructureStrings(
                    substitutionLeftStructureString, substitutionRightStructureString,
                    basedOnTaskContext = basedOnTaskContext, matchJumbledAndNested = matchJumbledAndNested
            )
    )

    val data = substitutionPlaces.map {
        SubstitutionPlaceOfflineData(
                it.nodeParent.startPosition, it.nodeParent.endPosition,
                it.nodeParent.children[it.nodeChildIndex].startPosition,
                it.nodeParent.children[it.nodeChildIndex].endPosition
        )
    }.joinToString(separator = ",") { it.toJSON() }

    return "{\"substitutionPlaces\":[$data]}"
}

fun applyExpressionBySubstitutionPlaceCoordinates(
        expression: String,
        substitutionLeft: String,
        substitutionRight: String,
        parentStartPosition: Int,
        parentEndPosition: Int,
        startPosition: Int,
        endPosition: Int,
        scope: String = "",
        basedOnTaskContext: Boolean = false,
        matchJumbledAndNested: Boolean = false,
        characterEscapingDepth: Int = 1,
        functionConfiguration: FunctionConfiguration = FunctionConfiguration(
                scopeFilter = scope.split(";").filter { it.isNotEmpty() }.toSet()
        ),
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration(functionConfiguration = functionConfiguration)
): String {
    val actualExpression = stringToExpression(expression, compiledConfiguration = compiledConfiguration)
    val actualSubstitution = expressionSubstitutionFromStrings(
            substitutionLeft, substitutionRight,
            basedOnTaskContext = basedOnTaskContext, compiledConfiguration = compiledConfiguration, matchJumbledAndNested = matchJumbledAndNested
    )
    val substitutionPlaces = findSubstitutionPlacesInExpression(
            actualExpression,
            actualSubstitution
    )

    val actualPlace = substitutionPlaces.filter {
        it.nodeParent.startPosition == parentStartPosition &&
                it.nodeParent.endPosition == parentEndPosition &&
                it.nodeParent.children[it.nodeChildIndex].startPosition == startPosition &&
                it.nodeParent.children[it.nodeChildIndex].endPosition == endPosition
    }

    val result = if (actualPlace.isNotEmpty()) {
        applySubstitution(actualExpression, actualSubstitution, actualPlace)
    } else {
        actualExpression
    }

    return escapeCharacters(expressionToString(result), characterEscapingDepth)
}

fun applyExpressionByStructureStringsSubstitutionPlaceCoordinates(
        expression: String,
        substitutionLeftStructureString: String,
        substitutionRightStructureString: String,
        parentStartPosition: Int,
        parentEndPosition: Int,
        startPosition: Int,
        endPosition: Int,
        scope: String = "",
        basedOnTaskContext: Boolean = false,
        matchJumbledAndNested: Boolean = false,
        characterEscapingDepth: Int = 1,
        functionConfiguration: FunctionConfiguration = FunctionConfiguration(
                scopeFilter = scope.split(";").filter { it.isNotEmpty() }.toSet()
        ),
        compiledConfiguration: CompiledConfiguration = CompiledConfiguration(functionConfiguration = functionConfiguration)
): String {
    val actualExpression = stringToExpression(expression, compiledConfiguration = compiledConfiguration)
    val actualSubstitution = expressionSubstitutionFromStructureStrings(
            substitutionLeftStructureString, substitutionRightStructureString,
            basedOnTaskContext = basedOnTaskContext,
            matchJumbledAndNested = matchJumbledAndNested
    )
    val substitutionPlaces = findSubstitutionPlacesInExpression(
            actualExpression,
            actualSubstitution
    )

    val actualPlace = substitutionPlaces.filter {
        it.nodeParent.startPosition == parentStartPosition &&
                it.nodeParent.endPosition == parentEndPosition &&
                it.nodeParent.children[it.nodeChildIndex].startPosition == startPosition &&
                it.nodeParent.children[it.nodeChildIndex].endPosition == endPosition
    }

    val result = if (actualPlace.isNotEmpty()) {
        applySubstitution(actualExpression, actualSubstitution, actualPlace)
    } else {
        actualExpression
    }

    return escapeCharacters(expressionToString(result), characterEscapingDepth)
}

fun checkAndAddNewVariableReplacement(
        variableName: String,
        variableValue: String,
        expression: String,
        compiledConfiguration: CompiledConfiguration
) = checkAndAddNewVariableReplacement(
        variableName,
        structureStringToExpression(variableValue),
        structureStringToExpression(expression),
        compiledConfiguration
)