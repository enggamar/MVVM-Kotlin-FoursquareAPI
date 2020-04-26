package test.fueled.model

import java.util.*
import kotlin.collections.ArrayList

data class Location(
    val lat: Double,
    val lng: Double, val
    labeledLatLngs: ArrayList<LabeledLatLng>?,
    val distance: Double,
    val state: String?,
    val formattedAddress: ArrayList<String>?
)