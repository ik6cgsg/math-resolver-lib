package mathhelper.twf.baseoperations

import mathhelper.twf.api.stringToExpression
import mathhelper.twf.config.FunctionIdentifier
import mathhelper.twf.expressiontree.ExpressionNode
import mathhelper.twf.expressiontree.NodeType
import mathhelper.twf.platformdependent.toShortString
import kotlin.math.pow

data class BaseOperationsDefinition(
        val name: String,
        val numberOfArguments: Int,
        val calculatingFunction: (ExpressionNode) -> ExpressionNode
)

enum class ComputeExpressionVariableType {
    ALL_TO_NUMBERS, // вычислять в том числе числовые значения констант
    ALL_TO_NUMBERS_OR_PI_OR_E, // оставлять константы
    ALL_TO_NUMBERS_OR_PI_OR_E_ADDITIVELY_GROUPED // оставлять и группировать константы, например pi + pi = 2 * pi
}

class BaseOperationsDefinitions(val epsilon: Double = 11.9e-6, var computeExpressionVariableType: ComputeExpressionVariableType = ComputeExpressionVariableType.ALL_TO_NUMBERS) {
    val baseOperationsDefinitions = listOf<BaseOperationsDefinition>(
            BaseOperationsDefinition("", 0, { argsParentNode: ExpressionNode -> emptyBrackets(argsParentNode) }),
            BaseOperationsDefinition("", 1, { argsParentNode: ExpressionNode -> brackets(argsParentNode) }),
            BaseOperationsDefinition("+", -1, { argsParentNode: ExpressionNode -> plus(argsParentNode) }),
            BaseOperationsDefinition("*", -1, { argsParentNode: ExpressionNode -> mul(argsParentNode) }),
            BaseOperationsDefinition("-", -1, { argsParentNode: ExpressionNode -> minus(argsParentNode) }),
            BaseOperationsDefinition("/", -1, { argsParentNode: ExpressionNode -> div(argsParentNode) }),
            BaseOperationsDefinition("^", -1, { argsParentNode: ExpressionNode -> pow(argsParentNode) }),
            BaseOperationsDefinition("mod", 2, { argsParentNode: ExpressionNode -> mod(argsParentNode) }),
            BaseOperationsDefinition("S", 4, { argsParentNode: ExpressionNode -> sumN(argsParentNode) }),
            BaseOperationsDefinition("P", 4, { argsParentNode: ExpressionNode -> prodN(argsParentNode) }),

            BaseOperationsDefinition("and", -1, { argsParentNode: ExpressionNode -> and(argsParentNode) }),
            BaseOperationsDefinition("or", -1, { argsParentNode: ExpressionNode -> or(argsParentNode) }),
            BaseOperationsDefinition("xor", -1, { argsParentNode: ExpressionNode -> xor(argsParentNode) }),
            BaseOperationsDefinition("alleq", -1, { argsParentNode: ExpressionNode -> alleq(argsParentNode) }),
            BaseOperationsDefinition("not", 1, { argsParentNode: ExpressionNode -> not(argsParentNode) }),

            BaseOperationsDefinition("sin", 1, { argsParentNode: ExpressionNode -> sin(argsParentNode) }),
            BaseOperationsDefinition("cos", 1, { argsParentNode: ExpressionNode -> cos(argsParentNode) }),
            BaseOperationsDefinition("sh", 1, { argsParentNode: ExpressionNode -> sinh(argsParentNode) }),
            BaseOperationsDefinition("ch", 1, { argsParentNode: ExpressionNode -> cosh(argsParentNode) }),
            BaseOperationsDefinition("tg", 1, { argsParentNode: ExpressionNode -> tan(argsParentNode) }),
            BaseOperationsDefinition("th", 1, { argsParentNode: ExpressionNode -> tanh(argsParentNode) }),
            BaseOperationsDefinition("asin", 1, { argsParentNode: ExpressionNode -> asin(argsParentNode) }),
            BaseOperationsDefinition("acos", 1, { argsParentNode: ExpressionNode -> acos(argsParentNode) }),
            BaseOperationsDefinition("atg", 1, { argsParentNode: ExpressionNode -> atan(argsParentNode) }),
            BaseOperationsDefinition("actg", 1, { argsParentNode: ExpressionNode -> actan(argsParentNode) }),
            BaseOperationsDefinition("exp", 1, { argsParentNode: ExpressionNode -> exp(argsParentNode) }),
            BaseOperationsDefinition("ln", 1, { argsParentNode: ExpressionNode -> ln(argsParentNode) }),
            BaseOperationsDefinition("abs", 1, { argsParentNode: ExpressionNode -> abs(argsParentNode) })
    )

    val mapNameAndArgsNumberToOperation = baseOperationsDefinitions.associateBy { FunctionIdentifier.getIdentifier(it.name, it.numberOfArguments) }
    val definedFunctionNameNumberOfArgsSet = mapNameAndArgsNumberToOperation.keys.toSet()

    fun getOperation(name: String, numberOfArguments: Int): BaseOperationsDefinition? {
        val baseOperationsDefinition = mapNameAndArgsNumberToOperation.get(FunctionIdentifier.getIdentifier(name, numberOfArguments))
        if (baseOperationsDefinition == null)
            return mapNameAndArgsNumberToOperation.get(FunctionIdentifier.getIdentifier(name, -1))
        else return baseOperationsDefinition
    }

    fun applyOperationToExpressionNode(node: ExpressionNode): ExpressionNode {
        if (node.value.isEmpty()) { // TODO: duplication is correct?
            val operation = getOperation(node.value, node.children.size)
            if (operation != null) {
                return operation.calculatingFunction(node)
            }
        }
        if (node.children.size > 0) { // TODO: duplication is correct?
            val operation = getOperation(node.value, node.children.size)
            if (operation != null) {
                return operation.calculatingFunction(node)
            }
        }
        return node
    }

    fun computeExpressionTree(node: ExpressionNode): ExpressionNode {
        for (child in node.children) {
            computeExpressionTree(child)
        }
        return applyOperationToExpressionNode(node)
    }

    fun sumN(argsParentNode: ExpressionNode): ExpressionNode {
        var low = argsParentNode.children[1].value.toDoubleOrNull() ?: return argsParentNode
        val up = argsParentNode.children[2].value.toDoubleOrNull() ?: return argsParentNode
        if (up < low) {
            argsParentNode.setVariable("0")
        } else {
            val counterName = argsParentNode.children[0].value
            var result = 0.0
            while (low <= up) {
                val expr = argsParentNode.children[3].cloneWithVariableReplacement(mutableMapOf(Pair(counterName, low.toString())))
                result += computeExpressionTree(expr).value.toDoubleOrNull() ?: return argsParentNode
                low += 1.0
            }
            argsParentNode.setVariable(result)
        }
        return argsParentNode
    }

    fun prodN(argsParentNode: ExpressionNode): ExpressionNode {
        var low = argsParentNode.children[1].value.toDoubleOrNull() ?: return argsParentNode
        val up = argsParentNode.children[2].value.toDoubleOrNull() ?: return argsParentNode
        if (up < low) {
            argsParentNode.setVariable("1")
        } else {
            val counterName = argsParentNode.children[0].value
            var result = 1.0
            var flagHasVariable = false
            while (low <= up) {
                val expr = argsParentNode.children[3].cloneWithVariableReplacement(mutableMapOf(Pair(counterName, low.toString())))
                val arg = computeExpressionTree(expr).value.toDoubleOrNull()
                if (arg == null) {
                    flagHasVariable = true
                } else {
                    result *= arg
                    if (result.additivelyEqualToZero()) {
                        argsParentNode.setVariable("0")
                        break
                    }
                }
                low += 1.0
            }
            if (!flagHasVariable)
                argsParentNode.setVariable(result)
        }
        return argsParentNode
    }

    fun plus(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 0.0
        val counts = mutableMapOf<String, Int>()
        for (argNode in argsParentNode.children) {
            if (argNode.children.isNotEmpty()) continue
            val argValue = argNode.getComputeNodeValue(computeExpressionVariableType).toDoubleOrNull()
            if (argValue != null) {
                result += argValue
                argNode.nodeType = NodeType.EMPTY
            } else if (argNode.children.size == 0 && computeExpressionVariableType == ComputeExpressionVariableType.ALL_TO_NUMBERS_OR_PI_OR_E_ADDITIVELY_GROUPED) {
                val nodeValueStr = argNode.getComputeNodeValue(computeExpressionVariableType)
                counts[nodeValueStr] = (counts[nodeValueStr] ?: 0) + 1
                argNode.nodeType = NodeType.EMPTY
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        for ((constant, count) in counts) {
            if (count > 1) {
                argsParentNode.addChild(stringToExpression("$count*$constant"))
            } else {
                argsParentNode.addChild(stringToExpression(constant).children[0])
            }
        }
        if (argsParentNode.children.size == 0) argsParentNode.setVariable(result)
        else if (result.additivelyEqualToZero()) {
            if (argsParentNode.children.size == 1) argsParentNode.setNode(argsParentNode.children[0])
        } else if (startSize > argsParentNode.children.size) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toString()))
        return argsParentNode
    }

    fun minus(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 0.0
        val first = argsParentNode.children[0].getComputeNodeValue(computeExpressionVariableType).toDoubleOrNull()
        val firstIsNumber = ((first != null) && (argsParentNode.children[0].children.size == 0))
        if (firstIsNumber) result = first!!
        if (argsParentNode.children.size == 1) {
            if (firstIsNumber) {
                argsParentNode.setVariable((-result))
            }
            return argsParentNode
        } else {
            for (i in 1 until argsParentNode.children.size) {
                if (argsParentNode.children[i].children.isNotEmpty()) continue
                val argValue = argsParentNode.children[i].getComputeNodeValue(computeExpressionVariableType).toDoubleOrNull()
                if (argValue != null) {
                    if (firstIsNumber) result -= argValue
                    else result += argValue
                    argsParentNode.children[i].nodeType = NodeType.EMPTY
                }
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 1) {
            if (firstIsNumber) argsParentNode.setVariable(result)
            else if (startSize > argsParentNode.children.size) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        } else {
            if (firstIsNumber) argsParentNode.children[0].setVariable(result)
            else if (!result.additivelyEqualToZero() && (startSize > argsParentNode.children.size)) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        }
        return argsParentNode
    }

    fun and(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 1.0
        for (argNode in argsParentNode.children) {
            if (argNode.children.isNotEmpty()) continue
            val argValue = argNode.value.toDoubleOrNull()
            if (argValue != null) {
                result *= argValue
                argNode.nodeType = NodeType.EMPTY
                if (result.additivelyEqualToZero()) {
                    argsParentNode.setVariable("0")
                    return argsParentNode
                }
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 0) argsParentNode.setVariable(result)
        else if (result.additivelyEqualTo(1.0)) {
            if (argsParentNode.children.size == 1) argsParentNode.setNode(argsParentNode.children[0])
        }
        return argsParentNode
    }

    fun or(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 0.0
        for (argNode in argsParentNode.children) {
            if (argNode.children.isNotEmpty()) continue
            val argValue = argNode.value.toDoubleOrNull()
            if (argValue != null) {
                result += argValue
                argNode.nodeType = NodeType.EMPTY
                if (!result.additivelyEqualToZero()) {
                    argsParentNode.setVariable("1")
                    return argsParentNode
                }
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 0) argsParentNode.setVariable(result)
        else if (result.additivelyEqualTo(1.0)) {
            if (argsParentNode.children.size == 1) argsParentNode.setNode(argsParentNode.children[0])
        }
        return argsParentNode
    }

    fun xor(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 0.0
        for (argNode in argsParentNode.children) {
            if (argNode.children.isNotEmpty()) continue
            val argValue = argNode.value.toDoubleOrNull()
            if (argValue != null) {
                result = result.addMod2(argValue)
                argNode.nodeType = NodeType.EMPTY
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 0) argsParentNode.setVariable(result)
        else if (result.additivelyEqualToZero()) {
            if (argsParentNode.children.size == 1) argsParentNode.setNode(argsParentNode.children[0])
        } else {
            argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        }
        return argsParentNode
    }

    fun alleq(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 0.5
        for (argNode in argsParentNode.children) {
            if (argNode.children.isNotEmpty()) continue
            val argValue = argNode.value.toDoubleOrNull()
            if (argValue != null) {
                val addArg = if (argValue.additivelyEqualToZero()) 0.0 else 1.0
                if (result == 0.5) result = addArg
                else if (!result.additivelyEqualTo(addArg)) {
                    argsParentNode.setVariable("0")
                    return argsParentNode
                }
                argNode.nodeType = NodeType.EMPTY
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 0) argsParentNode.setVariable("1")
        else if (result != 0.5) {
            argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        }
        return argsParentNode
    }

    fun Double.addMod2(arg: Double): Double {
        if (this.additivelyEqualToZero()) return arg
        else return arg.not()
    }

    fun mul(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 1.0
        val counts = mutableMapOf<String, Int>()
        for (argNode in argsParentNode.children) {
            if (argNode.children.isNotEmpty()) continue
            val argValue = argNode.getComputeNodeValue(computeExpressionVariableType).toDoubleOrNull()
            if (argValue != null) {
                result *= argValue
                argNode.nodeType = NodeType.EMPTY
                if (result.additivelyEqualToZero()) {
                    argsParentNode.setVariable("0")
                    return argsParentNode
                }
            } else if (argNode.children.size == 0 && computeExpressionVariableType == ComputeExpressionVariableType.ALL_TO_NUMBERS_OR_PI_OR_E_ADDITIVELY_GROUPED) {
                val nodeValueStr = argNode.getComputeNodeValue(computeExpressionVariableType)
                counts[nodeValueStr] = (counts[nodeValueStr] ?: 0) + 1
                argNode.nodeType = NodeType.EMPTY
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        for ((constant, count) in counts) {
            if (count > 1) {
                argsParentNode.addChild(stringToExpression("$constant^$count"))
            } else {
                argsParentNode.addChild(stringToExpression(constant).children[0])
            }
        }
        if (argsParentNode.children.size == 0) argsParentNode.setVariable(result)
        else if (result.additivelyEqualTo(1.0)) {
            if (argsParentNode.children.size == 1) argsParentNode.setNode(argsParentNode.children[0])
        } else if (startSize > argsParentNode.children.size) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        return argsParentNode
    }

    fun div(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 1.0
        val first = argsParentNode.children[0].getComputeNodeValue(computeExpressionVariableType).toDoubleOrNull()
        val firstIsNumber = ((first != null) && (argsParentNode.children[0].children.size == 0))
        if (firstIsNumber) result = first!!
        if (argsParentNode.children.size == 1) {
            if (firstIsNumber)
                argsParentNode.setVariable((1 / result))
            return argsParentNode
        } else {
            for (i in 1 until argsParentNode.children.size) {
                if (argsParentNode.children[i].children.isNotEmpty()) continue
                val argValue = argsParentNode.children[i].getComputeNodeValue(computeExpressionVariableType).toDoubleOrNull()
                if (argValue != null) {
                    if (firstIsNumber) result /= argValue
                    else result *= argValue
                    argsParentNode.children[i].nodeType = NodeType.EMPTY
                }
            }
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 1) {
            if (firstIsNumber) argsParentNode.setVariable(result)
            else if (startSize > argsParentNode.children.size) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        } else {
            if (firstIsNumber) argsParentNode.children[0].setVariable(result)
            else if (!result.additivelyEqualTo(1.0) && (startSize > argsParentNode.children.size)) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toString()))
        }
        return argsParentNode
    }

    fun pow(argsParentNode: ExpressionNode): ExpressionNode {
        val startSize = argsParentNode.children.size
        var result = 1.0
        for (i in argsParentNode.children.lastIndex downTo 0) {
            if (argsParentNode.children[i].children.isNotEmpty()) {
                break
            }
            val argValue = argsParentNode.children[i].value.toDoubleOrNull()
            if (argValue != null) {
                result = argValue.pow(result)
                argsParentNode.children[i].nodeType = NodeType.EMPTY
            } else if (result.additivelyEqualToZero()) {
                result = 1.0
                argsParentNode.children[i].nodeType = NodeType.EMPTY
            } else break
        }
        argsParentNode.children.removeAll({ it.nodeType == NodeType.EMPTY })
        if (argsParentNode.children.size == 0) argsParentNode.setVariable(result)
        else if (result.additivelyEqualToZero()) {
            if (argsParentNode.children.size == 1) argsParentNode.setVariable("1")
        } else if (result.additivelyEqualTo(1.0)) {
//            if (argsParentNode.children.size == 1) argsParentNode.setNode(argsParentNode.children[0])
        } else if (startSize > argsParentNode.children.size) argsParentNode.addChild(ExpressionNode(NodeType.VARIABLE, result.toShortString()))
        return argsParentNode
    }

    fun mod(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        val secondArg: Double?
        if (firstArg.additivelyEqualToZero()) {
            argsParentNode.setVariable("0")
        } else {
            secondArg = argsParentNode.children[1].value.toDoubleOrNull() ?: return argsParentNode
            argsParentNode.setVariable(firstArg.rem(secondArg))
        }
        return argsParentNode
    }

    fun sin(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.sin(firstArg))
        return argsParentNode
    }

    fun cos(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.cos(firstArg))
        return argsParentNode
    }

    fun tan(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.tan(firstArg))
        return argsParentNode
    }

    fun asin(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.asin(firstArg))
        return argsParentNode
    }

    fun acos(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.acos(firstArg))
        return argsParentNode
    }

    fun atan(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.atan(firstArg))
        return argsParentNode
    }

    fun actan(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.atan(1.0 / firstArg))
        return argsParentNode
    }

    fun sinh(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.sinh(firstArg))
        return argsParentNode
    }

    fun cosh(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.cosh(firstArg))
        return argsParentNode
    }

    fun tanh(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.tanh(firstArg))
        return argsParentNode
    }

    fun exp(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.exp(firstArg))
        return argsParentNode
    }

    fun ln(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.log(firstArg, kotlin.math.E))
        return argsParentNode
    }

    fun abs(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(kotlin.math.abs(firstArg))
        return argsParentNode
    }

    fun not(argsParentNode: ExpressionNode): ExpressionNode {
        val firstArg = argsParentNode.children[0].value.toDoubleOrNull() ?: return argsParentNode
        argsParentNode.setVariable(firstArg.not())
        return argsParentNode
    }

    fun Double.not() = if (this.additivelyEqualToZero()) 1.0 else 0.0

    fun brackets(argsParentNode: ExpressionNode): ExpressionNode {
        if (argsParentNode.children[0].children.size == 0) {
            argsParentNode.setVariable(argsParentNode.children[0].value)
            return argsParentNode
        } else {
            return argsParentNode.children[0]
        }
    }

    fun emptyBrackets(argsParentNode: ExpressionNode): ExpressionNode {
        argsParentNode.setVariable("0")
        return argsParentNode
    }

    private fun Double.additivelyEqualTo(number: Double, eps: Double = epsilon) = (this - number).additivelyEqualToZero(eps)
    private fun Double.additivelyEqualToZero(eps: Double = epsilon) = (this in (-eps)..eps)

    fun additivelyEqual(l: Double, r: Double, eps: Double = epsilon) = (l == r || l.additivelyEqualTo(r, eps))

}