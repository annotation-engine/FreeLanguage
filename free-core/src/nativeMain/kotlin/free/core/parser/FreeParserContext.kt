package free.core.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeToken
import free.core.lexer.FreeTokenType
import free.core.lexer.removeComments

class FreeParserContext(
	rawTokens: List<FreeToken>
) {
	
	private val tokens = rawTokens.removeComments()
	
	private var position = 0
	
	val current: FreeToken
		get() = this.tokens[position]
	
	val previous: FreeToken
		get() = this.tokens[position - 1]
	
	val next: FreeToken
		get() = this.tokens[position + 1]
	
	fun peek(offset: Int): FreeToken? {
		return tokens.getOrNull(position + offset)
	}
	
	fun advance(): FreeToken {
		if (position < tokens.size - 1) {
			position++
		}
		return current
	}
	
	fun retreat(): FreeToken {
		if (position > 0) {
			position--
		}
		return current
	}
	
	fun match(type: FreeTokenType, vararg types: FreeTokenType): Boolean {
		if (!check(type)) {
			return false
		}
		types.forEachIndexed { index, type ->
			if (peek(offset = index + 1)?.type != type) {
				return false
			}
		}
		position += types.size + 1
		return true
	}
	
	fun check(type: FreeTokenType): Boolean {
		return !isAtEnd() && current.type == type
	}
	
	context(_: FreeContext)
	fun expect(type: FreeTokenType, errorMessage: String) {
		if (check(type)) {
			advance()
			return
		}
		syntaxError(errorMessage, current.line, current.column)
	}
	
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.type == FreeTokenType.EOF
	}
}