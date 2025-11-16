package free.core.parser.parameter.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.parameter.Parameter
import free.core.parser.parameter.parseParameter

context(_: FreeContext)
fun parseLambdaParameters(ctx: FreeParserContext): List<Parameter> {
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += LambdaParameterParser(ctx).parse()
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.expect(FreeTokenType.COMMA, "参数缺少 ','")
		}
	}
	return parameters
}

private class LambdaParameterParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(): Parameter {
		return parseParameter(ctx, isSupportedAnonymous = true)
	}
}