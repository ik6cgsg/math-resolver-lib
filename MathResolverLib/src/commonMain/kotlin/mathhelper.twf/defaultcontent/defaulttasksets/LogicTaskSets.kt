package mathhelper.twf.defaultcontent.defaulttasksets

import mathhelper.twf.config.RulePackLinkITR
import mathhelper.twf.config.TaskITR
import mathhelper.twf.config.TaskSetITR
import mathhelper.twf.defaultcontent.TaskSetTagCode
import mathhelper.twf.defaultcontent.TaskTagCode.*

class LogicTaskSets {
    companion object {
        val logicBaseTrainSetTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(and(implic(b;a);implic(c;a)))",
                        goalExpressionStructureString = "(or(a;not(or(b;c))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                        subjectType = "logic",
                        difficulty = 1.0,
                        stepsNumber = 2,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(a;implic(b;a)))",
                        goalExpressionStructureString = "(a)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                        subjectType = "logic",
                        difficulty = 2.5,
                        tags = mutableSetOf(LOGIC.code, TRICK.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(implic(a;not(b));c))",
                        goalExpressionStructureString = "(or(and(a;b);c))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(not(implic(or(a;b);b));alleq(1;c)))",
                        goalExpressionStructureString = "(and(a;not(b);c))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(implic(a;b);implic(b;a)))",
                        goalExpressionStructureString = "(alleq(a;b))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(a;not(a));or(not(a);not(b));or(a;b);or(not(b);b)))",
                        goalExpressionStructureString = "(xor(a;b))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(xor(xor(and(a;b);a);b))",
                        goalExpressionStructureString = "(or(a;b))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 4.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(and(a;b);or(not(c);b)))",
                        goalExpressionStructureString = "(implic(a;implic(b;implic(c;b))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(a;b;c);implic(a;or(c;d));implic(c;or(b;d));not(b);not(d)))",
                        goalType = "simplification",
                        goalPattern = "?:0:?:?N",
                        otherGoalData = mapOf(
                                "hiddenGoalExpressions" to listOf("(0)")
                        ),
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(a;b);implic(not(c);not(a));implic(not(d);not(b));not(or(c;d));not(or(not(c);not(d)))))",
                        goalType = "simplification",
                        goalPattern = "?:0:?:?N",
                        otherGoalData = mapOf(
                                "hiddenGoalExpressions" to listOf("(0)")
                        ),
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 5.5,
                        tags = mutableSetOf(LOGIC.code)
                )
        )

        val logicRelativeComplementTrainSetTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(or(set-(not(A);B);C))",
                        goalExpressionStructureString = "(implic(or(A;B);C))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(not(and(A;B));B))",
                        goalExpressionStructureString = "(not(B))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(set-(set-(A;B);C);or(not(B);C)))",
                        goalExpressionStructureString = "(implic(implic(A;or(B;C));implic(B;C)))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "RelativeComplement"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 4.5,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(or(A;B);and(A;B)))",
                        goalExpressionStructureString = "(and(not(set-(A;B));not(set-(B;A))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "RelativeComplement"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 3.5,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(set-(or(A;B);and(implic(A;B);implic(B;A))))",
                        goalExpressionStructureString = "(or(set-(A;B);set-(B;A)))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "RelativeComplement"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 4.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(implic(set-(A;C);D);set-(D;B)))",
                        goalExpressionStructureString = "(or(set-(D;B);set-(A;or(C;D))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "RelativeComplement"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 4.5,
                        tags = mutableSetOf(LOGIC.code)
                )
        )

        val logicPeculiarOperationsTasks = listOf<TaskITR>(
        )

        val logicNormalFormsTrainSetTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(implic(implic(A;not(B));C))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                        subjectType = "logic",
                        difficulty = 1.5,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(not(or(and(not(A);not(B));and(not(C);not(D)))))",
                        goalPattern = "and : (or : 2) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 2,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, CNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(not(or(A;B;C)))",
                        goalPattern = "or : (and : 3) : : : not",
                        goalType = "DNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 2.5,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(and(implic(C;B);not(or(A;B;C)));C))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(not(or(and(A;B;or(and(B;C);not(A)));A));and(B;A)))",
                        goalPattern = "or : (and : 3) : : : not",
                        goalType = "DNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 3.5,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(implic(implic(implic(A;B);C);implic(implic(B;C);A));implic(implic(C;A);B)))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 4.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B);not(C));and(A;not(B);C);and(not(A);B;C)))",
                        goalPattern = "and : (or) : : : not",
                        goalType = "CNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;B;not(C));or(A;not(B);C);or(not(A);B;C)))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B));and(C;not(D));and(not(A);B);and(not(C);D)))",
                        goalPattern = "and : (or) : : : not",
                        goalType = "CNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;not(B));or(C;not(D));or(not(A);B);or(not(C);D)))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B);C;not(D));and(not(A);B;not(C);D)))",
                        goalPattern = "and : (or : 3) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;not(B);C;not(D));or(not(A);B;not(C);D)))",
                        goalPattern = "or : (and : 3) : : : not",
                        goalType = "DNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B));and(C;not(D));and(not(A);B);and(not(C);D)))",
                        goalPattern = "and : (or : 3) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;not(B));or(C;not(D));or(not(A);B);or(not(C);D)))",
                        goalPattern = "or : (and : 3) : : : not",
                        goalType = "DNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "LogicAbsorptionLaw",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                )
        )

        val logicResolutionTrainSetTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(and(A;B;implic(A;C);not(C)))",
                        goalExpressionStructureString = "(0)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicResolution")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code, RESOLUTION.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(not(A);implic(not(A);B);implic(B;C);not(C)))",
                        goalExpressionStructureString = "(0)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicResolution")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code, RESOLUTION.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;C);implic(D;C);implic(A;D);not(C)))",
                        goalExpressionStructureString = "(0)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicResolution")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code, RESOLUTION.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;B;C);implic(A;or(C;D));implic(C;or(B;D));not(B);not(D)))",
                        goalExpressionStructureString = "(0)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicResolution")),
                        subjectType = "logic",
                        difficulty = 4.0,
                        tags = mutableSetOf(LOGIC.code, RESOLUTION.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;B);implic(not(C);not(A));implic(not(D);not(B));not(or(C;D));not(or(not(C);not(D)))))",
                        goalExpressionStructureString = "(0)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicResolution")),
                        subjectType = "logic",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, RESOLUTION.code)
                )
        )

        val logicUsualCnfDnfMixCheckYourselfTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(not(or(A;B;C)))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 1.5,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, CNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(implic(and(a;b);or(c;b)))",
                        goalExpressionStructureString = "(implic(d;implic(b;implic(not(c);b))))",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 3.0,
                        tags = mutableSetOf(LOGIC.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(not(and(A;implic(B;C))))",
                        goalPattern = "and : (or : 3) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, CNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B);C;not(D));and(not(A);B;not(C);D)))",
                        goalPattern = "and : (or) : : : not",
                        goalType = "CNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;not(B);C;not(D));or(not(A);B;not(C);D)))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B);not(C));and(A;not(B);C);and(not(A);B;C)))",
                        goalPattern = "and : (or : 3) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;B;not(C));or(A;not(B);C);or(not(A);B;C)))",
                        goalPattern = "or : (and : 3) : : : not",
                        goalType = "DNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase"), RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                )
        )

        val logicMixCheckYourselfTasks = listOf<TaskITR>(
                TaskITR(
                        originalExpressionStructureString = "(set-(A;set-(A;B)))",
                        goalPattern = "and : (or) : : : not",
                        goalType = "CNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "RelativeComplement")),
                        subjectType = "logic",
                        difficulty = 1.5,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, CNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(not(and(A;set-(B;C))))",
                        goalPattern = "and : (or : 3) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "RelativeComplement")),
                        subjectType = "logic",
                        difficulty = 2.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, CNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B);C;not(D));and(not(A);B;not(C);D)))",
                        goalPattern = "and : (or) : : : not",
                        goalType = "CNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;not(B);C;not(D));or(not(A);B;not(C);D)))",
                        goalPattern = "or : (and) : : : not",
                        goalType = "DNF",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 5.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(not(set-(A;or(C;D)));implic(C;or(B;D));or(A;B;C);not(B);not(D)))",
                        goalExpressionStructureString = "(0)",
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicResolution")),
                        subjectType = "logic",
                        difficulty = 4.0,
                        tags = mutableSetOf(LOGIC.code, RESOLUTION.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(or(and(A;not(B);not(C));and(A;not(B);C);and(not(A);B;C)))",
                        goalPattern = "and : (or : 3) : : : not",
                        goalType = "CNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                ),
                TaskITR(
                        originalExpressionStructureString = "(and(or(A;B;not(C));or(A;not(B);C);or(not(A);B;C)))",
                        goalPattern = "or : (and : 3) : : : not",
                        goalType = "DNF",
                        goalNumberProperty = 3,
                        rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicAbsorptionLaw")),
                        subjectType = "logic",
                        difficulty = 6.0,
                        tags = mutableSetOf(LOGIC.code, NORMAL_FORMS.code, DNF.code)
                )
        )

        val allLogicTasks = logicBaseTrainSetTasks +
                logicRelativeComplementTrainSetTasks +
                logicPeculiarOperationsTasks +
                logicNormalFormsTrainSetTasks +
                logicResolutionTrainSetTasks +
                logicMixCheckYourselfTasks +
                logicUsualCnfDnfMixCheckYourselfTasks

        val defaultLogicTaskSets = listOf(
                TaskSetITR(
                        code = "LogicBaseTrainSet",
                        nameEn = "[Train Set] Basic Boolean Logic", nameRu = "[Тренировка] Основы булевой логики",
                        descriptionShortEn = "Logic Expressions Transformations",
                        descriptionShortRu = "Преобразования логических выражений",
                        descriptionEn = "[Train Set] Conjunction, Disjunction, Implication, de Morgan's laws, Absorption",
                        descriptionRu = "[Тренировка] Конъюнкция, дизъюнкция, импликация, законы де Моргана, поглощение",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.TRAIN_SET.code, TaskSetTagCode.LOGIC.code),
                        tasks = logicBaseTrainSetTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "LogicRelativeComplementTrainSet",
                        nameEn = "[Train Set] Logic Relative Complement", nameRu = "[Тренировка] Логическое дополнение",
                        descriptionShortEn = "Logic Expressions Transformations",
                        descriptionShortRu = "Преобразования логических выражений",
                        descriptionEn = "[Train Set] Relative Complement and Conjunction, Disjunction, Implication, de Morgan's laws, Absorption",
                        descriptionRu = "[Тренировка] Логическое дополнение и конъюнкция, дизъюнкция, импликация, законы де Моргана, поглощение",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.TRAIN_SET.code, TaskSetTagCode.LOGIC.code),
                        tasks = logicRelativeComplementTrainSetTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "LogicPeculiarOperations",
                        nameEn = "Logic Peculiar Operations", nameRu = "Необычные логические операции",
                        descriptionShortEn = "Logic Expressions Transformations: Negation of Conjunction and Disjunction",
                        descriptionShortRu = "Преобразования логических выражений: штрих Шефферра и стрелка Пирса",
                        descriptionEn = "Negation of Conjunction, Negation of Disjunction and Other Logic Operations",
                        descriptionRu = "Отрицание конъюнкции, отриицание дизъюнкции и другие логиические операции",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.LOGIC.code, TaskSetTagCode.EXTRAORDINARY.code),
                        tasks = logicPeculiarOperationsTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "LogicNormalFormsTrainSet",
                        nameEn = "[Train Set] Logic Normal Forms", nameRu = "[Тренировка] Нормальные формы",
                        descriptionShortEn = "Logic Expressions Transformations",
                        descriptionShortRu = "Преобразования логических выражений",
                        descriptionEn = "[Train Set] Normal Forms of Logic Expressions",
                        descriptionRu = "[Тренировка] Нормальные формы логических выражений",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.LOGIC.code, TaskSetTagCode.TRAIN_SET.code),
                        tasks = logicNormalFormsTrainSetTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "LogicResolutionTrainSet",
                        nameEn = "[Train Set] Resolution Method", nameRu = "[Тренировка] Метод резолюций",
                        descriptionShortEn = "Logic Expressions Transformations",
                        descriptionShortRu = "Преобразования логических выражений",
                        descriptionEn = "[Train Set] Resolution Method for Logic Expressions",
                        descriptionRu = "[Тренировка] Метод резолюций для логических выражений",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.LOGIC.code, TaskSetTagCode.TRAIN_SET.code, TaskSetTagCode.RESOLUTION.code),
                        tasks = logicResolutionTrainSetTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "LogicMixCheckYourself",
                        nameEn = "[Check Yourself] Mix of Logic Transformations", nameRu = "[Проверь себя] Микс логических преобразований",
                        descriptionShortEn = "Logic Expressions Transformations",
                        descriptionShortRu = "Преобразования логических выражений",
                        descriptionEn = "[Check Yourself] Logic Expressions Transformations",
                        descriptionRu = "[Проверь себя] Преобразования логических выражений",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.LOGIC.code, TaskSetTagCode.CHECK_YOURSELF.code),
                        tasks = logicMixCheckYourselfTasks.map { it.copy() }
                ),
                TaskSetITR(
                        code = "LogicUsualCnfDnfMixCheckYourself",
                        nameEn = "[Check Yourself] Mix of Logic Usual CNF DNF Transformations", nameRu = "[Проверь себя] Микс логических преобразований к КНФ ДНФ",
                        descriptionShortEn = "Logic Expressions Transformations",
                        descriptionShortRu = "Преобразования логических выражений",
                        descriptionEn = "[Check Yourself] Logic Expressions Usual CNF DNF Transformations",
                        descriptionRu = "[Проверь себя] Преобразования логических выражени, в том числе к нормальным формам КНФ ДНФ",
                        subjectType = "logic",
                        tags = mutableSetOf(TaskSetTagCode.LOGIC.code, TaskSetTagCode.CHECK_YOURSELF.code),
                        tasks = logicUsualCnfDnfMixCheckYourselfTasks.map { it.copy() }
                )
        )
    }
}