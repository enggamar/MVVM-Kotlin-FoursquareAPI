package test.fueled.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fueled_lunch.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.taghawk.adapters.RestaurentListAdapter
import test.fueled.R
import test.fueled.base.BaseActivity
import test.fueled.databinding.ActivityHomeBinding
import test.fueled.model.Item
import test.fueled.model.NearByRestaurentResponse
import test.fueled.model.ThumbsDownRestaurent
import test.fueled.preference.AppPreferencesHelper
import test.fueled.utils.AppUtils
import test.fueled.viewmodel.HomeViewModel

class HomeActivity : BaseActivity(), AppUtils.Companion.ILogoutListener {

    private lateinit var mViewModel: HomeViewModel
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mAdapter: RestaurentListAdapter
    private lateinit var mList: ArrayList<Item>
    private lateinit var mThumbsDownList: ArrayList<ThumbsDownRestaurent>

    init {
        mList = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initListener(signleEvnetListener)
        setupViewModel()
        setUpList()
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(HomeViewModel::class.java)
        mViewModel.getListResponse()?.observe(this, mListObserver)
        mBinding.actionBar.model = this
        mBinding.model = mViewModel
        mViewModel.getThumbsDownObserver()!!.observe(this, Observer { item ->
            item.venue?.id?.let { addThumbsDownRestaurentId(it) }
            mThumbsDownList.add(ThumbsDownRestaurent(item.venue?.id!!))
            mAdapter.notifyItemRemoved(AppUtils.getPositionInList(mList, item))
            mList.remove(item)
        })
    }

    /**
     * This function is used to call api which gives list of nested restaurent
     * **/
    private fun callApi() {
        if (AppUtils.isInternetAvailable(this))
            mViewModel.getNearByRestaurentList()
    }


    private fun setUpList() {
        mBinding.rvList.layoutManager = LinearLayoutManager(this)
        mAdapter = RestaurentListAdapter(mViewModel, mList)
        mBinding.rvList.adapter = mAdapter
    }

    override fun onApiFailure(code: Int, message: String?) {
        AppUtils.showToast(message!!, this)
    }

    /**
     * This function is observe restaurent response
     * **/
    var mListObserver = Observer<NearByRestaurentResponse?>() { nearByRestaurentList ->

        if (nearByRestaurentList!!.response!!.groups != null && nearByRestaurentList.response!!.groups!!.size > 0) {
            mBinding.isRefresh = false
            nearByRestaurentList.response.groups!!.get(0).items?.let {
                mList.clear()
                if (mThumbsDownList.size > 0) {
                    mList.addAll(AppUtils.removeThumbsDownItemFromList(it, mThumbsDownList))
                } else {
                    mList.addAll(it)
                }
            }

            mAdapter.notifyDataSetChanged()
            mBinding.shimmerContaimner.stopShimmerAnimation()
            mBinding.shimmerContaimner.visibility = View.GONE
        }
    }

    /**
     * This function is used open dialog confirmation for logout
     * **/
    fun logOutAction() {
        AppUtils.showDialog(this, this)

    }


    override fun onYesClicked() {
        AppPreferencesHelper.getInstance(this).clearAllPrefs()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish()
    }

    override fun onResume() {
        super.onResume()
        mBinding.shimmerContaimner.startShimmerAnimation()
    }

    override fun onPause() {
        mBinding.shimmerContaimner.stopShimmerAnimation()
        super.onPause()
    }

    /**
     * setup firebase listener
     * **/
    val signleEvnetListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            mThumbsDownList = AppUtils.createArrayListFromDataSnap(dataSnapshot)
            callApi()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callApi()
        }
    }


}