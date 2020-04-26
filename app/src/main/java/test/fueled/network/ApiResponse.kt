package test.fueled.network

import com.google.gson.Gson
import retrofit2.adapter.rxjava.HttpException
import test.fueled.Constant.Constant
import java.io.IOException
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

/**
 * This object stores the details of the API response
 */
class ApiResponse<T> {

    private var code: Int
    private var errorMessage: String? = null
    internal var data: T? = null

    constructor(mData: T) {
        data = mData
        code = HttpsURLConnection.HTTP_OK
    }

    constructor(throwable: Throwable?) {
        val exception = throwable as HttpException
        val errorBody: ErrorBody
        try {
            errorBody =
                Gson().fromJson(exception.response().errorBody().toString(), ErrorBody::class.java)
            code = exception.code()
            errorMessage = errorBody.Message

        } catch (e: IOException) {
            code = HttpURLConnection.HTTP_UNAUTHORIZED
            errorMessage = Constant.SOMETHING_WENT_WRONG
        }
    }

    constructor(mCode: Int, mErrorMessage: String?) {
        code = mCode
        errorMessage = mErrorMessage
    }

}

