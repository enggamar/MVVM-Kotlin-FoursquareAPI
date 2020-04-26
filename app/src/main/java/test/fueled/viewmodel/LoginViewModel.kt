package test.fueled.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import test.fueled.Constant.Constant
import test.fueled.base.BaseViewModel
import test.fueled.preference.AppPreferencesHelper
import test.fueled.utils.AppUtils

open class LoginViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var mContext: Context;
    var email: String = ""
    var password: String = ""
    private var loginSignUpViewModel: MutableLiveData<Boolean>? = null

    init {
        mContext = application.applicationContext
        loginSignUpViewModel = MutableLiveData()
    }

    fun getLoginResponse(): LiveData<Boolean?>? {
        return loginSignUpViewModel
    }


    fun loginClicked() {
        if (validate(email, password)) {
            progressDialogObserver.value = true
            loginSignUpViewModel?.value = true;
        }
    }

    fun signupClicked() {
        loginSignUpViewModel?.value = false;
    }

    public fun validate(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                AppUtils.showToast(Constant.EMAIL_EMPTY, mContext)
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                AppUtils.showToast(Constant.EMAIL_INVALID, mContext)
                return false
            }
            password.isEmpty() -> {
                AppUtils.showToast(Constant.PASSWORD_EMPTY, mContext)
                return false
            }
            password.length < 6 -> {
                AppUtils.showToast(Constant.PASSWORD_INVLIAD, mContext)
                return false
            }
        }

        return true
    }

    fun onEmailTextChanged(s: CharSequence) {
        email = s.toString()
    }

    fun onPasswordTextChanged(s: CharSequence) {
        password = s.toString()
    }

    fun saveToPrefence(user: FirebaseUser) {
        AppPreferencesHelper.getInstance(mContext).saveEmailId(user.email!!)
        AppPreferencesHelper.getInstance(mContext).saveUserId(user.uid)

    }

}
