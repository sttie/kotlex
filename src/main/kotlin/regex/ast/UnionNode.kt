package regex.ast

import regex.visitors.Visitor

class UnionNode(
    var left: RegexNode? = null,
    var right: RegexNode? = null
) : RegexNode() {
    override fun accept(visitor: Visitor): Any {
        return visitor.visitUnionNode(this)
    }

    fun isFull() = left != null && right != null

    fun addChild(child: RegexNode) {
        when {
            left == null  -> left = child
            right == null -> right = child
            else          -> throw IllegalStateException("Unable to add a child")
        }
    }

    override fun toString() = "UnionNode"
}