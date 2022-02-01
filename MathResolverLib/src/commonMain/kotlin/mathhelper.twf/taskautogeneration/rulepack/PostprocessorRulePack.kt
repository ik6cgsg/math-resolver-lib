package mathhelper.twf.taskautogeneration.rulepack

import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR

class PostprocessorRulePack {

    companion object {
        val postprocessorRulePack = listOf(
            RulePackITR(
                code = "PostprocessorRules",
                nameEn = "Postprocessor Rules", nameRu = "Подстановки постпроцессора",
                descriptionShortEn = "Postprocessor Rules", descriptionShortRu = "Подстановки постпроцессора",
                descriptionEn = "This rules are applied to every autogenerated task in order to normalize it's form",
                descriptionRu = "Данные правила применяются к каждой созданной автогенератором задаче с целью приведения задачи к нормальному виду",
                subjectType = "standard_math",
                rules = listOf(
                    // BASIC
                    RuleITR(leftStructureString = "(+(-(+(-(a)))))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(-(+(-(a))))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(-(-(a)))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(a;1))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(1;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(a;a))", rightStructureString = "(*(2;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*((+(-(A)));C))", rightStructureString = "(+(-(*(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(A;(+(-(C)))))", rightStructureString = "(+(-(*(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(+(-(A));C))", rightStructureString = "(+(-(*(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(A;+(-(C))))", rightStructureString = "(+(-(*(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(+(-(A));+(-(B))))", rightStructureString = "(*(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(A;+(-(1))))", rightStructureString = "(+(-(*(A;1))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(a;1))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(a;a))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(/(1;a);a))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;/(1;a)))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = true, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/((+(-(A)));C))", rightStructureString = "(+(-(/(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(A;(+(-(C)))))", rightStructureString = "(+(-(/(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(+(-(A));C))", rightStructureString = "(+(-(/(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(A;+(-(C))))", rightStructureString = "(+(-(/(A;C))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;/(B;A)))", rightStructureString = "(/(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(A;/(B;C)))", rightStructureString = "(/(*(A;C);B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(a;+(-(1))))", rightStructureString = "(/(1;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^((/(1;a));+(-(1))))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;^(a;+(-(1)))))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;^(a;+(-(b)))))", rightStructureString = "(^(a;b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(A;+(-(C))))", rightStructureString = "(/(1;^(A;C)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(A;1))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(a;a))", rightStructureString = "(^(a;2))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(a;a;b))", rightStructureString = "(*(^(a;2);b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(b;a;a))", rightStructureString = "(*(b;^(a;2)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(+(-(a));a))", rightStructureString = "(+(-(^(a;2))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(a;+(-(a))))", rightStructureString = "(+(-(^(a;2))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*((+(-(a)));(+(-(a)))))", rightStructureString = "(^(a;2))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = false, priority = 40, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(-(A);*(2;n)))", rightStructureString = "(^(A;*(2;n)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(-(A);2))", rightStructureString = "(^(A;2))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(+(-(A));*(2;n)))", rightStructureString = "(^(A;*(2;n)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(+(-(A));2))", rightStructureString = "(^(A;2))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(^(a;0.5);2))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(^(^(a;2);0.5))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;(^(a;(+(-(1)))))))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;(^(a;(+(-(b)))))))", rightStructureString = "(^(a;b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(a;-(a)))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(-(a);a))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(a;0))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(0;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(0;a))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(a;0))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(0;a))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(*(/(1;a);b))", rightStructureString = "(/(b;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(a;b;c))", rightStructureString = "(/(*(a;c);b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(+(a;b);c))", rightStructureString = "(+(a;b;c))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(+(a;b);-(c)))", rightStructureString = "(+(a;b;+(-(c))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(+(+(a;-(b));c))", rightStructureString = "(+(a;+(-(b));c))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(a;^(/(b;c);n)))", rightStructureString = "(/(*(a;^(c;n));^(b;n)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(a;0.5))", rightStructureString = "(*(2;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),

                    // TRIGONOMETRIC
                    RuleITR(leftStructureString = "(/(1;tg(a)))", rightStructureString = "(ctg(a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(1;ctg(a)))", rightStructureString = "(tg(a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(sin(a);cos(a)))", rightStructureString = "(tg(a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(/(cos(a);sin(a)))", rightStructureString = "(ctg(a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),

                    // LOGIC
                    RuleITR(leftStructureString = "(not(not(a)))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(or(a;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(and(a;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(or(a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(and(a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0),
                    RuleITR(leftStructureString = "(alleq(a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 4, code = "", useWhenPostprocessGeneratedExpression = true, weightInTaskAutoGeneration = 0.0)
                )
            )
        )

        fun get() = postprocessorRulePack[0]
    }
}