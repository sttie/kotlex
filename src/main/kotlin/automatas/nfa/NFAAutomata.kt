package automatas.nfa

import automatas.State
import automatas.TransitionCharacter

class NFAAutomata {
    val transitionTable = NFATransitionTable()
    lateinit var acceptingState: State
    val helpingAcceptingStatesSet = HashSet<State>()
//    val acceptingState = HashSet()
    var startState: State = createState()

    companion object {
        var freeId = 0
    }

    fun addTransition(from: State, byChar: TransitionCharacter, to: HashSet<State>) {
        transitionTable.add(from, byChar, to)
    }

    fun addTransition(from: State, byChar: TransitionCharacter, to: State) {
        addTransition(from, byChar, hashSetOf(to))
    }

    fun transit(from: State, byChar: TransitionCharacter): HashSet<State>?
        = transitionTable[from, byChar]

    fun transit(from: State, byChar: Char): HashSet<State>?
            = transitionTable[from, byChar]

    fun transit(from: State) = transitionTable[from]

    fun hasTransition(from: State, byChar: TransitionCharacter) = transitionTable.hasTransition(from, byChar)
    fun hasState(state: State) = transitionTable.containsState(state)

    fun isEmpty() = !::acceptingState.isInitialized

    fun removeState(state: State) {
        transitionTable.transitionTable.remove(state)
    }

    fun createStartState(): State {
        startState = createState()
        return startState
    }

    fun createAcceptingState(): State {
        acceptingState = createState()
        helpingAcceptingStatesSet.add(acceptingState)
        return acceptingState
    }

    fun setAccepting(state: State) {
        acceptingState = state
    }

    fun createState(): State {
        return State(freeId++)
    }
}