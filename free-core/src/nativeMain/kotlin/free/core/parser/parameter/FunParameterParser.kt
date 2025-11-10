package free.core.parser.parameter

import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.node.TypeReferenceParser

suspend fun parseFunParameters(ctx: FreeParserContext): List<Parameter> {
	ctx.expect(FreeTokenType.LPAREN, "函数缺少 '('")
	if (ctx.match(FreeTokenType.RPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += FunParameterParser(ctx).parse()
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.match(FreeTokenType.COMMA)
		}
	}
	return parameters
}

private class FunParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += if (ctx.match(FreeTokenType.VAR)) Modifier.VAR else Modifier.VAL
		ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
		val name = ctx.previous.value
		ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference
		)
	}
}