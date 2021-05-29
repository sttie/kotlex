package regex.ast

import regex.visitors.Visitor

class StarNode(
    var child: RegexNode?
) : RegexNode() {
    override fun accept(visitor: Visitor) = visitor.visitStarNode(this)
    override fun toString() = "StarNode"
}