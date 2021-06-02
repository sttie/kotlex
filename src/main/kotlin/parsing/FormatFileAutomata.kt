package parsing

import automatas.TransitionCharacter
import automatas.TransitionTable
import automatas.toTransitionCharacter

class FormatFileAutomata {
    private var transitionTable = HashMap<FormatFileState, TransitionTable<FormatFileState>>()
    private val acceptingStates = HashSet<FormatFileState>()
    var startState: FormatFileState = FormatFileState(freeId++, FormatFileTokenType.EMPTY)
    val breakState = FormatFileState(freeId++)

    private fun hasState(state: FormatFileState): Boolean = transitionTable.containsKey(state)

    fun addTransition(from: FormatFileState, byChar: TransitionCharacter, to: FormatFileState) {
        if (!hasState(from))
            transitionTable[from] = TransitionTable()
        transitionTable[from]!!.add(byChar, to)
    }

    fun addTransition(from: FormatFileState, byChar: Char, to: FormatFileState) =
        addTransition(from, byChar.toTransitionCharacter(), to)

    private fun transit(from: FormatFileState) = transitionTable[from]
    private fun transit(from: FormatFileState, byChar: TransitionCharacter) = (transit(from) ?: TransitionTable())[byChar]
    fun transit(from: FormatFileState, byChar: Char) = transit(from, TransitionCharacter(byChar..byChar))

    companion object {
        var freeId = 0
    }

    fun isAccepting(state: FormatFileState): Boolean
        = state in acceptingStates

    fun createAcceptingStateWithType(type: FormatFileTokenType): FormatFileState {
        val acceptingState = FormatFileState(freeId++, type)
        addTransition(acceptingState, ' ', breakState)
        addTransition(acceptingState, '\t', breakState)

        acceptingStates.add(acceptingState)
        return acceptingState
    }

}