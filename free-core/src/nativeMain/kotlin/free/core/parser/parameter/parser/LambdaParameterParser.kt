package free.core.parser.parameter.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.node.TypeReferenceParser
import free.core.parser.parameter.Parameter

context(_: FreeContext)
fun parseLambdaParameters(ctx: FreeParserContext): List<Parameter> {
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += LambdaParameterParser(ctx).parse()
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.expect(FreeTokenType.COMMA, "函数参数缺少 ','")
		}
	}
	return parameters
}

private class LambdaParameterParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): Parameter {
		val name = if (ctx.peek(offset = 1)?.type == FreeTokenType.COLON) {
			ctx.expect(FreeTokenType.IDENTIFIER, "参数缺少名称")
			ctx.previous.value.also {
				ctx.expect(FreeTokenType.COLON, "参数缺少 ':'")
			}
		} else ""
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = emptySet(),
			typeReference = typeReference,
		)
	}
}