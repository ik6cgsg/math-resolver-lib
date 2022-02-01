package mathhelper.twf.standartlibextensions

import mathhelper.twf.platformdependent.abs
import kotlin.math.min

fun Char.isLowerCaseLetter() = this in 'a'..'z' || this in 'α'..'ω'
fun Char.isUpperCaseLetter() = this in 'A'..'Z' || this in 'Α'..'Ω'
fun Char.isLetter() = this.isLowerCaseLetter() || this.isUpperCaseLetter()
fun Char.isLatinLetter() = this in 'a'..'z' || this in 'A'..'Z'
fun Char.isLetterOrUnderscore() = this.isLetter() || this == '_'
fun Char.isDigit() = this in '0'..'9'
fun Char.isLetterOrDigit() = this.isLetter() || this.isDigit()
fun Char.isLetterOrDigitOrUnderscore() = this.isLetterOrDigit() || this == '_'
fun Char.isNamePart() = this.isLetterOrDigitOrUnderscore() || (this > '~' && this != '↑' && this != '↓' )
fun Char.isNameOrNumberPart() = this.isNamePart() || this == '.'
fun Char.isNumberPart() = this.isDigit() || this == '.'
fun Char.isOpenBracket() = this == '(' || this == '{' || this == '['
fun Char.isCloseBracket() = this == ')' || this == '}' || this == ']'
fun Char.isTexArgumentsSeparator() = this == '^' || this == '_' || this == ' '
fun Char.isBracket() = this.isOpenBracket() || this.isCloseBracket()

fun isUnarySignPart(c: Char): Boolean = (c == '!' || c == '#' || c == '\'')
fun isBinarySignPart(c: Char, prevC: Char, isMathML: Boolean = false, hasUpAnd: Boolean = false): Boolean = (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%' || c == '|' || c == '↑' || c == '↓' ||
        (c == '\\' && (prevC == '\\' || (hasUpAnd && prevC == '/') || !prevC.isSign(' ', isMathML))) || (c == '&' && !isMathML))
fun Char.isSign(prevC: Char, isMathML: Boolean = false) = isUnarySignPart(this) || isBinarySignPart(this, prevC, isMathML)

fun String.isWhiteSpace(index: Int) = (this[index].isWhitespace() ||
        (this[index] == '\\' && (index + 1 >= this.length || this[index + 1].isWhitespace())))

fun String.prevCharOrSpace(pos: Int) = if ((pos - 1) in 0..this.lastIndex) this[pos - 1] else ' '

fun String.removeAllExceptLatin(): String {
    val result = StringBuilder()
    for (c in this) {
        if (c.isLetterOrDigitOrUnderscore()) {
            result.append(c)
        } else if (c.isWhitespace()) {
            result.append('_')
        }
    }
    return result.toString()
}

fun String.toCustomCodeSuffixPart(): String {
    val result = StringBuilder()
    var capilizeNext = true
    for (c in this) {
        if (c.isLetterOrDigitOrUnderscore()) {
            if (capilizeNext) {
                result.append(c.toUpperCase())
                capilizeNext = false
            } else {
                result.append(c)
            }
        } else if (c.isWhitespace()) {
            capilizeNext = true
        }
    }
    return result.toString()
}

fun texStringPrefiltering(string: String): String {
    var i = 0
    val result = StringBuilder()
    while (i < string.length) {
        if (remainingExpressionStartsWith("\\operatorname{", string, i)) {
            i += "\\operatorname{".length
            while (i < string.length && string[i] != '}'){
                result.append(string[i])
                i++
            }
        } else {
            result.append(string[i])
        }
        i++
    }
    return result.toString()
}

fun remainingExpressionStartsWith(string: String, expression: String, currentPosition: Int): Boolean {
    if (currentPosition < 0) {
        return false
    }
    for (i in 0 until string.length)
        if (currentPosition + i >= expression.length || expression[currentPosition + i] != string[i])
            return false
    return true
}

fun readFromRemainingExpressionWhile(symbolCondition: (Char) -> Boolean, expression: String, currentPosition: Int): String {
    var value = ""
    var pos = currentPosition
    while (pos < expression.length) {
        if (!symbolCondition(expression[pos]))
            break
        value += expression[pos]
        pos++
    }
    return value
}

fun skipFromRemainingExpressionWhile(symbolCondition: (Char) -> Boolean, expression: String, currentPosition: Int): Int {
    var pos = currentPosition
    while (pos < expression.length) {
        if (!symbolCondition(expression[pos]))
            break
        pos++
    }
    return pos
}

fun skipFromRemainingExpressionWhileClosingTagNotFound(closingTag: String, expression: String, currentPosition: Int): Int {
    var pos = currentPosition
    var openTagsCount = 1
    while (pos < expression.length) {
        if (remainingExpressionStartsWith("<$closingTag", expression, pos))
            openTagsCount++
        else if (remainingExpressionStartsWith("</$closingTag", expression, pos))
            openTagsCount--
        if (openTagsCount == 0)
            break
        pos++
    }
    return pos
}

fun getBracketLevelChangeBeforeClosingTag(closingTag: String, expression: String, currentPosition: Int): Int {
    var pos = currentPosition
    var openTagsCount = 0
    var openBracketsCount = 0
    while (pos < expression.length) {
        if (remainingExpressionStartsWith("<$closingTag", expression, pos))
            openTagsCount++
        else if (remainingExpressionStartsWith("</$closingTag", expression, pos))
            openTagsCount--
        if (openTagsCount == 0)
            break
        if (expression[pos].isOpenBracket()) {
            openBracketsCount++
        } else if (expression[pos].isCloseBracket()) {
            openBracketsCount--
        }
        pos++
    }
    return openBracketsCount
}

fun findFirstNotInTagNotInMtext(string: String, startPosition: Int): Int {
    for (i in startPosition..string.lastIndex) {
        if (string[i] == '<') {
            if (remainingExpressionStartsWith("</mtext>", string, i)) {
                for (j in startPosition downTo 0) {
                    if (string[j] == '>') {
                        return (j - "<mtext".length)
                    }
                }
            }
            return startPosition
        } else if (string[i] == '>') {
            if (remainingExpressionStartsWith("<mtext>", string, i - "<mtext".length)) {
                return i - "<mtext".length
            }
            return i + 1
        }
    }
    return string.length
}

fun skipClosingTags(string: String, startPosition: Int): Int {
    var currentStartPosition = startPosition
    while (remainingExpressionStartsWith("</", string, currentStartPosition)) {
        while (string[currentStartPosition] != '>') {
            currentStartPosition++
        }
        currentStartPosition++
    }
    return currentStartPosition
}

fun selectPlacesForColoringByFragment(mathML: String, approxStartPosition: Int, approxEndPosition: Int): Pair<Int, Int> {
    var startPosition = approxStartPosition
    var endPosition = approxEndPosition
    while (startPosition >= 0 && mathML[startPosition] != '<' && mathML[startPosition] != '>')
        startPosition--
    if (mathML[startPosition] == '>')
        startPosition++
    while (endPosition < mathML.length && mathML[endPosition] != '<' && mathML[endPosition] != '>')
        endPosition++
    if (mathML[endPosition] == '>')
        endPosition++

    var numberOfNotClosedTags = 0
    var currentStartPosition = startPosition
    while (currentStartPosition < endPosition) {
        if (mathML[currentStartPosition] == '<') {
            if (currentStartPosition + 1 < mathML.length && mathML[currentStartPosition + 1] == '/') {
                numberOfNotClosedTags--
            } else {
                numberOfNotClosedTags++
            }
        }
        currentStartPosition++
    }
    while (numberOfNotClosedTags > 0) {
        while (endPosition < mathML.length && mathML[endPosition] != '<')
            endPosition++
        if (endPosition + 1 < mathML.length && mathML[endPosition + 1] == '/') {
            numberOfNotClosedTags--
        } else {
            numberOfNotClosedTags++
        }
        while (endPosition < mathML.length && mathML[endPosition] != '>')
            endPosition++
        endPosition++
    }
    while (numberOfNotClosedTags < 0) {
        startPosition--
        while (startPosition >= 0 && mathML[startPosition] != '<')
            startPosition--
        if (startPosition + 1 < mathML.length && mathML[startPosition + 1] == '/') {
            numberOfNotClosedTags--
        } else {
            numberOfNotClosedTags++
        }
    }
    return Pair(startPosition, endPosition)
}

fun findClosestPlaceToTargetOnTheSameLevel(string: String, startPosition: Int, targetEndPosition: Int, isMathML: Boolean = true): Int {
    var pos = startPosition
    var openTagsCount = 0
    var inTag = false
    var minDist = Int.MAX_VALUE
    var currentResultPos = pos
    val maxEnd = min(string.length, targetEndPosition + targetEndPosition - startPosition)
    while (pos < maxEnd) {
        if (isMathML && remainingExpressionStartsWith("</", string, pos)) {
            openTagsCount--
            pos++
            inTag = true
        } else if (isMathML && string[pos] == '<') {
            openTagsCount++
            inTag = true
        } else if (isMathML && string[pos] == '>') {
            inTag = false
        } else if (string[pos].isOpenBracket()) {
            openTagsCount++
        } else if (string[pos].isCloseBracket()) {
            openTagsCount--
        } else if (!isMathML && remainingExpressionStartsWith("\\left(", string, pos)) {
            openTagsCount++
            pos += "\\left".length
        } else if (!isMathML && remainingExpressionStartsWith("\\right)", string, pos)) {
            openTagsCount--
            pos += "\\right".length
        }
        pos++
        if (openTagsCount == 0 && !inTag) {
            val currentDist = abs(targetEndPosition - pos)
            if (currentDist < minDist) {
                minDist = currentDist
                currentResultPos = pos
            } else if (pos > targetEndPosition) {
                break
            }
        } else if (openTagsCount < 0) {
            return currentResultPos
        }
    }
    return currentResultPos
}

fun skipFromRemainingExpressionWhileClosingBracketNotFound(closingBracket: String, openingBracket: String, expression: String, currentPosition: Int): Int {
    var pos = currentPosition
    var openTagsCount = 1
    while (pos < expression.length) {
        if (remainingExpressionStartsWith(openingBracket, expression, pos))
            openTagsCount++
        else if (remainingExpressionStartsWith(closingBracket, expression, pos))
            openTagsCount--
        if (openTagsCount == 0)
            break
        pos++
    }
    return pos
}

fun getIndexOfFirstBracketsValueEnd(string: String, startPosition: Int, openBracket: Char, closingBracket: Char): Int {
    var i = startPosition
    var numberOfOpenBrackets = 1
    while (i < string.length) {
        if (string[i] == openBracket) {
            numberOfOpenBrackets++
        } else if (string[i] == closingBracket) {
            numberOfOpenBrackets--
            if (numberOfOpenBrackets == 0) {
                return i;
            }
        }
        i++
    }
    return string.length
}

data class NamedList(
        val name: String,
        val list: List<String>,
        val endPosition: Int
)

/**
 * extracts from "...name{val1}{val2}...{valn}..." name and list of vals
 */
fun splitStringByBracketsOnTopLevel(originalString: String, openBracket: Char, closingBracket: Char, startPosition: Int = 0): NamedList {
    val string = originalString.substring(startPosition)
    val result = mutableListOf<String>()
    val firstOpenBracketIndex = string.indexOfFirst { it == openBracket }
    if (firstOpenBracketIndex < 0) {
        return NamedList(originalString, listOf(), originalString.length)
    }
    val name = string.substring(0, firstOpenBracketIndex).trim()
    var j = firstOpenBracketIndex
    var endPosition = originalString.length
    while (j < string.length && string[j] == openBracket) {
        val partStart = j + 1
        val partEnd = getIndexOfFirstBracketsValueEnd(string, partStart, '{', '}')
        if (partEnd >= string.length) {
            break
        }
        result.add(string.substring(partStart, partEnd))
        j = partEnd + 1
    }
    if (j < string.length) {
        endPosition = j + startPosition
    }
    return NamedList(name, result, endPosition)
}

data class SplittingString(
        val string: String,
        val keywordsWithStringAsPrefix: List<String> = listOf()
)

fun splitBySubstringOnTopLevel(substrings: List<SplittingString>, expression: String, startPosition: Int = 0, endPosition: Int = expression.length): List<StringPart> {
    val result = mutableListOf<StringPart>()
    var pos = startPosition
    var lastStartPos = startPosition
    var expressionNotCompleted = false
    while (pos < endPosition) {
        var splittingSubstring: String? = null
        for (substring in substrings) {
            if (remainingExpressionStartsWith(substring.string, expression, pos) &&
                    substring.keywordsWithStringAsPrefix.all { !remainingExpressionStartsWith(it, expression, pos) }) {
                splittingSubstring = substring.string
                break
            }
        }
        if (splittingSubstring != null && !expressionNotCompleted) {
            result.add(StringPart(lastStartPos, pos, splittingSubstring))
            pos += splittingSubstring.length
            lastStartPos = pos
        } else if (remainingExpressionStartsWith("<mo>", expression, pos)) {
            pos += 4
        } else if (remainingExpressionStartsWith("</mo>", expression, pos)) {
            pos += 5
        } else if (remainingExpressionStartsWith("<mrow>", expression, pos)) {
            pos += 6
        } else if (remainingExpressionStartsWith("</mrow>", expression, pos)) {
            pos += 7
        } else if (expression[pos].isOpenBracket()) {
            var numberOfOpenBrackets = 1
            expressionNotCompleted = true
            pos++
            while (pos < endPosition) {
                if (expression[pos].isOpenBracket()) numberOfOpenBrackets++
                else if (expression[pos].isCloseBracket()) numberOfOpenBrackets--
                pos++
                if (numberOfOpenBrackets == 0) {
                    expressionNotCompleted = false
                    break
                }
            }
        } else {
            val actualTag = readOpenTagStringIfItPresent(expression, pos)
            if (actualTag != null) {
                pos += actualTag.length
                if (!actualTag.endsWith("/>")) {
                    val tagName = getTagAttributes(actualTag)["name"]!!
                    pos = skipFromRemainingExpressionWhileClosingTagNotFound(tagName, expression, pos) + tagName.length + StringExtension.closing.length
                }
            } else pos++
        }
    }
    if (!expressionNotCompleted) {
        result.add(StringPart(lastStartPos, pos))
    }
    return result
}

fun readOpenTagStringIfItPresent(expression: String, currentPosition: Int): String? {
    if (expression[currentPosition] == '<' && expression.length > (currentPosition + 1) && expression[currentPosition + 1].isNamePart()) {
        val end = skipFromRemainingExpressionWhile({ it != '>' }, expression, currentPosition)
        if (end >= expression.length || expression[end] != '>') return null
        return expression.substring(currentPosition, end + 1)
    } else return null
}

fun String.trimmedMathML(): String {
    var startPosition = 0
    while (startPosition < this.length) {
        if (remainingExpressionStartsWith(StringExtension.newWhiteSpace, this, startPosition)) {
            startPosition += StringExtension.newWhiteSpace.length
        } else if (remainingExpressionStartsWith(StringExtension.newLineMspace, this, startPosition)) {
            startPosition += StringExtension.newLineMspace.length
        } else if (this[startPosition].isWhitespace()) {
            startPosition += 1
        } else {
            break
        }
    }
    var endPosition = this.length
    while (endPosition > 0) {
        if (remainingExpressionStartsWith(StringExtension.newWhiteSpace, this, endPosition - StringExtension.newWhiteSpace.length)) {
            endPosition -= StringExtension.newWhiteSpace.length
        } else if (remainingExpressionStartsWith(StringExtension.newLineMspace, this, endPosition - StringExtension.newLineMspace.length)) {
            endPosition -= StringExtension.newLineMspace.length
        } else if (this[endPosition-1].isWhitespace()) {
            endPosition -= 1
        } else {
            break
        }
    }
    return if (endPosition < startPosition) ""
    else this.substring(startPosition, endPosition)
}

fun getTagAttributes(tagString: String): Map<String, String> {
    val data = tagString.substringAfter('<').substringBefore('>')
    val attrValues = data.split(' ')
    val result = mutableMapOf<String, String>(Pair("name", attrValues[0]))
    for (i in 1..attrValues.lastIndex) {
        val attrValue = attrValues[i].split('=')
        result.put(attrValue[0], attrValue[1])
    }
    return result
}

fun removeNewLinesFromExpression(expression: String) =
        expression.replace("<mspace linebreak=\"newline\"/>", "") //If I use here variable 'StringExtension.newLineMspace' instead of "<mspace linebreak=\"newline\"/>", after compiling into js TypeError in function replace happens. TODO: ask kotlin support about this

fun findNewLinePlaces(expression: String): List<Int> {
    val result = mutableListOf<Int>()
    var startPosition = 0
    while (startPosition < expression.length) {
        val pos = expression.indexOf(StringExtension.newLineMspace, startPosition)
        if (pos < 0) {
            break
        }
        result.add(pos)
        startPosition = pos + 1
    }
    return result
}

class StringExtension {
    companion object {
        val mtable = "mtable"
        val mfenced = "mfenced"
        val closing = "</>"
        val newLineMspace = "<mspace linebreak=\"newline\"/>"
        val newWhiteSpace = "<mo>&#xA0;</mo>"
        val openCommentMathML = "<mo>/</mo><mo>*</mo>"
        val openCommentShort = "/*"
        val closeCommentMathML = "<mo>*</mo><mo>/</mo>"
        val closeCommentShort = "*/"
    }
}
