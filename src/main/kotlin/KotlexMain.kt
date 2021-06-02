import automatas.*
import generator.KotlexerGenerator
import parsing.FormatFileParser
import regex.RegexParser
import regex.visitors.NFAAutomataVisitor
import regex.visitors.PrintVisitor
import java.io.File


object KotlexMain {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2) {
            println("Usage: kotlex path/to/kotlexfile path/to/generator")
            runSomeTests()
        }
        else {
            val format = FormatFileParser.parseFile(File(args[0]))
            // KotlexerGenerator.generate(format, File(args[1]))
        }
    }

    // TODO: новый тайпэлиас для formatfileautomata
    private fun runSomeTests() {
        val format = FormatFileParser.parseFile(File("cpp_tokens.kotlex"))
        KotlexerGenerator.generate(format, File("CppLexer.kt"))

//        val regex1 = "\"for\""
//        val regex2 = "\"if\""
//
//        val (ast1, alphabet) = RegexParser.parseRegex(regex1)
//        val (ast2, alphabet2) = RegexParser.parseRegex(regex2)
//        alphabet.addAll(alphabet2)
//
//        ast1.accept(PrintVisitor())
//        ast2.accept(PrintVisitor())
//
//        val nfa = NFAAutomata()
//        val nfa1 = ast1.accept(NFAAutomataVisitor()) as NFAAutomata
//        val nfa2 = ast2.accept(NFAAutomataVisitor()) as NFAAutomata
//
//        nfa.addStateToSet(nfa.startState, TransitionCharacter.EPSILON, nfa1.startState)
//        nfa.uniteAutomatas(nfa1, true)
//
//        nfa.addStateToSet(nfa.startState, TransitionCharacter.EPSILON, nfa2.startState)
//        nfa.uniteAutomatas(nfa2, true)
//
//        val (dfa, _) = nfa.convertToDFA(alphabet)
//        println(dfa)
    }
}