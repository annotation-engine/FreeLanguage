package free.core.util

private val hexChars = "0123456789ABCDEFabcdef".toSet()

fun String.isHex(): Boolean {
	this.forEach {
		if (!it.isHex()) return false
	}
	return true
}

fun Char.isHex(): Boolean = this in hexChars

private val octalChars = "01234567".toSet()

fun String.isOctal(): Boolean {
	this.forEach {
		if (!it.isOctal()) return false
	}
	return true
}

fun Char.isOctal(): Boolean = this in octalChars

private val decimalChars = "0123456789".toSet()

fun String.isDecimal(): Boolean {
	this.forEach {
		if (!it.isDecimal()) return false
	}
	return true
}

fun Char.isDecimal(): Boolean = this in decimalChars

private val binaryChars = "01".toSet()

fun String.isBinary(): Boolean {
	this.forEach {
		if (!it.isBinary()) return false
	}
	return true
}

fun Char.isBinary(): Boolean = this in binaryChars