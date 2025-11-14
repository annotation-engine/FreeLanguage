package free.core.parser.declaration.parser

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.declaration.Declaration
import free.core.parser.declaration.SingleDeclaration
import free.core.parser.declaration.TypeKind
import free.core.parser.declaration.matcher.member.parseMemberDeclaration

class SingleDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(modifiers: Set<Modifier>, parentTypeKind: TypeKind? = null): SingleDeclaration {
		val name = when {
			parentTypeKind == null -> {
				ctx.expect(FreeTokenType.IDENTIFIER, "单例类缺少名称")
				ctx.previous.value
			}
			
			parentTypeKind == TypeKind.SINGLE -> {
				ctx.expect(FreeTokenType.IDENTIFIER, "内部单例类不支持默认名称")
				ctx.previous.value
			}
			
			ctx.match(FreeTokenType.IDENTIFIER) -> ctx.previous.value
			
			else -> ""
		}
		val singleAccess = modifiers.access
		if (!ctx.match(FreeTokenType.LBRACE)) {
			return SingleDeclaration(
				name = name,
				modifiers = modifiers
			)
		}
		val members = mutableListOf<Declaration>()
		while (!ctx.match(FreeTokenType.RBRACE)) {
			members += parseDeclaration(singleAccess, modifiers)
		}
		
		return SingleDeclaration(
			name = name,
			modifiers = modifiers,
			members = members
		)
	}
	
	context(_: FreeContext)
	private fun parseDeclaration(
		singleAccess: Modifier,
		singleModifiers: Set<Modifier>,
	): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, singleAccess) {
			"访问修饰符与类访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return parseMemberDeclaration(
			ctx = ctx,
			parentTypeKind = TypeKind.SINGLE,
			parentModifiers = singleModifiers,
			modifiers = memberModifiers
		)
	}
}