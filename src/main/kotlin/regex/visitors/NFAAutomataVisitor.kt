package regex.visitors

import automatas.*
import regex.ast.*


class NFAAutomataVisitor : Visitor() {
    override fun visitAnyNode(node: AnyNode): NFAAutomata {
        val anyNfa = NFAAutomata()
        val anyNfaAcceptingState = anyNfa.createAcceptingState()

        anyNfa.addStateToSet(anyNfa.startState, TransitionCharacter.ANY, anyNfaAcceptingState)

        return anyNfa
    }

    override fun visitEosNode(node: EosNode): NFAAutomata {
        TODO()
    }

    override fun visitPlusNode(node: PlusNode): NFAAutomata {
        val plusNfaAutomata = NFAAutomata()
        val plusAcceptingState = plusNfaAutomata.createAcceptingState()

        if (node.child == null)
            return plusNfaAutomata

        val underStarAutomata = (node.child ?: EmptyNode).accept(this) as NFAAutomata
        underStarAutomata.addStateToSet(underStarAutomata.getAcceptingState(),
            TransitionCharacter.EPSILON, underStarAutomata.startState)
        underStarAutomata.addStateToSet(underStarAutomata.getAcceptingState(),
            TransitionCharacter.EPSILON, plusAcceptingState)

        plusNfaAutomata.addStateToSet(plusNfaAutomata.startState,
            TransitionCharacter.EPSILON, underStarAutomata.startState)

        plusNfaAutomata.uniteAutomatas(underStarAutomata)

        return plusNfaAutomata
    }

    override fun visitRangeNode(node: RangeNode): NFAAutomata {
        val rangeNfaAutomata = NFAAutomata()
        val rangeAcceptingState = rangeNfaAutomata.createAcceptingState()

        rangeNfaAutomata.addStateToSet(rangeNfaAutomata.startState,
            TransitionCharacter(node.left.char..node.right.char), rangeAcceptingState)

        return rangeNfaAutomata
    }

    override fun visitSetNode(node: SetNode): NFAAutomata {
        val setAutomata = NFAAutomata()
        val setAcceptingState = setAutomata.createAcceptingState()

        val leftAutomata = (node.left ?: EmptyNode).accept(this) as NFAAutomata
        setAutomata.addStateToSet(setAutomata.startState, TransitionCharacter.EPSILON, leftAutomata.startState)
        setAutomata.uniteAutomatas(leftAutomata)
        setAutomata.addStateToSet(leftAutomata.getAcceptingState(), TransitionCharacter.EPSILON, setAcceptingState)

        val rightAutomata = (node.right ?: EmptyNode).accept(this) as NFAAutomata
        if (!rightAutomata.isEmpty()) {
            setAutomata.addStateToSet(setAutomata.startState, TransitionCharacter.EPSILON, rightAutomata.startState)
            setAutomata.uniteAutomatas(rightAutomata)
            setAutomata.addStateToSet(rightAutomata.getAcceptingState(), TransitionCharacter.EPSILON, setAcceptingState)
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
            mergeStates(simpleRegexAutomata, leftAutomata.getAcceptingState(), rightAutomata, rightAutomata.startState)
            simpleRegexAutomata.uniteAutomatas(rightAutomata)
            simpleRegexAutomata.removeState(rightAutomata.startState)

            simpleRegexAutomata.addAccepting(
                if (!rightAutomata.isEmpty()) rightAutomata.getAcceptingState()
                else leftAutomata.getAcceptingState()
            )
        } else {
            simpleRegexAutomata.addAccepting(leftAutomata.getAcceptingState())
        }

        return simpleRegexAutomata
    }

    override fun visitStarNode(node: StarNode): NFAAutomata {
        val starNfaAutomata = NFAAutomata()
        val starAcceptingState = starNfaAutomata.createAcceptingState()

        val underStarAutomata = (node.child ?: EmptyNode).accept(this) as NFAAutomata
        underStarAutomata.addStateToSet(underStarAutomata.getAcceptingState(), TransitionCharacter.EPSILON, underStarAutomata.startState)
        underStarAutomata.addStateToSet(underStarAutomata.getAcceptingState(), TransitionCharacter.EPSILON, starAcceptingState)

        starNfaAutomata.addStateToSet(starNfaAutomata.startState, TransitionCharacter.EPSILON, starAcceptingState)
        starNfaAutomata.addStateToSet(starNfaAutomata.startState, TransitionCharacter.EPSILON, underStarAutomata.startState)

        starNfaAutomata.uniteAutomatas(underStarAutomata)

        return starNfaAutomata
    }

    override fun visitQuestionNode(node: QuestionNode): NFAAutomata {
        val starNfaAutomata = NFAAutomata()
        val starAcceptingState = starNfaAutomata.createAcceptingState()

        val underStarAutomata = (node.child ?: EmptyNode).accept(this) as NFAAutomata
        underStarAutomata.addStateToSet(underStarAutomata.getAcceptingState(), TransitionCharacter.EPSILON, starAcceptingState)

        starNfaAutomata.addStateToSet(starNfaAutomata.startState, TransitionCharacter.EPSILON, starAcceptingState)
        starNfaAutomata.addStateToSet(starNfaAutomata.startState, TransitionCharacter.EPSILON, underStarAutomata.startState)

        starNfaAutomata.uniteAutomatas(underStarAutomata)

        return starNfaAutomata
    }

    // r = s | t
    override fun visitUnionNode(node: UnionNode): NFAAutomata {
        val unionNfaAutomata = NFAAutomata()
        val unionAcceptingState = unionNfaAutomata.createAcceptingState()

        val leftAutomata = (node.left ?: EmptyNode).accept(this) as NFAAutomata
        unionNfaAutomata.addStateToSet(unionNfaAutomata.startState, TransitionCharacter.EPSILON, leftAutomata.startState)
        unionNfaAutomata.uniteAutomatas(leftAutomata)
        unionNfaAutomata.addStateToSet(leftAutomata.getAcceptingState(), TransitionCharacter.EPSILON, unionAcceptingState)

        val rightAutomata = (node.right ?: EmptyNode).accept(this) as NFAAutomata

        if (!rightAutomata.isEmpty()) {
            unionNfaAutomata.addStateToSet(
                unionNfaAutomata.startState,
                TransitionCharacter.EPSILON,
                rightAutomata.startState
            )
            unionNfaAutomata.uniteAutomatas(rightAutomata)
            unionNfaAutomata.addStateToSet(
                rightAutomata.getAcceptingState(),
                TransitionCharacter.EPSILON,
                unionAcceptingState
            )
        }

        return unionNfaAutomata
    }

    override fun visitStringNode(node: StringNode): Any {
        val stringNfaAutomata = NFAAutomata()
        val stringNfaAcceptingState = stringNfaAutomata.createAcceptingState()

        var currentState = stringNfaAutomata.startState
        for (i in 0 until node.lexeme.length - 1) {
            val newState = stringNfaAutomata.createState()
            stringNfaAutomata.addStateToSet(currentState, node.lexeme[i].toTransitionCharacter(), newState)
            currentState = newState
        }

        stringNfaAutomata.addStateToSet(currentState, node.lexeme.last().toTransitionCharacter(), stringNfaAcceptingState)

        return stringNfaAutomata
    }

    // r = char
    override fun visitCharNode(node: CharNode): NFAAutomata {
        val charNfaAutomata = NFAAutomata()
        val charAcceptingState = charNfaAutomata.createAcceptingState()
        charNfaAutomata.addStateToSet(charNfaAutomata.startState,
            TransitionCharacter(node.char..node.char), charAcceptingState)
        return charNfaAutomata
    }

    override fun visitEmptyNode(node: EmptyNode) = NFAAutomata()

    private fun mergeStates(nfa1: NFAAutomata, state1: State, nfa2: NFAAutomata, state2: State) {
        if (nfa2.isEmpty())
            return
        else if (!nfa2.hasState(state2))
            throw IllegalStateException("lol what in mergeStates?")

        for ((char, toState) in nfa2.transit(state2)!!.table) {
            nfa1.addStatesToSet(state1, char, toState)
        }
    }
}