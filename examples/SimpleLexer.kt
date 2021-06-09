import java.util.HashMap
import SimpleIdsTokenType


data class Token(val lexeme: String, val type: SimpleIdsTokenType?, val start: Int)

class Kotlex {
	val a: Int = 0
	var input = ""
	var current = 0

	val kotlexTable = HashMap<Int, HashMap<Char, Int>>()
	fun set48State(): Unit {
		kotlexTable[48] = HashMap()
		kotlexTable[48]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[48]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[48]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[48]!![char] = 51		
	}

	fun set49State(): Unit {
		kotlexTable[49] = HashMap()
		kotlexTable[49]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[49]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[49]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[49]!![char] = 51		
	}

	fun set50State(): Unit {
		kotlexTable[50] = HashMap()
		kotlexTable[50]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[50]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[50]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[50]!![char] = 51		
	}

	fun set51State(): Unit {
		kotlexTable[51] = HashMap()
		kotlexTable[51]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[51]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[51]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[51]!![char] = 51		
	}

	fun set43State(): Unit {
		kotlexTable[43] = HashMap()
		kotlexTable[43]!!['_'] = 44		
		for (char in 'a'..'z')
			kotlexTable[43]!![char] = 45		
		for (char in 'A'..'Z')
			kotlexTable[43]!![char] = 46		
		for (char in '0'..'9')
			kotlexTable[43]!![char] = 47		
	}

	fun set44State(): Unit {
		kotlexTable[44] = HashMap()
		kotlexTable[44]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[44]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[44]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[44]!![char] = 51		
	}

	fun set45State(): Unit {
		kotlexTable[45] = HashMap()
		kotlexTable[45]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[45]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[45]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[45]!![char] = 51		
	}

	fun set46State(): Unit {
		kotlexTable[46] = HashMap()
		kotlexTable[46]!!['_'] = 48		
		for (char in 'a'..'z')
			kotlexTable[46]!![char] = 49		
		for (char in 'A'..'Z')
			kotlexTable[46]!![char] = 50		
		for (char in '0'..'9')
			kotlexTable[46]!![char] = 51		
	}

	fun set47State(): Unit {
		kotlexTable[47] = HashMap()
		for (char in '0'..'9')
			kotlexTable[47]!![char] = 47		
	}

	init {
		set48State()
		set49State()
		set50State()
		set51State()
		set43State()
		set44State()
		set45State()
		set46State()
		set47State()
	}

	fun reset(input_: String): Unit {
		input = input_
		current = 0
	}

	fun getToken(): Token? {
		var currentState = 43
		var start = current
		val acceptingStatesToTypes = hashMapOf(
			48 to SimpleIdsTokenType.ID, 49 to SimpleIdsTokenType.ID, 50 to SimpleIdsTokenType.ID,
			51 to SimpleIdsTokenType.ID, 44 to SimpleIdsTokenType.ID, 45 to SimpleIdsTokenType.ID,
			46 to SimpleIdsTokenType.ID, 47 to SimpleIdsTokenType.NUMBER,
		)
		
		while (true) {
			if (current >= input.length + 1) return null

			var newState = if (current < input.length) (kotlexTable[currentState] ?: HashMap()).getOrDefault(input[current], -1) else -1
			
			if (newState == -1 && currentState in acceptingStatesToTypes) {
				return Token(input.substring(start until current), acceptingStatesToTypes[currentState], start)
			}
			else if (newState == -1 && start == current) {
				start++
			}
			else if (newState == -1) {
				throw IllegalStateException("Undefined token!")
			}
			else {
				currentState = newState
			}
			current++
		}
	}

}
