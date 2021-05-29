package generator

import automatas.*
import automatas.dfa.DFAAutomata
import automatas.nfa.NFAAutomata
import java.io.File
import api.*
import regex.RegexParser
import regex.visitors.AutomataVisitor


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

        tabs++
        generateDataDeclarations(format)
        generateResetFunction()
        generateGetTokenFunction(format)
        tabs--

        outputFile.appendTextWithTabs("}\n", tabs)
    }

    private fun generateDataDeclarations(format: Format) {
        for (line in format.dataCode.split("\n")) {
            if (line.isNotEmpty())
                outputFile.appendTextWithTabs("$line\n", tabs)
        }

        outputFile.appendTextWithTabs("var input = \"\"\n", tabs)
        outputFile.appendTextWithTabs("var current = 0\n\n", tabs)
    }

    private fun generateResetFunction() {
        generateFunction("reset", "input_: String", "Unit") {
            outputFile.appendTextWithTabs("input = input_\n", tabs)
            outputFile.appendTextWithTabs("current = 0\n", tabs)
        }
    }

    private fun generateGetTokenFunction(format: Format) {
        val (dfa, acceptingStatesToTypes) = generateDfaFromRules(format)

        generateFunction("getToken", "", "Token") {
            outputFile.appendTextWithTabs("var currentState = ${dfa.startState.id}\n", tabs)
            outputFile.appendTextWithTabs("var start = current\n", tabs)

            generateTypesMap(acceptingStatesToTypes)

            outputFile.appendTextWithTabs("\n", tabs)
            generateWhile("true") {
                outputFile.appendTextWithTabs("var newState = -1\n\n", tabs)
                generateIf("current < input.length") {
                    generateWhenDeclaration("currentState") {
                        for ((state, transition) in dfa.transitionTable.transitionTable) {
                            generateStateTransition(state, transition)
                        }
                    }
                }

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

    private fun generateStateTransition(state: State, transition: HashMap<TransitionCharacter, State>) {
        outputFile.appendTextWithTabs("${state.id} -> ", tabs)

        val oldTabs = tabs
        tabs = 0
        generateWhenDeclaration("input[current]") {
            tabs = oldTabs + 1
            for ((char, destState) in transition)
                outputFile.appendTextWithTabs("in '${char.characters.first}'..'${char.characters.last}' -> newState = ${destState.id}\n", tabs)
            outputFile.appendTextWithTabs("else -> newState = -1\n", tabs)
        }
        tabs = oldTabs
    }

    private fun generateDfaFromRules(format: Format): Pair<DFAAutomata, HashMap<State, String>> {
        val nfa = NFAAutomata()
        val alphabetSet = HashSet<TransitionCharacter>()
        val acceptingStatesToType = HashMap<State, String>()

        for (rule in format.rulesList) {
            val (ruleAst, alphabet) = RegexParser.parseRegex(rule.regex)
            alphabetSet.addAll(alphabet)
            val ruleNfa = ruleAst.accept(AutomataVisitor()) as NFAAutomata

            for (acceptingState in ruleNfa.helpingAcceptingStatesSet)
                acceptingStatesToType[acceptingState] = rule.regexType

            ruleNfa.addTransition(nfa.startState, TransitionCharacter.epsilon, ruleNfa.startState)
            nfa.uniteAutomatas(ruleNfa, true)
        }

        val (dfa, dfaToNfaStates) = nfa.convertToDFA(alphabetSet)

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