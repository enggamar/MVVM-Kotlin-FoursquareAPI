package test.fueled.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import test.fueled.observables.ApiFailureObservable
import test.fueled.observables.ProgressDialogObservable
import test.fueled.observables.SingleLiveEvent

open class BaseViewModel
    (application: Application) : AndroidViewModel(application) {

    /**
     * LiveData for ProgressDialogObservable
     */
    var progressDialogObserver = ProgressDialogObservable()
        protected set

    /**
     * LiveData for ApiFailureObservable
     */
    var failureObserver =
        SingleLiveEvent<ApiFailureObservable>()
        protected set

    override fun onCleared() {
        super.onCleared()
        progressDialogObserver.value = false
    }
}