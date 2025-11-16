package free.core.parser.declaration.matcher.member

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.declaration.Declaration
import free.core.parser.declaration.TypeKind

sealed interface MemberDeclarationMatcher<out D : Declaration> {
	
	fun match(ctx: FreeParserContext): Boolean
	
	context(_: FreeContext)
	fun check(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		parentModifiers: Set<Modifier>,
		modifiers: Set<Modifier>
	)
	
	context(_: FreeContext)
	fun parse(
		ctx: FreeParserContext,
		parentTypeKind: TypeKind,
		modifiers: Set<Modifier>
	): D
}

private val matchers = listOf(
	MemberFunDeclarationMatcher,
	MemberClassDeclarationMatcher,
	MemberSingleDeclarationMatcher,
	MemberInterfaceDeclarationMatcher,
	MemberStructDeclarationMatcher,
	MemberEnumDeclarationMatcher,
	MemberAnnotationDeclarationMatcher
)

context(_: FreeContext)
fun parseMemberDeclaration(
	ctx: FreeParserContext,
	parentTypeKind: TypeKind,
	parentModifiers: Set<Modifier>,
	modifiers: Set<Modifier>
): Declaration {
	val matcher = matchers.find { it.match(ctx) }
		?: syntaxError("未知的成员声明", ctx.current)
	matcher.check(ctx, parentTypeKind, parentModifiers, modifiers)
	return matcher.parse(ctx, parentTypeKind, modifiers)
}