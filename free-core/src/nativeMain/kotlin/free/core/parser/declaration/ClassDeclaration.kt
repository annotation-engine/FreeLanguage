package free.core.parser.declaration

import free.core.exception.syntaxError
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class ClassDeclaration(
	val name: String,
	val modifiers: List<Modifier>,
	val members: List<Declaration>
) : Declaration

class ClassDeclarationParser(
	private val ctx: FreeParserContext,
) {
	
	suspend fun parse(vararg modifiers: Modifier): ClassDeclaration {
		val members = mutableListOf<Declaration>()
		ctx.expect(FreeTokenType.IDENTIFIER, "类缺少名称")
		val className = ctx.previous.value
		ctx.expect(FreeTokenType.LBRACE, "类 $className 缺少 '{'")
		val accessModifier = when {
			Modifier.PRIVATE in modifiers -> Modifier.PRIVATE
			Modifier.FILE in modifiers -> Modifier.FILE
			else -> Modifier.PUBLIC
		}
		while (!ctx.match(FreeTokenType.RBRACE)) {
			members += parseDeclaration(accessModifier)
		}
		
		return ClassDeclaration(
			name = className,
			modifiers = modifiers.toList(),
			members = members
		)
	}
	
	private suspend fun parseDeclaration(
		classAccess: Modifier
	): Declaration = when {
		ctx.match(FreeTokenType.PRIVATE) -> parseDeclaration(classAccess, Modifier.PRIVATE)
		ctx.match(FreeTokenType.FILE) -> parseDeclaration(classAccess, Modifier.FILE)
		ctx.match(FreeTokenType.LOCAL) -> parseDeclaration(classAccess, Modifier.LOCAL)
		ctx.match(FreeTokenType.MODULE) -> parseDeclaration(classAccess, Modifier.MODULE)
		ctx.match(FreeTokenType.PUBLIC) -> parseDeclaration(classAccess, Modifier.PUBLIC)
		else -> parseDeclaration(classAccess, null)
	}
	
	private suspend fun parseDeclaration(classAccess: Modifier, memberAccess: Modifier?): Declaration {
		if (memberAccess != null) {
			checkMemberAccess(classAccess, memberAccess)
		}
		val memberAccess = memberAccess ?: getDefaultMemberAccess(classAccess)
		return when {
			ctx.match(FreeTokenType.FUN) -> FunDeclarationParser(ctx).parse(memberAccess)
			ctx.match(FreeTokenType.CLASS) -> ClassDeclarationParser(ctx).parse(memberAccess)
			else -> syntaxError("未知的类元素声明: ", ctx.current)
		}
	}
	
	private suspend fun getDefaultMemberAccess(classAccess: Modifier): Modifier {
		return when (classAccess) {
			Modifier.PRIVATE, Modifier.FILE -> Modifier.FILE
			Modifier.LOCAL -> Modifier.LOCAL
			Modifier.MODULE -> Modifier.MODULE
			Modifier.PUBLIC -> Modifier.PUBLIC
			else -> syntaxError("不支持的访问修饰符", ctx.current)
		}
	}
	
	private suspend fun checkMemberAccess(
		classAccess: Modifier,
		memberAccess: Modifier
	) {
		if (memberAccess > classAccess) {
			syntaxError("函数访问级别不得大于所属类", ctx.current)
		}
	}
}