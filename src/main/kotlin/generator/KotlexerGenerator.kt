package generator

import api.Format
import automatas.*
import regex.RegexParser
import regex.visitors.NFAAutomataVisitor
import java.io.File


fun File.appendTextWithTabs(text: String, tabs: Int) {
    appendText("${"\t".repeat(tabs)}$text")
}


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
            outputFile.appendTextWithTabs("import $importStatement\n", tabs)
        outputFile.appendTextWithTabs("\n\n", tabs)
    }

    private fun generateTokenClass(format: Format) {
        outputFile.appendTextWithTabs(
            "data class Token(val lexeme: String, val type: ${format.type}?, val start: Int)\n\n",
            tabs)
    }

    private fun generateClassCode(format: Format) {
        outputFile.appendTextWithTabs("class Kotlex {\n", tabs)

        val (dfa, alphabet, acceptingStatesToTypes) = generateDfaFromRules(format)

        tabs++
        generateDataDeclarations(format, dfa)
        generateResetFunction()
        generateGetTokenFunction(dfa, acceptingStatesToTypes)
        tabs--

        outputFile.appendTextWithTabs("}\n", tabs)
    }

    private fun generateDataDeclarations(format: Format, dfa: DFAAutomata) {
        for (line in format.dataCode.split("\n")) {
            if (line.isNotEmpty())
                outputFile.appendTextWithTabs("$line\n", tabs)
        }

        outputFile.appendTextWithTabs("var input = \"\"\n", tabs)
        outputFile.appendTextWithTabs("var current = 0\n\n", tabs)

        generateTransitionTable(dfa)
    }

    private fun generateResetFunction() {
        generateFunction("reset", "input_: String", "Unit") {
            outputFile.appendTextWithTabs("input = input_\n", tabs)
            outputFile.appendTextWithTabs("current = 0\n", tabs)
        }
    }

    private fun generateGetTokenFunction(dfa: DFAAutomata, acceptingStatesToTypes: HashMap<State, String>) {
        generateFunction("getToken", "", "Token?") {
            outputFile.appendTextWithTabs("var currentState = ${dfa.startState.id}\n", tabs)
            outputFile.appendTextWithTabs("var start = current\n", tabs)
            generateTypesMap(acceptingStatesToTypes)

            outputFile.appendTextWithTabs("\n", tabs)
            generateWhile("true") {
                outputFile.appendTextWithTabs("if (current >= input.length + 1) return null\n\n", tabs)

                outputFile.appendTextWithTabs(
                    "var newState = if (current < input.length) (kotlexTable[currentState] ?: HashMap()).getOrDefault(input[current], -1)"
                    + " else -1\n", tabs
                )

                outputFile.appendTextWithTabs("\n", tabs)
                generateIf("newState == -1 && currentState in acceptingStatesToTypes") {
                    outputFile.appendTextWithTabs(
                        "return Token(input.substring(start until current), acceptingStatesToTypes[currentState], start)\n",
                        tabs
                    )
                }
                generateElseIf("newState == -1 && start == current") {
                    outputFile.appendTextWithTabs("start++\n", tabs)
                }
                generateElseIf("newState == -1") {
                    outputFile.appendTextWithTabs("throw IllegalStateException(\"Undefined token!\")\n", tabs)
                }
                generateElse {
                    outputFile.appendTextWithTabs("currentState = newState\n", tabs)
                }

                outputFile.appendTextWithTabs("current++\n", tabs)
            }
        }
    }

    private fun generateTypesMap(acceptingStatesToTypes: HashMap<State, String>) {
        outputFile.appendTextWithTabs("val acceptingStatesToTypes = hashMapOf(\n", tabs)
        tabs++

        var lineLimit = 3
        val content = ArrayList<String>()
        for ((state, stateType) in acceptingStatesToTypes) {
            if (lineLimit == 0) {
                outputFile.appendTextWithTabs("${content.joinToString { it }},\n", tabs)
                content.clear()
                lineLimit = 3
            }

            content.add("${state.id} to $stateType")
            lineLimit--
        }

        if (lineLimit != 5)
            outputFile.appendTextWithTabs("${content.joinToString { it }},\n", tabs)

        tabs--
        outputFile.appendTextWithTabs(")\n", tabs)
    }

    private fun generateTransitionTable(dfa: DFAAutomata) {
        outputFile.appendTextWithTabs("val kotlexTable = HashMap<Int, HashMap<Char, Int>>()\n", tabs)

        for ((state, _) in dfa.transitionTable) {
            generateNthStateTable(state, dfa)
        }

        outputFile.appendTextWithTabs("init {\n", tabs)
        tabs++

        for ((state, _) in dfa.transitionTable) {
            outputFile.appendTextWithTabs("set${state.id}State()\n", tabs)
        }

        tabs--
        outputFile.appendTextWithTabs("}\n\n", tabs)
    }

    private fun generateNthStateTable(state: State, dfa: DFAAutomata) {
        generateFunction("set${state.id}State", "", "Unit") {
            outputFile.appendTextWithTabs("kotlexTable[${state.id}] = HashMap()\n", tabs)
            for ((transitionChar, destState) in (dfa.transit(state) ?: TransitionTable()).table) {
                generateStateTransition(state.id, transitionChar, destState.id)
            }
        }
    }

    private fun generateStateTransition(stateId: Int, transitionChar: TransitionCharacter, destId: Int) {
        if (transitionChar.isRange()) {
            outputFile.appendTextWithTabs("for (char in '${transitionChar.characters.first}'..'${transitionChar.characters.last}')\n", tabs)
            tabs++
            outputFile.appendTextWithTabs("kotlexTable[$stateId]!![char] = $destId", tabs)
            tabs--
        }
        else {
            outputFile.appendTextWithTabs("kotlexTable[$stateId]!!['${transitionChar.characters.first}'] = $destId", tabs)
        }
        outputFile.appendTextWithTabs("\n", 0)
    }

    private fun generateDfaFromRules(
        format: Format
    ): Triple<DFAAutomata, HashSet<TransitionCharacter>, HashMap<State, String>> {
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

        return Triple(dfa, alphabetSet, acceptingDfaStatesType)
    }

    private fun generateWhenDeclaration(whenCondition: String, bodyGenerator: () -> Unit) {
        outputFile.appendTextWithTabs("when ($whenCondition) {\n", tabs)
        tabs++
        bodyGenerator()
        tabs--
        outputFile.appendTextWithTabs("}\n", tabs)
    }

    private fun generateIf(ifCondition: String, bodyGenerator: () -> Unit) {
        outputFile.appendTextWithTabs("if ($ifCondition) {\n", tabs)
        tabs++
        bodyGenerator()
        tabs--
        outputFile.appendTextWithTabs("}\n", tabs)
    }

    private fun generateElseIf(ifCondition: String, bodyGenerator: () -> Unit) {
        outputFile.appendTextWithTabs("else if ($ifCondition) {\n", tabs)
        tabs++
        bodyGenerator()
        tabs--
        outputFile.appendTextWithTabs("}\n", tabs)
    }

    private fun generateElse(bodyGenerator: () -> Unit) {
        outputFile.appendTextWithTabs("else {\n", tabs)
        tabs++
        bodyGenerator()
        tabs--
        outputFile.appendTextWithTabs("}\n", tabs)
    }

    private fun generateFunction(name: String, parameters: String, returnType: String, bodyGenerator: () -> Unit) {
        outputFile.appendTextWithTabs("fun $name($parameters): $returnType {\n", tabs)
        tabs++
        bodyGenerator()
        tabs--
        outputFile.appendTextWithTabs("}\n\n", tabs)
    }

    private fun generateWhile(condition: String, bodyGenerator: () -> Unit) {
        outputFile.appendTextWithTabs("while ($condition) {\n", tabs)
        tabs++
        bodyGenerator()
        tabs--
        outputFile.appendTextWithTabs("}\n", tabs)
    }
}