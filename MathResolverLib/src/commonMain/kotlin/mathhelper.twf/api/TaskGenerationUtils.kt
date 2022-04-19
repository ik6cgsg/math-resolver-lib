package mathhelper.twf.api

import mathhelper.twf.config.RulePackITR
import mathhelper.twf.defaultcontent.defaultrulepacks.DefaultStandardMathRulePacks

fun getDefaultRulePacks(): Array<RulePackITR> {
    val codes = listOf(
            "ArithmeticPositiveAddition",
            "ArithmeticAddition",
            "ArithmeticMultiplication",
            "ArithmeticDivision",
            "ArithmeticExponentiation",
            "ShortMultiplication",
            "BasicTrigonometricDefinitionsIdentity",
            "TrigonometrySinCosSumReduction",
            "Trigonometry",
            "AdvancedTrigonometry"
    )
    return DefaultStandardMathRulePacks.get().filter { codes.contains(it.code) }.toTypedArray()
}

fun mapGoalStepCount(complexity: Double, minStepCount: Int = 3, maxStepCount: Int = 9): Int {
    return minStepCount + (complexity * (maxStepCount - minStepCount)).toInt()
}
