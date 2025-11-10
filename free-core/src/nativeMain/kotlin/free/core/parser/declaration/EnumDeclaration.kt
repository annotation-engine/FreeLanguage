package free.core.parser.declaration

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.matcher.MemberDeclarationMatcher
import free.core.parser.matcher.TypeKind
import free.core.parser.parameter.Parameter
import free.core.parser.parameter.parseClassParameters
import kotlinx.serialization.Serializable

@Serializable
data class EnumDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val parameters: List<Parameter>,
	val entries: List<EnumEntry>,
	val members: List<Declaration> = emptyList(),
) : Declaration

@Serializable
data class EnumEntry(
	val name: String,
	val members: List<Declaration>,
)

class EnumDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(modifiers: Set<Modifier>): EnumDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "枚举缺少名称")
		val name = ctx.previous.value
		val enumAccess = modifiers.access
		val parameters = parseClassParameters(ctx, enumAccess)
		ctx.expect(FreeTokenType.LBRACE, "枚举缺少 '{'")
		val entries = parseEnumEntries(enumAccess)
		if (ctx.previous.type == FreeTokenType.RBRACE) {
			return EnumDeclaration(
				name = name,
				modifiers = modifiers,
				parameters = parameters,
				entries = entries
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!ctx.match(FreeTokenType.RBRACE)) {
			members += parseMemberDeclaration(enumAccess, modifiers)
		}
		
		return EnumDeclaration(
			name = name,
			modifiers = modifiers,
			parameters = parameters,
			entries = entries,
			members = members
		)
	}
	
	private suspend fun parseEnumEntries(enumAccess: Modifier): List<EnumEntry> {
		val entries = mutableListOf<EnumEntry>()
		while (!ctx.match(FreeTokenType.SEMICOLON) && !ctx.match(FreeTokenType.RBRACE)) {
			entries += EnumEntryParser(ctx).parse(enumAccess)
			if (!ctx.check(FreeTokenType.SEMICOLON) && !ctx.check(FreeTokenType.RBRACE)) {
				ctx.match(FreeTokenType.COMMA)
			}
		}
		if (entries.isEmpty()) {
			syntaxError("请至少为枚举设置一个常量", ctx.previous)
		}
		return entries
	}
	
	private suspend fun parseMemberDeclaration(enumAccess: Modifier, enumModifiers: Set<Modifier>): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, enumAccess) {
			"访问修饰符与枚举访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return MemberDeclarationMatcher.checkAndParse(
			ctx = ctx,
			typeKind = TypeKind.ENUM,
			parentModifiers = enumModifiers,
			memberModifiers = memberModifiers
		)
	}
}

private class EnumEntryParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(enumAccess: Modifier): EnumEntry {
		ctx.expect(FreeTokenType.IDENTIFIER, "枚举常量缺少名称")
		val name = ctx.previous.value
		if (ctx.match(FreeTokenType.LPAREN)) {
			while (!ctx.match(FreeTokenType.RPAREN)) {
				ctx.advance()
			}
		}
		val members = mutableListOf<Declaration>()
		if (ctx.match(FreeTokenType.LBRACE)) {
			while (!ctx.match(FreeTokenType.RBRACE)) {
				members += parseMemberDeclaration(enumAccess)
			}
		}
		return EnumEntry(
			name = name,
			members = members
		)
	}
	
	private suspend fun parseMemberDeclaration(enumAccess: Modifier): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, enumAccess) {
			"访问修饰符与枚举访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return MemberDeclarationMatcher.checkAndParse(
			ctx = ctx,
			typeKind = TypeKind.ENUM_ENTRY,
			parentModifiers = emptySet(),
			memberModifiers = memberModifiers
		)
	}
}