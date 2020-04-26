package test.fueled.observables

/**
 * Used for sending signal to activity from view model in case of api failure.
 */
class ApiFailureObservable(val code: Int, val errorMessage: String)