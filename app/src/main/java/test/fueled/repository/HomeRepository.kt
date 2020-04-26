package test.fueled.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import test.fueled.Constant.Constant
import test.fueled.model.NearByRestaurentResponse
import test.fueled.network.ApiInterface
import test.fueled.network.ApiManager
import test.fueled.network.ApiResponse

class HomeRepository {

    fun getNearByRestaurentList(): LiveData<ApiResponse<NearByRestaurentResponse>> {
        val manager = ApiManager.instance
        val service = manager.createService(ApiInterface::class.java)
        val response: MutableLiveData<ApiResponse<NearByRestaurentResponse>> = MutableLiveData()
        service.getNearByRestaurent(getParms())?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ nearByRestaurentRespose ->
                response.value = ApiResponse(nearByRestaurentRespose!!)
            },
                { throwable ->
                    response.value = ApiResponse(throwable)
                })
        return response
    }

    /**
     * Create the request for getting nearest restaurent
     * **/
    private fun getParms(): HashMap<String, Any> {
        var parms = HashMap<String, Any>()
        parms.put(Constant.CLIENT_ID_KEY, Constant.CLIENT_ID_VALUE)
        parms.put(Constant.CLIENT_SECRET_KEY, Constant.CLIENT_SECRET_VALUE)
        parms.put(Constant.CATEGORY_ID, "4bf58dd8d48988d10f941735")
        parms.put(Constant.LAT_LNG, "28.557020,77.326240")
        parms.put(Constant.LIMIT, 50)
        parms.put(Constant.QUERY, "Restaurant")
        parms.put(Constant.SORT_BY_DISTANCE,1)
        parms.put(Constant.RADIUS, 10000)
        parms.put(Constant.VENUE_PHOTOS, 1)
        parms.put(Constant.VERSION, "20200426")
        return parms
    }
}