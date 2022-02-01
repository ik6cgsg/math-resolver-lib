package mathhelper.twf.taskautogeneration.rulepack

import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR
import mathhelper.twf.config.RulePackLinkITR

class LogarithmsRulePack {
    companion object {
        val extendingFromSingleNumber = listOf(
            RuleITR(leftStructureString = "(+(-(1)))", rightStructureString = "(log(a;/(1;a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = ""),
            RuleITR(leftStructureString = "(0)", rightStructureString = "(log(1;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.7, code = ""),
            RuleITR(leftStructureString = "(0)", rightStructureString = "(+(log(a;b);-(log(a;b))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 5.0, code = ""),
            RuleITR(leftStructureString = "(1)", rightStructureString = "(log(a;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.6, code = ""),
            RuleITR(leftStructureString = "(1)", rightStructureString = "(^(x;log(a;1)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.6, code = ""),
            //RuleITR(leftStructureString = "(1)", rightStructureString = "(*(log(a;b);log(b;a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 1.8, code = ""),
            RuleITR(leftStructureString = "(2)", rightStructureString = "(+(log(a;a);log(b;b)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = ""),
            RuleITR(leftStructureString = "(2)", rightStructureString = "(log(^(a;2);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 4.75, code = ""),
            RuleITR(leftStructureString = "(3)", rightStructureString = "(log(^(a;3);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 4.75, code = ""),
            RuleITR(leftStructureString = "(3)", rightStructureString = "(+(log(a;a);log(b;b);log(c;c)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 3.5, code = ""),
            RuleITR(leftStructureString = "(4)", rightStructureString = "(log(^(a;4);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 5.0, code = "")
        )

        val extendingFromSimpleLogarithm = listOf(
            RuleITR(leftStructureString = "(log(a;a))", rightStructureString = "(/(log(a;b);log(a;b)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 6.0, code = ""),
            RuleITR(leftStructureString = "(log(a;a))", rightStructureString = "(log(*(a;3);*(a;3)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.0, code = ""),
            RuleITR(leftStructureString = "(log(a;a))", rightStructureString = "(log(*(a;2);*(a;2)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.0, code = ""),
            RuleITR(leftStructureString = "(log(a;b))", rightStructureString = "(log(*(a;1);b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = ""),
            RuleITR(leftStructureString = "(log(a;b))", rightStructureString = "(log(/(a;1);b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 6.0, code = ""),
            RuleITR(leftStructureString = "(log(a;b))", rightStructureString = "(log(^(a;1);b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = true, priority = 25, weightInTaskAutoGeneration = 6.0, code = ""),
            RuleITR(leftStructureString = "(log(a;b))", rightStructureString = "(log(/(*(a;c);c);b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = true, isExtending = true, priority = 25, weightInTaskAutoGeneration = 1.0, code = ""),
            RuleITR(leftStructureString = "(log(a;b))", rightStructureString = "(+(-(log(/(1;a);b))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 6.0, code = ""),
            RuleITR(leftStructureString = "(log(c;b))", rightStructureString = "(/(log(c;a);log(b;a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 5.0, code = "")
        )

        val extendingInLogarithmContext = listOf(
            RuleITR(leftStructureString = "(log(^(a;2);b))", rightStructureString = "(log(+(*(-(a;b);+(a;b));^(b;2));b))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 5.0, code = ""),
            RuleITR(leftStructureString = "(log(*(1;a);b))", rightStructureString = "(+(log(a;b);0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = ""),
            //RuleITR(leftStructureString = "(log(*(1;a);b))", rightStructureString = "(+(log(*(/(c;c);a);b);))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 1.75, code = ""),
            RuleITR(leftStructureString = "(log(*(2;a);b))", rightStructureString = "(+(log(a;b);log(a;b)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = ""),
            RuleITR(leftStructureString = "(log(/(b;c);a))", rightStructureString = "(+(log(b;a);-(log(c;a))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = ""),
            RuleITR(leftStructureString = "(log(/(b;c);a))", rightStructureString = "(+(log(b;a);-(log(c;a))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 2.5, code = "")
        )

        val extendingFromFraction = listOf(
            RuleITR(leftStructureString = "(/(m;k))", rightStructureString = "(log(^(a;k);^(a;m)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 3.0,  code = ""),
            RuleITR(leftStructureString = "(/(1;k))", rightStructureString = "(log(^(a;k);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 25, weightInTaskAutoGeneration = 3.0, code = "")
        )

        val logarithmsRulePack = RulePackITR(
            code = "LogarithmExtending",
            nameEn = "Logarithm extending rules", nameRu = "Расширяющие правила на логарифмы",
            descriptionShortEn = "", descriptionShortRu = "",
            subjectType = "standard_math",
            rulePacks = listOf(RulePackLinkITR(rulePackCode = "Logarithm")),
            rules = extendingFromSingleNumber + extendingFromSimpleLogarithm + extendingInLogarithmContext + extendingFromFraction
        )
    }
}