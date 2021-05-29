package regex.ast

import regex.visitors.Visitor

class PlusNode(
    var child: RegexNode? = null
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitPlusNode(this)
    override fun toString() = "PlusNode"
}