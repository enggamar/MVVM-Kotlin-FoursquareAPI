package test.fueled.network

import retrofit2.http.GET
import retrofit2.http.QueryMap
import test.fueled.model.NearByRestaurentResponse

interface ApiInterface {
    @GET("venues/explore")
    fun getNearByRestaurent(@QueryMap queryMap: HashMap<String, Any>): rx.Observable<NearByRestaurentResponse?>?


}