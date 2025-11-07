package free.core.parser

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
	
	/**
	 * 前进
	 */
	fun advance(): FreeToken {
		if (position < tokens.size - 1) {
			position++
		}
		return current
	}
	
	/**
	 * 匹配
	 */
	fun match(type: FreeTokenType): Boolean {
		if (check(type)) {
			advance()
			return true
		}
		return false
	}
	
	/**
	 * 检查
	 */
	fun check(type: FreeTokenType): Boolean {
		return !isAtEnd() && current.type == type
	}
	
	suspend fun expect(type: FreeTokenType, errorMessage: String) {
		if (check(type)) {
			advance()
			return
		}
		syntaxError(errorMessage, current.line, current.column)
	}
	
	/**
	 * 是否结束
	 */
	fun isAtEnd(): Boolean {
		return position >= tokens.size || current.type == FreeTokenType.EOF
	}
}