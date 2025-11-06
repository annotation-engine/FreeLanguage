package free.core.parser

enum class Modifier {
	PRIVATE, FILE, LOCAL, MODULE, PUBLIC,
	
	VAR, VAL
}

val Modifier.isAccess: Boolean
	get() = this in Modifier.PRIVATE..Modifier.PUBLIC