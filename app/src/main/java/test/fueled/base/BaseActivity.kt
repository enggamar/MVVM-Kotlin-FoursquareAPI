package test.fueled.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import test.fueled.Constant.Constant
import test.fueled.model.ThumbsDownRestaurent
import test.fueled.model.User
import test.fueled.observables.ApiFailureObservable
import test.fueled.observables.ProgressDialogObservable.ProgressDialogObserver
import test.fueled.preference.AppPreferencesHelper
import test.fueled.utils.AppUtils


/**
 * The base activity of the application that all other activities extend
 */
public abstract class BaseActivity : AppCompatActivity() {
    private var mDialog: Dialog? = null
    private var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    init {
        database = Firebase.database.reference
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * This function is used to create user to firebase database
     **/
    open fun writeNewUser() {
        val user = User(AppPreferencesHelper.getInstance(this).getEmailId())
        database.child(Constant.FIREBASE_USER_NODE)
            .child(AppPreferencesHelper.getInstance(this).getUserId())
            .setValue(user)
    }

    /**
     * This function is used to add listener to read firebase data
     **/
    fun initListener(signleEvnetListener: ValueEventListener) {
        database.child(Constant.FIREBASE_USER_NODE)
            .child(AppPreferencesHelper.getInstance(this).getUserId())
            .child(Constant.RESTAURENT_ID)
            .addListenerForSingleValueEvent(signleEvnetListener)
    }

    /**
     * This function is used to add restaurentId to firebase
     **/
    open fun addThumbsDownRestaurentId(item: String) {
        database.child(Constant.FIREBASE_USER_NODE)
            .child(AppPreferencesHelper.getInstance(this).getUserId())
            .child(Constant.RESTAURENT_ID).push().setValue(ThumbsDownRestaurent(item));
    }


    /**
     * Used to register the observers in the base view model
     * @param baseViewModel the base view model
     */
    protected fun registerObservers(baseViewModel: BaseViewModel) {
        baseViewModel.failureObserver.observe(this, mApiFailureObserver)
        baseViewModel.progressDialogObserver.observe(this, mProgressDialogObserver)
    }


    /**
     * Observer for listening to progress change events
     */
    protected var mProgressDialogObserver =
        object : ProgressDialogObserver {
            override fun onProgressChanged(canShowProgress: Boolean) {
                if (canShowProgress) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
        }
    /**
     * Observer to listen to API failure events
     */
    protected var mApiFailureObserver: Observer<ApiFailureObservable?> =
        object : Observer<ApiFailureObservable?> {
            override fun onChanged(e: ApiFailureObservable?) {
                if (e != null) {
                    onApiFailure(e.code, e.errorMessage)
                }
            }
        }

    /**
     * Used to handle API failure
     * @param code the status code
     * @param message the error message
     */
    protected open fun onApiFailure(code: Int, message: String?) {

    }

    /**
     * Used to show the progress dialog
     */
    protected fun showProgress() {
        hideProgress()
        mDialog = AppUtils.getAppProgressDialog(this);
        mDialog!!.show()
    }

    /**
     * Used to dismiss the progress dialog
     */
    protected fun hideProgress() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

}