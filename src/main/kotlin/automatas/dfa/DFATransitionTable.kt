package automatas.dfa

import automatas.State
import automatas.TransitionCharacter

// TODO: можно заюзать ArrayList<TransitionCharacter> -> ArrayList<State>
class DFATransitionTable {
    val transitionTable = HashMap<State, HashMap<TransitionCharacter, State>>()

    fun hasTransition(from: State, byChar: TransitionCharacter): Boolean
            = (transitionTable[from] ?: HashMap()).containsKey(byChar)

    fun containsState(state: State) = transitionTable.containsKey(state)

    fun add(from: State, byChar: TransitionCharacter, to: State) {
        if (transitionTable[from] == null)
            transitionTable[from] = HashMap()
        transitionTable[from]!![byChar] = to
    }

    operator fun get(state: State, byChar: TransitionCharacter): State? {
        if (!byChar.isSpecial() && !byChar.isRange())
            return get(state, byChar.characters.first)
        if (!byChar.isSpecial() && byChar.isRange())
            throw IllegalStateException("Unexpected range in table's get function")
        if (byChar.isAny())
            throw IllegalStateException("Unexpected 'any' in table's get function")

        for ((transitionChar, to) in transitionTable[state] ?: HashMap()) {
            if (transitionChar === TransitionCharacter.epsilon)
                return to
        }
        return null
    }

    operator fun get(state: State, byChar: Char): State? {
        for ((transitionChar, to) in transitionTable[state] ?: HashMap()) {
            if (!transitionChar.isSpecial() && byChar in transitionChar.characters || transitionChar === TransitionCharacter.any)
                return to
        }
        return null
    }
}