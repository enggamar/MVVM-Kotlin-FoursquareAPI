package test.fueled.network

/**
 * Class corresponding to the error body received from server
 */
data class ErrorBody(var Code: Int, var Data: Any?, var Message: String)