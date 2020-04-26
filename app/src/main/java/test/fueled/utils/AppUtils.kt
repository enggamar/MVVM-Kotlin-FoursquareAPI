package test.fueled.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import test.fueled.R
import test.fueled.model.Item
import test.fueled.model.ThumbsDownRestaurent
import java.text.DecimalFormat


public class AppUtils {

    companion object {
        // This function is used to make activity fullscreen and make statusbar transparent
        fun makeActivityFullScreen(window: Window) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    statusBarColor = Color.TRANSPARENT
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        }

        /**
         * This function is used to create  spannable string(Bold with underline)
         * **/
        fun getSpannableString(mContext: Context, text: String): SpannableString? {
            val str = SpannableString(text)
            str.setSpan(UnderlineSpan(), 17, text.length, 0)
            str.setSpan(
                ForegroundColorSpan(Color.RED),
                17, text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return str
        }

        /**
         * This function is used to show toast
         * **/
        fun showToast(msg: String, mContext: Context) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }

        /**
         * This function is used to create loading dialog
         * **/
        fun getAppProgressDialog(context: Context?): Dialog? {
            val dialog = Dialog(context!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_app_progress)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            return dialog
        }

        /**
         * This function is used to check internet available or not
         * **/
        fun isInternetAvailable(ctx: Context): Boolean {
            val connectivityManager =
                ctx.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return !(networkInfo == null || !networkInfo.isConnected)
        }

        /**
         * This function is used to convert meter to KM
         * **/
        fun convertToKm(data: Double): String {
            val df = DecimalFormat("0.0")
            return df.format((data / 1000)) + " Km"
        }

        /**
         * This function is used to create logout confirmation dialog
         * **/
        fun showDialog(context: Context, logoutListner: ILogoutListener) {
            AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.logout))
                .setMessage(context.resources.getString(R.string.logout_desc))
                .setCancelable(false)
                .setPositiveButton(
                    context.resources.getString(R.string.yes_text)
                ) { dialog, id -> logoutListner.onYesClicked() }
                .setNegativeButton(context.resources.getString(R.string.no_text), null)
                .show()
        }

        /**
         * This function is used navigation in Google MAP
         * **/
        fun goToSpecificLocation(mContext: Context, latitude: String, longitude: String) {
            try {
                val latlong = "google.navigation:q=$latitude,$longitude"
                val gmmIntentUri: Uri = Uri.parse(latlong)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(mapIntent)
            } catch (e: Exception) {
                showToast(e.message.toString(), mContext)
            }
        }

        /**
         * This function is used to convert DataSnapshot to arraylist
         * **/
        fun createArrayListFromDataSnap(dataSanp: DataSnapshot): ArrayList<ThumbsDownRestaurent> {
            val mList = ArrayList<ThumbsDownRestaurent>()
            for (data in dataSanp.children) {
                val gson = Gson()
                try {
                    val jsonElement = gson.toJsonTree(data.getValue() as HashMap<*, *>)
                    val item = gson.fromJson<ThumbsDownRestaurent>(
                        jsonElement,
                        ThumbsDownRestaurent::class.java
                    )
                    mList.add(item)
                } catch (e: java.lang.Exception) {
                }

            }
            return mList;
        }

        /**
         * This function is used to remove the thumbsDown restaurent from response
         * **/
        fun removeThumbsDownItemFromList(
            itemList: ArrayList<Item>,
            thumbsDownList: ArrayList<ThumbsDownRestaurent>
        ): ArrayList<Item> {
            val mList = itemList
            for (id in thumbsDownList) {
                for (index in itemList.indices) {
                    if (id.restaurentId.equals(itemList.get(index).venue?.id)) {
                        mList.removeAt(index)
                        break
                    }
                }
            }
            return mList
        }

        /**
         * This function is used to get the position from object in arraylist and return position
         * **/
        fun getPositionInList(mlist: ArrayList<Item>, item: Item): Int {
            val position = mlist.indexOf(item)
            return position
        }

        interface ILogoutListener {
            fun onYesClicked()
        }
    }


}