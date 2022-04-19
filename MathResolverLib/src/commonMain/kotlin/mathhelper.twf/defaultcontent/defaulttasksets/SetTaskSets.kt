package mathhelper.twf.defaultcontent.defaulttasksets

import mathhelper.twf.config.RulePackLinkITR
import mathhelper.twf.config.TaskITR
import mathhelper.twf.config.TaskSetITR
import mathhelper.twf.defaultcontent.TaskSetTagCode
import mathhelper.twf.defaultcontent.TaskTagCode.*

class SetTaskSets {
    companion object {
        val setBaseTrainSetTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;not(B));or(A;not(C))))",
                        goalExpressionStructureString = "(or(A;not(or(B;C))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase")),
                        subjectType = "set",
                        difficulty = 1.0,
                        stepsNumber = 2,
                        tags = mutableSetOf(SET.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(not(or(and(A;not(A));or(B;not(B));and(A;not(B)))))",
                        goalType = "simplification",
                        goalPattern = "?:0:?:?N",
                        otherGoalData = mapOf(
                                "hiddenGoalExpressions" to listOf("(0)")
                        ),
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase")),
                        subjectType = "set",
                        difficulty = 1.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(A;and(A;B)))",
                        goalExpressionStructureString = "(A)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase")),
                        subjectType = "set",
                        difficulty = 2.5,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(A;and(not(A);B)))",
                        goalExpressionStructureString = "(or(A;B))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase")),
                        subjectType = "set",
                        difficulty = 2.5,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(not(and(A;B));B))",
                        goalExpressionStructureString = "(not(B))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 2.0,
                        tags = mutableSetOf(SET.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(set-(A;B);not(C)))",
                        goalExpressionStructureString = "(and(A;not(B);C))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 2.5,
                        tags = mutableSetOf(SET.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;B);and(A;not(B));and(not(A);B)))",
                        goalExpressionStructureString = "(or(A;B))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(A;not(B);C;or(B;and(C;A))))",
                        goalExpressionStructureString = "(and(A;not(B);C))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(set-(or(A;B);B);set-(1;C)))",
                        goalExpressionStructureString = "(and(A;not(B);C))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(or(A;C);or(B;D));and(or(A;C);not(B);not(D));and(not(A);not(C);or(B;D))))",
                        goalExpressionStructureString = "(or(A;B;C;D))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                )
        )

        val setBaseCheckYourselfTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;B);not(A)))",
                        goalExpressionStructureString = "(or(not(A);B))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase")),
                        subjectType = "set",
                        difficulty = 2.5,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(not(and(A;not(B)));not(B)))",
                        goalExpressionStructureString = "(B)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 2.0,
                        tags = mutableSetOf(SET.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(set-(A;B);C))",
                        goalExpressionStructureString = "(and(A;not(B);not(C)))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 2.5,
                        tags = mutableSetOf(SET.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;B);and(not(A);B);and(A;not(B))))",
                        goalExpressionStructureString = "(or(A;B))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(A;B;C;D;or(not(B);and(C;D;A))))",
                        goalExpressionStructureString = "(and(A;B;C;D))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(set-(or(A;set-(1;B));not(B));set-(1;C)))",
                        goalExpressionStructureString = "(and(A;B;C))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(or(A;C);or(not(B);D));and(or(A;C);B;not(D));and(not(A);not(C);or(not(B);D))))",
                        goalExpressionStructureString = "(or(A;not(B);C;D))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase"), RulePackLinkITR(rulePackCode = "SetAbsorptionLaw")),
                        subjectType = "set",
                        difficulty = 3.0,
                        tags = mutableSetOf(SET.code, TRICK.code)
                )
        )

        val allSetTasks = setBaseTrainSetTasks +
                setBaseCheckYourselfTasks

        val defaultSetTaskSets = listOf(
                TaskSetITR(
                        code = "SetBaseTrainSet",
                        nameEn = "[Train Set] Basic Set Theory", nameRu = "[Тренировка] Основы теории множеств",
                        descriptionShortEn = "Set Theory Expressions Transformations",
                        descriptionShortRu = "Преобразования выражений над множествами",
                        descriptionEn = "[Train Set] Set intersection, union, difference, symmetric difference, complement",
                        descriptionRu = "[Тренировка] Пересечение, объединение, разность, симметрическая разнсть, дополнение",
                        subjectType = "set",
                        tags = mutableSetOf(TaskSetTagCode.TRAIN_SET.code, TaskSetTagCode.SET.code),
                        tasks = setBaseTrainSetTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "SetBaseCheckYourself",
                        nameEn = "[Check Yourself] Mix of Basic Set Transformations", nameRu = "[Проверь себя] Микс основ множественных преобразований",
                        descriptionShortEn = "Set Theory Expressions Transformations",
                        descriptionShortRu = "Преобразования выражений над множествами",
                        descriptionEn = "[Check Yourself] Set intersection, union, difference, symmetric difference, complement",
                        descriptionRu = "[Проверь себя] Пересечение, объединение, разность, симметрическая разнсть, дополнение",
                        subjectType = "set",
                        tags = mutableSetOf(TaskSetTagCode.SET.code, TaskSetTagCode.CHECK_YOURSELF.code),
                        tasks = setBaseCheckYourselfTasks.map { it.copy() }
                )
        )
    }
}