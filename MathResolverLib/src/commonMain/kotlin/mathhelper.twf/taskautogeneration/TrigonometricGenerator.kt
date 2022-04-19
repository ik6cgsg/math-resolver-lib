package mathhelper.twf.taskautogeneration

import mathhelper.twf.api.*
import mathhelper.twf.baseoperations.BaseOperationsComputation.Companion.epsilon
import mathhelper.twf.config.*
import mathhelper.twf.expressiontree.*
import mathhelper.twf.expressiontree.ExpressionComparator.Companion.trigonometricAutoCheckingFunctionsSet
import mathhelper.twf.platformdependent.abs
import mathhelper.twf.platformdependent.random
import mathhelper.twf.platformdependent.randomInt
import mathhelper.twf.taskautogeneration.ExpressionNodeBuilder.Companion.buildNodeFromConstant
import mathhelper.twf.taskautogeneration.ExpressionNodeBuilder.Companion.buildNodeFromDividendAndDivisor
import mathhelper.twf.taskautogeneration.ExpressionNodeBuilder.Companion.buildNodeFromMultipliers
import mathhelper.twf.taskautogeneration.ExpressionNodeBuilder.Companion.buildNodeFromTerms
import mathhelper.twf.taskautogeneration.rulepack.PostprocessorRulePack
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow


data class GeneratorSettings(
    val expressionGenerationDirection: ExpressionGenerationDirection = ExpressionGenerationDirection.FINAL_TO_ORIGINAL,
    val goalStepsCount: Int = 5,
    val taskStartGenerator: (compiledConfiguration: CompiledConfiguration) -> GeneratedExpression,
    val compiledConfiguration: CompiledConfiguration = CompiledConfiguration(),
    val expressionSubstitutions: List<ExpressionSubstitution> = compiledConfiguration.compiledExpressionTreeTransformationRules,
    val maxCountSelectedOfTasksOnIteration: Int = 20,
    val widthOfRulesApplicationsOnIteration: Int = 20,
    val minStepsCountInAutogeneration: Int = 4,
    val goalCompletionStepsCount: Int = (goalStepsCount + 1) / 2,
    val extendingExpressionSubstitutions: List<ExpressionSubstitution> = expressionSubstitutions.filter { it.isExtending },
    val reducingExpressionSubstitutions: List<ExpressionSubstitution> = expressionSubstitutions.filter { !it.isExtending },
    val postprocessExpressionSubstitutions: List<ExpressionSubstitution> = expressionSubstitutionsFromRulePackITR(
        PostprocessorRulePack.get(), mapOf(), false),
    val mandatoryResultTransformations: List<ExpressionSubstitution> = listOf(),
    val newVariablesExpressionSubstitutions: Map<String, List<ExpressionSubstitution>> = mapOf(),
    val nodeIdsToTransformSelector: (ExpressionNode) -> List<Int> = { expressionNode -> expressionNode.selectRandomNodeIdsToTransform() }
)

fun generateTrigonometricTasks(
    settings: GeneratorSettings
): List<TaskITR> {
    val compiledConfiguration = settings.compiledConfiguration
    val expressionComparator = compiledConfiguration.factComparator.expressionComparator
    val actualExtendingExpressionSubstitutions = settings.extendingExpressionSubstitutions
    val actualReducingExpressionSubstitutions = settings.reducingExpressionSubstitutions
    val taskStartGeneratedExpression = settings.taskStartGenerator.invoke(compiledConfiguration)

    val initialTask = ExpressionTask(taskStartGeneratedExpression.expressionNode.clone())
    var taskStart = initialTask.clone()
    taskStart = dragConstantsToTheLeft(taskStart)


    var allTasks = mutableListOf<ExpressionTask>()
    var currentTasks = mutableListOf<ExpressionTask>()
    currentTasks.add(taskStart)

    for (i in 1..5) {
        currentTasks.add(splitConstants(taskStart, compiledConfiguration, stepId = 0))
    }
    currentTasks = unify(currentTasks, compiledConfiguration)

    var stepId = 1

    while (currentTasks.isNotEmpty()) {
        var newCurrentTasks = mutableListOf<ExpressionTask>()
        for (currentTask in currentTasks) {
            val currentExpression = currentTask.currentExpression
            currentExpression.computeNodeIdsAsNumbersInDirectTraversalAndDistancesToRoot()
            compiledConfiguration.setExpressionSubstitutions(
                if (stepId < settings.goalCompletionStepsCount) {
                    settings.extendingExpressionSubstitutions
                } else {
                    settings.reducingExpressionSubstitutions
                })
            // 1. places, then substitutions
            for (j in 1..settings.widthOfRulesApplicationsOnIteration) {
                val selectedNodeIds = currentExpression.selectRandomNodeIdsToTransform()
                for (nodeId in selectedNodeIds) {
                    val applications = findApplicableSubstitutionsInSelectedPlace(currentExpression, listOf(nodeId).toTypedArray(),
                        compiledConfiguration, withReadyApplicationResult = true)

                    for (application in applications) {
                        if (application.containsTrigonometricFunctionInResult()
                            && (application.originalExpressionChangingPart.insideExponent() || application.originalExpressionChangingPart.insideTrigonometricFunction())) {
                            continue
                        }

                        val newTask = currentTask.clone()
                        newTask.previousExpressions.add(newTask.currentExpression.clone())
                        newTask.currentExpression = application.resultExpression
                        newTask.usedSubstitutions.add(application.expressionSubstitution)

                        if (currentTask.previousExpressions.any { compareExpressionNodes(it, application.resultExpression) }) {
                            continue
                        }
                        newTask.solutionsStepTree.add(SolutionsStepITR(
                            newTask.currentExpression.toString(),
                            application.expressionSubstitution,
                            listOf(nodeId),
                            stepId,
                            newTask.solutionsStepTree.lastOrNull()?.stepId ?: -1))
                        newCurrentTasks.add(newTask)
                    }
                }
            }

            // 2. substitutions, then places
            val functionsInExpression = currentExpression.getContainedFunctions()
            val appropriateSubstitutions = mutableListOf<ExpressionSubstitution>()
            val actualExpressionSubstitutions = if (stepId < settings.goalCompletionStepsCount) {
                // запутываем исходное выражение
                actualExtendingExpressionSubstitutions
            } else {
                // упрощаем получившеся выражение
                actualReducingExpressionSubstitutions
            }
            for (substitution in actualExpressionSubstitutions) {
                if (substitution.isAppropriateToFunctions(functionsInExpression) && substitution.left.nodeType != NodeType.EMPTY) {
                    if (substitution.findAllPossibleSubstitutionPlaces(currentExpression, expressionComparator).isNotEmpty()) {
                        substitution.setStepIdForRight(stepId)
                        appropriateSubstitutions.add(substitution)
                    }
                }
            }
            val appropriateSubstitutionWeight = appropriateSubstitutions.sumByDouble { it.weightInTaskAutoGeneration }
            if (appropriateSubstitutionWeight < epsilon) {
                continue
            }

            for (j in 1..settings.widthOfRulesApplicationsOnIteration) {
                val newTask = currentTask.clone()
                var selector = random(0.0, appropriateSubstitutionWeight)
                var currentSubstitutionIndex = 0
                while (selector > appropriateSubstitutions[currentSubstitutionIndex].weightInTaskAutoGeneration) {
                    selector -= appropriateSubstitutions[currentSubstitutionIndex].weightInTaskAutoGeneration
                    currentSubstitutionIndex++
                }

                val selectedSubstitution = appropriateSubstitutions[currentSubstitutionIndex]
                val places = selectedSubstitution.findAllPossibleSubstitutionPlaces(newTask.currentExpression, expressionComparator)
                if (selectedSubstitution.containsTrigonometricFunctionInResult()) {
                    if (places.any { it.originalValue.insideExponent() || it.originalValue.insideTrigonometricFunction() }) {
                        continue
                    }
                }
                if (places.size == 0) {
                    continue
                }
                val changedExpression = newTask.currentExpression.clone()
                newTask.previousExpressions.add(newTask.currentExpression.clone())
                newTask.usedSubstitutions.add(selectedSubstitution)
                val waysOfApplyingCount = (1 shl (places.size - 1)) + 1
                var bitMask = if (waysOfApplyingCount > 0) {
                    randomInt(1, waysOfApplyingCount)
                } else { // overflow
                    randomInt(1, Int.MAX_VALUE)
                }
                for ((i, place) in places.withIndex()) {
                    if (place.originalExpression.lastAppliedSubstitution == null) {
                        bitMask = bitMask or (1 shl (i + 1)) // set bit to 1 in order to enforce transformation
                    }
                }
                val changedNodeIds = selectedSubstitution.applySubstitutionByBitMask(places, bitMask)
                if (currentTask.previousExpressions.any { compareExpressionNodes(it, newTask.currentExpression) }) {
                    continue
                }
                newTask.solutionsStepTree.add(
                    SolutionsStepITR(
                        changedExpression.toString(),
                        selectedSubstitution,
                        changedNodeIds,
                        stepId,
                        newTask.solutionsStepTree.lastOrNull()?.stepId ?: -1
                    )
                )
                newTask.time += (selectedSubstitution.weight + 1).toInt() * changedNodeIds.size * 2 // 4 seconds on trivial rule

                newCurrentTasks.add(newTask)
            }
        }

        newCurrentTasks = unify(newCurrentTasks, compiledConfiguration)

        val groupsByPreviousExpression = newCurrentTasks.groupBy { it.previousExpressions.lastOrNull()?.identifier }
        val maxCountPerGroup = if (groupsByPreviousExpression.isEmpty()) { 0 }
        else {
            ceil(settings.maxCountSelectedOfTasksOnIteration.toDouble() / groupsByPreviousExpression.size).toInt()
        }
        val newCurrentTasksAfterCut = mutableListOf<ExpressionTask>()
        for ((_, tasksInGroup) in groupsByPreviousExpression) {
            val sortedByNumberOfFunctions = tasksInGroup.toMutableList()
            sortedByNumberOfFunctions.sortByDescending { it.currentExpression.getContainedFunctions().size }
            newCurrentTasksAfterCut.addAll(sortedByNumberOfFunctions.subList(0, min(maxCountPerGroup, tasksInGroup.size)))
        }

        newCurrentTasks = newCurrentTasksAfterCut

        currentTasks = newCurrentTasks.filter { it.previousExpressions.size < settings.goalStepsCount }.toMutableList()
        allTasks.addAll(newCurrentTasks.filter { it.previousExpressions.size >= settings.goalStepsCount })
        allTasks = allTasks.distinctBy { it.currentExpression.toString() }.toMutableList()

        stepId++
    }

    allTasks.forEach {
        it.currentExpression.applyAllSubstitutions(settings.mandatoryResultTransformations)
        it.currentExpression = simplifyAndNormalizeExpression(it.currentExpression, compiledConfiguration)
    }

    //postprocessing
    allTasks = allTasks.map {
        postprocess(it, settings.postprocessExpressionSubstitutions, compiledConfiguration, stepId)
    }.toMutableList()

    var resultAllTasks = unify(allTasks, compiledConfiguration)
    resultAllTasks.sortBy { it.currentExpression.value.length }

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
        it.solution = expressionsInSolution.joinToString(" = ") { i -> expressionToString(i) }
    }

    resultAllTasks = resultAllTasks
        .filter { validateTask(it, taskStartGeneratedExpression, expressionComparator) }
        .filter { verifyAllPartsHasBeenTransformed(initialTask.currentExpression, it.currentExpression) }
        .toMutableList()

    return resultAllTasks.map {
        TaskITR(
            code = taskStartGeneratedExpression.code,
            nameEn = taskStartGeneratedExpression.nameEn,
            nameRu = taskStartGeneratedExpression.nameRu,
            descriptionShortEn = taskStartGeneratedExpression.descriptionShortEn,
            descriptionShortRu = taskStartGeneratedExpression.descriptionShortRu,
            descriptionEn = taskStartGeneratedExpression.descriptionEn,
            descriptionRu = taskStartGeneratedExpression.descriptionRu,
            subjectType = taskStartGeneratedExpression.subjectType,
            tags = taskStartGeneratedExpression.tags,
            originalExpressionStructureString = it.startExpression.toString(),

            goalType = "expression",
            goalExpressionStructureString = it.currentExpression.toString(),
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

fun unify(tasks: List<ExpressionTask>, compiledConfiguration: CompiledConfiguration) : MutableList<ExpressionTask> {
    val unifiedTasks = mutableListOf<ExpressionTask>()

    for (task in tasks) {
        var isNew = true
        for (unifiedTask in unifiedTasks) {
            if (compiledConfiguration.factComparator.expressionComparator
                    .compareAsIs(task.currentExpression, unifiedTask.currentExpression)) {
                isNew = false
                break
            }
        }
        if (isNew) {
            unifiedTasks.add(task)
        }
    }
    return unifiedTasks
}

fun splitConstants(task: ExpressionTask, compiledConfiguration: CompiledConfiguration, stepId: Int): ExpressionTask {
    val normalizedTask = task.clone()
    normalizedTask.currentExpression = traverseAndSplitConstants(normalizedTask.currentExpression, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    logTransformation(task, normalizedTask, "splitConstants", stepId)
    return normalizedTask
}

fun traverseAndSplitConstants(node: ExpressionNode, compiledConfiguration: CompiledConfiguration): ExpressionNode {
    val updatedChildren = mutableListOf<ExpressionNode>()
    for (child in node.children) {
        val updatedChild = traverseAndSplitConstants(child, compiledConfiguration)
        updatedChildren.add(updatedChild)
        updatedChild.parent = node
    }
    node.children = updatedChildren
    if (node.isIntNumber() && node.value != "" && !node.insideExponent()) {
        val constantValue = node.value.toInt()
        if (constantValue <= 1) {
            return node
        }
        val firstHalf = randomInt(1, max(1, constantValue))
        val secondHalf = constantValue - firstHalf
        return buildNodeFromTerms(
            mutableListOf(buildNodeFromConstant(firstHalf), buildNodeFromConstant(secondHalf)),
            compiledConfiguration
        )
    }
    return node
}

fun verifyAllPartsHasBeenTransformed(initial: ExpressionNode, result: ExpressionNode): Boolean {
    val initialNode = getModule(initial)
    val resultNode = getModule(result)

    if (initialNode.value == "*" && resultNode.value == "*"
        && initialNode.children.size == resultNode.children.size) {
        return false
    }

    if (initialNode.value == "/" && resultNode.value == "/") {
        return false
    }

    if (resultNode.value == "+" || resultNode.value == "*") {
        val nodesShouldBeTransformed = getTermsOrMultipliers(initialNode)
        val actualNodes = getTermsOrMultipliers(resultNode)
        for (node in nodesShouldBeTransformed) {
            if (actualNodes.any { getModule(it).identifier == getModule(node).identifier }) {
                //println("DROPPED TASK $initialNode -> $resultNode")
                return false
            }
        }
    } else if (initialNode.value == "/") {
        val initialNominator = initialNode.children[0]
        val initialDenominator = initialNode.children[1]
        if (resultNode.value == "/") {
            val resultNominator = resultNode.children[0]
            val resultDenominator = resultNode.children[1]
            return getModule(initialNominator).identifier != getModule(resultNominator).identifier
                    && getModule(initialDenominator).identifier != getModule(resultDenominator).identifier
        }
    } else if (initialNode.identifier != "1") {
        val actualNodes = getTermsOrMultipliers(resultNode)
        return actualNodes.isEmpty() || actualNodes.none { getModule(it).identifier == initialNode.identifier }
    }
    return initialNode.identifier != result.identifier
}

fun getTermsOrMultipliers(root: ExpressionNode): List<ExpressionNode> {
    if (root.value == "*" || root.value == "+") {
        return root.children
    }
    return mutableListOf(root)
}

fun validateTask(resultTask: ExpressionTask, taskStart: GeneratedExpression, expressionComparator: ExpressionComparator): Boolean {
    val initialExpression = taskStart.expressionNode
    val resultExpression = resultTask.currentExpression
    val isValid = expressionComparator.fastProbabilityCheckOnIncorrectTransformation(initialExpression, resultExpression)
    if (!isValid) {
        val warningMessage = StringBuilder("Generated task is not equal to initial! \n")
            .append("Initial: ${initialExpression.identifier} \n")
            .append("Result: ${resultExpression.identifier}")
        // println(warningMessage.toString())
        return false
    }
    return true
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
fun applySimplePostprocessorRules(task: ExpressionTask, postprocessSubstitutions: List<ExpressionSubstitution>,
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

/**
 * Used to transform generated task to normal form,
 * but here are more complicated issues which can't be solved by simple rule substitution
 *
 * For example, this postprocessor can reduce fractions, can get rid of multilayer fractions etc.
 *
 * @param task: task to transform
 *
 * @return task in normalized form
 */
fun applySophisticatedPostprocessorRules(task: ExpressionTask, compiledConfiguration: CompiledConfiguration, stepId: Int): ExpressionTask {
    var prevTask = task

    var newTask = dragDescendantsUpper(task, "+", compiledConfiguration)
    logTransformation(prevTask, newTask, "dragTheDescendantsOfPlusUpper", stepId)

    prevTask = newTask
    newTask = dragDescendantsUpper(newTask, "*", compiledConfiguration)
    logTransformation(prevTask, newTask, "dragTheDescendantsOfMultiplicationUpper", stepId)

    prevTask = newTask
    newTask = reduceTerms(newTask, compiledConfiguration.factComparator.expressionComparator)
    logTransformation(prevTask, newTask, "reduceTerms", stepId)

    prevTask = newTask
    newTask = getRidOfMultilayerFractions(newTask, compiledConfiguration)
    logTransformation(prevTask, newTask, "getRidOfMultilayerFractions", stepId)

    prevTask = newTask
    newTask = reduceFractions(newTask)
    logTransformation(prevTask, newTask, "reduceFractions", stepId)

    prevTask = newTask
    newTask = dragConstantsToTheLeft(newTask)
    logTransformation(prevTask, newTask, "dragConstantsToTheLeft", stepId)

    prevTask = newTask
    newTask = getRidOfDuplicatedNodes(newTask, compiledConfiguration)
    logTransformation(prevTask, newTask, "getRidOfDuplicatedNodes", stepId)

    prevTask = newTask
    newTask = getRidOfLongFractionIfNeeded(newTask, compiledConfiguration)
    logTransformation(prevTask, newTask, "getRidOfLongFractionIfNeeded", stepId)

    prevTask = newTask
    newTask = performComputationIfNeeded(newTask, compiledConfiguration)
    logTransformation(prevTask, newTask, "performComputationIfNeeded", stepId)

    return newTask
}

fun postprocess(task: ExpressionTask, postprocessSubstitutions: List<ExpressionSubstitution>,
                compiledConfiguration: CompiledConfiguration, stepId: Int): ExpressionTask {
    var prevTask: ExpressionTask
    var postprocessedTask = task
    var iterationsCount = 0
    do {
        prevTask = postprocessedTask
        postprocessedTask = applySophisticatedPostprocessorRules(prevTask, compiledConfiguration, stepId)
        postprocessedTask = applySimplePostprocessorRules(postprocessedTask, postprocessSubstitutions, compiledConfiguration.factComparator.expressionComparator, stepId)
        iterationsCount++
        if (iterationsCount > 50) {
            println("WARNING: endless cycle")
            println("Prev task" + prevTask.currentExpression.identifier)
            println("New task " + postprocessedTask.currentExpression.identifier)
            return postprocessedTask
        }
    } while (prevTask.currentExpression.identifier != postprocessedTask.currentExpression.identifier)
    return postprocessedTask
}

fun performComputationIfNeeded(task: ExpressionTask, compiledConfiguration: CompiledConfiguration): ExpressionTask {
    val normalizedTask = task.clone()
    traverseAndPerformComputation(normalizedTask.currentExpression, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun traverseAndPerformComputation(inputNode: ExpressionNode, compiledConfiguration: CompiledConfiguration) {
    var node = inputNode
    if (node.nodeType == NodeType.FUNCTION && node.value == "+") {
        node = makeComputation(node, "+")
    }
    if (node.nodeType == NodeType.FUNCTION && node.value == "*") {
        node = makeComputation(node, "*")
    }
    for (child in node.children) {
        traverseAndPerformComputation(child, compiledConfiguration)
    }
}

fun retrieveNumbers(nodes: List<ExpressionNode>) : MutableList<ExpressionNode> {
    return nodes
        .map { computeExponentIfNeeded(it) }
        .filter { it.isIntNumber()
                || (it.value == "-" && it.children.size == 1 && it.children[0].isIntNumber())
        }.toMutableList()
}

fun findGCD(a: Int, b: Int) : Int {
    var n1 = max(abs(a), abs(b))
    var n2 = min(abs(a), abs(b))
    while (n1 != n2) {
        if (n1 > n2)
            n1 -= n2
        else
            n2 -= n1
    }
    return n1
}

fun computeSum(numbers: List<ExpressionNode>) : Int {
    return numbers
        .map { if (it.value == "-") {-it.children[0].value.toInt()} else it.value.toInt() }
        .reduce { sum, x -> sum + x }
}

fun computeProd(numbers: List<ExpressionNode>) : Int {
    return numbers
        .map { if (it.value == "-") {-it.children[0].value.toInt()} else it.value.toInt() }
        .reduce { sum, x -> sum * x }
}

fun makeComputation(node: ExpressionNode, operation: String): ExpressionNode {
    val terms = node.children
    val numbers = retrieveNumbers(terms)
    val notNumbers = terms.filter { !numbers.contains(it) }
    if (numbers.size > 1) {
        when (operation) {
            "+" -> {
                val sum = computeSum(numbers)
                val sumNode = buildNodeFromConstant(value = sum, parent = node)
                node.children = mutableListOf(sumNode)
                node.children.addAll(notNumbers)
            }
            "*" -> {
                val prod = computeProd(numbers)
                val prodNode = buildNodeFromConstant(value = prod, parent = node)
                node.children = mutableListOf(prodNode)
                node.children.addAll(notNumbers)
            }
        }
    }
    if (node.children.size == 1) {
        return node.children[0]
    }
    return node
}

fun computeExponentIfNeeded(node: ExpressionNode): ExpressionNode {
    if (node.nodeType == NodeType.FUNCTION && node.value == "^"
        && node.children[0].isIntNumber() && node.children[1].isIntNumber()) {
        val base = node.children[0].value.toInt()
        val degree = node.children[1].value.toInt()
        val result = powInt(base, degree)
        return buildNodeFromConstant(value = result, parent = node)
    }
    return node
}

fun powInt(base: Int, degree: Int) : Int {
    return base.toDouble().pow(degree.toDouble()).toInt()
}

fun traverseAndGetRidOfLongFractionIfNeeded(
    node: ExpressionNode,
    compiledConfiguration: CompiledConfiguration
): ExpressionNode {
    val updatedChildren = mutableListOf<ExpressionNode>()
    for (child in node.children) {
        val updatedChild = traverseAndGetRidOfLongFractionIfNeeded(child, compiledConfiguration)
        updatedChildren.add(updatedChild)
        updatedChild.parent = node
    }
    node.children = updatedChildren
    return getRidOfLongFraction(node, compiledConfiguration)
}

fun getRidOfLongFraction(node: ExpressionNode, compiledConfiguration: CompiledConfiguration,
                                 nominatorToDenominatorRatioMax: Double = 20.0): ExpressionNode {
    if (node.nodeType == NodeType.FUNCTION && node.value == "/") {
        val ratio = node.children[0].identifier.length / node.children[1].identifier.length
        if (ratio > nominatorToDenominatorRatioMax) {
            val nominator = node.children[0]
            val denominator = node.children[1]
            val one = buildNodeFromConstant(1)
            val oneDividedByDenominator = buildNodeFromDividendAndDivisor(one, denominator, compiledConfiguration)
            return buildNodeFromMultipliers(listOf(oneDividedByDenominator, nominator), compiledConfiguration)
        }
    }
    return node
}

fun logTransformation(prevTask: ExpressionTask, newTask: ExpressionTask, transformationName: String, stepId: Int) {
    if (prevTask.currentExpression.identifier != newTask.currentExpression.identifier) {
        val substitution = ExpressionSubstitution(
            left = prevTask.currentExpression,
            right = newTask.currentExpression,
            code = transformationName
        )

        newTask.expressionTaskIntermediateData.appliedPostprocessSubstitutions[substitution.code] = stepId
        newTask.solutionsStepTree.add(SolutionsStepITR(
            newTask.currentExpression.toString(),
            substitution,
            mutableListOf(),
            stepId,
            newTask.solutionsStepTree.lastOrNull()?.stepId ?: -1))
        newTask.previousExpressions.add(newTask.currentExpression.clone())
    }
}

fun getRidOfDuplicatedNodes(task: ExpressionTask, compiledConfiguration: CompiledConfiguration): ExpressionTask {
    var normalizedTask = task.clone()
    normalizedTask.currentExpression = traverseAndGetRidOfDuplicatedTerms(normalizedTask.currentExpression, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()

    normalizedTask = normalizedTask.clone()
    normalizedTask.currentExpression = traverseAndGetRidOfDuplicatedMultipliers(normalizedTask.currentExpression, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()

    return normalizedTask
}

fun traverseAndGetRidOfDuplicatedTerms(node: ExpressionNode, compiledConfiguration: CompiledConfiguration) : ExpressionNode {
    for (child in node.children) {
        traverseAndGetRidOfDuplicatedTerms(child, compiledConfiguration)
    }
    if (node.nodeType == NodeType.FUNCTION && node.value == "+") {
        val nodesMap: Map<String, List<ExpressionNode>> = node.children.groupBy { getKeyPartForTerm(it, compiledConfiguration).computeIdentifier() }
        if (nodesMap.all { it.value.size <= 1 }) {
            return node
        }
        val result = mutableListOf<ExpressionNode>()
        for ((id, nodes) in nodesMap) {
            if (nodes.all { it.isConstant() }) {
                result.addAll(nodes)
                continue
            }
            val count = nodes
                .map { getCountForTerm(it) }
                .reduce { sum, x -> sum + x }
            if (count == 1) {
                result.add(nodes[0])
            } else {
                val counterNode = buildNodeFromConstant(count)
                val idNode = structureStringToExpression(id)
                val resultNode = if (idNode.children.size > 0) idNode.children[0] else idNode
                result.add(buildNodeFromMultipliers(mutableListOf(counterNode, resultNode), compiledConfiguration))
            }
        }

        if (result.size == 1) {
            val updatedNode = result[0]
            node.children = updatedNode.children
            node.value = updatedNode.value
            node.identifier = updatedNode.identifier
            for (child in updatedNode.children) {
                child.parent = node
            }
        } else {
            node.children = result
        }
    }
    return node
}

fun traverseAndGetRidOfDuplicatedMultipliers(node: ExpressionNode, compiledConfiguration: CompiledConfiguration) : ExpressionNode {
    for (child in node.children) {
        traverseAndGetRidOfDuplicatedMultipliers(child, compiledConfiguration)
    }
    if (node.nodeType == NodeType.FUNCTION && node.value == "*") {
        val nodesMap: Map<String, List<ExpressionNode>> = node.children.groupBy { getKeyPartForMultiplier(it).identifier }
        if (nodesMap.all { it.value.size <= 1 }) {
            return node
        }
        val result = mutableListOf<ExpressionNode>()
        for ((id, nodes) in nodesMap) {
            if (nodes.all { it.isConstant() }) {
                result.addAll(nodes)
                continue
            }
            val degree = nodes
                .map { getCountForMultiplier(it) }
                .reduce { sum, x -> sum + x }
            if (degree == 1) {
                result.add(nodes[0])
            } else {
                val degreeExponent = buildNodeFromConstant(degree)
                val idNode = structureStringToExpression(id)
                val degreeBase = if (idNode.children.size > 0) idNode.children[0] else idNode
                val exp = compiledConfiguration.createExpressionFunctionNode(
                    "^", -1,
                    children = listOf(degreeBase, degreeExponent)
                )
                result.add(exp)
            }
        }

        if (result.size == 1) {
            val updatedNode = result[0]
            node.children = updatedNode.children
            node.value = updatedNode.value
            node.identifier = updatedNode.identifier
            for (child in updatedNode.children) {
                child.parent = node
            }
        } else {
            node.children = result
        }
    }
    return node
}

fun getCountForTerm(node: ExpressionNode): Int {
    val numbers = retrieveNumbers(node.children)
    if (numbers.isEmpty()) {
        return 1
    }
    return computeProd(numbers)
}

fun getCountForMultiplier(node: ExpressionNode): Int {
    if (node.nodeType == NodeType.FUNCTION && node.value == "^") {
        return computeSum(listOf(node.children[1]))
    }
    return 1
}

fun getKeyPartForTerm(node: ExpressionNode, compiledConfiguration: CompiledConfiguration) : ExpressionNode {
    if (node.nodeType == NodeType.FUNCTION && node.value == "*") {
        val numbers = retrieveNumbers(node.children)
        val notNumbers = node.children.filter { !numbers.contains(it) }
        if (notNumbers.size == 1) {
            return notNumbers[0]
        }
        return buildNodeFromMultipliers(notNumbers, compiledConfiguration)
    }
    return node
}

fun getKeyPartForMultiplier(node: ExpressionNode) : ExpressionNode {
    if (node.nodeType == NodeType.FUNCTION && node.value == "^") {
        return node.children[0]
    }
    return node
}

fun getRidOfMultilayerFractions(task: ExpressionTask, compiledConfiguration: CompiledConfiguration): ExpressionTask {
    val normalizedTask = task.clone()
    normalizedTask.currentExpression = traverseAndLayFractions(normalizedTask.currentExpression, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun traverseAndLayFractions(node: ExpressionNode, compiledConfiguration: CompiledConfiguration): ExpressionNode {
    val updatedChildren = mutableListOf<ExpressionNode>()
    for (child in node.children) {
        val updatedChild = traverseAndLayFractions(child, compiledConfiguration)
        updatedChildren.add(updatedChild)
        updatedChild.parent = node
    }
    node.children = updatedChildren
    if (node.nodeType == NodeType.FUNCTION && node.value == "/") {
        val nominatorNodes = mutableListOf<ExpressionNode>()
        val denominatorNodes = mutableListOf<ExpressionNode>()

        layFractionRecursively(node, nominatorNodes, denominatorNodes)

        return buildNodeFromDividendAndDivisor(
            nominator = buildNodeFromMultipliers(nominatorNodes, compiledConfiguration),
            denominator = buildNodeFromMultipliers(denominatorNodes, compiledConfiguration),
            compiledConfiguration = compiledConfiguration
        )
    }
    return node
}

fun layFractionRecursively(node: ExpressionNode,
                           nominatorNodes: MutableList<ExpressionNode>,
                           denominatorNodes: MutableList<ExpressionNode>,
                           isInsideNominator: Boolean = true) {
    if (node.nodeType == NodeType.FUNCTION && node.value == "/") {
        val children: List<ExpressionNode> = node.children
        layFractionRecursively(children[0], nominatorNodes, denominatorNodes, isInsideNominator)
        layFractionRecursively(children[1], nominatorNodes, denominatorNodes, !isInsideNominator)
    } else if (node.nodeType == NodeType.FUNCTION && node.value == "*") {
        for (child in node.children) {
            layFractionRecursively(child, nominatorNodes, denominatorNodes, isInsideNominator)
        }
    } else {
        if (isInsideNominator) {
            nominatorNodes.add(node)
        } else {
            denominatorNodes.add(node)
        }
    }
}

fun dragDescendantsUpper(task: ExpressionTask, operation: String, compiledConfiguration: CompiledConfiguration): ExpressionTask {
    val normalizedTask = task.clone()
    normalizedTask.currentExpression = traverseAndDragDescendantsUpper(normalizedTask.currentExpression, operation, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun traverseAndDragDescendantsUpper(node: ExpressionNode, operation: String, compiledConfiguration: CompiledConfiguration): ExpressionNode {
    val updatedChildren = mutableListOf<ExpressionNode>()
    for (child in node.children) {
        val updatedChild = traverseAndDragDescendantsUpper(child, operation, compiledConfiguration)
        updatedChildren.add(updatedChild)
        updatedChild.parent = node
    }
    node.children = updatedChildren
    if (node.nodeType == NodeType.FUNCTION && node.value == operation) {
        val nodes: MutableList<ExpressionNode> = mutableListOf()
        collectNodesRecursively(node, operation, nodes)
        if (operation == "+") {
            return buildNodeFromTerms(nodes, compiledConfiguration)
        } else if (operation == "*") {
            return buildNodeFromMultipliers(nodes, compiledConfiguration)
        }
    }
    return node
}

fun collectNodesRecursively(node: ExpressionNode, operation: String, nodes: MutableList<ExpressionNode>) {
    if (node.nodeType == NodeType.FUNCTION && node.value == operation) {
        for (child in node.children) {
            collectNodesRecursively(child, operation, nodes)
        }
    } else {
        nodes.add(node)
    }
}

fun dragConstantsToTheLeft(task: ExpressionTask): ExpressionTask {
    val normalizedTask = task.clone()
    traverseAndSortChildrenAscendingByIdentifier(normalizedTask.currentExpression)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun getRidOfLongFractionIfNeeded(task: ExpressionTask, compiledConfiguration: CompiledConfiguration): ExpressionTask {
    val normalizedTask = task.clone()
    normalizedTask.currentExpression = traverseAndGetRidOfLongFractionIfNeeded(normalizedTask.currentExpression, compiledConfiguration)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun traverseAndSortChildrenAscendingByIdentifier(node: ExpressionNode) {
    for (child in node.children) {
        traverseAndSortChildrenAscendingByIdentifier(child)
    }
    if (node.nodeType == NodeType.FUNCTION && node.value == "*") {
        node.children.sortWith(compareBy({!it.isConstant()}, {getModule(it).identifier.length}, {it.identifier}))
    }
}

fun reduceFractions(task: ExpressionTask): ExpressionTask {
    val normalizedTask = task.clone()
    traverseAndReduceFractions(normalizedTask.currentExpression)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun traverseAndReduceFractions(node: ExpressionNode) {
    for (child in node.children) {
        traverseAndReduceFractions(child)
    }
    if (node.nodeType == NodeType.FUNCTION && node.value == "/") {
        val nominatorNodes: MutableList<ExpressionNode> = getMultipliers(node.children[0])
        val denominatorNodes: MutableList<ExpressionNode> = getMultipliers(node.children[1])
        val nominatorNodesAfterReduce = makeReduce(nominatorNodes, denominatorNodes)
        val denominatorNodesAfterReduce = makeReduce(denominatorNodes, nominatorNodes)

        reduceIntegersInFraction(node, nominatorNodesAfterReduce, denominatorNodesAfterReduce)

        if (nominatorNodesAfterReduce.isEmpty()) {
            node.children[0] = buildNodeFromConstant(value = 1, parent = node)
        } else if (nominatorNodesAfterReduce.size == 1) {
            node.children[0] = nominatorNodesAfterReduce[0]
        } else {
            node.children[0].children = nominatorNodesAfterReduce
        }

        if (denominatorNodesAfterReduce.isEmpty()) {
            node.children[1] = buildNodeFromConstant(value = 1, parent = node)
        } else if (denominatorNodesAfterReduce.size == 1) {
            node.children[1] = denominatorNodesAfterReduce[0]
        } else {
            node.children[1].children = denominatorNodesAfterReduce
        }
    }
}

fun reduceIntegersInFraction(
    parentNode: ExpressionNode,
    nominatorNodes: MutableList<ExpressionNode>,
    denominatorNodes: MutableList<ExpressionNode>
) {
    val numbersInNominator = retrieveNumbers(nominatorNodes)
    val numbersInDenominator = retrieveNumbers(denominatorNodes)
    if (numbersInNominator.isEmpty() || numbersInDenominator.isEmpty()) {
        return
    }
    val nominatorProd = computeProd(numbersInNominator)
    if (nominatorProd == 0) {
        return
    }
    val denominatorProd = computeProd(numbersInDenominator)
    val gcd = findGCD(nominatorProd, denominatorProd)
    val nominatorResult = nominatorProd / gcd
    val denominatorResult = denominatorProd / gcd
    nominatorNodes.removeAll(numbersInNominator)
    denominatorNodes.removeAll(numbersInDenominator)
    nominatorNodes.add(0, buildNodeFromConstant(value = nominatorResult, parent = parentNode))
    denominatorNodes.add(0, buildNodeFromConstant(value = denominatorResult, parent = parentNode))
}

fun makeReduce(nodesToReduce: MutableList<ExpressionNode>,
               nodesMustNotBeInResult: MutableList<ExpressionNode>): MutableList<ExpressionNode> {
    val result: MutableList<ExpressionNode> = mutableListOf()
    for (node in nodesToReduce) {
        if (node.value == "1" || node.value == "(1)") {
            continue
        }
        if (nodesMustNotBeInResult.none { it.identifier == node.identifier }) {
            result.add(node)
        }
    }
    return result
}

fun getMultipliers(node: ExpressionNode): MutableList<ExpressionNode> {
    if (node.nodeType == NodeType.FUNCTION && node.value == "*") {
        return node.children
    }
    return mutableListOf(node)
}

fun reduceTerms(task: ExpressionTask, expressionComparator: ExpressionComparator): ExpressionTask {
    val normalizedTask = task.clone()
    traverseAndReduceTerms(normalizedTask.currentExpression, expressionComparator)
    normalizedTask.currentExpression.identifier = normalizedTask.currentExpression.toString()
    return normalizedTask
}

fun traverseAndReduceTerms(node: ExpressionNode, expressionComparator: ExpressionComparator) {
    for (child in node.children) {
        traverseAndReduceTerms(child, expressionComparator)
    }
    if (node.nodeType == NodeType.FUNCTION && node.value == "+") {
        var remainingNodes = node.children
            .filter { it.value != "0" && it.value != "(0)" }
            .toMutableList()
        while (true) {
            val nodesAfterReduce = makeSingleReduce(remainingNodes)
            if (remainingNodes.size == nodesAfterReduce.size) {
                remainingNodes = nodesAfterReduce
                break
            }
            remainingNodes = nodesAfterReduce
        }
        if (remainingNodes.isEmpty()) {
            remainingNodes.add(buildNodeFromConstant(value = 0, parent = node))
        }
        node.children = remainingNodes
    }
}

fun makeSingleReduce(nodesToReduce: MutableList<ExpressionNode>): MutableList<ExpressionNode> {
    var nodes = nodesToReduce.toMutableList()
    for (node in nodes) {
        val oppositeNode = getOppositeNode(nodes, node)
        if (oppositeNode != null) {
            nodes = nodes.filter { it != node && it != oppositeNode }.toMutableList()
            return nodes
        }
    }
    return nodesToReduce
}

fun getOppositeNode(allNodes: List<ExpressionNode>, nodeToCheck: ExpressionNode): ExpressionNode? {
    for (node in allNodes) {
        if (hasDifferentSign(node, nodeToCheck) && areEqualByModule(node, nodeToCheck)) {
            return node
        }
    }
    return null
}

fun hasDifferentSign(term1: ExpressionNode, term2: ExpressionNode): Boolean {
    return term1.patternStartWithUnaryMinus() != term2.patternStartWithUnaryMinus()
}

fun areEqualByModule(term1: ExpressionNode, term2: ExpressionNode): Boolean {
    val moduleOfTerm1 = getModule(term1)
    val moduleOfTerm2 = getModule(term2)
    return moduleOfTerm1.identifier == moduleOfTerm2.identifier
}

fun getModule(node: ExpressionNode): ExpressionNode {
    var currentNode = node
    while ((currentNode.value == "" || currentNode.value == "+" || currentNode.value == "-")
        && currentNode.children.size == 1) {
        currentNode = currentNode.children[0]
    }
    return currentNode
}

fun ExpressionNode.containsTrigonometricFunction() = this.containsFunctionBesides(trigonometricAutoCheckingFunctionsSet)

fun ExpressionSubstitution.containsTrigonometricFunctionInResult() = this.right.containsTrigonometricFunction()

fun SubstitutionApplication.containsTrigonometricFunctionInResult() = this.expressionSubstitution.right.containsTrigonometricFunction()


fun ExpressionNode.insideExponent(): Boolean {
    var p = this.parent
    while (p != null) {
        if (p.nodeType == NodeType.FUNCTION && p.value == "^") {
            return true
        }
        p = p.parent
    }
    return false
}

fun ExpressionNode.insideTrigonometricFunction(): Boolean {
    var p = this.parent
    while (p != null) {
        if (p.nodeType == NodeType.FUNCTION && trigonometricAutoCheckingFunctionsSet.contains(p.value)) {
            return true
        }
        p = p.parent
    }
    return false
}
