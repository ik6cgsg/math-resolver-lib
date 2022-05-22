package mathhelper.utility.math_resolver_lib

enum class OperationType(val names: Array<String>) {
    POW(arrayOf("^")),
    DIV(arrayOf("/")),
    MINUS(arrayOf("-")),
    PLUS(arrayOf("+")),
    MULT(arrayOf("*")),
    LOG(arrayOf("log")),
    FUNCTION(arrayOf("")),
    RIGHT_UNARY(arrayOf("")),
    AND(arrayOf("&", "and")),
    OR(arrayOf("|", "or")),
    XOR(arrayOf("^", "xor")),
    SET_MINUS(arrayOf("\\", "set-")),
    NOT(arrayOf("!", "not")),
    IMPLIC(arrayOf("->", "implic")),
    ALLEQ(arrayOf("==", "alleq"))
}



class Operation(var name: String) {
    val priority: Int
    val type: OperationType

    init {
        if (name == "factorial") {
            name = "!"
            type = OperationType.RIGHT_UNARY
            priority = 5
        } else {
            priority = getPriority(name)
            val types = OperationType.values().filter {
                name in it.names
            }
            type = if (types.isEmpty()) {
                OperationType.FUNCTION
            } else {
                types[0]
            }
        }
    }

    companion object {
        fun getPriority(name: String): Int {
            return when (name) {
                in OperationType.POW.names -> 4
                in OperationType.DIV.names -> 3
                in OperationType.MULT.names -> 2
                in OperationType.MINUS.names -> 1
                in OperationType.PLUS.names -> 0
                // set/logic
                in OperationType.AND.names -> 4
                in OperationType.OR.names -> 3
                in OperationType.XOR.names -> 3
                in OperationType.SET_MINUS.names -> 2
                in OperationType.IMPLIC.names -> 1
                in OperationType.ALLEQ.names -> 0
                else -> -1
            }
        }
    }
}