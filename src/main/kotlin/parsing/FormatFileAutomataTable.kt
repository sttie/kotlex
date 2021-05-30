package parsing

import automatas.TransitionCharacter
import java.lang.IllegalStateException

// TODO: можно заюзать ArrayList<TransitionCharacter> -> ArrayList<_root_ide_package_.parsing.FormatFileState>
class FormatFileAutomataTable {
    private val transitionTable = HashMap<FormatFileState, HashMap<TransitionCharacter, FormatFileState>>()

    fun hasTransition(from: FormatFileState, byChar: TransitionCharacter): Boolean
            = (transitionTable[from] ?: HashMap()).containsKey(byChar)

    fun containsFormatFileState(state: FormatFileState) = transitionTable.containsKey(state)

    fun add(from: FormatFileState, byChar: TransitionCharacter, to: FormatFileState) {
        if (transitionTable[from] == null)
            transitionTable[from] = HashMap()
        transitionTable[from]!![byChar] = to
    }

    operator fun get(state: FormatFileState, byChar: TransitionCharacter): FormatFileState? {
        if (!byChar.isSpecial() && !byChar.isRange())
            return get(state, byChar.characters.first())
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

    operator fun get(state: FormatFileState, byChar: Char): FormatFileState? {
        for ((transitionChar, to) in transitionTable[state] ?: HashMap()) {
            if (!transitionChar.isSpecial() && byChar in transitionChar.characters || transitionChar === TransitionCharacter.any)
                return to
        }
        return null
    }
}