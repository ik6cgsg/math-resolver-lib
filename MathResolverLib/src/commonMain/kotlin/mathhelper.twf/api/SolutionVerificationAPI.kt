package mathhelper.twf.api

import mathhelper.twf.config.*
import mathhelper.twf.defaultcontent.ExpressionFrontInput
import mathhelper.twf.defaultcontent.RuleFrontInput
import mathhelper.twf.defaultcontent.toStructureStructureString
import mathhelper.twf.expressiontree.ExpressionSubstitution
import mathhelper.twf.expressiontree.NodeType
import mathhelper.twf.factstransformations.FactConstructorViewer
import mathhelper.twf.logs.log
import mathhelper.twf.mainpoints.*
import mathhelper.twf.platformdependent.JsonParser

fun createExpressionFrontInput(expression: String, format: String) = ExpressionFrontInput(expression, format)

fun createRuleITR(
        code: String? = null,
        nameEn: String? = null,
        nameRu: String? = null,
        descriptionShortEn: String? = null,
        descriptionShortRu: String? = null,
        descriptionEn: String? = null,
        descriptionRu: String? = null,

        left: ExpressionFrontInput? = null,
        right: ExpressionFrontInput? = null,
        priority: Int? = null,
        isExtending: Boolean? = null,
        matchJumbledAndNested: Boolean? = null,
        simpleAdditional: Boolean? = null,
        basedOnTaskContext: Boolean? = null,
        normalizationType: String? = null,
        weight: Double? = null,
        subjectType: String? = null
) = RuleITR(
        code, nameEn, nameRu, descriptionShortEn, descriptionShortRu, descriptionEn, descriptionRu,
        left?.toStructureStructureString(subjectType ?: "") ?: "",
        right?.toStructureStructureString(subjectType ?: "") ?: "",
        priority, isExtending, matchJumbledAndNested, simpleAdditional, basedOnTaskContext, normalizationType, weight
)

fun createRulePackLinkITR(
        namespaceCode: String? = null,
        rulePackCode: String? = null
) = RulePackLinkITR(
        namespaceCode, rulePackCode
)

fun createRulePackITR(
        code: String? = null,
        version: Int = 0,
        namespaceCode: String? = null,
        nameEn: String? = null,
        nameRu: String? = null,
        descriptionShortEn: String? = null,
        descriptionShortRu: String? = null,
        descriptionEn: String? = null,
        descriptionRu: String? = null,

        subjectType: String = "standard_math",
        rulePacks: List<RulePackLinkITR>? = null,
        rules: List<RuleITR>? = null,

        otherCheckSolutionData: String? = null, //parameters in task redefine parameters in this map
        otherAutoGenerationData: String? = null,
        otherData: String? = null
) = RulePackITR(
        code, version, namespaceCode, nameEn, nameRu, descriptionShortEn, descriptionShortRu, descriptionEn, descriptionRu,
        subjectType, rulePacks, rules,

        otherCheckSolutionData = if (otherCheckSolutionData != null) JsonParser.parseMap(otherCheckSolutionData) as Map<String, Any> else null,
        otherAutoGenerationData = if (otherAutoGenerationData != null) JsonParser.parseMap(otherAutoGenerationData) as Map<String, Any> else null,
        otherData = if (otherData != null) JsonParser.parseMap(otherData) as Map<String, Any> else null
)

fun createTaskITR(
        taskCreationType: String = "manual",

        code: String? = null,
        namespaceCode: String? = null,
        nameEn: String? = null,
        nameRu: String? = null,
        descriptionShortEn: String? = null,
        descriptionShortRu: String? = null,
        descriptionEn: String? = null,
        descriptionRu: String? = null,

        subjectType: String? = null,
        tags: Array<String>? = null,

        originalExpression: ExpressionFrontInput? = null,

        goalType: String? = null,
        goalExpression: ExpressionFrontInput? = null,
        goalPattern: String? = null,
        goalNumberProperty: Int? = null,
        otherGoalData: String? = null,

        rulePacks: Array<RulePackLinkITR>? = null,
        rules: Array<RuleITR>? = null,

        stepsNumber: Int? = null,
        time: Int? = null,
        difficulty: Double,

        solution: ExpressionFrontInput? = null,
        solutionsStepsTree: String? = null,

        interestingFacts: String? = null,
        nextRecommendedTasks: String? = null,

        hints: String? = null,
        otherCheckSolutionData: String? = null,

        countOfAutoGeneratedTasks: Int? = 0,
        otherAutoGenerationData: String? = null,

        otherAwardData: String? = null,
        otherData: String? = null
) = TaskITR(
        taskCreationType ?: "manual", code, 1, namespaceCode,
        nameEn, nameRu, descriptionShortEn, descriptionShortRu, descriptionEn, descriptionRu, subjectType,
        tags?.toMutableSet() ?: mutableSetOf<String>(),
        originalExpressionStructureString = originalExpression?.toStructureStructureString(subjectType ?: "") ?: "",

        goalType = goalType,
        goalExpressionStructureString = goalExpression?.toStructureStructureString(subjectType ?: "") ?: "",
        goalPattern = goalPattern,
        goalNumberProperty = goalNumberProperty,
        otherGoalData = if (otherGoalData != null) JsonParser.parseMap(otherGoalData) as Map<String, Any> else null,

        rulePacks = rulePacks?.toList(),
        rules = rules?.toList(),

        stepsNumber = stepsNumber,
        time = time,
        difficulty = difficulty,

        solutionPlainText = solution?.toStructureStructureString(subjectType ?: "") ?: "",
        solutionsStepsTree = if (solutionsStepsTree != null) JsonParser.parseMap(solutionsStepsTree) as Map<String, Any> else null,
        interestingFacts = if (interestingFacts != null) JsonParser.parseMap(interestingFacts) as Map<String, Any> else null,
        nextRecommendedTasks = if (nextRecommendedTasks != null) JsonParser.parseMap(nextRecommendedTasks) as Map<String, Any> else null,
        hints = if (hints != null) JsonParser.parseMap(hints) as Map<String, Any> else null,
        otherCheckSolutionData = if (otherCheckSolutionData != null) JsonParser.parseMap(otherCheckSolutionData) as Map<String, Any> else null,
        countOfAutoGeneratedTasks = countOfAutoGeneratedTasks,
        otherAutoGenerationData = if (otherAutoGenerationData != null) JsonParser.parseMap(otherAutoGenerationData) as Map<String, Any> else null,
        otherAwardData = if (otherAwardData != null) JsonParser.parseMap(otherAwardData) as Map<String, Any> else null,
        otherData = if (otherData != null) JsonParser.parseMap(otherData) as Map<String, Any> else null
)


fun createCompiledConfigurationFromITR(
        taskITR: TaskITR, //solving task
        rulePacksITR: Array<RulePackITR>, //corresponding rule packs
        comparisonSettings: ComparisonSettings = ComparisonSettings()
): CompiledConfiguration {

    log.addMessage({ "build verification settings" }, level = 0)

    val scopeFilter = mutableSetOf<String>()
    if (taskITR.subjectType in listOf("set", "logic")) {
        log.addMessage({ "'setTheory' is added to scopeFilter" }, level = 1)
        scopeFilter.add("setTheory")
    }
    val functionConfiguration = FunctionConfiguration(scopeFilter)
    if (taskITR.rules?.isNotEmpty() == true || taskITR.rulePacks?.isNotEmpty() == true) {
        functionConfiguration.treeTransformationRules = mutableListOf()
        functionConfiguration.taskContextTreeTransformationRules = mutableListOf()
        log.addMessage({ "functionConfiguration rules cleaned" }, level = 1)
    }

    val expressionSubstitutions = mutableListOf<ExpressionSubstitution>()
    taskITR.rules?.forEach {
        expressionSubstitutions.add(expressionSubstitutionFromRuleITR(it))
    }
    log.addMessage({ "rulePacksITR: ${rulePacksITR.joinToString { "${it.code!!} : ${it.rules?.size} rules : ${it.rulePacks?.size} rulePacks" }}" }, level = 1)
    val rulePacksMap = rulePacksITR.filter { it.code != null }.associateBy { it.code!! }
    log.addMessage({ "rulePacksMap: ${rulePacksMap.values.joinToString { "${it.code!!} : ${it.rules?.size} rules : ${it.rulePacks?.size} rulePacks" }}" }, level = 1)
    log.addMessage({ "taskRulePacks: ${taskITR.rulePacks?.joinToString { "${it.rulePackCode!!}" }}" }, level = 1)
    val allRulePackCodes = mutableSetOf<String>()
    taskITR.rulePacks?.forEach {
        val rulePack = rulePacksMap[it.rulePackCode]
        if (rulePack != null) {
            allRulePackCodes.addAll(getRulePackCodesFromTree(rulePack, rulePacksMap))
        }
    }

    //collect all well-known functions and operations from all RulePacks
    val notChangesOnVariablesInComparisonFunctionSet = mutableSetOf<FunctionIdentifier>()
    val notChangesOnVariablesInComparisonFunctionWithoutTransformationsSet = mutableSetOf<FunctionIdentifier>()
    allRulePackCodes.forEach {
        val rulePack = rulePacksMap[it]
        if (rulePack != null) {
            rulePack.rules?.forEach {
                expressionSubstitutions.add(expressionSubstitutionFromRuleITR(it))
            }

            if (rulePack.otherCheckSolutionData != null && rulePack.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionJsonName] is List<*>) {
                notChangesOnVariablesInComparisonFunctionSet.addAll(rulePack.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionJsonName] as List<FunctionIdentifier>)
            }
            if (rulePack.otherCheckSolutionData != null && rulePack.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionWithoutTransformationsJsonName] is List<*>) {
                notChangesOnVariablesInComparisonFunctionWithoutTransformationsSet.addAll(rulePack.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionWithoutTransformationsJsonName] as List<FunctionIdentifier>)
            }
        }
    }

    functionConfiguration.notChangesOnVariablesInComparisonFunction = notChangesOnVariablesInComparisonFunctionSet.toList()
    functionConfiguration.notChangesOnVariablesInComparisonFunctionWithoutTransformations = notChangesOnVariablesInComparisonFunctionWithoutTransformationsSet.toList()

    if (taskITR.otherCheckSolutionData != null && taskITR.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionJsonName] is List<*>) {
        functionConfiguration.notChangesOnVariablesInComparisonFunction = taskITR.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionJsonName] as List<FunctionIdentifier>
    }
    if (taskITR.otherCheckSolutionData != null && taskITR.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionWithoutTransformationsJsonName] is List<*>) {
        functionConfiguration.notChangesOnVariablesInComparisonFunctionWithoutTransformations = taskITR.otherCheckSolutionData[notChangesOnVariablesInComparisonFunctionWithoutTransformationsJsonName] as List<FunctionIdentifier>
    }

    log.addMessage({ "notChangesOnVariablesInComparisonFunction: ${functionConfiguration.notChangesOnVariablesInComparisonFunction.joinToString { it.getIdentifier() }}" }, level = 1)
    log.addMessage({ "notChangesOnVariablesInComparisonFunctionWithoutTransformations: ${functionConfiguration.notChangesOnVariablesInComparisonFunctionWithoutTransformations.joinToString { it.getIdentifier() }}" }, level = 1)

    val compiledConfiguration = CompiledConfiguration(
            functionConfiguration = functionConfiguration,
            subjectType = taskITR.subjectType ?: "",
            comparisonSettings = comparisonSettings
    )

    if (expressionSubstitutions.isNotEmpty()) {
        compiledConfiguration.compiledExpressionTreeTransformationRules.clear()
        log.addMessage({ "expression substitutions handing" }, level = 1)

        val handledCodesHashSet = hashSetOf<String>()
        for (substitution in expressionSubstitutions) {
            if (handledCodesHashSet.contains(substitution.code))
                continue
            handledCodesHashSet.add(substitution.code)
            if (substitution.left.nodeType == NodeType.EMPTY || substitution.right.nodeType == NodeType.EMPTY) {
                if (substitution.code.isNotEmpty()) {
                    log.addMessage({ "substitution '${substitution.code}' added to expressionTreeAutogeneratedTransformationRuleIdentifiers" }, level = 2)
                    compiledConfiguration.expressionTreeAutogeneratedTransformationRuleIdentifiers.put(substitution.code, substitution)
                }
            } else {
                compiledConfiguration.compiledExpressionTreeTransformationRules.add(substitution)
                if (substitution.simpleAdditional) {
                    log.addMessage({ "substitution '${substitution.code}' added to compiledExpressionTreeTransformationRules and compiledExpressionSimpleAdditionalTreeTransformationRules" }, level = 2)
                    compiledConfiguration.compiledExpressionSimpleAdditionalTreeTransformationRules.add(substitution)
                } else {
                    log.addMessage({ "substitution '${substitution.code}' added to compiledExpressionTreeTransformationRules" }, level = 2)
                }
            }
        }
    }

    allRulePackCodes.forEach {
        val rulePack = rulePacksMap[it]
        if (rulePack != null) {
            compiledConfiguration.setInfoFromAdditionalParams(rulePack.otherCheckSolutionData)
        }
    }
    compiledConfiguration.setInfoFromAdditionalParams(taskITR.otherCheckSolutionData)

    return compiledConfiguration
}


fun checkSolutionInTexITR(
        originalTexSolution: String, //string with learner solution in Tex format
        taskITR: TaskITR, //solving task
        rulePacksITR: Array<RulePackITR>, //corresponding rule packs
        shortErrorDescription: String = "0", //make error message shorter and easier to understand: crop parsed steps from error description
        skipTrivialCheck: Boolean = false, //do not check completeness of transformations, only correctness,
        comparisonSettings: ComparisonSettings = ComparisonSettings(),
        inputCompiledConfiguration: CompiledConfiguration? = null // can be specified to make verification process faster
): TexVerificationResult {
    log.clear()

    val finalCompiledConfiguration = inputCompiledConfiguration ?: createCompiledConfigurationFromITR(taskITR, rulePacksITR, comparisonSettings)

    return checkFactsInTex(
            originalTexSolution,
            taskITR.originalExpressionStructureString ?: "",
            taskITR.goalExpressionStructureString ?: "",
            "",
            taskITR.goalPattern ?: "",
            "", // TODO: support inequalities in tasks
            "",
            shortErrorDescription = shortErrorDescription,
            skipTrivialCheck = skipTrivialCheck,
            compiledConfiguration = finalCompiledConfiguration,
            otherGoalData = taskITR.otherGoalData)
}


fun checkSolutionInTex(
        originalTexSolution: String, //string with learner solution in Tex format

        //// individual task parameters:
        startExpressionIdentifier: String = "", //Expression, from which learner need to start the transformations in structure string
        targetFactPattern: String = "", //Pattern that specify criteria that learner's answer must meet
        additionalFactsIdentifiers: String = "", ///Identifiers split by configSeparator - task condition facts should be here, that can be used as rules only for this task

        endExpressionIdentifier: String = "", //Expression, which learner need to deduce
        targetFactIdentifier: String = "", //Fact that learner need to deduce. It is more flexible than startExpressionIdentifier and endExpressionIdentifier, allow to specify inequality like '''EXPRESSION_COMPARISON{(+(/(sin(x);+(1;cos(x)));/(+(1;cos(x));sin(x))))}{<=}{(/(2;sin(x)))}'''
        comparisonSign: String = "", //Comparison sign

        //// general configuration parameters
        //functions, which null-weight transformations allowed (if no other transformations), split by configSeparator
        //choose one of 2 api forms:
        wellKnownFunctions: List<FunctionIdentifier> = listOf(),
        wellKnownFunctionsString: String = "${configSeparator}0$configSeparator${configSeparator}1$configSeparator+$configSeparator-1$configSeparator-$configSeparator-1$configSeparator*$configSeparator-1$configSeparator/$configSeparator-1$configSeparator^$configSeparator-1",

        //functions, which null-weight transformations allowed with any other transformations, split by configSeparator
        //choose one of 2 api forms:
        unlimitedWellKnownFunctions: List<FunctionIdentifier> = wellKnownFunctions,
        unlimitedWellKnownFunctionsString: String = wellKnownFunctionsString,

        //expression transformation rules
        //choose one of api forms:
        expressionTransformationRules: List<ExpressionSubstitution> = listOf(), //full list of expression transformations rules
        expressionTransformationRulesString: String = "S(i, a, a, f(i))${configSeparator}f(a)${configSeparator}S(i, a, b, f(i))${configSeparator}S(i, a, b-1, f(i)) + f(b)", //function transformation rules, parts split by configSeparator; if it equals " " then expressions will be checked only by testing
        taskContextExpressionTransformationRules: String = "", //for expression transformation rules based on variables
        rulePacks: Array<String> = listOf<String>().toTypedArray(),

        maxExpressionTransformationWeight: String = "1.0",
        maxDistBetweenDiffSteps: String = "", //is it allowed to differentiate expression in one step
        scopeFilter: String = "", //subject scopes which user representation sings is used

        shortErrorDescription: String = "0", //make error message shorter and easier to understand: crop parsed steps from error description
        skipTrivialCheck: Boolean = false, //do not check completeness of transformations, only correctness
        otherGoalData: Map<String, Any>? = null, // may contains key="hiddenGoalExpressions" with list of possible goal expression values in structure string
        subjectType: String = ""
): TexVerificationResult {
    log.clear()
    val compiledConfiguration = createConfigurationFromRulePacksAndDetailSolutionCheckingParams(
            rulePacks,
            wellKnownFunctionsString,
            expressionTransformationRulesString,
            maxExpressionTransformationWeight,
            unlimitedWellKnownFunctionsString,
            taskContextExpressionTransformationRules,
            maxDistBetweenDiffSteps,
            scopeFilter,

            wellKnownFunctions,
            unlimitedWellKnownFunctions,
            expressionTransformationRules,
            subjectType)
    return checkFactsInTex(
            originalTexSolution,
            startExpressionIdentifier,
            endExpressionIdentifier,
            targetFactIdentifier,
            targetFactPattern,
            comparisonSign,
            additionalFactsIdentifiers,
            shortErrorDescription,
            skipTrivialCheck,
            compiledConfiguration,
            otherGoalData)
}


fun checkSolutionInTexWithCompiledConfiguration(
        originalTexSolution: String, //string with learner solution in Tex format
        compiledConfiguration: CompiledConfiguration,

        //// individual task parameters:
        startExpressionIdentifier: String = "", //Expression, from which learner need to start the transformations
        targetFactPattern: String = "", //Pattern that specify criteria that learner's answer must meet
        comparisonSign: String = "", //Comparison sign
        additionalFactsIdentifiers: String = "", ///Identifiers split by configSeparator - task condition facts should be here, that can be used as rules only for this task

        endExpressionIdentifier: String = "", //Expression, which learner need to deduce
        targetFactIdentifier: String = "", //Fact that learner need to deduce. It is more flexible than startExpressionIdentifier and endExpressionIdentifier, allow to specify inequality like '''EXPRESSION_COMPARISON{(+(/(sin(x);+(1;cos(x)));/(+(1;cos(x));sin(x))))}{<=}{(/(2;sin(x)))}'''

        shortErrorDescription: String = "0", //make error message shorter and easier to understand: crop parsed steps from error description
        skipTrivialCheck: Boolean = false, //do not check completeness of transformations, only correctness
        otherGoalData: Map<String, Any>? = null // may contains key="hiddenGoalExpressions" with list of possible goal expression values in structure string
): TexVerificationResult {
    log.clear()
    return checkFactsInTex(
            originalTexSolution,
            startExpressionIdentifier,
            endExpressionIdentifier,
            targetFactIdentifier,
            targetFactPattern,
            comparisonSign,
            additionalFactsIdentifiers,
            shortErrorDescription,
            skipTrivialCheck,
            compiledConfiguration,
            otherGoalData)
}

fun checkChainCorrectnessInTex(originalTexSolution: String, subjectType: String = "") = checkSolutionInTex(
        originalTexSolution = originalTexSolution,
        startExpressionIdentifier = "",
        targetFactPattern = "",
        additionalFactsIdentifiers = "",
        endExpressionIdentifier = "",
        targetFactIdentifier = "",
        comparisonSign = "",
        wellKnownFunctions = listOf(),
        wellKnownFunctionsString = "",
        unlimitedWellKnownFunctions = listOf(),
        unlimitedWellKnownFunctionsString = "",
        expressionTransformationRules = listOf(),
        expressionTransformationRulesString = "",
        taskContextExpressionTransformationRules = "",
        rulePacks = listOf("").toTypedArray(),
        maxExpressionTransformationWeight = "",
        maxDistBetweenDiffSteps = "",
        scopeFilter = "",
        shortErrorDescription = "",
        skipTrivialCheck = true,
        otherGoalData = null,
        subjectType = subjectType
)