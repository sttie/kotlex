package parsing

import automatas.TransitionCharacter
import java.lang.IllegalStateException

class FormatFileLexAutomata {
    var transitionTable = FormatFileAutomataTable()
    var startState: FormatFileState = FormatFileState(freeId++, FormatFileTokenType.EMPTY)
    val acceptingStates: HashSet<FormatFileState> = HashSet()
    val breakState = FormatFileState(freeId++)

    companion object {
        var freeId = 0
    }

    fun addTransition(from: FormatFileState, byCharacter: TransitionCharacter, to: FormatFileState) {
        transitionTable.add(from, byCharacter, to)
    }

    fun transit(state: FormatFileState, byChar: Char): FormatFileState?
        = transitionTable[state, byChar]

    fun addTransition(from: FormatFileState, byCharacter: Char, to: FormatFileState) {
        addTransition(from, TransitionCharacter(byCharacter), to)
    }

    fun isAccepting(state: FormatFileState) = state in acceptingStates

    fun createState(): FormatFileState = FormatFileState(freeId++)
    fun createStateWithType(type: FormatFileTokenType): FormatFileState = FormatFileState(freeId++, type)

    fun createAcceptingState(): FormatFileState {
        val acceptingState = FormatFileState(freeId++)
        acceptingStates.add(acceptingState)
        return acceptingState
    }

    fun createAcceptingStateWithType(type: FormatFileTokenType): FormatFileState {
        val acceptingState = FormatFileState(freeId++, type)
        addTransition(acceptingState, ' ', breakState)
        addTransition(acceptingState, '\t', breakState)

        acceptingStates.add(acceptingState)
        return acceptingState
    }

}