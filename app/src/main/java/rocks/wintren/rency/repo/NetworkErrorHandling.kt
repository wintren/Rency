package rocks.wintren.rency.repo

import io.reactivex.Single
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException

fun <T> Single<T>.convertNetworkErrors(): Single<T> {
    return this.onErrorResumeNext { error ->
        Timber.e("Network Error: $error")
        when (error) {
            is UnknownHostException,
            is ConnectException -> Single.error(NoInternet())
            else -> Single.error(ServerError(error))
        }
    }
}

class NoInternet : Exception("No Internet Available")
class ServerError(error: Throwable) : Exception("Some error from server: ${error.localizedMessage}")
