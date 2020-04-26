package test.fueled.observables
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * Used for sending signal to activity from view model for showing progress dialog.
 */
class ProgressDialogObservable : SingleLiveEvent<Boolean?>() {

    fun observe(owner: LifecycleOwner?, observer: ProgressDialogObserver) {
        super.observe(owner!!, Observer { aBoolean ->
            if (aBoolean == null) {
                return@Observer
            }
            observer.onProgressChanged(aBoolean)
        })
    }


    interface ProgressDialogObserver {
        fun onProgressChanged(canShowProgress: Boolean)
    }
}