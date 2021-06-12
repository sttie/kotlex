package regex.visitors

import regex.ast.*

/*
    Possible simplifications:
    1. regex** == regex*
    2. (regex*)+ == regex*
    3. UnionNode that has only one SimpleRegexNode as child can be just removed (it can be done in parser actually)
    4. (s | s) == s
    5. (ab | ac) == a(b | c)
 */

class SimplifierVisitor : Visitor() {
    override fun visitAnyNode(node: AnyNode): Any = Unit
    override fun visitCharNode(node: CharNode): Any = Unit
    override fun visitEosNode(node: EosNode): Any = Unit

    override fun visitPlusNode(node: PlusNode) {
        if (node.child is StarNode) {
            node.child = (node.child as StarNode).child
        }
    }

    override fun visitRangeNode(node: RangeNode): Any = Unit
    override fun visitSetNode(node: SetNode): Any = Unit
    override fun visitSimpleRegexNode(node: SimpleRegexNode): Any = Unit

    override fun visitStarNode(node: StarNode) {
        if (node.child is StarNode) {
            node.child = (node.child as StarNode).child
        }
    }

    override fun visitQuestionNode(node: QuestionNode): Any = Unit

    override fun visitUnionNode(node: UnionNode) {
        if (node.left != null && node.right != null && node.left!!.accept(EqualityVisitor(node.right!!)) as Boolean) {
            node.right = null
        }
    }

    override fun visitStringNode(node: StringNode): Any = Unit
    override fun visitEmptyNode(node: EmptyNode): Any = Unit
}