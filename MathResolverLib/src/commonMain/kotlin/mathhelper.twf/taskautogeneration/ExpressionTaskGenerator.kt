package mathhelper.twf.taskautogeneration

// TODO: dkorotchenko delete removed commented code

import mathhelper.twf.api.*
import mathhelper.twf.baseoperations.BaseOperationsComputation.Companion.epsilon
import mathhelper.twf.config.*
import mathhelper.twf.expressiondomain.containsUncertainties
import mathhelper.twf.expressiontree.*
import mathhelper.twf.platformdependent.random
import mathhelper.twf.platformdependent.randomBoolean
import mathhelper.twf.platformdependent.randomInt
import mathhelper.twf.taskautogeneration.rulepack.PostprocessorRulePack
import kotlin.math.max
import kotlin.math.min

enum class ExpressionGenerationDirection { ORIGINAL_TO_FINAL, FINAL_TO_ORIGINAL }

data class GeneratedExpression(
        var expressionNode: ExpressionNode,
        var code: String? = null,
        var nameEn: String? = null,
        var nameRu: String? = null,
        var descriptionShortEn: String? = null,
        var descriptionShortRu: String? = null,
        var descriptionEn: String? = null,
        var descriptionRu: String? = null,

        var subjectType: String? = null,
        var tags: MutableSet<String> = mutableSetOf()
)

data class ExpressionTaskGeneratorSettings(
    val expressionGenerationDirection: ExpressionGenerationDirection = ExpressionGenerationDirection.FINAL_TO_ORIGINAL,
    val goalStepsCount: Int = 5,
    val goalDifferentRulesCount: Int = 4,
    val taskStartGenerator: (compiledConfiguration: CompiledConfiguration) -> GeneratedExpression = {
            GeneratedExpression(
                    ExpressionNode(NodeType.FUNCTION, "").apply { addChild(ExpressionNode(NodeType.VARIABLE, "1")) },
                    "e1",
                    subjectType = "standard_math"
            )
        },
    val compiledConfiguration: CompiledConfiguration = CompiledConfiguration(),
    val expressionSubstitutions: List<ExpressionSubstitution> = compiledConfiguration.compiledExpressionTreeTransformationRules,
    val mandatorySubstitutions: Set<ExpressionSubstitution> = setOf(),
    val maxCountSelectedOfTasksOnIteration: Int = 10,
    val widthOfRulesApplicationsOnIteration: Int = 10,
    val minStepsCountInAutogeneration: Int = 4,
    val goalCompletionStepsCount: Int = (goalStepsCount + 1) / 2,
    val extendingSubstitutionsFilter: (ExpressionSubstitution) -> Boolean = { it -> it.priority!! >= 20 },
    val reducingSubstitutionsFilter: (ExpressionSubstitution) -> Boolean = { it -> it.priority!! <= 30 },
    val extendingExpressionSubstitutions: List<ExpressionSubstitution> = expressionSubstitutions.filter { extendingSubstitutionsFilter(it) },
    val reducingExpressionSubstitutions: List<ExpressionSubstitution> = expressionSubstitutions.filter { reducingSubstitutionsFilter(it) },
    val postprocessExpressionSubstitutions: List<ExpressionSubstitution> = expressionSubstitutionsFromRulePackITR(PostprocessorRulePack.get(), mapOf(), false),
    val mandatoryResultTransformations: List<ExpressionSubstitution> = listOf(),
    val newVariablesExpressionSubstitutions: Map<String, List<ExpressionSubstitution>> = mapOf(),
    val nodeIdsToTransformSelector: (ExpressionNode) -> List<Int> = { expressionNode -> expressionNode.selectRandomNodeIdsToTransform() },
    val substitutionChains: Map<String, List<ExpressionSubstitution>> = mapOf()
) {
    init {
        compiledConfiguration.setExpressionSubstitutions(expressionSubstitutions)
    }
}

data class ExpressionTaskIntermediateData(
    var expressionBeforePostprocessPhase1: ExpressionNode = ExpressionNode(NodeType.FUNCTION, ""),
    var expressionBeforePostprocessPhase2: ExpressionNode = ExpressionNode(NodeType.FUNCTION, ""),
    var appliedPostprocessSubstitutions: MutableMap<String, Int> = mutableMapOf() // stores id of step where postprocess substitution was applied
) {
    fun clone(): ExpressionTaskIntermediateData =
        ExpressionTaskIntermediateData(
            this.expressionBeforePostprocessPhase1.clone(),
            this.expressionBeforePostprocessPhase2.clone(),
            appliedPostprocessSubstitutions.toMutableMap()
        )
}

data class ExpressionTask(
        var startExpression: ExpressionNode,
        var currentExpression: ExpressionNode = startExpression,
        var requiredSubstitutions: MutableSet<ExpressionSubstitution> = mutableSetOf(),
        var usedSubstitutions: MutableList<ExpressionSubstitution> = mutableListOf(),
        var previousExpressions: MutableList<ExpressionNode> = mutableListOf(),

        var solution: String = "",
        var solutionsStepTree: MutableList<SolutionsStepITR> = mutableListOf(),
        var hints: MutableList<HintITR> = mutableListOf(),
        var time: Int = 0, //seconds
        var badStructureFine: Double = 0.0,

        var expressionTaskIntermediateData: ExpressionTaskIntermediateData = ExpressionTaskIntermediateData()
) {
    fun clone(): ExpressionTask {
        var result = copy()
        result.currentExpression = currentExpression.clone()
        result.requiredSubstitutions = mutableSetOf()
        result.requiredSubstitutions.addAll(requiredSubstitutions)
        result.usedSubstitutions = mutableListOf()
        result.usedSubstitutions.addAll(usedSubstitutions)
        result.previousExpressions = mutableListOf()
        result.previousExpressions.addAll(previousExpressions)

        result.solutionsStepTree = mutableListOf()
        result.solutionsStepTree.addAll(solutionsStepTree)
        result.hints = mutableListOf()
        result.hints.addAll(hints)

        result.expressionTaskIntermediateData = expressionTaskIntermediateData.clone()
        return result
    }

    fun badExpressionStructureFine(): Double {
        badStructureFine = currentExpression.badStructureFine()
        return badStructureFine
    }
}

fun reweighNewVariablesRules(newVariablesSubstitutionsMap: Map<String, List<ExpressionSubstitution>>, currentTask: ExpressionTask): List<ExpressionSubstitution> {
    val counter = mutableMapOf<String, Int>()
    for (varName in newVariablesSubstitutionsMap.keys) {
        counter[varName] = 0
    }
    currentTask.currentExpression.countNumberOfVariablesInMap(counter)
    val weightPerVar = if (counter.values.any { it == 0 }) 50 else 80
    val overallNumberOfVariables = counter.values.sum() + newVariablesSubstitutionsMap.values.size
    val sumMaxWeigh = weightPerVar * newVariablesSubstitutionsMap.values.size
    val answer = mutableListOf<ExpressionSubstitution>()
    for (varName in newVariablesSubstitutionsMap.keys) {
        for (substitution in newVariablesSubstitutionsMap[varName]!!) {
            answer.add(substitution.copy(newWeightInTaskAutoGeneration = (sumMaxWeigh * ((counter[varName] ?: 0) + 1).toDouble()) / overallNumberOfVariables))
        }
    }
    return answer
}

fun generateExpressionTransformationTasks(
        expressionTaskGeneratorSettings: ExpressionTaskGeneratorSettings
): List<TaskITR> {
    val compiledConfiguration = expressionTaskGeneratorSettings.compiledConfiguration
    val expressionComparator = compiledConfiguration.factComparator.expressionComparator
    val actualExtendingExpressionSubstitutions = if (expressionTaskGeneratorSettings.expressionGenerationDirection == ExpressionGenerationDirection.FINAL_TO_ORIGINAL) {
        swapPartsInExpressionSubstitutions(expressionTaskGeneratorSettings.extendingExpressionSubstitutions)
    } else {
        expressionTaskGeneratorSettings.extendingExpressionSubstitutions
    }
    val actualReducingExpressionSubstitutions = if (expressionTaskGeneratorSettings.expressionGenerationDirection == ExpressionGenerationDirection.FINAL_TO_ORIGINAL) {
        swapPartsInExpressionSubstitutions(expressionTaskGeneratorSettings.reducingExpressionSubstitutions)
    } else {
        expressionTaskGeneratorSettings.reducingExpressionSubstitutions
    }
    val taskStart = expressionTaskGeneratorSettings.taskStartGenerator.invoke(compiledConfiguration)

    var allTasks = mutableListOf<ExpressionTask>()
    var currentTasks = mutableListOf<ExpressionTask>()
    // Apply immediate substitution to unify sqrt, x^0.5, etc
    val startExpression = ExpressionTask(taskStart.expressionNode.clone().apply {
        applyAllImmediateSubstitutions(compiledConfiguration)
    })
    currentTasks.add(startExpression)

    val iterationsCount = expressionTaskGeneratorSettings.goalStepsCount + 2
    var stepId = 1
    //compiledConfiguration.setExpressionSubstitutions(expressionTaskGeneratorSettings.extendingExpressionSubstitutions) // сначала расширяем исходное выражение
    while (currentTasks.isNotEmpty()) {
        var newCurrentTasks = mutableListOf<ExpressionTask>()
        for (currentTask in currentTasks) {
            val currentExpression = currentTask.currentExpression
            currentExpression.computeNodeIdsAsNumbersInDirectTraversalAndDistancesToRoot()
            val actualNewVariablesExpressionSubstitutions = reweighNewVariablesRules(expressionTaskGeneratorSettings.newVariablesExpressionSubstitutions, currentTask)
            compiledConfiguration.setExpressionSubstitutions(
                if (stepId < expressionTaskGeneratorSettings.goalCompletionStepsCount) {
                    expressionTaskGeneratorSettings.extendingExpressionSubstitutions + actualNewVariablesExpressionSubstitutions
                } else {
                    expressionTaskGeneratorSettings.reducingExpressionSubstitutions
                })
            // 1. places, then substitutions
            for (j in 1..expressionTaskGeneratorSettings.widthOfRulesApplicationsOnIteration) {
                var selectedNodeIds = currentExpression.selectNodeIdsToTransformByLastStepId()
                if (selectedNodeIds.isEmpty() || (selectedNodeIds.size == 1 && randomInt(0, 100) == 0)) {
                    val randomNodeId = currentExpression.getAllChildrenNodeIds().random()
                    if (selectedNodeIds.isEmpty() || (selectedNodeIds.first() != randomNodeId)) {
                        selectedNodeIds = selectedNodeIds + listOf(randomNodeId)
                    }
                }
                if (selectedNodeIds.isEmpty()) {
                    continue
                }
                val applications = findApplicableSubstitutionsInSelectedPlace(currentExpression, selectedNodeIds.toTypedArray(),
                        compiledConfiguration, withReadyApplicationResult = true)
                for (application in applications.sortedByDescending { it.expressionSubstitution.weightInTaskAutoGeneration }.take(expressionTaskGeneratorSettings.widthOfRulesApplicationsOnIteration)) {
                    if (!application.expressionSubstitution.isNormalType()) {
                        application.addSubstitutionToResultExpression()
                        application.addStepIdToResultExpression(stepId)
                    }
                    val newTask = currentTask.clone()
                    newTask.currentExpression = application.resultExpression
                    newTask.previousExpressions.add(newTask.currentExpression.clone())
                    newTask.usedSubstitutions.add(application.expressionSubstitution)

                    if (currentTask.previousExpressions.any { compareExpressionNodes(it, newTask.currentExpression) }) {
                        continue
                    }
                    newTask.solutionsStepTree.add(SolutionsStepITR(
                            newTask.currentExpression.toString(),
                            application.expressionSubstitution,
                            selectedNodeIds,
                            stepId,
                            newTask.solutionsStepTree.lastOrNull()?.stepId ?: -1))
                    newTask.time += (application.expressionSubstitution.weight + 1).toInt() * selectedNodeIds.size * 2 // 4 seconds on trivial rule
                    newCurrentTasks.add(newTask)
                }
            }


            // 2. substitutions, then places
            val functionsInExpression = currentExpression.getContainedFunctions()
            val appropriateSubstitutions = mutableListOf<ExpressionSubstitution>()
            val actualExpressionSubstitutions = if (stepId < expressionTaskGeneratorSettings.goalCompletionStepsCount) {
                //запутываем исходное выражение
                actualExtendingExpressionSubstitutions + actualNewVariablesExpressionSubstitutions
            } else {
                //упрощаем получившеся выражение
                actualReducingExpressionSubstitutions
            }
            for (expressionSubstitution in actualExpressionSubstitutions) {
                if (expressionSubstitution.isAppropriateToFunctions(functionsInExpression) && expressionSubstitution.left.nodeType != NodeType.EMPTY) {
                    if (expressionSubstitution.findAllPossibleSubstitutionPlaces(currentExpression, expressionComparator).isNotEmpty()) {
                        expressionSubstitution.setStepIdForRight(stepId)
                        appropriateSubstitutions.add(expressionSubstitution)
                    }
                }
            }
            val appropriateSubstitutionWeight = appropriateSubstitutions.sumByDouble { it.weightInTaskAutoGeneration }
            if (appropriateSubstitutionWeight < epsilon) {
                continue
            }

            for (j in 1..expressionTaskGeneratorSettings.widthOfRulesApplicationsOnIteration) {
                val newTask = currentTask.clone()
                var selector = random(0.0, appropriateSubstitutionWeight)
                var currentSubstitutionIndex = 0
                while (selector > appropriateSubstitutions[currentSubstitutionIndex].weightInTaskAutoGeneration) {
                    selector -= appropriateSubstitutions[currentSubstitutionIndex].weightInTaskAutoGeneration
                    currentSubstitutionIndex++
                }

                val selectedSubstitution = appropriateSubstitutions[currentSubstitutionIndex]
                val places = selectedSubstitution.findAllPossibleSubstitutionPlaces(newTask.currentExpression, expressionComparator)
                if (places.size == 0) {
                    continue
                } else {
                    val changedExpression = newTask.currentExpression.clone()
                    newTask.previousExpressions.add(newTask.currentExpression.clone())
                    newTask.usedSubstitutions.add(selectedSubstitution)
                    val waysOfApplyingCount = (1 shl places.size) + 1
                    val bitMask = if (waysOfApplyingCount > 0) {
                        randomInt(1, waysOfApplyingCount)
                    } else { // overflow
                        randomInt(1, Int.MAX_VALUE)
                    }
                    val changedNodeIds = selectedSubstitution.applySubstitutionByBitMask(places, bitMask)
                    if (currentTask.previousExpressions.any { compareExpressionNodes(it, newTask.currentExpression) }) {
                        continue
                    }
                    newTask.solutionsStepTree.add(SolutionsStepITR(
                            changedExpression.toString(),
                            selectedSubstitution,
                            changedNodeIds,
                            stepId,
                            newTask.solutionsStepTree.lastOrNull()?.stepId ?: -1))
                    newTask.time += (selectedSubstitution.weight + 1).toInt() * changedNodeIds.size * 2 // 4 seconds on trivial rule

                    // apply substitution chains
                    val substitutionChains = expressionTaskGeneratorSettings.substitutionChains
                    val chainContinuation = substitutionChains[selectedSubstitution.code]
                    if (chainContinuation != null) {
                        val randomSubstitutionContinuation = chainContinuation.random()

                        val taskAfterChainApplication = newTask.clone()
                        val changedAfterChainApplicationExpression = taskAfterChainApplication.currentExpression.clone()
                        taskAfterChainApplication.previousExpressions.add(taskAfterChainApplication.currentExpression.clone())
                        taskAfterChainApplication.usedSubstitutions.add(randomSubstitutionContinuation)


                        val substPlaces = randomSubstitutionContinuation
                            .findAllPossibleSubstitutionPlaces(changedAfterChainApplicationExpression, expressionComparator)
                        val modifiedNodeIds =
                            randomSubstitutionContinuation.applySubstitutionByBitMask(substPlaces, bitMask)

                        taskAfterChainApplication.solutionsStepTree.add(SolutionsStepITR(
                            changedAfterChainApplicationExpression.toString(),
                            randomSubstitutionContinuation,
                            modifiedNodeIds,
                            stepId, // TODO: надо ли увеличивать шаг?
                            taskAfterChainApplication.solutionsStepTree.lastOrNull()?.stepId ?: -1))
                        taskAfterChainApplication.time += (selectedSubstitution.weight + 1).toInt() * changedNodeIds.size * 2 // 4 seconds on trivial rule
                    }
                }
                newCurrentTasks.add(newTask)
            }
        }

        newCurrentTasks = newCurrentTasks
            .distinctBy { it.currentExpression.toString() }
            .toMutableList()
        /*newCurrentTasks = newCurrentTasks
            .distinctBy { it.currentExpression.toString() }
            .filter {
                !compiledConfiguration.factComparator.expressionComparator.compareAsIs(
                    it.currentExpression,
                    it.startExpression
                )
            }
            .filter {
                expressionTaskGeneratorSettings.mandatorySubstitutions.isEmpty() || it.usedSubstitutions.toSet()
                    .intersect(expressionTaskGeneratorSettings.mandatorySubstitutions)
                    .isNotEmpty()
            }
            .toMutableList()*/
        if (stepId <= expressionTaskGeneratorSettings.goalCompletionStepsCount / 2) {
            /*
            * newCurrentTasks.sortByDescending {
                compiledConfiguration.similarityMetric(it.startExpression, it.currentExpression)
            }
            * */
            //запутываем исходное выражение
            newCurrentTasks =
                (newCurrentTasks.sortedByDescending { it.requiredSubstitutions.sumByDouble { it.weightInTaskAutoGeneration } }.take(min(expressionTaskGeneratorSettings.maxCountSelectedOfTasksOnIteration, newCurrentTasks.size)) +
                        newCurrentTasks.sortedByDescending { compiledConfiguration.similarityMetric(it.startExpression, it.currentExpression) }.take(min(expressionTaskGeneratorSettings.maxCountSelectedOfTasksOnIteration, newCurrentTasks.size)))
                    .distinctBy { it.currentExpression.toString() }.toMutableList()
        } else {
            //упрощаем получившеся выражение
            //newCurrentTasks.sortBy { it.badExpressionStructureFine() }
            newCurrentTasks =
                (newCurrentTasks.sortedByDescending { it.requiredSubstitutions.sumByDouble { it.weightInTaskAutoGeneration } }.take(min(expressionTaskGeneratorSettings.maxCountSelectedOfTasksOnIteration, newCurrentTasks.size)) +
                        newCurrentTasks.sortedBy { it.badExpressionStructureFine() }.take(min(expressionTaskGeneratorSettings.maxCountSelectedOfTasksOnIteration, newCurrentTasks.size)))
                    .distinctBy { it.currentExpression.toString() }.toMutableList()
            //newCurrentTasks.sortByDescending { it.usedSubstitutions.sumByDouble { it.weightInTaskAutoGeneration } }
            //compiledConfiguration.setExpressionSubstitutions(expressionTaskGeneratorSettings.reducingExpressionSubstitutions)
        }
        //newCurrentTasks = newCurrentTasks.subList(0, min(expressionTaskGeneratorSettings.maxCountSelectedOfTasksOnIteration, newCurrentTasks.size))

        allTasks.addAll(newCurrentTasks.filter{ it.previousExpressions.size >= expressionTaskGeneratorSettings.minStepsCountInAutogeneration })
        currentTasks = newCurrentTasks.filter { it.previousExpressions.size < iterationsCount }.toMutableList()

        stepId++
    }

    //val baseOperationsDefinitions = compiledConfiguration.factComparator.expressionComparator.baseOperationsDefinitions
    allTasks = allTasks.distinctBy { it.currentExpression.toString() }.toMutableList()
    allTasks.forEach {
        it.currentExpression.applyAllSubstitutions(expressionTaskGeneratorSettings.mandatoryResultTransformations)
        it.currentExpression = simplifyAndNormalizeExpression(it.currentExpression, compiledConfiguration)
    }
    //unification
    var resultAllTasks = mutableListOf<ExpressionTask>()
    for (task in allTasks) {
        var isNew = true
        for (resultTask in resultAllTasks) {
            if (compiledConfiguration.factComparator.expressionComparator
                            .compareAsIs(task.currentExpression, resultTask.currentExpression)) {
                isNew = false
                break
            }
        }
        if (isNew) {
            resultAllTasks.add(task)
        }
    }

    resultAllTasks = resultAllTasks
        .map {
            postprocessGeneratedTask(it, expressionTaskGeneratorSettings.postprocessExpressionSubstitutions, expressionComparator, stepId)
        }
        .filter { !it.currentExpression.containsUncertainties(expressionComparator) }
        .distinctBy {
            it.currentExpression.cloneWithNormalization(sorted = true).toString()
        }
        .toMutableList()

    resultAllTasks.sortByDescending {
        compiledConfiguration.similarityMetric(it.startExpression, it.currentExpression)
    }

    //resultAllTasks.sortBy { it.badExpressionStructureFine() }
    resultAllTasks.forEach {
        it.requiredSubstitutions = it.usedSubstitutions.groupBy { it.code }.values.map { it.first() }.toMutableSet()
        it.hints = it.requiredSubstitutions.map {
            HintITR(
                    textEn = "Use rule $$${expressionToTexString(it.left)}=${expressionToTexString(it.right)}$$",
                    textRu = "Используй правило $$${expressionToTexString(it.left)}=${expressionToTexString(it.right)}$$"
            )
        }.toMutableList()

        val expressionsInSolution = mutableListOf<ExpressionNode>().apply {
            addAll(it.previousExpressions)
            add(it.currentExpression)
        }
        if (expressionTaskGeneratorSettings.expressionGenerationDirection == ExpressionGenerationDirection.FINAL_TO_ORIGINAL) {
            expressionsInSolution.reverse()
        }
        it.solution = expressionsInSolution.map { i -> expressionToString(i) }.joinToString(" = ")
    }

    return resultAllTasks.map {
        TaskITR(
                code = taskStart.code,
                nameEn = taskStart.nameEn,
                nameRu = taskStart.nameRu,
                descriptionShortEn = taskStart.descriptionShortEn,
                descriptionShortRu = taskStart.descriptionShortRu,
                descriptionEn = taskStart.descriptionEn,
                descriptionRu = taskStart.descriptionRu,
                subjectType = taskStart.subjectType,
                tags = taskStart.tags,
                originalExpressionStructureString = if (expressionTaskGeneratorSettings.expressionGenerationDirection == ExpressionGenerationDirection.FINAL_TO_ORIGINAL) {
                    it.currentExpression.toString()
                } else {
                    it.startExpression.toString()
                },

                goalType = "expression",
                goalExpressionStructureString = if (expressionTaskGeneratorSettings.expressionGenerationDirection == ExpressionGenerationDirection.FINAL_TO_ORIGINAL) {
                    it.startExpression.toString()
                } else {
                    it.currentExpression.toString()
                },
                goalPattern = "",

                rulePacks = listOf(RulePackLinkITR(rulePackCode = "AdvancedTrigonometry")),
                rules = listOf(),

                stepsNumber = it.previousExpressions.size,
                time = it.time,
                difficulty = 1.0, //TODO correct difficulty

                solutionPlainText = it.solution,
                solutionsStepsTree = mapOf("data" to it.solutionsStepTree),
                hints = mapOf("data" to it.hints),

                expressionTaskIntermediateData = it.expressionTaskIntermediateData
        )
    }
}

fun findPossibleSubstitutions(
    task: ExpressionTask,
    allSubstitutions: List<ExpressionSubstitution>,
    expressionComparator: ExpressionComparator
): List<ExpressionSubstitution> {
    val possibleSubstitutions : MutableList<ExpressionSubstitution> = mutableListOf()
    allSubstitutions.forEach {
        val places = it.findAllPossibleSubstitutionPlaces(
            task.currentExpression,
            expressionComparator
        )
        if (places.size > 0) {
            possibleSubstitutions.add(it)
        }
    }
    return possibleSubstitutions
}

/**
 * Used to transform generated task to normal form
 * For example, this postprocessor removes defects like 1/(1/a), -(-(a)), etc.
 *
 * @param task: task to transform
 * @param postprocessSubstitutions: substitutions that postprocessor will try to apply
 * See [mathhelper.twf.taskautogeneration.rulepack.PostprocessorRulePack]
 *
 * @return task with applied substitutions
 */
fun postprocessGeneratedTask(task: ExpressionTask, postprocessSubstitutions: List<ExpressionSubstitution>,
                             expressionComparator: ExpressionComparator, stepId: Int) : ExpressionTask {
    var currentTask = task.clone()

    var possibleSubstitutions =
        findPossibleSubstitutions(currentTask, postprocessSubstitutions, expressionComparator)
    while (possibleSubstitutions.isNotEmpty()) {
        for (substitution in postprocessSubstitutions) {
            val newTask = currentTask.clone()

            val places = substitution.findAllPossibleSubstitutionPlaces(newTask.currentExpression, expressionComparator)
            if (places.size == 0) {
                continue
            } else {
                val changedExpression = newTask.currentExpression.clone()
                newTask.previousExpressions.add(newTask.currentExpression.clone())
                newTask.usedSubstitutions.add(substitution)
                val bitMask = (1 shl places.size) - 1
                val changedNodeIds = substitution.applySubstitutionByBitMask(places, bitMask)
                newTask.expressionTaskIntermediateData.appliedPostprocessSubstitutions[substitution.code] = stepId
                newTask.solutionsStepTree.add(SolutionsStepITR(
                    changedExpression.toString(),
                    substitution,
                    changedNodeIds,
                    stepId,
                    newTask.solutionsStepTree.lastOrNull()?.stepId ?: -1))
            }
            currentTask = newTask
        }
        possibleSubstitutions =
            findPossibleSubstitutions(currentTask, postprocessSubstitutions, expressionComparator)
    }
    currentTask.expressionTaskIntermediateData.expressionBeforePostprocessPhase1 = task.currentExpression.clone()
    return currentTask
}

fun simplifyAndNormalizeExpression(expression: ExpressionNode, compiledConfiguration: CompiledConfiguration): ExpressionNode{
    var result = compiledConfiguration.factComparator.expressionComparator.baseOperationsDefinitions.computeExpressionTree(expression)
    normalizeExpressionToUsualForm(result, compiledConfiguration)
    result.normalizeTrivialFunctions()
    simplifyExpressionRecursive(result, compiledConfiguration)
    if (result.value != "") {
        result = ExpressionNode(NodeType.FUNCTION, "").apply {addChild(result) }
    }
    return result
}

fun simplifyExpressionRecursive(expression: ExpressionNode, compiledConfiguration: CompiledConfiguration): ExpressionNode{
    for (child in expression.children) {
        simplifyExpressionRecursive(child, compiledConfiguration)
    }
    if (expression.functionStringDefinition?.function?.isCommutativeWithNullWeight == true) {
        expression.children.sortBy {
            it.toString()
                    .replace("(", "").replace(")", "")
                    .replace('-', '~') //to move minus from start
        }
    }
    return expression
}


fun swapPartsInExpressionSubstitutions(
        expressionSubstitutions: List<ExpressionSubstitution>
): List<ExpressionSubstitution> {
    val swappedExpressionSubstitutions = mutableListOf<ExpressionSubstitution>()
    for (expressionSubstitution in expressionSubstitutions) {
        swappedExpressionSubstitutions.add(ExpressionSubstitution(
                expressionSubstitution.right,
                expressionSubstitution.left,
                expressionSubstitution.weight,
                expressionSubstitution.basedOnTaskContext,
                expressionSubstitution.code,
                expressionSubstitution.nameEn,
                expressionSubstitution.nameRu,
                expressionSubstitution.comparisonType,
                matchJumbledAndNested = expressionSubstitution.matchJumbledAndNested,
                priority = expressionSubstitution.priority,
                changeOnlyOrder = expressionSubstitution.changeOnlyOrder,
                simpleAdditional = expressionSubstitution.simpleAdditional,
                isExtending = expressionSubstitution.isExtending,
                normalizationType = expressionSubstitution.normalizationType,
                weightInTaskAutoGeneration = expressionSubstitution.weightInTaskAutoGeneration
        ))

    }
    return swappedExpressionSubstitutions
}

fun compareExpressionNodes(first: ExpressionNode, second: ExpressionNode): Boolean {
    first.fillStructureStringIdentifiers()
    second.fillStructureStringIdentifiers()
    var result = first.expressionStrictureIdentifier?.equals(second.expressionStrictureIdentifier)
    if (result == null) {
        result = false
    }
    return result as Boolean
}

fun ExpressionNode.badStructureFine(): Double {
    var badStructureFine = 0.0
    if (patternDoubleMinus()) {
        badStructureFine += 2
    }
    if (patternUnaryMinus()) {
        badStructureFine += 0.5
    }
    if (patternDoubleMinusInFraction()) {
        badStructureFine += 3
    }
    if (patternThreeLevelsInFraction()) {
        badStructureFine += 0.5
    }
    /*if (patternTooManyLevelsInFraction()) {
        patternCheckerFine += 3
    }*/
    if (patternTooManyLevelsExist()) {
        badStructureFine += 3
    }
    if (patternConstMulConst()) {
        badStructureFine += 3
    }
    badStructureFine += getDepth()
    return badStructureFine
}

fun ExpressionNode.getMaxFractionLevel(initialCount: Int = 0) : Int {
    if (children.isEmpty())
        return initialCount

    val fracCount = if (value == "/") initialCount + 1 else 0

    return (children.map { it.getMaxFractionLevel(fracCount) }.maxOrNull() ?: 0)
}

fun ExpressionNode.selectRandomNodeIdsToTransform(): List<Int> {
    val depth = getDepth()
    val rnd = randomInt(0, depth)
    if (rnd == 0 && value != "") {
        return listOf(nodeId)
    } else if (rnd == 1 && functionStringDefinition?.function?.isCommutativeWithNullWeight == true) {
        val result = mutableListOf<Int>()
        val childrenShuffled = children.shuffled()
        for (child in childrenShuffled) {
            if (randomInt(0, result.size) == 0) {
                result.add(child.nodeId)
            }
        }
        return result
    } else {
        val childrenShuffled = children.shuffled()
        for (child in childrenShuffled) {
            val result = child.selectRandomNodeIdsToTransform()
            if (result.isNotEmpty() && randomBoolean()) {
                return result
            }
        }
    }
    return emptyList()
}

fun ExpressionNode.nodeIdToPlaceWeight(parentStepId: Int): Pair<Map<ExpressionNode, Int>, Int> {
    val resultWeights: MutableMap<ExpressionNode, Int> = mutableMapOf()
    val startWeight = max(lastModifiedStepId + 2, parentStepId - 1)
    var currentWeight = startWeight
    for (child in children) {
        val childResult = child.nodeIdToPlaceWeight(startWeight)
        resultWeights.putAll(childResult.first)
        currentWeight = max(currentWeight, childResult.second - 1)
    }
    resultWeights[this] = currentWeight
    return resultWeights to currentWeight
}

fun ExpressionNode.selectNodeIdsToTransformByLastStepId(): List<Int> {
    val placesWeights = nodeIdToPlaceWeight(lastModifiedStepId).first
    val placesWeightsCount = placesWeights.values.sum()
    val choice = randomInt(0, placesWeightsCount)
    //val selectedNodeIds = mutableListOf<Int>()
    // sort by priority, выбор точки от 0 до sum(priority), смотреть какой попадает в отрезок с нужным приоритетом
    // возможно, стоит брать квадраты приоритетов (или другие степени)
    var current = 0
    for ((v, k) in placesWeights) {
        if (current <= choice && choice < current + k) {
            if (v.functionStringDefinition?.function?.isCommutativeWithNullWeight == true && randomBoolean()) {
                val result = mutableListOf<Int>()
                val childrenShuffled = children.shuffled()
                for (child in childrenShuffled) {
                    if (randomInt(0, result.size) == 0) {
                        result.add(child.nodeId)
                    }
                }
                return result
            } else if (v.value != "") {
                return listOf(v.nodeId)
            } else {
                return emptyList()
            }
        }
        current += k
    }
    return emptyList()
}