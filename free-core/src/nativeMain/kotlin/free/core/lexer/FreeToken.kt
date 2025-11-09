package free.core.lexer

import free.core.FreeContext
import kotlinx.coroutines.currentCoroutineContext
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

suspend fun List<FreeToken>.formatToString(): String {
	val sourcePath = currentCoroutineContext()[FreeContext]!!.sourcePath
	val lineColumns = this.map { "$sourcePath:${it.line}:${it.column}" }
	val max = lineColumns.maxOf { it.length }
	val tokens = this
	return buildString {
		tokens.forEachIndexed { index, token ->
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