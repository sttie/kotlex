package automatas.transition


class TransitionString(val lexeme: String) : Transition {
    override fun toString(): String = lexeme

    override fun equals(other: Any?): Boolean =
        other is TransitionString && lexeme == other.lexeme
    override fun hashCode(): Int =
        lexeme.hashCode()
}

fun String.toTransitionString(): TransitionString =
    TransitionString(this)