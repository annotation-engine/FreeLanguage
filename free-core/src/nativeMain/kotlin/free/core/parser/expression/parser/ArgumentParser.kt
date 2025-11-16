package free.core.parser.expression.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.expression.Argument
import free.core.parser.expression.InvokeType
import free.core.parser.expression.InvokeType.CALL
import free.core.parser.expression.InvokeType.INDEX_ACCESS
import free.core.parser.expression.matcher.parseCompleteExpression

context(_: FreeContext)
fun parseArguments(ctx: FreeParserContext, type: InvokeType): List<Argument> {
	if (ctx.match(type.endTokenType)) return emptyList()
	val arguments = mutableListOf<Argument>()
	do {
		arguments += CallArgumentParser(ctx).parse(type)
		if (!ctx.check(type.endTokenType)) {
			ctx.expect(FreeTokenType.COMMA, "参数缺少 ','")
		}
	} while (!ctx.match(type.endTokenType))
	return arguments
}

private class CallArgumentParser(
	private val ctx: FreeParserContext
) {
	context(_: FreeContext)
	fun parse(type: InvokeType): Argument {
		val name = if (ctx.peek(offset = 1)?.type == FreeTokenType.COLON) {
			ctx.expect(FreeTokenType.IDENTIFIER, "型参名称必须为标识符")
			val name = ctx.previous.value
			ctx.expect(FreeTokenType.COLON, "型参名称后必须为 ':'")
			name
		} else null
		val expression = parseCompleteExpression(ctx)
		if (ctx.previous.type == FreeTokenType.SEMICOLON) {
			syntaxError("语法错误", ctx.previous)
		}
		val currentType = ctx.current.type
		if (currentType == FreeTokenType.COMMA) {
			return Argument(name, expression)
		}
		when (type) {
			CALL if (currentType == INDEX_ACCESS.endTokenType) -> syntaxError("函数参数表达式后只允许根 ')' 和 ','", ctx.current)
			INDEX_ACCESS if (currentType == CALL.endTokenType) -> syntaxError("索引访问参数表达式后只允许根 ']' 和 ','", ctx.current)
			else -> return Argument(name, expression)
		}
	}
}