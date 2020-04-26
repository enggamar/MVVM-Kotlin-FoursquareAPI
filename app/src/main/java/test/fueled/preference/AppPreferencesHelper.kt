package test.fueled.preference

import android.content.Context
import android.content.SharedPreferences
import com.celllink.data.prefs.SharedPreferenceManager
import test.fueled.Constant.Constant

class AppPreferencesHelper private constructor(val context: Context) : SharedPreferenceManager {


    private var mPref: SharedPreferences? = null

    init {
        mPref = context.getSharedPreferences(Constant.FUELED_PREF, Context.MODE_PRIVATE)
    }

    override fun getEmailId(): String {
        return mPref?.getString(Constant.EMAIL, "")!!
    }

    override fun saveEmailId(email: String) {
        mPref?.edit()?.putString(Constant.EMAIL, email)?.apply()
    }

    override fun getUserId(): String {
        return mPref?.getString(Constant.UID, "")!!
    }

    override fun saveUserId(uid: String) {
        mPref?.edit()?.putString(Constant.UID, uid)?.apply()
    }

    override fun clearAllPrefs() {
        val editor = mPref?.edit()
        editor?.clear()
        editor?.apply()
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreferencesHelper? = null

        fun getInstance(context: Context): AppPreferencesHelper {
            return INSTANCE
                ?: synchronized(this) {
                    AppPreferencesHelper(context).also {
                        INSTANCE = it
                    }
                }
        }
    }
}
