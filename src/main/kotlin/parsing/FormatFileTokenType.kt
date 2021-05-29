package parsing

enum class FormatFileTokenType {
    IMPORT,
    ENUMTYPE,
    IS,
    DATA,
    RULES,
    ID,
    COLON,
    DOT,
    COMMA,
    ASSIGN,
    LBRACE,
    RBRACE,
    NEWLINE,
    END,
    EMPTY
}