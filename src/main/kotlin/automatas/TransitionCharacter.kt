package automatas

class TransitionCharacter {
    var characters = ArrayList<Char>()
    private var type = ""

    companion object {
        val epsilon = TransitionCharacter("epsilon")
        val any = TransitionCharacter("any")
    }

    constructor(range: CharRange) {
        range.forEach { characters.add(it) }
    }

    constructor(char: Char) {
        characters.add(char)
    }

    private constructor(type_: String) {
        type = type_
    }

    operator fun contains(char: Char) = char in characters

    fun add(char: Char) {
        characters.add(char)
    }
    fun add(char: CharRange) {
        characters.addAll(char)
    }

    fun isEpsilon() = type == "epsilon"
    fun isAny() = type == "any"
    fun isSpecial() = type == "any" || type == "epsilon"
    fun isRange(): Boolean = characters.first() != characters.last()
}