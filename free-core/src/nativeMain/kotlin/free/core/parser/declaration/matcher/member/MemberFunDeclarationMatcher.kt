package free.core.parser.declaration.matcher.member

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.declaration.FunDeclaration
import free.core.parser.declaration.TypeKind
import free.core.parser.declaration.parser.FunDeclarationParser

object MemberFunDeclarationMatcher : MemberDeclarationMatcher<FunDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.FUN)
	}
	
	context(_: FreeContext)
	override fun check(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		when (parentTypeKind) {
			TypeKind.ENUM -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "枚举成员函数",
				isSupportedOpen = true,
				isSupportedAbstract = true,
			)
			
			TypeKind.ENUM_ENTRY -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "枚举常量成员函数",
				isSupportedOverride = true
			)
			
			else -> checkSupportedDeclarationModifiers(
				ctx, modifiers, name = "成员函数",
				isSupportedOpen = parentModifiers.isOpen,
				isSupportedAbstract = parentModifiers.isAbstract,
				isSupportedFinalOverride = parentModifiers.isAbstract,
				isSupportedOverride = parentModifiers.isAbstract,
			)
		}
	}
	
	context(_: FreeContext)
	override fun parse(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): FunDeclaration {
		return FunDeclarationParser(ctx).parse(modifiers)
	}
}