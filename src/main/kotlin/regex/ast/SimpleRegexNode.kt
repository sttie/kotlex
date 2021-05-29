package regex.ast

import regex.visitors.Visitor

class SimpleRegexNode(
    var left: RegexNode? = null,
    var right: RegexNode? = null
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitSimpleRegexNode(this)

    fun isFull() = left != null && right != null

    fun addChild(child: RegexNode) {
        when {
            left == null  -> left = child
            right == null -> right = child
            else          -> throw IllegalStateException("Unable to add a child")
        }
    }

    override fun toString() = "SimpleRegexNode"
}