package test.fueled.model

import java.util.*

data class Response(
    val headerLocation: String?,
    val headerFullLocation: String?,
    val headerLocationGranularity: String?,
    val query: String?,
    val groups: ArrayList<Group>?
)