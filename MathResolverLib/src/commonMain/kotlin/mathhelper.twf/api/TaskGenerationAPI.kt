package mathhelper.twf.api

import mathhelper.twf.config.RulePackITR
import mathhelper.twf.config.TaskITR
import mathhelper.twf.defaultcontent.defaultrulepacks.DefaultStandardMathRulePacks
import mathhelper.twf.defaultcontent.defaultrulepacks.autogeneration.RuleTag
import mathhelper.twf.taskautogeneration.ExpressionUtils
import mathhelper.twf.taskautogeneration.GeneratorSettings
import mathhelper.twf.taskautogeneration.generateTrigonometricTasks

fun generateTasks(area: String,
                  startExpression: String,
                  rulepacks: Array<RulePackITR> = arrayOf(),
                  additionalParamsMap: Map<String, Any> = mapOf()): Array<TaskITR> {
    if (area != "(Trigonometry)") {
        return arrayOf()
    }
    val tags: Array<RuleTag> = if (additionalParamsMap["tags"] == null) {
        RuleTag.values()
    } else {
        val tagsChosen = (additionalParamsMap["tags"] as Array<String>)
                .map { RuleTag.valueOf(it) }
                .toMutableList()
        if (!tagsChosen.contains(RuleTag.BASIC_MATH)) {
            tagsChosen.add(RuleTag.BASIC_MATH)
        }
        tagsChosen.toTypedArray()
    }

    val settings = GeneratorSettings(
        goalStepsCount = mapGoalStepCount(additionalParamsMap["complexity"] as Double),
        expressionSubstitutions = ExpressionUtils.toExpressionSubstitutions(
                rulepacks.ifEmpty { getDefaultRulePacks() }.toList(),
                tags
        ),
        taskStartGenerator = { ExpressionUtils.structureStringToGeneratedExpression(startExpression) }
    )
    return generateTrigonometricTasks(settings).toTypedArray()
}

fun getAllTagsForGeneration(area: String): Array<RuleTag> {
    if (area != "(Trigonometry)") {
        return arrayOf()
    }
    return DefaultStandardMathRulePacks.get()
            .flatMap { it.rules ?: emptyList() }
            .flatMap { it.tagsForTaskGenerator }
            .distinct()
            .filter { it.readyForUseInProduction }
            .filter { it != RuleTag.BASIC_MATH }
            .toTypedArray()
}
