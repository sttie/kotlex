package automatas

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import automatas.transition.*


fun NFAAutomata.epsilonClosure(state: State): HashSet<State>
    = epsilonClosure(hashSetOf(state))

fun NFAAutomata.epsilonClosure(states: HashSet<State>): HashSet<State> {
    val answer = HashSet<State>()
    val stateStack = Stack<State>()

    for (state in states) {
        answer.add(state)
        stateStack.push(state)
    }

    while (!stateStack.empty()) {
        for (state in transit(stateStack.pop(), TransitionCharacter.EPSILON) ?: HashSet()) {
            answer.add(state)
            stateStack.push(state)
        }
    }

    return answer
}

fun NFAAutomata.move(states: HashSet<State>, character: TransitionCharacter): HashSet<State> {
    val answer = HashSet<State>()
    states.forEach { state ->
        answer.addAll(transit(state, character) ?: HashSet())
    }
    return answer
}

fun NFAAutomata.uniteAutomatas(other: NFAAutomata, withAccepting: Boolean = false) {
    for ((from, transitionColumn) in other.transitionTable) {
        for ((char, to) in transitionColumn) {
            addTransition(from, char, to)
            if (withAccepting && other.acceptingStates.intersect(to).isNotEmpty())
                acceptingStates.addAll(to)
        }
    }
}


fun NFAAutomata.convertToDFA(
    alphabet: Collection<TransitionCharacter>
): Pair<DFAAutomata, HashMap<State, HashSet<State>>> {
    val dfaAutomata = DFAAutomata()

    val stateSetsId = HashMap<HashSet<State>, State>()
    val dfaStateToId = HashMap<State, HashSet<State>>()

    val almostDFAStartState = epsilonClosure(startState)
    stateSetsId[almostDFAStartState] = dfaAutomata.startState
    if (almostDFAStartState.any { isAccepting(it) })
        dfaAutomata.addAccepting(dfaAutomata.startState)

    val untrackedStates = Stack<HashSet<State>>()
    untrackedStates.push(almostDFAStartState)

    while (untrackedStates.isNotEmpty()) {
        val currentState = untrackedStates.pop()
        // Если мы еще не присвоили этому множеству свое DFA состояние, делаем это
        val currentDfaState: State = stateSetsId[currentState] ?: run {
            stateSetsId[currentState] =
                if (currentState.any { isAccepting(it) })
                    dfaAutomata.createAcceptingState()
                else
                    dfaAutomata.createState()
            stateSetsId[currentState]!!
        }

        if (!dfaStateToId.contains(currentDfaState)) {
            dfaStateToId[currentDfaState] = currentState
        }

        for (character in alphabet) {
            val uState = epsilonClosure(move(currentState, character))
            if (uState.size == 0)
                continue

            // Если мы еще не присвоили этому множеству свое DFA состояние, делаем это
            val dfaUState: State = stateSetsId[uState] ?: run {
                stateSetsId[uState] =
                    if (uState.any { isAccepting(it) })
                        dfaAutomata.createAcceptingState()
                    else
                        dfaAutomata.createState()
                stateSetsId[uState]!!
            }

            if (!dfaAutomata.hasState(dfaUState))
                untrackedStates.push(uState)

            // TODO: это точно верное условие?
            if (!dfaAutomata.hasTransition(currentDfaState, character))
                dfaAutomata.addTransition(currentDfaState, character, dfaUState)
        }
    }

    return dfaAutomata to dfaStateToId
}


fun DFAAutomata.minimize(alphabet: Collection<TransitionCharacter>) {

}