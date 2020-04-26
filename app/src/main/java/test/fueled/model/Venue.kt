package test.fueled.model

data class Venue(
    val id: String?,
    val name: String?,
    val location: Location?,
    val categories: List<Category>?,
    val photos: Photos?
)