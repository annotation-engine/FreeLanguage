package free.core.parser.parameter

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.getDefaultMemberAccessModifier
import free.core.parser.node.TypeReferenceParser

context(_: FreeContext)
fun parseStructParameters(ctx: FreeParserContext, structAccess: Modifier): List<Parameter> {
	ctx.expect(FreeTokenType.LPAREN, "结构体缺少 '('")
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += StructParameterParser(ctx).parse(structAccess)
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.match(FreeTokenType.COMMA)
		}
	}
	return parameters
}

private class StructParameterParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(structAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getDefaultMemberAccessModifier(structAccess)
		if (ctx.match(FreeTokenType.IGNORE)) {
			modifiers += Modifier.IGNORE
		}
		modifiers += when {
			ctx.match(FreeTokenType.VAR) -> Modifier.VAR
			ctx.match(FreeTokenType.VAL) -> Modifier.VAL
			else -> syntaxError("结构体参数必须添加 'var' 或 'val' 修饰符", ctx.current)
		}
		ctx.expect(FreeTokenType.IDENTIFIER, "结构体参数缺少名称")
		val name = ctx.previous.value
		ctx.expect(FreeTokenType.COLON, "结构体参数缺少 ':'")
		val typeReference = TypeReferenceParser(ctx).parse(isSupportedLambda = false)
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference,
		)
	}
}