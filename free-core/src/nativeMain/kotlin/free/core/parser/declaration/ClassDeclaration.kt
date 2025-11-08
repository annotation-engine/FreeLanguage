package free.core.parser.declaration

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.*
import free.core.parser.node.ClassParameterParser
import free.core.parser.node.Parameter
import kotlinx.serialization.Serializable

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: Set<Modifier>,
	val constructorModifiers: Set<Modifier>,
	val parameters: List<Parameter>,
	val members: List<Declaration>
) : Declaration

class ClassDeclarationParser(
	private val ctx: FreeParserContext,
) {
	
	suspend fun parse(modifiers: Set<Modifier>): ClassDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "类缺少名称")
		val name = ctx.previous.value
		val classAccess = getClassAccess(modifiers)
		val constructorModifiers = getConstructorModifiers(classAccess)
		val parameters = mutableListOf<Parameter>()
		if (ctx.match(FreeTokenType.LPAREN)) {
			while (!ctx.match(FreeTokenType.RPAREN)) {
				parameters += ClassParameterParser(ctx).parse(classAccess)
				if (!ctx.check(FreeTokenType.RPAREN)) {
					ctx.match(FreeTokenType.COMMA)
				}
			}
		}
		
		ctx.expect(FreeTokenType.LBRACE, "类 $name 缺少 '{'")
		
		val members = mutableListOf<Declaration>()
		while (!ctx.match(FreeTokenType.RBRACE)) {
			members += parseDeclaration(classAccess)
		}
		
		return ClassDeclaration(
			name = name,
			modifiers = modifiers.toSet(),
			constructorModifiers = constructorModifiers,
			parameters = parameters,
			members = members
		)
	}
	
	private suspend fun parseDeclaration(
		classAccess: Modifier
	): Declaration = when {
		ctx.match(FreeTokenType.PRIVATE) -> parseDeclaration(classAccess, Modifier.PRIVATE)
		ctx.match(FreeTokenType.FILE) -> parseDeclaration(classAccess, Modifier.FILE)
		ctx.match(FreeTokenType.INTERNAL) -> parseDeclaration(classAccess, Modifier.INTERNAL)
		ctx.match(FreeTokenType.MODULE) -> parseDeclaration(classAccess, Modifier.MODULE)
		ctx.match(FreeTokenType.PUBLIC) -> parseDeclaration(classAccess, Modifier.PUBLIC)
		else -> parseDeclaration(classAccess, null)
	}
	
	private suspend fun parseDeclaration(classAccess: Modifier, memberAccess: Modifier?): Declaration {
		if (memberAccess != null) {
			checkMemberAccess(classAccess, memberAccess, ctx.previous)
		}
		val memberAccess = memberAccess ?: getDefaultMemberAccess(classAccess, ctx.previous)
		return when {
			ctx.match(FreeTokenType.FUN) -> FunDeclarationParser(ctx).parse(setOf(memberAccess))
			ctx.match(FreeTokenType.CLASS) -> ClassDeclarationParser(ctx).parse(setOf(memberAccess))
			else -> syntaxError("未知的类元素声明: ", ctx.current)
		}
	}
	
	private fun getClassAccess(modifiers: Set<Modifier>): Modifier {
		Modifiers.accessModifiers.forEach {
			if (it in modifiers) return it
		}
		return Modifier.PUBLIC
	}
	
	private suspend fun getConstructorModifiers(classAccess: Modifier): Set<Modifier> {
		return when {
			ctx.match(FreeTokenType.PRIVATE) -> getConstructorModifiers(classAccess, Modifier.PRIVATE)
			ctx.match(FreeTokenType.FILE) -> getConstructorModifiers(classAccess, Modifier.FILE)
			ctx.match(FreeTokenType.INTERNAL) -> getConstructorModifiers(classAccess, Modifier.INTERNAL)
			ctx.match(FreeTokenType.MODULE) -> getConstructorModifiers(classAccess, Modifier.MODULE)
			ctx.match(FreeTokenType.PUBLIC) -> getConstructorModifiers(classAccess, Modifier.PUBLIC)
			else -> getConstructorModifiers(classAccess, null)
		}
	}
	
	private suspend fun getConstructorModifiers(classAccess: Modifier, constructorAccess: Modifier?): Set<Modifier> {
		if (constructorAccess != null) {
			checkMemberAccess(classAccess, constructorAccess, ctx.previous, isConstructor = true)
			return setOf(constructorAccess)
		}
		return setOf(getDefaultMemberAccess(classAccess, ctx.previous))
	}
}