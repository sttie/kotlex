package automatas.nfa

import automatas.State
import automatas.TransitionCharacter

// TODO: можно заюзать ArrayList<TransitionCharacter> -> ArrayList<HashSet<State>>
class NFATransitionTable {
    val transitionTable = HashMap<State, HashMap<TransitionCharacter, HashSet<State>>>()

    fun hasTransition(from: State, byChar: TransitionCharacter): Boolean
        = (transitionTable[from] ?: HashMap()).containsKey(byChar)

    fun add(from: State, byChar: TransitionCharacter, to: HashSet<State>) {
        if (transitionTable[from] == null)
            transitionTable[from] = HashMap()
        if (transitionTable[from]!![byChar] == null)
            transitionTable[from]!![byChar] = HashSet()
        transitionTable[from]!![byChar]!!.addAll(to)
    }

    fun containsState(state: State) = transitionTable.containsKey(state)

    operator fun get(state: State): HashMap<TransitionCharacter, HashSet<State>>?
        = transitionTable[state]

    operator fun get(state: State, byChar: TransitionCharacter): HashSet<State>? {
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

    operator fun get(state: State, byChar: Char): HashSet<State>? {
        for ((transitionChar, to) in transitionTable[state] ?: HashMap()) {
            if (!transitionChar.isSpecial() && byChar in transitionChar.characters
                    || transitionChar === TransitionCharacter.any)
                return to
        }
        return null
    }
}