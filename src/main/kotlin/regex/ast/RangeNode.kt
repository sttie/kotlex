package regex.ast

import regex.visitors.Visitor

class RangeNode(
    val left: CharNode,
    val right: CharNode,
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitRangeNode(this)
    override fun toString() = "RangeNode $left..$right"
}