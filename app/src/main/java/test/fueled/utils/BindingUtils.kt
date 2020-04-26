package test.fueled.utils

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import test.fueled.R

object BindingUtils {
    @JvmStatic
    @BindingAdapter("bindtext")
    fun bindtext(view: AppCompatTextView, isSpan: Boolean) {
        view.text = AppUtils.getSpannableString(
            view.context,
            view.context.getString(R.string.i_am_a_new_user_signup)
        )
    }

    @JvmStatic
    @BindingAdapter("bindDistance")
    fun bindDistance(view: AppCompatTextView, distance: Double) {
        view.text = AppUtils.convertToKm(distance)
    }

    @JvmStatic
    @BindingAdapter("bindAddress")
    fun bindAddress(view: AppCompatTextView, mAddress: ArrayList<String>?) {
        if (mAddress != null && mAddress.size > 0) {
            var address: String = ""
            for (position in mAddress.indices) {
                if (position == 0)
                    address = address + mAddress.get(position)
                else
                    address = address + ", " + mAddress.get(position)

            }
            view.text = address
        }
    }
}