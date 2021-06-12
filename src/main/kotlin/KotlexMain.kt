import generator.KotlexerGenerator
import parsing.FormatFileParser
import regex.RegexParser
import regex.visitors.PrintVisitor
import java.io.File


object KotlexMain {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty() || args.size > 2) {
            println("Usage:\n\t" +
                    "java -jar kotlex.jar path/to/kotlexfile path/to/generator\n\t" +
                    "java -jar kotlex.jar regular_expression")
        }
        if (args.size == 1) {
            val (ast, alphabet) = RegexParser.parseRegex(args[0])
            ast.accept(PrintVisitor())
        }
        else {
            val format = FormatFileParser.parseFile(File(args[0]))
            KotlexerGenerator.generate(format, File(args[1]))
        }
    }
}