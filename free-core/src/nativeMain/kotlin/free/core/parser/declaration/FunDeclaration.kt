package free.core.parser.declaration

import free.core.constants.FreeTypes
import free.core.lexer.FreeTokenType
import free.core.parser.FreeParserContext
import free.core.parser.Modifier
import free.core.parser.matcher.StatementMatcher
import free.core.parser.node.TypeReference
import free.core.parser.node.TypeReferenceParser
import free.core.parser.parameter.Parameter
import free.core.parser.parameter.parseFunParameters
import free.core.parser.statement.Statement
import kotlinx.serialization.Serializable

@Serializable
data class FunDeclaration(
	val name: String,
	val parameters: List<Parameter>,
	val modifiers: Set<Modifier>,
	val returnTypes: List<TypeReference>,
	val statements: List<Statement> = emptyList(),
) : Declaration

class FunDeclarationParser(
	private val ctx: FreeParserContext
) {
	
	suspend fun parse(modifiers: Set<Modifier>): FunDeclaration {
		ctx.expect(FreeTokenType.IDENTIFIER, "函数缺少名称")
		val funName = ctx.previous.value
		val parameters = parseFunParameters(ctx)
		
		val returnTypes = mutableListOf<TypeReference>()
		if (ctx.match(FreeTokenType.COLON)) {
			do {
				returnTypes += TypeReferenceParser(ctx).parse()
			} while (ctx.match(FreeTokenType.COMMA))
		} else {
			returnTypes += TypeReference(FreeTypes.Unit)
		}
		
		if (!ctx.match(FreeTokenType.LBRACE)) {
			return FunDeclaration(
				name = funName,
				parameters = parameters,
				modifiers = modifiers.toSet(),
				returnTypes = returnTypes
			)
		}
		val statements = mutableListOf<Statement>()
		while (!ctx.match(FreeTokenType.RBRACE)) {
			statements += StatementMatcher.parse(ctx)
		}
		return FunDeclaration(
			name = funName,
			parameters = parameters,
			modifiers = modifiers.toSet(),
			returnTypes = returnTypes,
			statements = statements
		)
	}
}