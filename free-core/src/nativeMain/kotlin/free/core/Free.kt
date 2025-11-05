package free.core

import free.core.lexer.lexers
import kotlinx.coroutines.runBlocking
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun main(vararg args: String) {
	val duration = measureTime {
		val command = args.firstOrNull() ?: run {
			help()
			return
		}
		when (command) {
			"run" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					println("Error: Missing file path.\nUsage: free run <file1.free> [file2.free ...]")
					return
				}
				run(args.drop(1))
			}
			
			else -> help()
		}
	}
	println("Execution time: $duration")
}

private fun run(paths: List<String>) = runBlocking {
	val tokensValue = measureTimedValue { lexers(paths) }
	val tokens = tokensValue.value
	println("FreeLexer tokens: ${tokens.sumOf { it.size }} use ${tokensValue.duration}")
}

private fun help() {
	val help = """
		Usage:
            free run <file1.free> [file2.free ...]   Run one or more Free source files.
            free help                                Show command usage and options.
	""".trimIndent()
	println(help)
}