package automatas

import automatas.transition.*

class Automata<To> {
    val transitionTable = HashMap<State, TransitionTable<To>>()
    val acceptingStates = HashSet<State>()
    var startState: State = createState()

    fun allStates(): HashSet<State>
        = HashSet(transitionTable.keys)

    fun hasState(state: State): Boolean = transitionTable.containsKey(state)
    fun hasTransition(state: State, byChar: TransitionCharacter): Boolean
        = hasState(state) && transitionTable[state]!!.hasTransition(byChar)

    fun addTransition(from: State, byChar: TransitionCharacter, to: To) {
        if (!hasState(from))
            transitionTable[from] = TransitionTable()
        transitionTable[from]!!.add(byChar, to)
    }

    fun addTransition(from: State, byChar: Char, to: To) = addTransition(from, TransitionCharacter(byChar..byChar), to)

    fun transit(from: State) = transitionTable[from]
    fun transit(from: State, byChar: TransitionCharacter) = (transit(from) ?: TransitionTable())[byChar]
    fun transit(from: State, byChar: Char) = transit(from, TransitionCharacter(byChar..byChar))
    fun removeState(state: State) = transitionTable.remove(state)

    fun isEmpty(): Boolean = transitionTable.isEmpty()

    companion object {
        var freeId = 0

        fun resetIdCounter() {
            freeId = 0
        }
    }

    fun createState() = State(freeId++)

    fun createAcceptingState(): State {
        val acceptingState = createState()
        acceptingStates.add(acceptingState)
        return acceptingState
    }
    fun isAccepting(state: State) = state in acceptingStates
    fun addAccepting(state: State) = acceptingStates.add(state)
}


typealias DFAAutomata = Automata<State>
typealias NFAAutomata = Automata<HashSet<State>>

fun NFAAutomata.addStateToSet(from: State, byChar: TransitionCharacter, to: State) {
    if (!hasState(from))
        transitionTable[from] = TransitionTable()
    if (!transitionTable[from]!!.hasTransition(byChar))
        transitionTable[from]!!.add(byChar, hashSetOf())
    transitionTable[from]!![byChar]!!.add(to)
}

fun NFAAutomata.addStatesToSet(from: State, byChar: TransitionCharacter, to: HashSet<State>) {
    if (!hasState(from))
        transitionTable[from] = TransitionTable()
    if (!transitionTable[from]!!.hasTransition(byChar))
        transitionTable[from]!!.add(byChar, hashSetOf())
    transitionTable[from]!![byChar]!!.addAll(to)
}

// TODO: ужасный костыль, вместо этого лучше добавить проперти-расширения
fun NFAAutomata.getAcceptingState(): State {
    assert(acceptingStates.size == 1)
    return acceptingStates.iterator().next()
}