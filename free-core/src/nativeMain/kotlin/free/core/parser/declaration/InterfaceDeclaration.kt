package free.core.parser.declaration

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.access
import free.core.parser.getDefaultMemberAccessModifier
import free.core.parser.matcher.MemberDeclarationMatcher
import free.core.parser.matcher.TypeKind
import kotlinx.serialization.Serializable

@Serializable
data class InterfaceDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val members: List<Declaration> = emptyList(),
) : Declaration

class InterfaceDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(modifiers: Set<Modifier>): InterfaceDeclaration {
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
			members += parseMemberDeclaration(interfaceAccess)
		}
		return InterfaceDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
	
	private suspend fun parseMemberDeclaration(
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
		return MemberDeclarationMatcher.checkAndParse(
			ctx = ctx,
			typeKind = TypeKind.INTERFACE,
			parentModifiers = setOf(interfaceAccess),
			memberModifiers = setOf(memberAccess)
		)
	}
}