package regex

enum class RegexType {
    UNION,
    STAR,
    PLUS,
    CHAR,
    LBRACKET,
    RBRACKET,
    LSQUAREBRACKET,
    RSQUAREBRACKET,
    MINUS,
    EOS,
    ANY,
    QUESTION,
    EXCEPT,
    BACKSLASH,
    EMPTY,
    END
}