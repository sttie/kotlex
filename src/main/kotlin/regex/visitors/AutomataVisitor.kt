package regex.visitors

import automatas.State
import automatas.TransitionCharacter
import automatas.nfa.NFAAutomata
import automatas.uniteAutomatas
import regex.ast.*


class AutomataVisitor : Visitor() {
    override fun visitAnyNode(node: AnyNode): NFAAutomata {
        val anyNfa = NFAAutomata()
        val anyNfaAcceptingState = anyNfa.createAcceptingState()

        anyNfa.addTransition(anyNfa.startState, TransitionCharacter.any, anyNfaAcceptingState)

        return anyNfa
    }

    override fun visitEosNode(node: EosNode): NFAAutomata {
        return NFAAutomata()
    }

    override fun visitPlusNode(node: PlusNode): NFAAutomata {
        val plusNfaAutomata = NFAAutomata()
        val plusAcceptingState = plusNfaAutomata.createAcceptingState()

        if (node.child == null)
            return plusNfaAutomata

        val underStarAutomata = (node.child ?: EmptyNode).accept(this) as NFAAutomata
        underStarAutomata.addTransition(underStarAutomata.acceptingState, TransitionCharacter.epsilon, underStarAutomata.startState)
        underStarAutomata.addTransition(underStarAutomata.acceptingState, TransitionCharacter.epsilon, plusAcceptingState)

        plusNfaAutomata.addTransition(plusNfaAutomata.startState, TransitionCharacter.epsilon, underStarAutomata.startState)

        plusNfaAutomata.uniteAutomatas(underStarAutomata)

        return plusNfaAutomata
    }

    override fun visitRangeNode(node: RangeNode): NFAAutomata {
        val rangeNfaAutomata = NFAAutomata()
        val rangeAcceptingState = rangeNfaAutomata.createAcceptingState()

        rangeNfaAutomata.addTransition(
            rangeNfaAutomata.startState, TransitionCharacter(node.left.char..node.right.char), rangeAcceptingState
        )

        return rangeNfaAutomata
    }

    override fun visitSetNode(node: SetNode): NFAAutomata {
        val setAutomata = NFAAutomata()
        val setAcceptingState = setAutomata.createAcceptingState()

        val leftAutomata = (node.left ?: EmptyNode).accept(this) as NFAAutomata
        setAutomata.addTransition(setAutomata.startState, TransitionCharacter.epsilon, leftAutomata.startState)
        setAutomata.uniteAutomatas(leftAutomata)
        setAutomata.addTransition(leftAutomata.acceptingState, TransitionCharacter.epsilon, setAcceptingState)

        val rightAutomata = (node.right ?: EmptyNode).accept(this) as NFAAutomata
        if (!rightAutomata.isEmpty()) {
            setAutomata.addTransition(setAutomata.startState, TransitionCharacter.epsilon, rightAutomata.startState)
            setAutomata.uniteAutomatas(rightAutomata)
            setAutomata.addTransition(rightAutomata.acceptingState, TransitionCharacter.epsilon, setAcceptingState)
        }

        return setAutomata
    }

    override fun visitSimpleRegexNode(node: SimpleRegexNode): NFAAutomata {
        val leftAutomata = (node.left ?: EmptyNode).accept(this) as NFAAutomata
        val rightAutomata = (node.right ?: EmptyNode).accept(this) as NFAAutomata
        val simpleRegexAutomata = NFAAutomata()

        simpleRegexAutomata.startState = leftAutomata.startState
        simpleRegexAutomata.uniteAutomatas(leftAutomata)

        if (!rightAutomata.isEmpty()) {
            mergeStates(simpleRegexAutomata, leftAutomata.acceptingState, rightAutomata, rightAutomata.startState)
            simpleRegexAutomata.uniteAutomatas(rightAutomata)
            simpleRegexAutomata.removeState(rightAutomata.startState)

            simpleRegexAutomata.setAccepting(
                if (!rightAutomata.isEmpty()) rightAutomata.acceptingState
                else leftAutomata.acceptingState
            )
        } else {
            simpleRegexAutomata.setAccepting(leftAutomata.acceptingState)
        }

        return simpleRegexAutomata
    }

    override fun visitStarNode(node: StarNode): NFAAutomata {
        val starNfaAutomata = NFAAutomata()
        val starAcceptingState = starNfaAutomata.createAcceptingState()

        val underStarAutomata = (node.child ?: EmptyNode).accept(this) as NFAAutomata
        underStarAutomata.addTransition(underStarAutomata.acceptingState, TransitionCharacter.epsilon, underStarAutomata.startState)
        underStarAutomata.addTransition(underStarAutomata.acceptingState, TransitionCharacter.epsilon, starAcceptingState)

        starNfaAutomata.addTransition(starNfaAutomata.startState, TransitionCharacter.epsilon, starAcceptingState)
        starNfaAutomata.addTransition(starNfaAutomata.startState, TransitionCharacter.epsilon, underStarAutomata.startState)

        starNfaAutomata.uniteAutomatas(underStarAutomata)

        return starNfaAutomata
    }

    override fun visitQuestionNode(node: QuestionNode): NFAAutomata {
        val starNfaAutomata = NFAAutomata()
        val starAcceptingState = starNfaAutomata.createAcceptingState()

        val underStarAutomata = (node.child ?: EmptyNode).accept(this) as NFAAutomata
        underStarAutomata.addTransition(underStarAutomata.acceptingState, TransitionCharacter.epsilon, starAcceptingState)

        starNfaAutomata.addTransition(starNfaAutomata.startState, TransitionCharacter.epsilon, starAcceptingState)
        starNfaAutomata.addTransition(starNfaAutomata.startState, TransitionCharacter.epsilon, underStarAutomata.startState)

        starNfaAutomata.uniteAutomatas(underStarAutomata)

        return starNfaAutomata
    }

    // r = s | t
    override fun visitUnionNode(node: UnionNode): NFAAutomata {
        val unionNfaAutomata = NFAAutomata()
        val unionAcceptingState = unionNfaAutomata.createAcceptingState()

        val leftAutomata = (node.left ?: EmptyNode).accept(this) as NFAAutomata
        unionNfaAutomata.addTransition(unionNfaAutomata.startState, TransitionCharacter.epsilon, leftAutomata.startState)
        unionNfaAutomata.uniteAutomatas(leftAutomata)
        unionNfaAutomata.addTransition(leftAutomata.acceptingState, TransitionCharacter.epsilon, unionAcceptingState)

        val rightAutomata = (node.right ?: EmptyNode).accept(this) as NFAAutomata

        if (!rightAutomata.isEmpty()) {
            unionNfaAutomata.addTransition(
                unionNfaAutomata.startState,
                TransitionCharacter.epsilon,
                rightAutomata.startState
            )
            unionNfaAutomata.uniteAutomatas(rightAutomata)
            unionNfaAutomata.addTransition(
                rightAutomata.acceptingState,
                TransitionCharacter.epsilon,
                unionAcceptingState
            )
        }

        return unionNfaAutomata
    }

    // r = char
    override fun visitCharNode(node: CharNode): NFAAutomata {
        val charNfaAutomata = NFAAutomata()
        val charAcceptingState = charNfaAutomata.createAcceptingState()
        charNfaAutomata.addTransition(charNfaAutomata.startState, TransitionCharacter(node.char), charAcceptingState)
        return charNfaAutomata
    }

    override fun visitEmptyNode(node: EmptyNode) = NFAAutomata()

    private fun mergeStates(nfa1: NFAAutomata, state1: State, nfa2: NFAAutomata, state2: State) {
        if (nfa2.isEmpty())
            return
        else if (!nfa2.hasState(state2))
            throw IllegalStateException("lol what in mergeStates?")

        for ((char, toState) in nfa2.transit(state2)!!) {
            nfa1.addTransition(state1, char, toState)
        }
    }
}