package regex

import automatas.TransitionCharacter
import automatas.toTransitionCharacter
import regex.ast.*

object RegexParser {
    private var current = 0
    private var tokens = ArrayList<RegexToken>()
    private val alphabet = HashSet<TransitionCharacter>()

    fun parseRegex(regex: String): Pair<RegexNode, HashSet<TransitionCharacter>> {
        tokens = RegexLexer.lex(regex)
        current = 0
        alphabet.clear()

        return parseUnion() to HashSet(alphabet)
    }

    // union ::= simple-RE ("|" simple-RE)*
    private fun parseUnion(endType: RegexType = RegexType.END): UnionNode {
        var unionNode = UnionNode()

        while (lookToken().type != endType) {
            val simpleRegexNode = parseSimpleRegex(endType)
            if (!unionNode.isFull())
                unionNode.addChild(simpleRegexNode)
            else
                unionNode = UnionNode(unionNode, simpleRegexNode)

            if (lookToken().type == RegexType.UNION) {
                getToken()
                match(lookToken().type != endType, "Expected $endType")
            }
        }

        return unionNode
    }

    // simple-RE ::= basic-RE*
    private fun parseSimpleRegex(endType: RegexType): SimpleRegexNode {
        var simpleRegexNode = SimpleRegexNode()

        while (lookToken().type != RegexType.UNION && lookToken().type != endType) {
            val basicRegexNode = parseBasicRegex()
            if (!simpleRegexNode.isFull())
                simpleRegexNode.addChild(basicRegexNode)
            else
                simpleRegexNode = SimpleRegexNode(simpleRegexNode, basicRegexNode)
        }

        return simpleRegexNode
    }

    // basic-RE ::= elementary-RE ("*" | "+" | "?")?
    private fun parseBasicRegex(): RegexNode {
        var elementaryNode = parseElementaryRegex()

        if (lookToken().type == RegexType.STAR || lookToken().type == RegexType.PLUS || lookToken().type == RegexType.QUESTION) {
            elementaryNode = when (lookToken().type) {
                RegexType.STAR   -> StarNode(elementaryNode)
                RegexType.PLUS   -> PlusNode(elementaryNode)
                else             -> QuestionNode(elementaryNode)
            }

            getToken()
        }

        return elementaryNode
    }

    // elementary-RE ::= "(" RE ")" | any | eos | char | set
    private fun parseElementaryRegex(): RegexNode {
        match(notEnd(), "Unexpected end of input")

        return when (lookToken().type) {
            RegexType.LBRACKET -> parseBracketedRegex()
            RegexType.ANY -> parseAnyNode()
            RegexType.EOS -> parseEos()
            RegexType.CHAR -> parseChar()
            RegexType.LSQUAREBRACKET -> parseSet()
            RegexType.STRING -> parseString()
            else -> panic("Unexpected token: ${lookToken()}")
        }
    }

    // "(" RE ")"
    private fun parseBracketedRegex(): RegexNode {
        match(getToken().type == RegexType.LBRACKET, "Expected \"(\"")
        val regexNode = parseUnion(RegexType.RBRACKET)
        match(getToken().type == RegexType.RBRACKET, "Expected \")\"")

        return regexNode
    }

    // any ::= "."
    private fun parseAnyNode(): RegexNode {
        match(getToken().type == RegexType.ANY, "Expected \".\"")
        return AnyNode()
    }

    // eos ::= "$"
    private fun parseEos(): RegexNode {
        match(getToken().type == RegexType.EOS, "Expected \"$\"")
        return EosNode()
    }

    // set ::= "[" ("^")? (set-item)* "]"
    private fun parseSet(): SetNode {
        match(getToken().type == RegexType.LSQUAREBRACKET, "Expected \"[\"")

        var negate = false
        if (lookToken().type == RegexType.EXCEPT) {
            negate = true
            getToken()
        }

        var setNode = SetNode(negate)
        while (notEnd() && lookToken().type != RegexType.RSQUAREBRACKET) {
            if (!setNode.isFull())
                setNode.addChild(parseSetItem())
            else
                setNode = SetNode(negate, setNode, parseSetItem())
        }

        match(getToken().type == RegexType.RSQUAREBRACKET, "Expected \"]\"")
        return setNode
    }

    // set-item ::= range | char
    private fun parseSetItem(): RangeNode {
        match(lookToken().type == RegexType.CHAR, "Expected char")
        return if (lookNextToken().type == RegexType.MINUS) parseRange() else {
            val charNode = parseChar()
            return RangeNode(charNode, charNode)
        }
    }

    // range ::= char "-" char
    private fun parseRange(): RangeNode {
        val leftCharNode = parseChar(false)
        match(getToken().type == RegexType.MINUS, "Expected \"-\"")
        val rightCharNode = parseChar(false)

        alphabet.add(TransitionCharacter(leftCharNode.char..rightCharNode.char))

        return RangeNode(leftCharNode, rightCharNode)
    }

    // char ::= any non metacharacter | "\" metacharacter
    private fun parseChar(updateAlphabet: Boolean = true): CharNode {
        if (!isMetacharacter(lookToken()) || notEnd(1)
            && lookToken().type == RegexType.BACKSLASH && isMetacharacter(lookNextToken())
        ) {
            val charNode = CharNode(getToken().lexeme[0])
            if (updateAlphabet)
                alphabet.add(charNode.char.toTransitionCharacter())
            return charNode
        }

        panic("Expected character")
    }

    private fun parseString(): StringNode {
        for (char in lookToken().lexeme)
            alphabet.add(char.toTransitionCharacter())
        return StringNode(getToken().lexeme)
    }

    private fun lookToken(): RegexToken = if (current < tokens.size) tokens[current] else RegexToken(
        "\b",
        RegexType.END
    )
    private fun lookNextToken(): RegexToken = if (current < tokens.size - 1) tokens[current + 1] else RegexToken(
        "\b",
        RegexType.EMPTY
    )
    private fun getToken(): RegexToken = if (current < tokens.size) tokens[current++] else RegexToken(
        "\b",
        RegexType.END
    )

    private fun match(condition: Boolean, message: String) {
        if (!condition)
            panic(message)
    }

    private fun panic(message: String): Nothing = throw RegexParseException(message)

    private fun isMetacharacter(token: RegexToken): Boolean
            = token.type in arrayListOf(
        RegexType.EXCEPT, RegexType.BACKSLASH, RegexType.BACKSLASH, RegexType.MINUS,
        RegexType.EOS, RegexType.EOS, RegexType.ANY, RegexType.QUESTION, RegexType.PLUS,
        RegexType.STAR, RegexType.UNION
    )

    private fun notEnd(offset: Int = 0): Boolean = current < tokens.size - offset
}