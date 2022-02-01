package mathhelper.twf.config

data class VariableReplacementRule (
        val left: String,
        val right: String
)

//val pi = "3.1415926535897932384626433832795"
val PI_STRING = "3.14159265" // short pi makes string comparison operations faster
val PI_STRING_ASCII = "pi"
val PI_STRING_UNICODE = "π"
val PI_STRING_TEX = "\\pi"

val E_STRING = "2.71828183" // 2.7182818284590452353602874713526
val E_STRING_UNICODE = "e"

val PI_STRING_USUAL = PI_STRING_UNICODE
val E_STRING_USUAL = E_STRING_UNICODE

class VariableConfiguration {
    var variableImmediateReplacementRules = listOf<VariableReplacementRule>(
            VariableReplacementRule("e", E_STRING),
            VariableReplacementRule("pi", PI_STRING),
            VariableReplacementRule("π", PI_STRING),
            VariableReplacementRule("&#x3C0", PI_STRING),
            VariableReplacementRule("&#x3C0;", PI_STRING)
    )

    var variableImmediateReplacementMap = variableImmediateReplacementRules.map { Pair(it.left, it.right) }.toMap()
}