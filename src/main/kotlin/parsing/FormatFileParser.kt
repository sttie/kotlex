package parsing

import java.io.File
import java.lang.IllegalStateException
import api.*

object FormatFileParser {
    private lateinit var lexer: FormatFileLexer
    private lateinit var currentToken: FormatFileToken

    fun parseFile(file: File): Format {
        lexer = FormatFileLexer(file.readText())
        return startParser()
    }

    fun parseText(text: String): Format {
        lexer = FormatFileLexer(text)
        return startParser()
    }

    private fun startParser(): Format {
        currentToken = getToken()
        return parseFormatFile()
    }

    // format_file := import_list data_list rules_list
    private fun parseFormatFile(): Format {
        return Format(parseImportList(), parseTypeDeclaration(), parseDataList(), parseRulesList())
    }

    // import_list := (import_declaration newline)*
    private fun parseImportList(): ArrayList<String> {
        val importList = ArrayList<String>()

        while (currentToken.type != FormatFileTokenType.ENUMTYPE) {
            importList.add(parseImportDeclaration())
            match(currentToken.type, FormatFileTokenType.NEWLINE)
            lexer.skipNewlines()
            getToken()
        }

        return importList
    }

    private fun parseTypeDeclaration(): String {
        lexer.skipNewlines()

        match(currentToken.type, FormatFileTokenType.ENUMTYPE)
        getToken()
        match(currentToken.type, FormatFileTokenType.IS)
        getToken()

        return parsePath()
    }

    // import_declaration := "import" path
    private fun parseImportDeclaration(): String {
        match(currentToken.type, FormatFileTokenType.IMPORT)
        getToken()
        return parsePath()
    }

    // path := id ("." id)*
    private fun parsePath(): String {
        val path = StringBuilder()

        while (currentToken.type != FormatFileTokenType.NEWLINE) {
            match(currentToken.type, FormatFileTokenType.ID)
            path.append(currentToken.lexeme)
            getToken()

            if (currentToken.type != FormatFileTokenType.DOT)
                break

            path.append(".")
            getToken()
        }

        return path.toString()
    }

    // data_list := "data" ":" newline (data_declaration newline)*
    private fun parseDataList(): String {
        lexer.skipNewlines()
        if (currentToken.type == FormatFileTokenType.NEWLINE)
            getToken()

        match(currentToken.type, FormatFileTokenType.DATA)
        getToken()
        match(currentToken.type, FormatFileTokenType.COLON)
        getToken()
        match(currentToken.type, FormatFileTokenType.NEWLINE)
        lexer.skipNewlines()

        val dataDeclarations = StringBuilder()
        while (lookAhead().type != FormatFileTokenType.RULES) {
            dataDeclarations.append("${parseDataDeclaration()}\n")

            getToken()
            match(currentToken.type, FormatFileTokenType.NEWLINE)
            lexer.skipNewlines()
        }

        getToken()
        return dataDeclarations.toString()
    }

    // data_declaration := kotlin-declarations
    private fun parseDataDeclaration(): String {
        return lexer.getTextUntil('\n')
    }

    // rules_list  := "rules" ":" newline (rule_declaration newline)*
    private fun parseRulesList(): ArrayList<Rule> {
        match(currentToken.type, FormatFileTokenType.RULES)
        getToken()
        match(currentToken.type, FormatFileTokenType.COLON)
        getToken()
        match(currentToken.type, FormatFileTokenType.NEWLINE)
        getToken()

        val rulesList = ArrayList<Rule>()
        while (currentToken.type != FormatFileTokenType.END) {
            rulesList.add(parseRuleDeclaration())
            match(currentToken.type, FormatFileTokenType.NEWLINE)
            lexer.skipNewlines()
            getToken()
        }

        return rulesList
    }

    // rule_declaration := id ":=" regex "," id "{" kotlin-code "}"
    private fun parseRuleDeclaration(): Rule {
        match(currentToken.type, FormatFileTokenType.ID)
        val regexId = currentToken.lexeme

        getToken()
        match(currentToken.type, FormatFileTokenType.ASSIGN)

        lexer.skipWhitespaces()

        val regex = lexer.getTextUntil(',')

        getToken()
        match(currentToken.type, FormatFileTokenType.COMMA)
        getToken()

        val regexType = parsePath()
//
//        match(currentToken.type, FormatFileTokenType.LBRACE)
//        lexer.skipWhitespaces()
//
//        // parse kotlin code
//        val actionCode = lexer.getTextUntil('}')
//
//        getToken()
//        match(currentToken.type, FormatFileTokenType.RBRACE)

//        getToken()
        return Rule(regexId, regex, regexType, "")
    }

    private fun skipWhitespaces() {
        while (lookAhead().type == FormatFileTokenType.NEWLINE)
            getToken()
    }

    private fun match(left: FormatFileTokenType, right: FormatFileTokenType)
            = if (left != right) throw IllegalStateException("Expected $right, but got $left") else Unit
    private fun getToken(): FormatFileToken = run { currentToken = lexer.getToken(); currentToken }
    private fun lookAhead(): FormatFileToken = lexer.lookAhead()
}