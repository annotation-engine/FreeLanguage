package free.core.parser.declaration.parser

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.access
import free.core.parser.declaration.Declaration
import free.core.parser.declaration.InterfaceDeclaration
import free.core.parser.declaration.TypeKind
import free.core.parser.declaration.matcher.member.parseMemberDeclaration
import free.core.parser.getDefaultMemberAccessModifier

class InterfaceDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(modifiers: Set<Modifier>): InterfaceDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "接口缺少名称")
		val name = ctx.previous.value
		if (!ctx.match(FreeTokenType.LBRACE)) {
			return InterfaceDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val interfaceAccess = modifiers.access
		val members = mutableListOf<Declaration>()
		while (!ctx.match(FreeTokenType.RBRACE)) {
			members += parseDeclaration(interfaceAccess)
		}
		return InterfaceDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
	
	context(_: FreeContext)
	private fun parseDeclaration(
		interfaceAccess: Modifier
	): Declaration {
		val memberAccess = when {
			ctx.match(FreeTokenType.PRIVATE) -> Modifier.PRIVATE
			ctx.match(FreeTokenType.FILE) -> syntaxError("接口内部不允许使用 'file'", ctx.previous)
			ctx.match(FreeTokenType.INTERNAL) -> syntaxError("接口内部不允许使用 'internal'", ctx.previous)
			ctx.match(FreeTokenType.MODULE) -> syntaxError("接口内部不允许使用 'module'", ctx.previous)
			ctx.match(FreeTokenType.PUBLIC) -> syntaxError("接口内部不允许使用 'public'", ctx.previous)
			else -> getDefaultMemberAccessModifier(interfaceAccess)
		}
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.INTERFACE,
			parentModifiers = setOf(interfaceAccess),
			modifiers = setOf(memberAccess)
		)
	}
}