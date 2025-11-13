package free.core.parser.declaration

import free.core.FreeContext
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.matcher.MemberDeclarationMatcher
import free.core.parser.matcher.TypeKind
import free.core.parser.parameter.Parameter
import free.core.parser.parameter.parseClassParameters
import kotlinx.serialization.Serializable

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val constructorModifiers: Set<Modifier>,
	val parameters: List<Parameter>,
	val members: List<Declaration> = emptyList(),
) : Declaration

class ClassDeclarationParser(
	private val ctx: FreeParserContext,
) {
	
	context(_: FreeContext)
	fun parse(modifiers: Set<Modifier>): ClassDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "类缺少名称")
		val name = ctx.previous.value
		val classAccess = modifiers.access
		val constructorModifiers = mutableSetOf<Modifier>()
		constructorModifiers += getMemberAccessModifier(ctx, classAccess) {
			"主构造函数访问修饰符与类访问修饰符不兼容"
		}
		val parameters = parseClassParameters(ctx, classAccess)
		if (!ctx.match(FreeTokenType.LBRACE)) {
			return ClassDeclaration(
				name = name,
				modifiers = modifiers,
				constructorModifiers = constructorModifiers,
				parameters = parameters,
			)
		}
		
		val members = mutableListOf<Declaration>()
		while (!ctx.match(FreeTokenType.RBRACE)) {
			members += parseMemberDeclaration(classAccess, modifiers)
		}
		
		return ClassDeclaration(
			name = name,
			modifiers = modifiers,
			constructorModifiers = constructorModifiers,
			parameters = parameters,
			members = members
		)
	}
	
	context(_: FreeContext)
	private fun parseMemberDeclaration(
		classAccess: Modifier,
		classModifiers: Set<Modifier>,
	): Declaration {
		val memberModifiers = mutableSetOf<Modifier>()
		memberModifiers += getMemberAccessModifier(ctx, classAccess) {
			"访问修饰符与类访问修饰符不兼容"
		}
		memberModifiers += getDeclarationModifiers(ctx)
		return MemberDeclarationMatcher.checkAndParse(
			ctx = ctx,
			typeKind = TypeKind.CLASS,
			parentModifiers = classModifiers,
			memberModifiers = memberModifiers
		)
	}
}