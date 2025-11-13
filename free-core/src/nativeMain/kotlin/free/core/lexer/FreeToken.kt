package free.core.lexer

import free.core.FreeContext
import kotlinx.serialization.Serializable

@Serializable
data class FreeToken(
	val type: FreeTokenType,
	val value: String,
	val start: Int,
	val end: Int,
	val line: Int,
	val column: Int,
) {
	
	val length: Int
		get() = this.end - this.start
}

context(context: FreeContext)
fun List<FreeToken>.formatToString(): String {
	val lineColumns = this.map { "${context.sourcePath}:${it.line}:${it.column}" }
	val max = lineColumns.maxOf { it.length }
	return buildString {
		this@formatToString.forEachIndexed { index, token ->
			val lineColumn = lineColumns[index]
			append(lineColumn)
			append(" ".repeat(max - lineColumn.length + 2))
			if (token.type == FreeTokenType.STRING || token.value.isNotEmpty()) {
				append("${token.type}(\"${token.value}\")")
			} else {
				append(token.type)
			}
			append("\n")
		}
	}
}

fun List<FreeToken>.removeComments(): List<FreeToken> {
	return this.filter { it.type != FreeTokenType.COMMENT }
}