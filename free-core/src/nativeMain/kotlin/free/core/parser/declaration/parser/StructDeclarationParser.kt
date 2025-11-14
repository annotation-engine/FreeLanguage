package free.core.parser.declaration.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.access
import free.core.parser.declaration.StructDeclaration
import free.core.parser.parameter.parser.parseStructParameters

class StructDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(modifiers: Set<Modifier>): StructDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "结构体缺少名称")
		val name = ctx.previous.value
		val structAccess = modifiers.access
		val parameters = parseStructParameters(ctx, structAccess)
		if (ctx.match(FreeTokenType.LBRACE)) {
			syntaxError("结构体不支持 '{'", ctx.previous)
		}
		return StructDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters
		)
	}
}