package mathhelper.twf.defaultcontent.defaulttasksets

import mathhelper.twf.config.RulePackLinkITR
import mathhelper.twf.config.TaskITR
import mathhelper.twf.config.TaskSetITR
import mathhelper.twf.defaultcontent.TaskSetTagCode
import mathhelper.twf.defaultcontent.TaskTagCode.*

class CombinatoricTaskSets {
    companion object {

        val basicCombinatoricsFormulasCheckYourselfTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(*(V(+(m;1);n);*(P(m);P(n))))",
                        goalExpressionStructureString = "(*(P(m);A(+(m;n);n)))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(S2(/(P(+(m;1));P(m));+(n;S2(m;m))))",
                        goalExpressionStructureString = "(+(S2(m;n);*(n;S2(m;+(n;1)))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(*(C(n);C(0);A(+(n;1);1)))",
                        goalExpressionStructureString = "(C(*(2;n);n))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(*(+(m;n;1);C(+(m;n);n)))",
                        goalExpressionStructureString = "(*(+(n;1);C(+(m;n;1);+(n;1))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(/(*(S1(n;m);A(+(m;n);n));*(V(+(m;1);n);factorial(m);factorial(n))))",
                        goalExpressionStructureString = "(S2(n;m))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 4.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(+(C(+(*(2;n);1);n);C(+(*(2;n);1);+(n;1))))",
                        goalExpressionStructureString = "(C(*(2;+(n;1));+(n;1)))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(*(/(A(+(n;m;1);m);+(m;n;1));/(+(n;1);P(m))))",
                        goalExpressionStructureString = "(V(+(n;1);m))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(*(C(+(n;k;m);+(k;m));C(+(k;m);m)))",
                        goalExpressionStructureString = "(*(C(+(n;k;m);m);C(+(n;k);k)))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 3.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(/(V(+(m;1);+(n;k;1));A(+(k;m;1);+(k;1))))",
                        goalExpressionStructureString = "(/(A(+(m;n;k;1);n);P(+(n;k;1))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 4.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(*(/(A(+(n;1;m);m);+(m;n;1));/(C(*(2;n);n);*(C(n);P(m)))))",
                        goalExpressionStructureString = "(V(+(m;1);n))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "BasicCombinatorics")),
                        subjectType = "combinatorics",
                        difficulty = 4.0,
                        tags = mutableSetOf(COMBINATORICS.code)
                )
        )

        val allCombinatoricsTasks = basicCombinatoricsFormulasCheckYourselfTasks

        val defaultCombinatoricsTaskSets = listOf(
                TaskSetITR(
                        code = "BasicCombinatoricsFormulasCheckYourself",
                        nameEn = "[Check Yourself] Basic Combinatorics Formulas", nameRu = "[Проверь себя] Базовые формулы комбинаторики",
                        descriptionShortEn = "Combinatorics Expressions Transformations",
                        descriptionShortRu = "Преобразования комбинаторных выражений",
                        descriptionEn = "[Check Yourself] Combinatorics Expressions Transformations by Simple Formulas",
                        descriptionRu = "[Проверь себя] Преобразования комбинаторных выражений по простым формулам",
                        subjectType = "combinatorics",
                        tags = mutableSetOf(TaskSetTagCode.COMBINATORICS.code, TaskSetTagCode.CHECK_YOURSELF.code),
                        tasks = basicCombinatoricsFormulasCheckYourselfTasks.map { it.copy() }
                )
        )
    }
}