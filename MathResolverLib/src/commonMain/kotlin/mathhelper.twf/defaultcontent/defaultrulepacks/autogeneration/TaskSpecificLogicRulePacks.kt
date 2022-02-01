package mathhelper.twf.defaultcontent.defaultrulepacks.logic

import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR

class TaskSpecificLogicRulePacks {
    companion object {
        val deMorganLogicRulePack = listOf(
            RulePackITR(
                code = "LogicBaseOrAndNot",
                nameEn = "Basic Boolean Logic: Or, And, Not", nameRu = "Основны булевой логики",
                descriptionShortEn = "Basic operations, it's definitions and properties", descriptionShortRu = "Основные операции, их определения и свойства",
                descriptionEn = "Boolean Algebra Conjunction, Disjunction, Negation, Implication, Exclusive Or, Equivalence", descriptionRu = "Булева алгебра: конъюнкция, дизъюнкция, отрицание, импликация, исключающее или, эквиваленция",
                subjectType = "logic",
                rules = listOf(
                    RuleITR(leftStructureString = "(not(and(A;B)))", rightStructureString = "(or(not(A);not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 100.0),
                    RuleITR(leftStructureString = "(or(not(A);not(B)))", rightStructureString = "(not(and(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 100.0),
                    RuleITR(leftStructureString = "(not(or(A;B)))", rightStructureString = "(and(not(A);not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 100.0),
                    RuleITR(leftStructureString = "(and(not(A);not(B)))", rightStructureString = "(not(or(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 100.0),
                    RuleITR(leftStructureString = "(not(not(a)))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(not(not(a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(or(a;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(a;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(and(a;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(a;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(and(a;b))", rightStructureString = "(and(b;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 40, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(or(a;b))", rightStructureString = "(or(b;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 40, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(a;1))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(a;0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(or(A;not(A)))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(or(not(A);A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(and(A;not(A)))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(and(not(A);A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(or(A;1))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(or(1;A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(and(A;1))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(and(1;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(or(A;0))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(or(0;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(and(A;0))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(and(0;A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(not(0))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "(not(1))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 3.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "SimpleComputation", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "ZeroComputation", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 30, code = "ParentBracketsExpansion", weightInTaskAutoGeneration = 4.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "ArgumentsSwap", weightInTaskAutoGeneration = 4.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "ArgumentsPermutation", weightInTaskAutoGeneration = 4.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 30, code = "OpeningBrackets", weightInTaskAutoGeneration = 4.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 41, code = "ArgumentsPermutationInOriginalOrder", weightInTaskAutoGeneration = 4.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "ReduceArithmetic", weightInTaskAutoGeneration = 4.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "TwoSidesArithmeticReduce", weightInTaskAutoGeneration = 4.0)
                )
            )
        )

        fun get() = deMorganLogicRulePack
        fun map() = get().associateBy { it.code!! }
    }
}