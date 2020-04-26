package test.fueled.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import test.fueled.base.BaseViewModel
import test.fueled.model.Item
import test.fueled.model.Location
import test.fueled.model.NearByRestaurentResponse
import test.fueled.network.ApiResponse
import test.fueled.repository.HomeRepository
import test.fueled.utils.AppUtils

class HomeViewModel(application: Application) : BaseViewModel(application) {
    private var mContext: Context?
    private var mListResonse: MediatorLiveData<NearByRestaurentResponse>? = null
    private var mListDataSource: LiveData<ApiResponse<NearByRestaurentResponse>>? = null
    private var homeRepository: HomeRepository? = null
    private var thumbusDownLiveData: MutableLiveData<Item>? = null

    init {
        mContext = application.applicationContext
        mListResonse = MediatorLiveData()
        homeRepository = HomeRepository()
        thumbusDownLiveData = MutableLiveData()
    }

    fun getListResponse(): LiveData<NearByRestaurentResponse?>? {
        return mListResonse
    }

    fun getNearByRestaurentList() {
        mListDataSource = homeRepository?.getNearByRestaurentList()
        mListResonse?.addSource(mListDataSource!!, mListObserver)
    }

    var mListObserver = Observer<ApiResponse<NearByRestaurentResponse>>() { nearByRestaurentList ->
        if (nearByRestaurentList.data != null)
            mListResonse?.value = nearByRestaurentList.data;
    }

    fun goToMap(location: Location) {
        AppUtils.goToSpecificLocation(mContext!!, location.lat.toString(), location.lng.toString())
    }


    fun getThumbsDownObserver(): LiveData<Item>? {
        return thumbusDownLiveData
    }

    fun removeItemFromList(item: Item) {
        progressDialogObserver.value = true
        thumbusDownLiveData!!.value = item
    }

    override fun onCleared() {
        super.onCleared()
        if (mListDataSource != null) {
            mListResonse!!.removeSource(mListDataSource!!)
        }
    }
}