package free.core.parser.parameter

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.getDefaultMemberAccessModifier
import free.core.parser.node.TypeReferenceParser

suspend fun parseAnnotationParameters(
	ctx: FreeParserContext,
	annotationAccess: Modifier
): List<Parameter> {
	if (!ctx.match(FreeTokenType.LPAREN)) {
		return emptyList()
	}
	val parameters = mutableListOf<Parameter>()
	while (!ctx.match(FreeTokenType.RPAREN)) {
		parameters += AnnotationParameterParser(ctx).parse(annotationAccess)
		if (!ctx.check(FreeTokenType.RPAREN)) {
			ctx.match(FreeTokenType.COMMA)
		}
	}
	return parameters
}

class AnnotationParameterParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(annotationAccess: Modifier): Parameter {
		val modifiers = mutableSetOf<Modifier>()
		modifiers += getDefaultMemberAccessModifier(annotationAccess)
		modifiers += when {
			ctx.match(FreeTokenType.VAR) -> syntaxError("注解参数不支持 'var' 修饰符", ctx.current)
			ctx.match(FreeTokenType.VAL) -> Modifier.VAL
			else -> syntaxError("注解参数必须添加 'val' 修饰符", ctx.current)
		}
		ctx.expect(FreeTokenType.IDENTIFIER, "注解参数缺少名称")
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