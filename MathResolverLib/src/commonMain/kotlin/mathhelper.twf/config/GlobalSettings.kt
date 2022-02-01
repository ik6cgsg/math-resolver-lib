package mathhelper.twf.config

import mathhelper.twf.api.expressionToString
import mathhelper.twf.api.structureStringToExpression
import mathhelper.twf.api.structureStringToString
import mathhelper.twf.api.structureStringToTexString
import mathhelper.twf.defaultcontent.TaskTagCode
import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.twf.expressiontree.ExpressionStructureConditionConstructor
import mathhelper.twf.expressiontree.ExpressionSubstitution
import mathhelper.twf.expressiontree.NodeType
import mathhelper.twf.standartlibextensions.toCustomCodeSuffixPart
import mathhelper.twf.taskautogeneration.ExpressionTaskIntermediateData

val namespaceCodeAndCodeDivider = "__"
val taskSetCodeAndCodeDivider = "__"

fun concatNamespaceAndCode(namespaceCode: String, code: String?) = namespaceCode + namespaceCodeAndCodeDivider + code

data class RuleITR(
        var code: String? = null,
        val nameEn: String? = null,
        val nameRu: String? = null,
        val descriptionShortEn: String? = null,
        val descriptionShortRu: String? = null,
        val descriptionEn: String? = null,
        val descriptionRu: String? = null,

        val leftStructureString: String? = null,
        val rightStructureString: String? = null,
        val priority: Int? = null, // param to sort rules in mobile app
        val isExtending: Boolean? = null, // extend expression, for example, a = a + 0
        val matchJumbledAndNested: Boolean? = null, // should permutations be taken into account when applying rules to commutative operations (+,*) (a^2+2ab+b^2) -> (a+b)^2, 2xy+x^2+y^2 can be converted due to this parameter
        val simpleAdditional: Boolean? = null, // the rule can be combined with others, for example x = x^1, use this to multiply degrees, the rules with this flag are applied at the beginning, for example x * x^2 = x^1 * x^2 = x^3
        val basedOnTaskContext: Boolean? = null, // the rule is true only for these variables, for example, a physics problem, I = U/R, in general the rule is incorrect, this expression is true only for I, U, R; or solve the problem provided that cos(a) = 0.5
        val normalizationType: String? = null, // the type of sorting of variables when comparing expressions
        val weight: Double? = null, // the complexity of the rule for the learner, there is a limit on the maximum weight of the rules
        val weightInTaskAutoGeneration: Double? = null, // proportional to the probability of choosing a rule when auto-generating a task
        val useWhenPostprocessGeneratedExpression: Boolean = false // used by autogenerator of tasks. If value is `true`, the rule can be applied to generated task in order to remove some useless defects like -(-(-(-x))) -> x
) {
    init {
        if (code.isNullOrBlank() && leftStructureString?.isNotBlank() == true && rightStructureString?.isNotBlank() == true) {
            code = "${leftStructureString}__to__$rightStructureString"
        }
    }
}

data class SolutionsStepITR(
    val stepExpression: String, //step expression in structure string
    val substitution: ExpressionSubstitution, //substitution that should be applied to <stepExpression>
    val selectedPlacesNodeIds: List<Int>, //nodeIds in which <rule> should be applied to <stepExpression>
    val stepId: Int, // identifier of such step in solution tree. StartExpression may has not smallest stepId
    val prevStepId: Int // stepId of previous expression
)

class HintITR(
        var textEn: String = "", //use dollars "$$" to specify formulas in text
        var textRu: String = "", //use dollars "$$" to specify formulas in text. Like "Поможет правило $$\sin^2 + \cos^2 = 1$$"
        val showPattern: String? = null,
        text: String? = null
) {
    init {
        if (text != null) {
            if (textEn.isBlank()) {
                textEn = text
            }
            if (textRu.isBlank()) {
                textRu = text
            }
        }
        if (!(textEn.isNotBlank() && textRu.isNotBlank())) {
            throw IllegalArgumentException("textEn='$textEn' textRu='$textRu'")
        }
    }
}

data class InterestingFact(
        val textEn: String,
        val textRu: String,
        val showCondition: String // beforeBeginning / afterEnd

)

data class TaskITR(
        val taskCreationType: String? = null,

        var code: String? = null,
        val version: Int = 0,
        var namespaceCode: String? = null,
        var nameEn: String? = null,
        var nameRu: String? = null,
        var descriptionShortEn: String? = null,
        var descriptionShortRu: String? = null,
        var descriptionEn: String? = null,
        var descriptionRu: String? = null,

        val subjectType: String? = "standard_math",
        val tags: MutableSet<String> = mutableSetOf(),

        val originalExpressionStructureString: String? = null,
        val originalExpressionTex: String? = if (originalExpressionStructureString != null) structureStringToTexString(originalExpressionStructureString) else null, // not mandatory field in ITR
        val originalExpressionPlainText: String? = if (originalExpressionStructureString != null) structureStringToString(originalExpressionStructureString) else null, // not mandatory field in ITR

        var goalType: String? = null,
        val goalExpressionStructureString: String? = null,
        val goalExpressionTex: String? = if (goalExpressionStructureString != null) structureStringToTexString(goalExpressionStructureString) else null, // not mandatory field in ITR
        val goalExpressionPlainText: String? = if (goalExpressionStructureString != null) structureStringToString(goalExpressionStructureString) else null, // not mandatory field in ITR

        var goalPattern: String? = null,
        val goalNumberProperty: Int? = null,
        val otherGoalData: Map<String, Any>? = null,

        val rulePacks: List<RulePackLinkITR>? = null,
        val rules: List<RuleITR>? = null,

        val stepsNumber: Int? = null,
        val time: Int? = null,
        val difficulty: Double,

        var solutionPlainText: String? = null, // plain text of solution is easier to read in most types of applications
        val solutionsStepsTree: Map<String, Any>? = null,
        val hints: Map<String, Any>? = null,
        val otherCheckSolutionData: Map<String, Any>? = null,
        val countOfAutoGeneratedTasks: Int? = null,
        val otherAutoGenerationData: Map<String, Any>? = null,
        val interestingFacts: Map<String, Any>? = null,
        val otherAwardData: Map<String, Any>? = null,
        val nextRecommendedTasks: Map<String, Any>? = null,
        val otherData: Map<String, Any>? = null,

        val comment: String? = null, // only for working
        val domainConditionDescription: String? = null, // only for usual descriptions generation
        val domainConditionDescriptionEn: String? = null, // only for usual descriptions generation
        val domainConditionDescriptionRu: String? = null, // only for usual descriptions generation

        var expressionTaskIntermediateData: ExpressionTaskIntermediateData = ExpressionTaskIntermediateData() // debug values for reports
) {
    init {
        val expressionStructureConditionConstructor = ExpressionStructureConditionConstructor(CompiledConfiguration(functionConfiguration = FunctionConfiguration()))
        if (goalType == "computation" && goalPattern.isNullOrBlank()) {
            goalPattern = "+:0-1(-:1):?:?R"
        }
        if (goalType == null) {
            if (goalExpressionStructureString != null) {
                goalType = "expression"
            } else if (goalPattern != null) {
                if (goalPattern!!.contains('R') || goalPattern!!.contains('N') || goalPattern!!.contains('Z')) {
                    goalType = "computation"
                } else if (expressionStructureConditionConstructor.parse(goalPattern!!).treeVariables.isNotEmpty()) {
                    goalType = "express"
                } else {
                    goalType = "simplification"
                }
            }
        }

        val originalExpression = if (originalExpressionStructureString != null) {
            structureStringToExpression(originalExpressionStructureString)
        } else ExpressionNode(nodeType = NodeType.EMPTY, value = "")
        val goalExpression = if (goalExpressionStructureString != null) {
            structureStringToExpression(goalExpressionStructureString)
        } else ExpressionNode(nodeType = NodeType.EMPTY, value = "")

        when (goalType) {
            "expression" -> {
                if (nameRu == null) {
                    if (tags.contains(TaskTagCode.RESOLUTION.code)) {
                        nameRu = "Метод Резолюций"
                    } else {
                        nameRu = "Доказательство"
                    }
                }
                if (nameEn == null) {
                    if (tags.contains(TaskTagCode.RESOLUTION.code)) {
                        nameEn = "Resolution Method"
                    } else {
                        nameEn = "Proof"
                    }
                }
                if (descriptionShortRu == null) {
                    if (tags.contains(TaskTagCode.RESOLUTION.code)) {
                        descriptionShortRu = "Доказать методом резолюций"
                    } else {
                        descriptionShortRu = "Свести к"
                    }
                    addTaskRulePacksToDescriptionShortRu(rules)
                }
                if (descriptionShortEn == null) {
                    if (tags.contains(TaskTagCode.RESOLUTION.code)) {
                        descriptionShortEn = "Prove by Resolution Method"
                    } else {
                        descriptionShortEn = "Relate to"
                    }
                    addTaskRulePacksToDescriptionShortEn(rules)
                }
                if (descriptionRu == null) {
                    descriptionRu = "Доказать ${ if (tags.contains(TaskTagCode.RESOLUTION.code)) "методом резолюций" else ""
                    } '${expressionToString(originalExpression)} = ${expressionToString(goalExpression)}'"
                    addTaskRulePacksToDescriptionRu(rules)
                }
                if (descriptionEn == null) {
                    descriptionEn = "Prove ${ if (tags.contains(TaskTagCode.RESOLUTION.code)) "by Resolution Method" else ""
                    } '${expressionToString(originalExpression)} = ${expressionToString(goalExpression)}'"
                    addTaskRulePacksToDescriptionEn(rules)
                }
            }

            "computation" -> {
                if (nameRu == null) {
                    nameRu = "Вычисление"
                }
                if (nameEn == null) {
                    nameEn = "Computation"
                }
                if (descriptionShortRu == null) {
                    descriptionShortRu = "Вычислить"
                    addTaskRulePacksToDescriptionShortRu(rules)
                }
                if (descriptionShortEn == null) {
                    descriptionShortEn = "Compute"
                    addTaskRulePacksToDescriptionShortEn(rules)
                }
                if (descriptionRu == null) {
                    descriptionRu = "Вычислить '${expressionToString(originalExpression)}'"
                    addTaskRulePacksToDescriptionRu(rules)
                }
                if (descriptionEn == null) {
                    descriptionEn = "Compute '${expressionToString(originalExpression)}'"
                    addTaskRulePacksToDescriptionEn(rules)
                }
            }

            "express" -> {
                if (nameRu == null) {
                    nameRu = "Преобразование"
                }
                if (nameEn == null) {
                    nameEn = "Transformation"
                }
                val patternTreeVariables = expressionStructureConditionConstructor.parse(goalPattern!!).treeVariables // goalPatter must be != null as it must contain the list of allowed variables
                if (descriptionShortRu == null) {
                    descriptionShortRu = "Выразить '${expressionToString(originalExpression)}'"
                    if (patternTreeVariables.treePermittedVariables.isNotEmpty()) {
                        descriptionShortRu += " через " + patternTreeVariables.treePermittedVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    if (patternTreeVariables.treeForbiddenVariables.isNotEmpty()) {
                        descriptionShortRu += " не используя " + patternTreeVariables.treeForbiddenVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    addTaskRulePacksToDescriptionShortRu(rules)
                }
                if (descriptionShortEn == null) {
                    descriptionShortEn = "Express '${expressionToString(originalExpression)}'"
                    if (patternTreeVariables.treePermittedVariables.isNotEmpty()) {
                        descriptionShortEn += " using " + patternTreeVariables.treePermittedVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    if (patternTreeVariables.treeForbiddenVariables.isNotEmpty()) {
                        descriptionShortEn += " without " + patternTreeVariables.treeForbiddenVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    addTaskRulePacksToDescriptionShortEn(rules)
                }

                if (descriptionRu == null) {
                    descriptionRu = "Выразить '${expressionToString(originalExpression)}'"
                    if (patternTreeVariables.treePermittedVariables.isNotEmpty()) {
                        descriptionRu += " через " + patternTreeVariables.treePermittedVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    if (patternTreeVariables.treeForbiddenVariables.isNotEmpty()) {
                        descriptionRu += " не используя " + patternTreeVariables.treeForbiddenVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    addTaskRulePacksToDescriptionRu(rules)
                }
                if (descriptionEn == null) {
                    descriptionEn = "Express '${expressionToString(originalExpression)}'"
                    if (patternTreeVariables.treePermittedVariables.isNotEmpty()) {
                        descriptionEn += " using " + patternTreeVariables.treePermittedVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    if (patternTreeVariables.treeForbiddenVariables.isNotEmpty()) {
                        descriptionEn += " without " + patternTreeVariables.treeForbiddenVariables.joinToString(", ", transform = {
                            it.variableName
                        })
                    }
                    addTaskRulePacksToDescriptionEn(rules)
                }
            }

            "simplification" -> {
                if (nameRu == null) {
                    nameRu = "Упрощение"
                }
                if (nameEn == null) {
                    nameEn = "Simplification"
                }
                if (descriptionShortRu == null) {
                    descriptionShortRu = "Упростить"
                    addTaskRulePacksToDescriptionShortRu(rules)
                }
                if (descriptionShortEn == null) {
                    descriptionShortEn = "Simplify"
                    addTaskRulePacksToDescriptionShortEn(rules)
                }
                if (descriptionRu == null) {
                    descriptionRu = "Упростить '${expressionToString(originalExpression)}'"
                    addTaskRulePacksToDescriptionRu(rules)
                }
                if (descriptionEn == null) {
                    descriptionEn = "Simplify '${expressionToString(originalExpression)}'"
                    addTaskRulePacksToDescriptionEn(rules)
                }
            }

            "CNF","DNF" -> {
                val goalTypeRu = if (goalType == "CNF") "КНФ" else "ДНФ"
                if (nameRu == null) {
                    nameRu = (if (goalNumberProperty != null) "$goalNumberProperty-" else "") + goalTypeRu
                }
                if (nameEn == null) {
                    nameEn = (if (goalNumberProperty != null) "$goalNumberProperty-" else "") + goalType
                }
                if (descriptionShortRu == null) {
                    descriptionShortRu = "Свести к $nameRu"
                    addTaskRulePacksToDescriptionShortRu(rules)
                }
                if (descriptionShortEn == null) {
                    descriptionShortEn = "Relate to $nameEn"
                    addTaskRulePacksToDescriptionShortEn(rules)
                }
                if (descriptionRu == null) {
                    descriptionRu = "Свести '${expressionToString(originalExpression)}' к $nameRu"
                    addTaskRulePacksToDescriptionRu(rules)
                }
                if (descriptionEn == null) {
                    descriptionEn = "Relate '${expressionToString(originalExpression)}' to $nameEn"
                    addTaskRulePacksToDescriptionEn(rules)
                }
            }
        }

        if (!domainConditionDescription.isNullOrBlank()) {
            descriptionShortEn += " if $domainConditionDescription"
            descriptionEn += " if $domainConditionDescription"
            descriptionShortRu += ", если $domainConditionDescriptionRu"
            descriptionRu += ", если $domainConditionDescriptionRu"
        } else {
            if (!domainConditionDescriptionEn.isNullOrBlank()) {
                descriptionShortEn += " $domainConditionDescriptionEn"
                descriptionEn += " $domainConditionDescriptionEn"
            }
            if (!domainConditionDescriptionRu.isNullOrBlank()) {
                descriptionShortRu += " $domainConditionDescriptionRu"
                descriptionRu += " $domainConditionDescriptionRu"
            }
        }

        enrichTags()
    }

    private fun addTaskRulePacksToDescriptionRu(rules: List<RuleITR>?) {
        if (rules?.isNotEmpty() == true) {
            descriptionRu += ", если " + rules.joinToString(" и ", transform = {
                "'${structureStringToString(it.leftStructureString!!)} = ${structureStringToString(it.rightStructureString!!)}'"
            })
        }
    }

    private fun addTaskRulePacksToDescriptionEn(rules: List<RuleITR>?) {
        if (rules?.isNotEmpty() == true) {
            descriptionEn += " if " + rules.joinToString(" and ", transform = {
                "'${structureStringToString(it.leftStructureString!!)} = ${structureStringToString(it.rightStructureString!!)}'"
            })
        }
    }

    private fun addTaskRulePacksToDescriptionShortRu(rules: List<RuleITR>?) {
        if (rules?.isNotEmpty() == true) {
            descriptionShortRu += ", если " + rules.joinToString(" и ", transform = {
                "'${structureStringToString(it.leftStructureString!!)} = ${structureStringToString(it.rightStructureString!!)}'"
            })
        }
    }

    private fun addTaskRulePacksToDescriptionShortEn(rules: List<RuleITR>?) {
        if (rules?.isNotEmpty() == true) {
            descriptionShortEn += " if " + rules.joinToString(" and ", transform = {
                "'${structureStringToString(it.leftStructureString!!)} = ${structureStringToString(it.rightStructureString!!)}'"
            })
        }
    }

    fun enrichTags() {
        when (goalType) {
            "expression" -> tags.add(TaskTagCode.PROOF.code)
            "custom" -> {
                when (goalPattern) {
                    "?:0:?:?N", "?:0:?:?R" -> tags.add(TaskTagCode.COMPUTATION.code) // TODO
                }
            }
            else -> if (goalType != null) tags.add(goalType!!)
        }

        val originalExpression = if (originalExpressionStructureString != null) {
            structureStringToExpression(originalExpressionStructureString)
        } else ExpressionNode(nodeType = NodeType.EMPTY, value = "")
        val goalExpression = if (goalExpressionStructureString != null) {
            structureStringToExpression(goalExpressionStructureString)
        } else ExpressionNode(nodeType = NodeType.EMPTY, value = "")

        tags.addAll(originalExpression.getContainedFunctions())
        tags.addAll(goalExpression.getContainedFunctions())

        if (tags.contains("sin") || tags.contains("cos") || tags.contains("tg") || tags.contains("ctg")) {
            tags.add(TaskTagCode.TRIGONOMETRY.code)
        }
        if (tags.contains("asin") || tags.contains("acos") || tags.contains("atg") || tags.contains("actg")) {
            tags.add(TaskTagCode.TRIGONOMETRY.code)
            tags.add(TaskTagCode.INVERSE_TRIGONOMETRY.code)
        }
        if (tags.contains("ln") || tags.contains("log")) {
            tags.add(TaskTagCode.LOGARITHM.code)
        }
        tags.remove("+")
        tags.remove("-")
        tags.remove("*")
        tags.remove("/")
    }

    fun cloneBesidesMaps(newNamespaceCode: String) = copy(
            code = concatNamespaceAndCode(newNamespaceCode, code),
            namespaceCode = newNamespaceCode,
            rulePacks = rulePacks?.map { it.copy(namespaceCode = newNamespaceCode, rulePackCode = concatNamespaceAndCode(newNamespaceCode, it.rulePackCode)) }
    )
}

data class TaskSetITR(
        val code: String? = null,
        val version: Int = 0,
        val namespaceCode: String? = null,
        val nameEn: String? = null,
        val nameRu: String? = null,
        val descriptionShortEn: String? = null,
        val descriptionShortRu: String? = null,
        val descriptionEn: String? = null,
        val descriptionRu: String? = null,

        val subjectType: String = "standard_math",
        val tags: MutableSet<String> = mutableSetOf(),
        val recommendedByCommunity: Boolean? = null,
        val otherData: Map<String, Any>? = null,
        val tasks: List<TaskITR>? = null
) {
    init {
        if (tasks != null) {
            for (i in 0..tasks.lastIndex) {
                if (tasks[i].code.isNullOrBlank()) {
                    tasks[i].code = code + taskSetCodeAndCodeDivider + "${i + 1}_${tasks[i].nameEn?.toCustomCodeSuffixPart() ?: "x"}"
                }
                if (tasks[i].nameRu?.startsWith("Уровень") == false) {
                    tasks[i].nameRu = "Уровень ${i + 1} ${tasks[i].nameRu}"
                }
                if (tasks[i].nameEn?.startsWith("Level") == false) {
                    tasks[i].nameEn = "Level ${i + 1} ${tasks[i].nameEn}"
                }

            }
        }
    }

    fun cloneBesidesMaps(newNamespaceCode: String) = copy(
            code = concatNamespaceAndCode(newNamespaceCode, code),
            namespaceCode = newNamespaceCode,
            tasks = tasks?.map { it.cloneBesidesMaps(newNamespaceCode = newNamespaceCode) }
    )
}

data class RulePackLinkITR(
        val namespaceCode: String? = null,
        val rulePackCode: String? = null
)

data class RulePackITR(
        val code: String? = null,
        val version: Int = 0,
        val namespaceCode: String? = null,
        val nameEn: String? = null,
        val nameRu: String? = null,
        val descriptionShortEn: String? = null,
        val descriptionShortRu: String? = null,
        val descriptionEn: String? = null,
        val descriptionRu: String? = null,

        val subjectType: String = "standard_math",
        val rulePacks: List<RulePackLinkITR>? = null,
        val rules: List<RuleITR>? = null,

        val otherCheckSolutionData: Map<String, Any>? = null,
        val otherAutoGenerationData: Map<String, Any>? = null,
        val otherData: Map<String, Any>? = null
) {
    fun cloneBesidesMaps(newNamespaceCode: String) = copy(
            code = concatNamespaceAndCode(newNamespaceCode, code),
            namespaceCode = newNamespaceCode,
            rulePacks = rulePacks?.map { it.copy(namespaceCode = newNamespaceCode, rulePackCode = concatNamespaceAndCode(newNamespaceCode, it.rulePackCode)) },
            rules = rules?.map { it.copy() }
    )
}

data class GameITR(
        val taskSet: TaskSetITR,
        val rulePacks: List<RulePackITR>
)