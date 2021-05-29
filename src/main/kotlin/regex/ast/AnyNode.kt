package regex.ast

import regex.visitors.Visitor

class AnyNode(
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitAnyNode(this)
    override fun toString() = "AnyNode"
}