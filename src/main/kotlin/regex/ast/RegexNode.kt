package regex.ast

import regex.visitors.Visitor

// TODO: реализуй equals с помощью визитора
abstract class RegexNode {
    abstract fun accept(visitor: Visitor): Any
}