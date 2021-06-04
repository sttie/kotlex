package automatas.transition

class TransitionCharacter(val characters: CharRange) : Transition {
    companion object {
        val EPSILON = TransitionCharacter(CharRange.EMPTY)
        val ANY = TransitionCharacter(CharRange.EMPTY)
    }

    fun isRange(): Boolean = !isSpecial() && characters.first < characters.last
    fun isSpecial(): Boolean = this === EPSILON || this === ANY
    fun isEpsilon(): Boolean = this === EPSILON
    fun isAny(): Boolean = this === ANY

    operator fun contains(char: Char): Boolean = char in characters
    operator fun contains(charRange: TransitionCharacter): Boolean =
        characters.first <= charRange.characters.first && charRange.characters.last <= characters.last

    override fun hashCode(): Int = characters.hashCode()
    override fun equals(other: Any?): Boolean =
        other is TransitionCharacter && (isSpecial() && (isEpsilon() && other.isEpsilon()
                || isAny() && other.isAny()) || characters == other.characters)

    override fun toString(): String =
        when (this) {
            EPSILON -> "EPSILON"
            ANY     -> "ANY"
            else    -> characters.toString()
        }
}

fun Char.toTransitionCharacter(): TransitionCharacter
    = TransitionCharacter(this..this)