package automatas.dfa

import automatas.State
import automatas.TransitionCharacter


class DFAAutomata {
    val transitionTable = DFATransitionTable()
    val acceptingStates = HashSet<State>()
    var startState: State = createState()

    companion object {
        var freeId = 0
    }

    fun addTransition(from: State, byChar: TransitionCharacter, to: State) {
        transitionTable.add(from, byChar, to)
    }

    fun transit(from: State, byChar: TransitionCharacter): State?
            = transitionTable[from, byChar]

    fun transit(from: State, byChar: Char): State?
            = transitionTable[from, byChar]

    fun hasTransition(from: State, byChar: TransitionCharacter) = transitionTable.hasTransition(from, byChar)

    fun addAccepting(state: State) {
        acceptingStates.add(state)
    }

    fun hasState(state: State) = transitionTable.containsState(state)

    fun createStartState(): State {
        startState = createState()
        return startState
    }

    fun createAcceptingState(): State {
        val acceptingState = createState()
        acceptingStates.add(acceptingState)
        return acceptingState
    }

    fun createState(): State {
        return State(freeId++)
    }
}