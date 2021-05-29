package regex.ast

import regex.visitors.Visitor

class CharNode(
    val char: Char,
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitCharNode(this)
    override fun toString() = "CharNode $char"
}