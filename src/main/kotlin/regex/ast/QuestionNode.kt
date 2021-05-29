package regex.ast

import regex.visitors.*

class QuestionNode(
    var child: RegexNode? = null
) : RegexNode() {
    override fun accept(visitor: Visitor): Any = visitor.visitQuestionNode(this)
    override fun toString() = "QuestionNode"
}