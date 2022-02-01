package mathhelper.twf.defaultcontent.defaultrulepacks.logic

// TODO: dkorotchenko rename

import mathhelper.twf.config.RuleITR
import mathhelper.twf.config.RulePackITR
import mathhelper.twf.config.RulePackLinkITR

class NewWeightsLogicRulePack {
    companion object {
        val newWeightsLogicRulePack = listOf(
            RulePackITR(
                code = "LogicBaseOrAndNot",
                nameEn = "Basic Boolean Logic: Or, And, Not", nameRu = "Основны булевой логики",
                descriptionShortEn = "Basic operations, it's definitions and properties", descriptionShortRu = "Основные операции, их определения и свойства",
                descriptionEn = "Boolean Algebra Conjunction, Disjunction, Negation, Implication, Exclusive Or, Equivalence", descriptionRu = "Булева алгебра: конъюнкция, дизъюнкция, отрицание, импликация, исключающее или, эквиваленция",
                subjectType = "logic",
                rules = listOf(
                    RuleITR(leftStructureString = "(not(and(A;B)))", rightStructureString = "(or(not(A);not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(or(not(A);not(B)))", rightStructureString = "(not(and(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(not(or(A;B)))", rightStructureString = "(and(not(A);not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 15, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(and(not(A);not(B)))", rightStructureString = "(not(or(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "", nameEn = "De Morgan Law", nameRu = "Закон де Моргана", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(not(not(a)))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(not(not(a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 25.0),
                    RuleITR(leftStructureString = "(or(a;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(a;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 25.0),
                    RuleITR(leftStructureString = "(and(a;a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(a;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 25.0),
                    RuleITR(leftStructureString = "(and(a;b))", rightStructureString = "(and(b;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 40, code = "", weightInTaskAutoGeneration = 15.0),
                    RuleITR(leftStructureString = "(or(a;b))", rightStructureString = "(or(b;a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 40, code = "", weightInTaskAutoGeneration = 15.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(a;1))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(a;0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(or(A;not(A)))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(or(not(A);A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(and(A;not(A)))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(and(not(A);A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(or(A;1))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(or(1;A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(and(A;1))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 15.0),
                    RuleITR(leftStructureString = "(and(1;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 15.0),
                    RuleITR(leftStructureString = "(or(A;0))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 15.0),
                    RuleITR(leftStructureString = "(or(0;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 15.0),
                    RuleITR(leftStructureString = "(and(A;0))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(and(0;A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(not(0))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 10.0),
                    RuleITR(leftStructureString = "(not(1))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 10.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "SimpleComputation", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "ZeroComputation", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 30, code = "ParentBracketsExpansion", weightInTaskAutoGeneration = 30.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "ArgumentsSwap", weightInTaskAutoGeneration = 25.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 40, code = "ArgumentsPermutation", weightInTaskAutoGeneration = 25.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 30, code = "OpeningBrackets", weightInTaskAutoGeneration = 30.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 41, code = "ArgumentsPermutationInOriginalOrder", weightInTaskAutoGeneration = 25.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "ReduceArithmetic", weightInTaskAutoGeneration = 40.0),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 10, code = "TwoSidesArithmeticReduce", weightInTaskAutoGeneration = 40.0)
                )
            ),
            RulePackITR(
                code = "LogicBaseImplicXorAlleq",
                nameEn = "Basic Boolean Logic: Or, And, Not", nameRu = "Основны булевой логики",
                descriptionShortEn = "Basic operations, it's definitions and properties", descriptionShortRu = "Основные операции, их определения и свойства",
                descriptionEn = "Boolean Algebra Conjunction, Disjunction, Negation, Implication, Exclusive Or, Equivalence", descriptionRu = "Булева алгебра: конъюнкция, дизъюнкция, отрицание, импликация, исключающее или, эквиваленция",
                subjectType = "logic",
                rules = listOf(
                    RuleITR(leftStructureString = "(or(not(A);B))", rightStructureString = "(implic(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(or(B;not(A)))", rightStructureString = "(implic(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(implic(A;B))", rightStructureString = "(or(not(A);B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(implic(0;A))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(implic(A;1))", rightStructureString = "(1)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 5.0),
                    RuleITR(leftStructureString = "(implic(A;0))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 80.0),
                    RuleITR(leftStructureString = "(implic(1;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 80.0),
                    RuleITR(leftStructureString = "(A)", rightStructureString = "(implic(1;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "", weightInTaskAutoGeneration = 2.0),

                    RuleITR(leftStructureString = "(xor(A;B))", rightStructureString = "(or(and(not(A);B);and(A;not(B))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 30, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(or(and(not(A);B);and(A;not(B))))", rightStructureString = "(xor(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 30, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(alleq(A;B))", rightStructureString = "(or(and(A;B);and(not(A);not(B))))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 30, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(or(and(A;B);and(not(A);not(B))))", rightStructureString = "(alleq(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 30, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(xor(A;0))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(xor(A;1))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(xor(0;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(xor(1;A))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(alleq(0;A))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(alleq(A;1))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(alleq(A;0))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(alleq(1;A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(A)", rightStructureString = "(xor(A;0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(A)", rightStructureString = "(alleq(1;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(not(A))", rightStructureString = "(xor(A;1))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "", weightInTaskAutoGeneration = 2.0),
                    RuleITR(leftStructureString = "(not(A))", rightStructureString = "(alleq(0;A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "", weightInTaskAutoGeneration = 2.0)
                )
            ),
            RulePackITR(
                code = "RelativeComplement",
                nameEn = "Relative Complement", nameRu = "Логическое дополнение",
                descriptionShortEn = "Definition and basic properties", descriptionShortRu = "Определение и основные свойства",
                descriptionEn = "Boolean Algebra Conjunction, Disjunction, Negation, Implication, Exclusive Or, Equivalence, Relative Complement", descriptionRu = "Булева алгебра: конъюнкция, дизъюнкция, отрицание, импликация, исключающее или, эквиваленция, дополнение",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(and(A;not(B)))", rightStructureString = "(set-(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 35, code = "", weightInTaskAutoGeneration = 85.0),
                    RuleITR(leftStructureString = "(and(not(B);A))", rightStructureString = "(set-(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 35, code = "", weightInTaskAutoGeneration = 85.0),
                    RuleITR(leftStructureString = "(set-(A;B))", rightStructureString = "(and(A;not(B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 35, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(set-(A;B))", rightStructureString = "(not(implic(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 35, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(not(implic(A;B)))", rightStructureString = "(set-(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(implic(A;B))", rightStructureString = "(not(set-(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(not(set-(A;B)))", rightStructureString = "(implic(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(set-(A;0))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 85.0),
                    RuleITR(leftStructureString = "(set-(A;1))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 8.0),
                    RuleITR(leftStructureString = "(set-(0;A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 8.0),
                    RuleITR(leftStructureString = "(set-(1;A))", rightStructureString = "(not(A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(A)", rightStructureString = "(set-(A;0))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 91, code = "", weightInTaskAutoGeneration = 40.0)
                )
            ),
            RulePackITR(
                code = "LogicNotAnd",
                nameEn = "Negation of Conjunction", nameRu = "Отрицание конъюнкции (Штрих Шеффера)",
                descriptionShortEn = "Definition and basic properties", descriptionShortRu = "Определение и основные свойства",
                descriptionEn = "Boolean Algebra Conjunction, Disjunction, Negation, Implication, Exclusive Or, Equivalence, Negation of Conjunction", descriptionRu = "Булева алгебра: конъюнкция, дизъюнкция, отрицание, импликация, исключающее или, эквиваленция, отрицание конъюнкции",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(nand(A;B))", rightStructureString = "(not(and(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(not(and(A;B)))", rightStructureString = "(nand(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0)
                )
            ),
            RulePackITR(
                code = "LogicNotOr",
                nameEn = "Negation of Disjunction", nameRu = "Отрицание дизъюнкции (Стрелка Пирса)",
                descriptionShortEn = "Definition and basic properties", descriptionShortRu = "Определение и основные свойства",
                descriptionEn = "Boolean Algebra Conjunction, Disjunction, Negation, Implication, Exclusive Or, Equivalence, Negation of Disjunction", descriptionRu = "Булева алгебра: конъюнкция, дизъюнкция, отрицание, импликация, исключающее или, эквиваленция, отрицание дизъюнкции",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(nor(A;B))", rightStructureString = "(not(or(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0),
                    RuleITR(leftStructureString = "(not(or(A;B)))", rightStructureString = "(nor(A;B))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 35, code = "", weightInTaskAutoGeneration = 70.0)
                )
            ),
            RulePackITR(
                code = "LogicAbsorptionLaw",
                nameEn = "Absorption Law", nameRu = "Закон поглощения",
                descriptionShortEn = "Law and basic properties in Boolean Algebra", descriptionShortRu = "Закон и основные свойства в булевой алгебре",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(and(A;or(A;B)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(or(A;and(A;B)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(and(A;or(B;A)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(or(A;and(B;A)))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(and(or(A;B);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(or(and(A;B);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(and(or(B;A);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    RuleITR(leftStructureString = "(or(and(B;A);A))", rightStructureString = "(A)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 6, code = "", weightInTaskAutoGeneration = 90.0),
                    //RuleITR(leftStructureString = "(A)", rightStructureString = "(and(A;or(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                    //RuleITR(leftStructureString = "(A)", rightStructureString = "(or(A;and(A;B)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 90, code = ""),
                    RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 92, code = "SetComplicatingExtension", weightInTaskAutoGeneration = 7.0)
                )
            ),
            RulePackITR(
                code = "LogicResolution",
                nameEn = "Resolution", nameRu = "Метод резолюций",
                descriptionShortEn = "Rules for proof by Resolution method", descriptionShortRu = "правила для доказательств методом резолюций",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(and(not(A);A))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 40.0),
                    RuleITR(leftStructureString = "(and(A;not(A)))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 40.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A)))", rightStructureString = "(and(X;or(A;X);not(A)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X)))", rightStructureString = "(and(X;not(A);or(A;X)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A))", rightStructureString = "(and(X;or(not(A);X);A))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X)))", rightStructureString = "(and(X;A;or(not(A);X)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y)))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y)))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C))", rightStructureString = "(and(X;or(A;X);not(A);C))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C))", rightStructureString = "(and(X;not(A);or(A;X);C))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C))", rightStructureString = "(and(X;or(not(A);X);A;C))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C))", rightStructureString = "(and(X;A;or(not(A);X);C))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C;D))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C;D))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C;D))", rightStructureString = "(and(X;or(A;X);not(A);C;D))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C;D))", rightStructureString = "(and(X;not(A);or(A;X);C;D))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C;D))", rightStructureString = "(and(X;or(not(A);X);A;C;D))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C;D))", rightStructureString = "(and(X;A;or(not(A);X);C;D))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C;D))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C;D))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C;D))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C;D))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C;D;E))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C;D;E))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C;D;E))", rightStructureString = "(and(X;or(A;X);not(A);C;D;E))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C;D;E))", rightStructureString = "(and(X;not(A);or(A;X);C;D;E))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C;D;E))", rightStructureString = "(and(X;or(not(A);X);A;C;D;E))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C;D;E))", rightStructureString = "(and(X;A;or(not(A);X);C;D;E))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C;D;E))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C;D;E))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C;D;E))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C;D;E))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C;D;E;F))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C;D;E;F))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C;D;E;F))", rightStructureString = "(and(X;or(A;X);not(A);C;D;E;F))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C;D;E;F))", rightStructureString = "(and(X;not(A);or(A;X);C;D;E;F))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C;D;E;F))", rightStructureString = "(and(X;or(not(A);X);A;C;D;E;F))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C;D;E;F))", rightStructureString = "(and(X;A;or(not(A);X);C;D;E;F))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C;D;E;F))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C;D;E;F))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C;D;E;F))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C;D;E;F))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C;D;E;F;G))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C;D;E;F;G))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C;D;E;F;G))", rightStructureString = "(and(X;or(A;X);not(A);C;D;E;F;G))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C;D;E;F;G))", rightStructureString = "(and(X;not(A);or(A;X);C;D;E;F;G))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C;D;E;F;G))", rightStructureString = "(and(X;or(not(A);X);A;C;D;E;F;G))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C;D;E;F;G))", rightStructureString = "(and(X;A;or(not(A);X);C;D;E;F;G))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C;D;E;F;G))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C;D;E;F;G))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C;D;E;F;G))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C;D;E;F;G))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C;D;E;F;G;H))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C;D;E;F;G;H))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C;D;E;F;G;H))", rightStructureString = "(and(X;or(A;X);not(A);C;D;E;F;G;H))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C;D;E;F;G;H))", rightStructureString = "(and(X;not(A);or(A;X);C;D;E;F;G;H))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C;D;E;F;G;H))", rightStructureString = "(and(X;or(not(A);X);A;C;D;E;F;G;H))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C;D;E;F;G;H))", rightStructureString = "(and(X;A;or(not(A);X);C;D;E;F;G;H))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C;D;E;F;G;H))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C;D;E;F;G;H))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C;D;E;F;G;H))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C;D;E;F;G;H))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);A;C;D;E;F;G;H;I))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(A;not(A);C;D;E;F;G;H;I))", rightStructureString = "(0)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 60.0),
                    RuleITR(leftStructureString = "(and(or(A;X);not(A);C;D;E;F;G;H;I))", rightStructureString = "(and(X;or(A;X);not(A);C;D;E;F;G;H;I))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(not(A);or(A;X);C;D;E;F;G;H;I))", rightStructureString = "(and(X;not(A);or(A;X);C;D;E;F;G;H;I))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);A;C;D;E;F;G;H;I))", rightStructureString = "(and(X;or(not(A);X);A;C;D;E;F;G;H;I))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(A;or(not(A);X);C;D;E;F;G;H;I))", rightStructureString = "(and(X;A;or(not(A);X);C;D;E;F;G;H;I))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(A;X);or(not(A);Y);C;D;E;F;G;H;I))", rightStructureString = "(and(or(X;Y);or(A;X);or(not(A);Y);C;D;E;F;G;H;I))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0),
                    RuleITR(leftStructureString = "(and(or(not(A);X);or(A;Y);C;D;E;F;G;H;I))", rightStructureString = "(and(or(X;Y);or(not(A);X);or(A;Y);C;D;E;F;G;H;I))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = true, priority = 10, code = "", weightInTaskAutoGeneration = 75.0)
                )
            ),
            RulePackITR(
                code = "LogicSimplification",
                nameEn = "Logic Simplifications", nameRu = "Упрощения логических выражений",
                descriptionShortEn = "Rules for trivial simplifications in logic", descriptionShortRu = "правила для упрощения бессмысленных выражений",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(and(a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(or(a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0),
                    RuleITR(leftStructureString = "(alleq(a))", rightStructureString = "(a)", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 5, code = "", weightInTaskAutoGeneration = 1.0)
                )
            ),
            RulePackITR(
                code = "LogicNewVariables",
                nameEn = "New variables introduction in logic", nameRu = "Введение новых переменных в логических выражениях",
                descriptionEn = "Introduction of new variables in logic expressions", descriptionRu = "Правила для введения новых переменных в логические выражения",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(1)", rightStructureString = "(or(not(z);z))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(0)", rightStructureString = "(and(not(z);z))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0)
                )
            ),
            RulePackITR(
                code = "LogicAbsorptionLawReverse",
                nameEn = "Absorption Law Reverse", nameRu = "Обратные Закон поглощения",
                descriptionShortEn = "Reverse rules to absorption law for new variables", descriptionShortRu = "Обратные правила к закону поглощения для воода новых переменных",
                subjectType = "logic",
                rulePacks = listOf(RulePackLinkITR(rulePackCode = "LogicBase")),
                rules = listOf(
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(a;or(a;z)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(a;and(a;z)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(a;or(z;a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(a;and(z;a)))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(or(a;z);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(and(a;z);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(and(or(z;a);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0),
                    RuleITR(leftStructureString = "(a)", rightStructureString = "(or(and(z;a);a))", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 50, code = "", weightInTaskAutoGeneration = 50.0)//,
                    //RuleITR(leftStructureString = "", rightStructureString = "", basedOnTaskContext = false, matchJumbledAndNested = false, simpleAdditional = false, isExtending = false, priority = 92, code = "SetComplicatingExtension")
                )
            )
        )

        fun get() = newWeightsLogicRulePack
        fun map() = get().associateBy { it.code!! }
    }
}