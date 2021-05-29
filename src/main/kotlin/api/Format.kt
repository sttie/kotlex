package api

data class Format(
    val importList: ArrayList<String>,
    val type: String,
    val dataCode: String,
    val rulesList: ArrayList<Rule>
)
