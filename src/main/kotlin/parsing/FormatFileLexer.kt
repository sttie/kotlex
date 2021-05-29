package parsing

import automatas.TransitionCharacter
import java.lang.IllegalArgumentException

class FormatFileLexer(private val input: String) {
    private val automata: FormatFileLexAutomata = FormatFileLexAutomata()
    private var current: Int = 0

    fun getToken(): FormatFileToken {
        if (current >= input.length)
            return FormatFileToken("", FormatFileTokenType.END, current)

        var currentState: FormatFileState = automata.startState

        skipWhitespaces()

        val start = current
        while (current < input.length) {
            val newState = automata.transit(currentState, input[current])
            if (newState == null || newState == automata.breakState)
                break

            current++
            currentState = newState
        }

        if (!automata.isAccepting(currentState))
            throw IllegalArgumentException("Undefined token: ${input.substring(start until current)}")

        val lexeme = input.substring(start until current)
        return if (lexeme.toUpperCase() in FormatFileTokenType.values().map { it.toString() })
            FormatFileToken(lexeme, FormatFileTokenType.valueOf(lexeme.toUpperCase()), start)
        else
            FormatFileToken(lexeme, currentState.type, start)
    }

    fun lookAhead(): FormatFileToken {
        val start = current
        val aheadToken = getToken()
        current = start
        return aheadToken
    }

    fun getTextUntil(symbol: Char): String {
        val start = current
        while (input[current] != symbol)
            current++
        return input.substring(start until current)
    }

    fun skipWhitespaces() {
        while (current < input.length && automata.transit(automata.startState, input[current]) == automata.breakState) {
            current++
        }
    }

    fun skipNewlines() {
        while (current < input.length && input[current] == '\n')
            current++
    }

    init {
        val idState = automata.createAcceptingStateWithType(FormatFileTokenType.ID)

        automata.addTransition(idState, TransitionCharacter('a'..'z'), idState)
        automata.addTransition(idState, TransitionCharacter('A'..'Z'), idState)

        automata.addTransition(idState, TransitionCharacter('0'..'9'), idState)

        automata.addTransition(idState, '_', idState)

        automata.addTransition(automata.startState, TransitionCharacter('a'..'z'), idState)
        automata.addTransition(automata.startState, TransitionCharacter('A'..'Z'), idState)

        automata.addTransition(automata.startState, '_', idState)

        val assignState = automata.createAcceptingStateWithType(FormatFileTokenType.ASSIGN)

        val colonState = automata.createAcceptingStateWithType(FormatFileTokenType.COLON)
        automata.addTransition(automata.startState, ':', colonState)
        automata.addTransition(colonState, '=', assignState)

        val dotState = automata.createAcceptingStateWithType(FormatFileTokenType.DOT)
        automata.addTransition(automata.startState, '.', dotState)

        val commaState = automata.createAcceptingStateWithType(FormatFileTokenType.COMMA)
        automata.addTransition(automata.startState, ',', commaState)

        val lbraceState = automata.createAcceptingStateWithType(FormatFileTokenType.LBRACE)
        automata.addTransition(automata.startState, '{', lbraceState)

        val rbraceState = automata.createAcceptingStateWithType(FormatFileTokenType.RBRACE)
        automata.addTransition(automata.startState, '}', rbraceState)

        val newlineState = automata.createAcceptingStateWithType(FormatFileTokenType.NEWLINE)
        automata.addTransition(automata.startState, '\n', newlineState)

        automata.addTransition(automata.startState, ' ', automata.breakState)
        automata.addTransition(automata.startState, '\t', automata.breakState)
    }
}