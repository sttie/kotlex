package api

data class Rule(
    val regexId: String,
    val regex: String,
    val regexType: String,
    val actionCode: String
)