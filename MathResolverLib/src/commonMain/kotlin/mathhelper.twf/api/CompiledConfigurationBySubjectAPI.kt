package mathhelper.twf.api

import mathhelper.twf.config.*
import mathhelper.twf.defaultcontent.defaultrulepacks.DefaultRulePacks
import mathhelper.twf.defaultcontent.defaultrulepacks.logic.ModifiedLogicRulePacks
import mathhelper.twf.expressiontree.ExpressionSubstitution
import mathhelper.twf.expressiontree.NodeType
import mathhelper.twf.mainpoints.TexVerificationResult
import mathhelper.twf.mainpoints.checkFactsInTex
import mathhelper.twf.mainpoints.compiledConfigurationBySettings
import mathhelper.twf.mainpoints.configSeparator


fun createConfigurationFromRulePacksAndParams(
        rulePacks: Array<String> = listOf("Algebra").toTypedArray(),
        additionalParamsMap: Map<String, String> = mapOf()
): CompiledConfiguration {
    val expressionSubstitutions = getSubstitutionsByRulePacks(rulePacks)
    val simpleComputationRuleCodesCandidates = expressionSubstitutions.map { it.code }.filter { it.isNotBlank() }.toSet()
    return CompiledConfiguration(additionalParamsMap = additionalParamsMap, simpleComputationRuleCodesCandidates = simpleComputationRuleCodesCandidates).apply {
        compiledExpressionTreeTransformationRules.clear()
        compiledExpressionSimpleAdditionalTreeTransformationRules.clear()
        val handledCodesHashSet = hashSetOf<String>()
        for (substitution in expressionSubstitutions) {
            if (handledCodesHashSet.contains(substitution.code))
                continue
            handledCodesHashSet.add(substitution.code)
            if (substitution.left.nodeType == NodeType.EMPTY || substitution.right.nodeType == NodeType.EMPTY) {
                if (substitution.code.isNotEmpty()) {
                    expressionTreeAutogeneratedTransformationRuleIdentifiers.put(substitution.code, substitution)
                }
            } else {
                compiledExpressionTreeTransformationRules.add(substitution)
                if (substitution.simpleAdditional) {
                    compiledExpressionSimpleAdditionalTreeTransformationRules.add(substitution)
                }
            }
        }
    }
}


fun createConfigurationFromRulePacksAndDetailSolutionCheckingParams(
        rulePacks: Array<String> = listOf("Algebra").toTypedArray(),
        wellKnownFunctionsString: String = "${configSeparator}0$configSeparator${configSeparator}1$configSeparator+$configSeparator-1$configSeparator-$configSeparator-1$configSeparator*$configSeparator-1$configSeparator/$configSeparator-1$configSeparator^$configSeparator-1",
        expressionTransformationRulesString: String = "S(i, a, a, f(i))${configSeparator}f(a)${configSeparator}S(i, a, b, f(i))${configSeparator}S(i, a, b-1, f(i)) + f(b)", //function transformation rules, parts split by configSeparator; if it equals " " then expressions will be checked only by testing
        maxExpressionTransformationWeight: String = "1.0",
        unlimitedWellKnownFunctionsString: String = wellKnownFunctionsString,
        taskContextExpressionTransformationRules: String = "", //for expression transformation rules based on variables
        maxDistBetweenDiffSteps: String = "", //is it allowed to differentiate expression in one step
        scopeFilter: String = "", //subject scopes which user representation sings is used
        wellKnownFunctions: List<FunctionIdentifier> = listOf(),
        unlimitedWellKnownFunctions: List<FunctionIdentifier> = wellKnownFunctions,
        expressionTransformationRules: List<ExpressionSubstitution> = listOf(), //full list of expression transformations rules
        subjectType: String = ""
): CompiledConfiguration {
    val compiledConfiguration = compiledConfigurationBySettings(
            wellKnownFunctionsString,
            expressionTransformationRulesString,
            maxExpressionTransformationWeight,
            unlimitedWellKnownFunctionsString,
            taskContextExpressionTransformationRules,
            maxDistBetweenDiffSteps,
            scopeFilter,

            wellKnownFunctions,
            unlimitedWellKnownFunctions,
            expressionTransformationRules,
            subjectType)
    if (rulePacks.isNotEmpty()) {
        val expressionSubstitutions = getSubstitutionsByRulePacks(rulePacks)
        compiledConfiguration.setExpressionSubstitutions(expressionSubstitutions)
    }
    return compiledConfiguration
}


fun CompiledConfiguration.setExpressionSubstitutions(expressionSubstitutions: List<ExpressionSubstitution>) {
    apply {
        compiledExpressionTreeTransformationRules.clear()
        compiledExpressionSimpleAdditionalTreeTransformationRules.clear()
        val handledCodesHashSet = hashSetOf<String>()
        for (substitution in expressionSubstitutions) {
            if (handledCodesHashSet.contains(substitution.code))
                continue
            handledCodesHashSet.add(substitution.code)
            if (substitution.left.nodeType == NodeType.EMPTY || substitution.right.nodeType == NodeType.EMPTY) {
                if (substitution.code.isNotEmpty()) {
                    expressionTreeAutogeneratedTransformationRuleIdentifiers.put(substitution.code, substitution)
                }
            } else {
                if (substitution.left.containsFunctionBesides(definedFunctionNameNumberOfArgsSet) || substitution.right.containsFunctionBesides(definedFunctionNameNumberOfArgsSet)) {
                    compiledExpressionTreeTransformationRules.add(substitution)
                    if (substitution.simpleAdditional) {
                        compiledExpressionSimpleAdditionalTreeTransformationRules.add(substitution)
                    }
                }
            }
        }
    }
}

val rulePacksMap = mapOf(
        Pair("Algebra", getAllAlgebraSubstitutions()),
        Pair("ArithmeticPositiveAddition", getArithmeticPositiveAdditionSubstitutions()),
        Pair("ArithmeticAddition", getArithmeticAdditionSubstitutions()),
        Pair("ArithmeticMultiplication", getArithmeticMultiplicationSubstitutions()),
        Pair("ArithmeticDivision", getArithmeticDivisionSubstitutions()),
        Pair("ArithmeticPow", getArithmeticPowSubstitutions()),
        Pair("ArithmeticPowExtension", getArithmeticPowExtensionSubstitutions()),
        Pair("ShortMultiplication", getShortMultiplicationSubstitutions()),
        Pair("Logarithm", getLogarithmSubstitutions()),
        Pair("FactorialRecurrent", getFactorialSubstitutions()),
        Pair("Combinatorics", getCombinatoricsSubstitutions()),
        Pair("Trigonometry", getTrigonometrySubstitutions()),
        Pair("Complexes", getComplexNumbersSubstitutions()),
        Pair("TrigonometryCompleteTgCtg", getAdvancedTrigonometrySubstitutions()),
        Pair("Logic", getLogicBaseSubstitutions()),
        Pair("LogicAbsorption", getLogicAbsorptionLawSubstitutions()),
        Pair("LogicResolution", getLogicResolutionSubstitutions()),
        Pair("PhysicsSimpleMoving", getPhysicsSimpleMovingSubstitutions()),
        Pair("PhysicsCircleMoving", getPhysicsCircleMovingSubstitutions()),
        Pair("PhysicsNuclear", getPhysicsNuclearSubstitutions()),
        Pair("PhysicsMolecular", getPhysicsMolecularSubstitutions()),
        Pair("PhysicsElectrodynamics", getPhysicsElectrodynamicsSubstitutions())
)

fun getSubstitutionsByRulePacks(rulePacks: Array<String>): List<ExpressionSubstitution> {
    val result = mutableListOf<ExpressionSubstitution>()
    for (rulePack in rulePacks) {
        val newRules = rulePacksMap[rulePack] ?: listOf<ExpressionSubstitution>()
        result += newRules
    }
    return result.distinctBy { it.code }
}

fun getAllAlgebraSubstitutions() = getLogarithmSubstitutions() + getFactorialSubstitutions() + getAdvancedTrigonometrySubstitutions()

fun getArithmeticPositiveAdditionSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ArithmeticPositiveAddition"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getArithmeticAdditionSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ArithmeticAddition"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getArithmeticMultiplicationSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ArithmeticMultiplication"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getArithmeticDivisionSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ArithmeticDivision"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getArithmeticPowSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ArithmeticExponentiation"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getArithmeticPowExtensionSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ArithmeticPowExtension"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getShortMultiplicationSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ShortMultiplication"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getLogarithmSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["Logarithm"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getFactorialSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["Factorial"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getCombinatoricsSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["BasicCombinatorics"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getBasicTrigonometricDefinitionsIdentitySubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["BasicTrigonometricDefinitionsIdentity"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getTrigonometrySinCosSumReductionSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["TrigonometrySinCosSumReduction"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getTrigonometrySubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["Trigonometry"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getAdvancedTrigonometrySubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["AdvancedTrigonometry"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getInverseTrigonometricFunctionsSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["InverseTrigonometricFunctions"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getComplexNumbersSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["ComplexNumbers"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getAllExtendedAlgebraSubstitutions() = getAllAlgebraSubstitutions() + getInverseTrigonometricFunctionsSubstitutions() + getComplexNumbersSubstitutions()


fun getLogicBaseSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["RelativeComplement"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getLogicAbsorptionLawSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["LogicAbsorptionLaw"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getLogicResolutionSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["LogicResolution"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getAllLogicSubstitutions() = getLogicBaseSubstitutions() + getLogicAbsorptionLawSubstitutions() + getLogicResolutionSubstitutions()// + getLogicNewVariablesSubstitutions()

fun getLogicBaseOrAndNot(packsMap: Map<String, RulePackITR>, includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(
    packsMap["LogicBaseOrAndNot"]!!, packsMap, includeChildRulePacks
)

fun getLogicBaseImplicXorAlleq(packsMap: Map<String, RulePackITR>, includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(
    packsMap["LogicBaseImplicXorAlleq"]!!, packsMap, includeChildRulePacks
)

fun getLogicBaseSubstitutions(packsMap: Map<String, RulePackITR>, includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(
    packsMap["RelativeComplement"]!!, packsMap, includeChildRulePacks) +
        expressionSubstitutionsFromRulePackITR(packsMap["RelativeComplement"]!!, packsMap, includeChildRulePacks)

fun getLogicAbsorptionLawSubstitutions(packsMap: Map<String, RulePackITR>, includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(
    packsMap["LogicAbsorptionLaw"]!!, packsMap, includeChildRulePacks)

fun getLogicResolutionSubstitutions(packsMap: Map<String, RulePackITR>, includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(
    packsMap["LogicResolution"]!!, packsMap, includeChildRulePacks)

fun getLogicNewVariablesSubstitutions(numberOfVariables: Int = 0, includeChildRulePacks: Boolean = false): Map<String, List<ExpressionSubstitution>> {
    return getLogicNewVariablesSubstitutions(ModifiedLogicRulePacks.map(), numberOfVariables, includeChildRulePacks)
}



fun getPhysicsNuclearSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["PhysicsNuclear"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getPhysicsCircleMovingSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["PhysicsCircleMoving"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getPhysicsMolecularSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["PhysicsMolecular"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getPhysicsSimpleMovingSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["PhysicsStraightMoving"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getPhysicsElectrodynamicsSubstitutions(includeChildRulePacks: Boolean = true) = expressionSubstitutionsFromRulePackITR(DefaultRulePacks.map()["PhysicsElectrodynamics"]!!, DefaultRulePacks.map(), includeChildRulePacks)

fun getAllPhysicsSubstitutions() = getPhysicsNuclearSubstitutions() + getPhysicsCircleMovingSubstitutions() + getPhysicsMolecularSubstitutions() + getPhysicsSimpleMovingSubstitutions() + getPhysicsElectrodynamicsSubstitutions()


fun getAllSubstitutions() = getAllExtendedAlgebraSubstitutions() + getAllLogicSubstitutions() + getAllPhysicsSubstitutions()


fun getLogicNewVariablesSubstitutions(packsMap: Map<String, RulePackITR>, numberOfVariables: Int = 0, includeChildRulePacks: Boolean = false): Map<String, List<ExpressionSubstitution>> {
    val result: MutableMap<String, MutableList<ExpressionSubstitution>> = emptyMap<String, MutableList<ExpressionSubstitution>>().toMutableMap()
    for (ch in 'z' downTo 'z'-numberOfVariables+1) {
        result["$ch"] = expressionSubstitutionsFromRulePackITR(
            packsMap["LogicNewVariables"]!!.copy(
                rules = packsMap["LogicNewVariables"]!!.rules?.map {
                    it.copy(rightStructureString = it.rightStructureString?.replace(
                        Regex("[^a-zA-Z]z[^a-zA-Z]")
                    ) { matchResult -> "${matchResult.value[0]}$ch${matchResult.value[2]}" })
                }),
            packsMap,
            includeChildRulePacks
        )
        result["$ch"] = (result.getOrElse("$ch", { mutableListOf<ExpressionSubstitution>() }) +
                expressionSubstitutionsFromRulePackITR(
                    packsMap["LogicAbsorptionLawReverse"]!!.copy(
                        rules = packsMap["LogicAbsorptionLawReverse"]!!.rules?.map {
                            it.copy(rightStructureString = it.rightStructureString?.replace(
                                Regex("[^a-zA-Z]z[^a-zA-Z]")
                            ) { matchResult -> "${matchResult.value[0]}$ch${matchResult.value[2]}" })
                        }),
                    packsMap,
                    includeChildRulePacks
                )).toMutableList()
    }
    return result
}