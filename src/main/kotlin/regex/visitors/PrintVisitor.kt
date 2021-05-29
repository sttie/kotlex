package regex.visitors

import regex.ast.*

class PrintVisitor : Visitor() {
    var indentation = 0

    override fun visitAnyNode(node: AnyNode) {
        printNode(node)
    }

    override fun visitCharNode(node: CharNode) {
        printNode(node)
    }

    override fun visitEosNode(node: EosNode) {
        printNode(node)
        processIndentation(arrayListOf(node.child ?: EmptyNode))
    }

    override fun visitPlusNode(node: PlusNode) {
        printNode(node)
        processIndentation(arrayListOf(node.child ?: EmptyNode))
    }

    override fun visitRangeNode(node: RangeNode) {
        printNode(node)
    }

    override fun visitSetNode(node: SetNode) {
        printNode(node)
        processIndentation(arrayListOf(node.left ?: EmptyNode, node.right ?: EmptyNode))
    }

    override fun visitSimpleRegexNode(node: SimpleRegexNode) {
        printNode(node)
        processIndentation(arrayListOf(node.left ?: EmptyNode, node.right ?: EmptyNode))
    }

    override fun visitStarNode(node: StarNode) {
        printNode(node)
        processIndentation(arrayListOf(node.child ?: EmptyNode))
    }

    override fun visitQuestionNode(node: QuestionNode) {
        printNode(node)
        processIndentation(arrayListOf(node.child ?: EmptyNode))
    }

    override fun visitUnionNode(node: UnionNode) {
        printNode(node)
        processIndentation(arrayListOf(node.left ?: EmptyNode, node.right ?: EmptyNode))
    }

    private fun printNode(node: RegexNode) {
        if (indentation != 0) {
            print("${"\t".repeat(indentation)}|")
        }
        println(node)
    }

    private fun processIndentation(children: ArrayList<RegexNode>) {
        indentation++
        for (child in children)
            child.accept(this)
        indentation--
    }
}