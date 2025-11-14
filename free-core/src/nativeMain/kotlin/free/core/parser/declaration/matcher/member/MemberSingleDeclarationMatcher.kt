package free.core.parser.declaration.matcher.member

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.SingleDeclaration
import free.core.parser.declaration.TypeKind
import free.core.parser.declaration.parser.SingleDeclarationParser

object MemberSingleDeclarationMatcher : MemberDeclarationMatcher<SingleDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.SINGLE)
	}
	
	context(_: FreeContext)
	override fun check(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "单例类"
		)
	}
	
	context(_: FreeContext)
	override fun parse(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): SingleDeclaration {
		return SingleDeclarationParser(ctx).parse(modifiers, parentTypeKind)
	}
}