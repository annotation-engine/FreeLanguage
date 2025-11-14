package free.core.parser.declaration.matcher.member

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.InterfaceDeclaration
import free.core.parser.declaration.TypeKind
import free.core.parser.declaration.parser.InterfaceDeclarationParser

object MemberInterfaceDeclarationMatcher : MemberDeclarationMatcher<InterfaceDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.INTERFACE)
	}
	
	context(_: FreeContext)
	override fun check(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "接口",
		)
	}
	
	context(_: FreeContext)
	override fun parse(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): InterfaceDeclaration {
		return InterfaceDeclarationParser(ctx).parse(modifiers)
	}
}