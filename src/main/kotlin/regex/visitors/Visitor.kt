package regex.visitors

import regex.ast.*

abstract class Visitor {
    open fun visitAnyNode(node: AnyNode): Any = Unit
    open fun visitCharNode(node: CharNode): Any = Unit
    open fun visitEosNode(node: EosNode): Any = Unit
    open fun visitPlusNode(node: PlusNode): Any = Unit
    open fun visitRangeNode(node: RangeNode): Any = Unit
    open fun visitSetNode(node: SetNode): Any = Unit
    open fun visitSimpleRegexNode(node: SimpleRegexNode): Any = Unit
    open fun visitStarNode(node: StarNode): Any = Unit
    open fun visitQuestionNode(node: QuestionNode): Any = Unit
    open fun visitUnionNode(node: UnionNode): Any = Unit
    open fun visitEmptyNode(node: EmptyNode): Any = Unit
}