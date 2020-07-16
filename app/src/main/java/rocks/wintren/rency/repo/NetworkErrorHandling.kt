package rocks.wintren.rency.repo

import io.reactivex.Single
import java.net.ConnectException
import java.net.UnknownHostException

fun <T> Single<T>.convertErrors(): Single<T> {
    return this.onErrorResumeNext { error ->
        when (error) {
            is UnknownHostException,
            is ConnectException -> Single.error(NoInternet())
            else -> Single.error(ServerError(error))
        }
    }
}

class NoInternet : Exception("No Internet Available")
class ServerError(error: Throwable) : Exception("Some error from server: ${error.localizedMessage}")
