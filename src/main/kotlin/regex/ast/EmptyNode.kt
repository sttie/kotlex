package regex.ast

import regex.visitors.Visitor

object EmptyNode : RegexNode() {
    override fun accept(visitor: Visitor) = visitor.visitEmptyNode(this)
    override fun toString(): String = "EmptyNode"
}