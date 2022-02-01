package mathhelper.twf.expressiontree

data class ParserError(val position: Int = -1, val description: String = "", val endPosition: Int = -1)

data class GeneralError(val description: String = "", val code: String = "")

data class SubstitutionApplicationError(val descriptionEn: String = "", val descriptionRu: String = "", val affectedNodeIds: Array<Int>, val expressionNode: ExpressionNode)