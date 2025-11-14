package free.core.parser.declaration.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.access
import free.core.parser.declaration.AnnotationDeclaration
import free.core.parser.parameter.parser.parseAnnotationParameters

class AnnotationDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(modifiers: Set<Modifier>): AnnotationDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "注解缺少名称")
		val annotationAccess = modifiers.access
		val name = ctx.previous.value
		val parameters = parseAnnotationParameters(ctx, annotationAccess)
		if (ctx.match(FreeTokenType.LBRACE)) {
			syntaxError("注解不支持 '{'", ctx.previous)
		}
		return AnnotationDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters,
		)
	}
}