package mathhelper.twf.defaultcontent.defaultrulepacks

import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR
import mathhelper.twf.config.RulePackLinkITR

//TODO: introduce ∅ and U, now its written as 0 and 1
//TODO: introduce operations with set elements

class SetRulePacks {
        companion object {
                val defaultSetRulePacks = listOf(
                        RulePackITR(
                                code = "SetBase",
                                nameEn = "Basic Set Theory", nameRu = "Основны теории множеств",
                                descriptionShortEn = "Basic operations, it's definitions and properties", descriptionShortRu = "Основные операции, их определения и свойства",
                                descriptionEn = "Set intersection, union, difference, symmetric difference, complement", descriptionRu = "Пересечение, объединение, разность, симметрическая разнсть, дополнение",
                                subjectType = "set",
                                rules = listOf(
                                        RuleITR(leftStructureString = "(not(and(A;B)))", rightStructureString = "(or(not(A);not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(or(not(A);not(B)))", rightStructureString = "(not(and(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(not(or(A;B)))", rightStructureString = "(and(not(A);not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(and(not(A);not(B)))", rightStructureString = "(not(or(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(and(A;B))", rightStructureString = "(not(or(not(A);not(B))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(not(or(not(A);not(B))))", rightStructureString = "(and(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(or(A;B))", rightStructureString = "(not(and(not(A);not(B))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),
                                        RuleITR(leftStructureString = "(not(and(not(A);not(B))))", rightStructureString = "(or(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана"),

                                        RuleITR(leftStructureString = "(not(not(A)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(not(not(A)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 89, code = ""),
                                        RuleITR(leftStructureString = "(or(A;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(or(A;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                                        RuleITR(leftStructureString = "(and(A;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(and(A;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                                        RuleITR(leftStructureString = "(and(A;B))", rightStructureString = "(and(B;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 40, code = ""),
                                        RuleITR(leftStructureString = "(or(A;B))", rightStructureString = "(or(B;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 40, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(and(A;1))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(or(A;0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                                        RuleITR(leftStructureString = "(or(A;not(A)))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(or(not(A);A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(and(A;not(A)))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(and(not(A);A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(or(A;1))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(or(1;A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(and(A;1))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(and(1;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(or(A;0))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(or(0;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(and(A;0))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(and(0;A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(not(0))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(not(1))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "SimpleComputation"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "ZeroComputation"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 30, code = "ParentBracketsExpansion"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "ArgumentsSwap"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "ArgumentsPermutation"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 30, code = "OpeningBrackets"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 41, code = "ArgumentsPermutationInOriginalOrder"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "ReduceArithmetic"),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "TwoSidesArithmeticReduce"),

                                        RuleITR(leftStructureString = "(and(A;not(B)))", rightStructureString = "(set-(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = ""),
                                        RuleITR(leftStructureString = "(and(not(B);A))", rightStructureString = "(set-(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = ""),
                                        RuleITR(leftStructureString = "(set-(A;B))", rightStructureString = "(and(A;not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = ""),
                                        RuleITR(leftStructureString = "(set-(A;0))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(set-(A;1))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(set-(0;A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(set-(1;A))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(set-(A;0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = ""),
                                        RuleITR(leftStructureString = "(not(A))", rightStructureString = "(set-(1;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = ""),
                                        RuleITR(leftStructureString = "(0)", rightStructureString = "(set-(A;1))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = ""),
                                        RuleITR(leftStructureString = "(0)", rightStructureString = "(set-(0;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "")

                                )
                        ),
                        RulePackITR(
                                code = "SetAbsorptionLaw",
                                nameEn = "Absorption Law", nameRu = "Закон поглощения",
                                descriptionShortEn = "Law and basic properties in Set Theory", descriptionShortRu = "Закон и основные свойства в теории множеств",
                                subjectType = "set",
                                rulePacks = listOf(RulePackLinkITR(rulePackCode = "SetBase")),
                                rules = listOf(
                                        RuleITR(leftStructureString = "(and(A;or(A;B)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(or(A;and(A;B)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(and(A;or(B;A)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(or(A;and(B;A)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(and(or(A;B);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(or(and(A;B);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(and(or(B;A);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(or(and(B;A);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(and(A;or(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                                        RuleITR(leftStructureString = "(A)", rightStructureString = "(or(A;and(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                                        RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 92, code = "SetComplicatingExtension")
                                )
                        )
                )

                fun get() = defaultSetRulePacks
        }
}