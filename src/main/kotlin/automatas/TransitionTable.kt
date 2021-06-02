package automatas

class TransitionTable<T> {
    val table = LinkedHashMap<TransitionCharacter, T>()

    fun add(byChar: TransitionCharacter, to: T) {
        table[byChar] = to
    }

    fun hasTransition(byChar: TransitionCharacter) = table.containsKey(byChar)

    operator fun get(byChar: TransitionCharacter): T? {
        if (byChar.isAny())
            TODO()

        for ((transition, to) in table)
            if (byChar in transition)
                return to
        return null
    }
}