package generator

import api.Format
import automatas.*
import regex.RegexParser
import regex.visitors.NFAAutomataVisitor
import java.io.File


object KotlexerGenerator {
    lateinit var outputFile: File
    var tabs = 0

    fun generate(format: Format, outputFile_: File) {
        outputFile = outputFile_
        outputFile.writeText("")
        tabs = 0

        generateImportCode(format.importList)
        generateTokenClass(format)
        generateClassCode(format)
    }

    private fun generateImportCode(importList: ArrayList<String>) {
        for (importStatement in importList)
            appendTextWithTabs("import $importStatement\n")
        appendTextWithTabs("\n\n")
    }

    private fun generateTokenClass(format: Format) {
        appendTextWithTabs(
            "data class Token(val lexeme: String, val type: ${format.type}?, val start: Int)\n\n",
        )
    }

    private fun generateClassCode(format: Format) {
        appendTextWithTabs("class Kotlex {\n")

        val (dfa, acceptingStatesToTypes) = generateDfaFromRules(format)

        tabs++
        generateDataDeclarations(format, dfa)
        generateResetFunction()
        generateGetTokenFunction(dfa, acceptingStatesToTypes)
        tabs--

        appendTextWithTabs("}\n")
    }

    private fun generateDataDeclarations(format: Format, dfa: DFAAutomata) {
        for (line in format.dataCode.split("\n")) {
            if (line.isNotEmpty())
                appendTextWithTabs("$line\n")
        }

        appendTextWithTabs("var input = \"\"\n")
        appendTextWithTabs("var current = 0\n\n")

        generateTransitionTable(dfa)
    }

    private fun generateResetFunction() {
        generateFunction("reset", "input_: String", "Unit") {
            appendTextWithTabs("input = input_\n")
            appendTextWithTabs("current = 0\n")
        }
    }

    private fun generateGetTokenFunction(dfa: DFAAutomata, acceptingStatesToTypes: HashMap<State, String>) {
        generateFunction("getToken", "", "Token?") {
            appendTextWithTabs("var currentState = ${dfa.startState.id}\n")
            appendTextWithTabs("var start = current\n")
            generateTypesMap(acceptingStatesToTypes)

            appendTextWithTabs("\n")
            generateWhile("true") {
                appendTextWithTabs("if (current >= input.length + 1) return null\n\n")

                appendTextWithTabs(
                    "var newState = if (current < input.length) (kotlexTable[currentState] ?: " +
                            "HashMap()).getOrDefault(input[current], -1) else -1\n"
                )

                appendTextWithTabs("\n")
                generateIf("newState == -1 && currentState in acceptingStatesToTypes") {
                    appendTextWithTabs(
                        "return Token(input.substring(start until current), acceptingStatesToTypes[currentState], start)\n",
                    )
                }
                generateElseIf("newState == -1 && start == current") {
                    appendTextWithTabs("start++\n")
                }
                generateElseIf("newState == -1") {
                    appendTextWithTabs("throw IllegalStateException(\"Undefined token!\")\n")
                }
                generateElse {
                    appendTextWithTabs("currentState = newState\n")
                }

                appendTextWithTabs("current++\n")
            }
        }
    }

    private fun generateTypesMap(acceptingStatesToTypes: HashMap<State, String>) {
        appendTextWithTabs("val acceptingStatesToTypes = hashMapOf(\n")
        tabs++

        var lineLimit = 3
        val content = ArrayList<String>()
        for ((state, stateType) in acceptingStatesToTypes) {
            if (lineLimit == 0) {
                appendTextWithTabs("${content.joinToString { it }},\n")
                content.clear()
                lineLimit = 3
            }

            content.add("${state.id} to $stateType")
            lineLimit--
        }

        if (lineLimit != 5)
            appendTextWithTabs("${content.joinToString { it }},\n")

        tabs--
        appendTextWithTabs(")\n")
    }

    private fun generateTransitionTable(dfa: DFAAutomata) {
        appendTextWithTabs("val kotlexTable = HashMap<Int, HashMap<Char, Int>>()\n")

        for ((state, _) in dfa.transitionTable)
            generateNthStateTable(state, dfa)

        appendTextWithTabs("init {\n")
        tabs++

        for ((state, _) in dfa.transitionTable)
            appendTextWithTabs("set${state.id}State()\n")

        tabs--
        appendTextWithTabs("}\n\n")
    }

    private fun generateNthStateTable(state: State, dfa: DFAAutomata) {
        generateFunction("set${state.id}State", "", "Unit") {
            appendTextWithTabs("kotlexTable[${state.id}] = HashMap()\n")
            for ((transitionChar, destState) in (dfa.transit(state) ?: TransitionTable())) {
                generateStateTransition(state.id, transitionChar, destState.id)
            }
        }
    }

    private fun generateStateTransition(stateId: Int, transitionChar: TransitionCharacter, destId: Int) {
        if (transitionChar.isRange()) {
            appendTextWithTabs("for (char in '${transitionChar.characters.first}'..'${transitionChar.characters.last}')\n")
            tabs++
            appendTextWithTabs("kotlexTable[$stateId]!![char] = $destId")
            tabs--
        }
        else
            appendTextWithTabs("kotlexTable[$stateId]!!['${transitionChar.characters.first}'] = $destId")
        appendTextWithTabs("\n")
    }

    private fun generateDfaFromRules(format: Format): Pair<DFAAutomata, HashMap<State, String>> {
        val nfa = NFAAutomata()
        val alphabetSet = HashSet<TransitionCharacter>()
        val acceptingStatesToType = HashMap<State, String>()

        for (rule in format.rulesList) {
            val (ruleAst, alphabet) = RegexParser.parseRegex(rule.regex)
            alphabetSet.addAll(alphabet)
            val ruleNfa = ruleAst.accept(NFAAutomataVisitor()) as NFAAutomata

            for (acceptingState in ruleNfa.acceptingStates)
                acceptingStatesToType[acceptingState] = rule.regexType

            nfa.addStateToSet(nfa.startState, TransitionCharacter.EPSILON, ruleNfa.startState)
            nfa.uniteAutomatas(ruleNfa, true)
        }

        val (dfa, dfaToNfaStates) = nfa.convertToDFA(alphabetSet)
        dfa.minimize(alphabetSet)

        val acceptingDfaStatesType = HashMap<State, String>()
        for ((acceptingNfaState, tokenType) in acceptingStatesToType) {
            for ((dfaState, nfaStatesSet) in dfaToNfaStates) {
                if (acceptingNfaState in nfaStatesSet) {
                    acceptingDfaStatesType[dfaState] = tokenType
                }
            }
        }

        return dfa to acceptingDfaStatesType
    }

    private fun generateWhenDeclaration(whenCondition: String, bodyGenerator: () -> Unit) {
        appendTextWithTabs("when ($whenCondition) {\n")
        tabs++
        bodyGenerator()
        tabs--
        appendTextWithTabs("}\n")
    }

    private fun generateIf(ifCondition: String, bodyGenerator: () -> Unit) {
        appendTextWithTabs("if ($ifCondition) {\n")
        tabs++
        bodyGenerator()
        tabs--
        appendTextWithTabs("}\n")
    }

    private fun generateElseIf(ifCondition: String, bodyGenerator: () -> Unit) {
        appendTextWithTabs("else if ($ifCondition) {\n")
        tabs++
        bodyGenerator()
        tabs--
        appendTextWithTabs("}\n")
    }

    private fun generateElse(bodyGenerator: () -> Unit) {
        appendTextWithTabs("else {\n")
        tabs++
        bodyGenerator()
        tabs--
        appendTextWithTabs("}\n")
    }

    private fun generateFunction(name: String, parameters: String, returnType: String, bodyGenerator: () -> Unit) {
        appendTextWithTabs("fun $name($parameters): $returnType {\n")
        tabs++
        bodyGenerator()
        tabs--
        appendTextWithTabs("}\n\n")
    }

    private fun generateWhile(condition: String, bodyGenerator: () -> Unit) {
        appendTextWithTabs("while ($condition) {\n")
        tabs++
        bodyGenerator()
        tabs--
        appendTextWithTabs("}\n")
    }

    private fun appendTextWithTabs(text: String) {
        outputFile.appendText("${"\t".repeat(tabs)}$text")
    }
}