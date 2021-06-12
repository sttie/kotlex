package regex.visitors

import regex.ast.*
import java.util.*

class EqualityVisitor(private var currentNode: RegexNode) : Visitor() {
    private val nodes = Stack<RegexNode>()

    override fun visitAnyNode(node: AnyNode): Boolean
        = currentNode is AnyNode

    override fun visitCharNode(node: CharNode): Boolean {
        return currentNode is CharNode && node.char == (currentNode as CharNode).char
    }

    override fun visitEosNode(node: EosNode): Boolean
        = currentNode is EosNode

    override fun visitPlusNode(node: PlusNode): Boolean {
        if (currentNode !is PlusNode || !areBothNull(node.child, (currentNode as PlusNode).child)
            && !areBothNotNull(node.child, (currentNode as PlusNode).child))
            return false
        if (areBothNull(node.child, (currentNode as PlusNode).child))
            return true

        nodes.push(currentNode)
        currentNode = (currentNode as PlusNode).child!!
        val isEqual = node.child!!.accept(this) as Boolean
        currentNode = nodes.pop()
        return isEqual
    }

    override fun visitRangeNode(node: RangeNode): Boolean
        = currentNode is RangeNode && node.left == (currentNode as RangeNode).left
            && node.right == (currentNode as RangeNode).right

    override fun visitSetNode(node: SetNode): Boolean {
        if (currentNode !is SetNode || node.negate != (currentNode as SetNode).negate)
            return false
        if ((node.left == null) xor ((currentNode as SetNode).left == null)
                    || (node.right == null) xor ((currentNode as SetNode).right == null))
            return false

        var isEqual = true

        nodes.push(currentNode)
        if (!areBothNull(node.left, (currentNode as SetNode).left)) {
            currentNode = (currentNode as SetNode).left!!
            isEqual = isEqual && node.left!!.accept(this) as Boolean
            currentNode = nodes.pop()
        }
        if (!areBothNull(node.right, (currentNode as SetNode).right)) {
            currentNode = (currentNode as SetNode).right!!
            isEqual = isEqual && node.right!!.accept(this) as Boolean
            currentNode = nodes.pop()
        }

        return isEqual
    }

    override fun visitSimpleRegexNode(node: SimpleRegexNode): Boolean {
        if (currentNode !is SimpleRegexNode)
            return false
        if ((node.left == null) xor ((currentNode as SimpleRegexNode).left == null)
            || (node.right == null) xor ((currentNode as SimpleRegexNode).right == null))
            return false

        var isEqual = true

        nodes.push(currentNode)
        if (!areBothNull(node.left, (currentNode as SimpleRegexNode).left)) {
            currentNode = (currentNode as SimpleRegexNode).left!!
            isEqual = isEqual && node.left!!.accept(this) as Boolean
            currentNode = nodes.pop()
        }
        if (!areBothNull(node.right, (currentNode as SimpleRegexNode).right)) {
            currentNode = (currentNode as SimpleRegexNode).right!!
            isEqual = isEqual && node.right!!.accept(this) as Boolean
            currentNode = nodes.pop()
        }

        return isEqual
    }

    override fun visitStarNode(node: StarNode): Boolean {
        if (currentNode !is StarNode || !areBothNull(node.child, (currentNode as StarNode).child)
                                     && !areBothNotNull(node.child, (currentNode as StarNode).child))
            return false
        if (areBothNull(node.child, (currentNode as StarNode).child))
            return true

        nodes.push(currentNode)
        currentNode = (currentNode as StarNode).child!!
        val isEqual = node.child!!.accept(this) as Boolean
        currentNode = nodes.pop()
        return isEqual
    }

    override fun visitQuestionNode(node: QuestionNode): Boolean {
        if (currentNode !is QuestionNode || !areBothNull(node.child, (currentNode as QuestionNode).child)
            && !areBothNotNull(node.child, (currentNode as QuestionNode).child))
            return false
        if (areBothNull(node.child, (currentNode as QuestionNode).child))
            return true

        nodes.push(currentNode)
        currentNode = (currentNode as QuestionNode).child!!
        val isEqual = node.child!!.accept(this) as Boolean
        currentNode = nodes.pop()
        return isEqual
    }

    override fun visitUnionNode(node: UnionNode): Boolean {
        if (currentNode !is UnionNode)
            return false
        if ((node.left == null) xor ((currentNode as UnionNode).left == null)
            || (node.right == null) xor ((currentNode as UnionNode).right == null))
            return false

        var isEqual = true

        nodes.push(currentNode)
        if (!areBothNull(node.left, (currentNode as UnionNode).left)) {
            currentNode = (currentNode as UnionNode).left!!
            isEqual = isEqual && node.left!!.accept(this) as Boolean
            currentNode = nodes.pop()
        }
        if (!areBothNull(node.right, (currentNode as UnionNode).right)) {
            currentNode = (currentNode as UnionNode).right!!
            isEqual = isEqual && node.right!!.accept(this) as Boolean
            currentNode = nodes.pop()
        }

        return isEqual
    }

    override fun visitStringNode(node: StringNode): Boolean
        = currentNode is StringNode && node.lexeme == (currentNode as StringNode).lexeme

    override fun visitEmptyNode(node: EmptyNode): Boolean = true

    private fun areBothNull(left: RegexNode?, right: RegexNode?): Boolean
        = left == null && right == null
    private fun areBothNotNull(left: RegexNode?, right: RegexNode?): Boolean
            = left != null && right != null
}