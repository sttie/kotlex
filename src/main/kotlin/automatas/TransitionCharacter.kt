package automatas

class TransitionCharacter {
    lateinit var characters: CharRange
    private var type = ""

    companion object {
        val epsilon = TransitionCharacter("epsilon")
        val any = TransitionCharacter("any")
    }

    constructor(range: CharRange) {
        characters = range
    }

    constructor(char: Char) {
        characters = char..char
    }

    private constructor(type_: String) {
        type = type_
    }

    fun isEpsilon() = type == "epsilon"
    fun isAny() = type == "any"
    fun isSpecial() = type == "any" || type == "epsilon"

    fun isRange(): Boolean = characters.first != characters.last
}