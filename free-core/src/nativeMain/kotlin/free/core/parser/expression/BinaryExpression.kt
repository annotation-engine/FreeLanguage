package free.core.parser.expression

import free.core.FreeContext
import free.core.exception.syntaxError
import free.core.parser.Associativity
import free.core.parser.FreeParserContext
import free.core.parser.Operator
import free.core.parser.matcher.ExpressionMatcher
import free.core.parser.toOperator
import kotlinx.serialization.Serializable

@Serializable
data class BinaryExpression(
	val left: Expression,
	val operator: Operator,
	val right: Expression,
) : Expression

class BinaryExpressionParser(
	private val ctx: FreeParserContext
) {
	
	context(_: FreeContext)
	fun parse(left: Expression): BinaryExpression {
		val previous = ctx.previous
		val operator = previous.type.toOperator()
		val right = ExpressionMatcher.parse(ctx)
		if (left !is BinaryExpression) {
			return BinaryExpression(left, operator, right)
		}
		val lastPriority = left.operator.priority
		val priority = operator.priority
		return when {
			lastPriority > priority -> BinaryExpression(left, operator, right)
			lastPriority < priority -> exchange(left, operator, right)
			operator.associativity == Associativity.LEFT -> BinaryExpression(left, operator, right)
			operator.associativity == Associativity.RIGHT -> exchange(left, operator, right)
			else -> syntaxError("没有结合性的运算符不允许连续", previous)
		}
	}
	
	private fun exchange(left: BinaryExpression, operator: Operator, right: Expression): BinaryExpression {
		return BinaryExpression(
			left = left.left,
			operator = left.operator,
			right = BinaryExpression(
				left = left.right,
				operator = operator,
				right = right
			)
		)
	}
}