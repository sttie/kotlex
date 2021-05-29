package regex.ast

import regex.visitors.Visitor

class StringNode(val lexeme: String) : RegexNode() {
    override fun accept(visitor: Visitor) = visitor.visitStringNode(this)
}