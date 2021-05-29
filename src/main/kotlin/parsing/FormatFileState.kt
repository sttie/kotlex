package parsing


data class FormatFileState(var id: Int, var type: FormatFileTokenType = FormatFileTokenType.EMPTY) {
    override fun equals(other: Any?): Boolean = id == (other as FormatFileState).id && type == other.type
    override fun hashCode(): Int = super.hashCode()
}