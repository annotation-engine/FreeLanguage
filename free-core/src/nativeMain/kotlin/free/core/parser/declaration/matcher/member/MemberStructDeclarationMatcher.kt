package free.core.parser.declaration.matcher.member

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.checkSupportedDeclarationModifiers
import free.core.parser.declaration.StructDeclaration
import free.core.parser.declaration.TypeKind
import free.core.parser.declaration.parser.StructDeclarationParser

object MemberStructDeclarationMatcher : MemberDeclarationMatcher<StructDeclaration> {
	
	override fun match(ctx: FreeParserContext): Boolean {
		return ctx.match(FreeTokenType.STRUCT)
	}
	
	context(_: FreeContext)
	override fun check(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	) {
		checkSupportedDeclarationModifiers(
			ctx, modifiers, name = "结构体",
		)
	}
	
	context(_: FreeContext)
	override fun parse(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): StructDeclaration {
		return StructDeclarationParser(ctx).parse(modifiers)
	}
}