package free.core.parser.parameter

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.getClassParameterAccessModifier
import free.core.parser.getDefaultMemberAccessModifier
import free.core.parser.node.TypeReferenceParser

suspend fun parseClassParameters(
	ctx: FreeParserContext,
	classAccess: Modifier
): List<Parameter> {
	if (!ctx.match(FreeTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += ClassParameterParser(ctx).parse(classAccess)
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.match(FreeTokenType.COMMA)
		}
	}
	return parameters
}

private class ClassParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(classAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		var parameterAccess = getClassParameterAccessModifier(ctx, classAccess) {
			"主构造参数访问修饰符与类访问修饰符不兼容"
		}
		val visibleModifier = when {
			ctx.match(FreeTokenType.VAR) -> Modifier.VAR
			ctx.match(FreeTokenType.VAL) -> Modifier.VAL
			else -> if (parameterAccess != null) {
				syntaxError("主构造参数使用访问修饰符后必须跟 'var' 或 'val' 修饰符", ctx.current)
			} else null
		}
		if (visibleModifier != null && parameterAccess == null) {
			parameterAccess = getDefaultMemberAccessModifier(classAccess)
		}
		if (parameterAccess != null) {
			modifiers += parameterAccess
		}
		if (visibleModifier != null) {
			modifiers += visibleModifier
		}
		ctx.expect(FreeTokenType.IDENTIFIER, "主构造参数缺少名称")
		val name = ctx.previous.value
		ctx.expect(FreeTokenType.COLON, "主构造参数缺少 ':'")
		val typeReference = TypeReferenceParser(ctx).parse()
		return Parameter(
			name = name,
			modifiers = modifiers,
			typeReference = typeReference
		)
	}
}