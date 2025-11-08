package free.core.parser

import free.core.exception.syntaxError
import free.core.lexer.FreeToken
import free.core.parser.Modifier.*

enum class Modifier {
	PRIVATE, FILE, INTERNAL, MODULE, PUBLIC,
	
	VAR, VAL,
	
	OPEN, ABSTRACT, OVERRIDE, FINAL
}

object Modifiers {
	
	val accessModifiers = listOf(PRIVATE, FILE, INTERNAL, MODULE, PUBLIC)
}

/**
 * 检查类与成员的访问权限
 */
suspend fun checkMemberAccess(
	classAccess: Modifier,
	memberAccess: Modifier,
	token: FreeToken,
	isConstructor: Boolean = false
) {
	if (!((classAccess == PRIVATE && memberAccess <= FILE) || (memberAccess <= classAccess))) {
		if (isConstructor) {
			syntaxError("主构造函数访问修饰符与类访问修饰符不兼容", token)
		} else {
			syntaxError("函数访问修饰符与类访问修饰符不兼容", token)
		}
	}
}

suspend fun getDefaultMemberAccess(classAccess: Modifier, token: FreeToken): Modifier {
	return when (classAccess) {
		PRIVATE, FILE -> FILE
		INTERNAL -> INTERNAL
		MODULE -> MODULE
		PUBLIC -> PUBLIC
		else -> syntaxError("不支持的访问修饰符", token)
	}
}