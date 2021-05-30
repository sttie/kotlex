import generator.KotlexerGenerator
import parsing.FormatFileParser
import java.io.File


object KotlexMain {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 2)
            println("Usage: kotlex path/to/klexfile path/to/generator")

        val format = FormatFileParser.parseFile(File(args[0]))
        KotlexerGenerator.generate(format, File(args[1]))
    }
}