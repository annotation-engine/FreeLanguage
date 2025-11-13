package free.core

import free.core.io.File
import free.core.lexer.FreeLexer
import free.core.parser.FreeParser
import free.core.parser.node.Program
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.time.measureTime

fun main(vararg args: String) {
	val duration = measureTime {
		val command = args.firstOrNull() ?: run {
			help()
			return
		}
		when (command) {
			"-c" -> {
				val paths = args.drop(1)
				if (paths.isEmpty()) {
					println("请至少指定一个 Free 程序文件，使用 free -h 查看使用手册")
					return
				}
				run(args.drop(1))
			}
			
			else -> help()
		}
	}
	println("执行耗时: $duration")
}

private fun run(paths: List<String>) = runBlocking {
	val files = paths.toSet().map(::File).distinctBy { it.absolutePath }
	val jobs = files.map { file ->
		async(Dispatchers.Default) {
			val context = FreeContext(file.absolutePath)
			context(context) {
				val input = file.readFileChars()
				val rawTokens = FreeLexer(input).lex()
//				println(rawTokens.formatToString())
				FreeParser(rawTokens).parse()
			}
		}
	}
	val sourceFileNodes = jobs.awaitAll()
	val program = Program(sourceFileNodes)
	println(json.encodeToString(program))
}

class FreeContext(
	val sourcePath: String
)

private val json = Json {
	prettyPrint = true
	encodeDefaults = true
	classDiscriminator = "class"
}

private fun help() {
	val help = """
		使用方式:
            free -c <file1.free> [file2.free ...]   编译 Free 程序文件
            free -h                                 查看使用手册
	""".trimIndent()
	println(help)
}