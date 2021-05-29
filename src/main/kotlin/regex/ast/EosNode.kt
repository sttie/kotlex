package regex.ast

import regex.visitors.Visitor

class EosNode(
    var child: RegexNode? = null
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitEosNode(this)
    override fun toString() = "EosNode"
}