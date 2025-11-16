package free.core.parser.parameter.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.parameter.Parameter
import free.core.parser.parameter.parseParameter

context(_: FreeContext)
fun parseFunParameters(ctx: FreeParserContext): List<Parameter> {
	ctx.expect(FreeTokenType.LPAREN, "函数缺少 '('")
	if (ctx.match(FreeTokenType.RPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += FunParameterParser(ctx).parse()
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.expect(FreeTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class FunParameterParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += if (ctx.match(FreeTokenType.VAR)) Modifier.VAR else Modifier.VAL
		return parseParameter(ctx, modifiers)
	}
}