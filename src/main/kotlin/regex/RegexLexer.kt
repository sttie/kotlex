package regex

object RegexLexer {
    fun lex(regex: String): ArrayList<RegexToken> {
        val tokens = ArrayList<RegexToken>()
        val metacharacters = arrayListOf('$', '.', '?', '*', '+', '\\', '^', '-', '(', ')', '[', ']', '|')

        var index = 0
        while (index < regex.length) {
            if (regex[index].isWhitespace()) {
                index++
                continue
            }

            if (regex[index] == '"') {
                index++
                val start = index
                while (regex[index] != '"') { index++ }
                tokens.add(RegexToken(regex.substring(start until index), RegexType.STRING))
                index++
            }
            else {
                val type = when (regex[index]) {
                    '(' -> RegexType.LBRACKET
                    ')' -> RegexType.RBRACKET
                    '[' -> RegexType.LSQUAREBRACKET
                    ']' -> RegexType.RSQUAREBRACKET
                    '|' -> RegexType.UNION
                    '*' -> RegexType.STAR
                    '+' -> RegexType.PLUS
                    '?' -> RegexType.QUESTION
                    '.' -> RegexType.ANY
                    '$' -> RegexType.EOS
                    '-' -> RegexType.MINUS
                    '\\' -> RegexType.BACKSLASH
                    '^' -> RegexType.EXCEPT
                    !in metacharacters -> RegexType.CHAR
                    else -> RegexType.EMPTY
                }

                tokens.add(RegexToken(regex[index].toString(), type))
                index++
            }
        }

        return tokens
    }
}