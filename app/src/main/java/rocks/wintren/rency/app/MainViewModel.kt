package rocks.wintren.rency.app

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import rocks.wintren.rency.repo.CurrencyRepository
import rocks.wintren.rency.repo.NoInternet
import rocks.wintren.rency.repo.ServerError
import rocks.wintren.rency.util.RxSchedulers
import rocks.wintren.rency.util.SingleLiveEvent
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: CurrencyRepository,
    private val rxSchedulers: RxSchedulers
) : ViewModel() {

    var disposables = CompositeDisposable()
    var currencyUpdate: Disposable? = null
    var currency = "SEK"
    val events: SingleLiveEvent<MainEvent> = SingleLiveEvent()

    override fun onCleared() {
        super.onCleared()
    }

    fun startCurrencyUpdates() {
        if(currencyUpdate != null) return Timber.i("Currency Update already running")
        Observable.interval(0L, 5, TimeUnit.SECONDS)
            .flatMap { repo.fetchLatestCurrencyRatings(currency).toObservable() }
            .observeOn(rxSchedulers.main())
            .subscribe(
                { update ->
                    Timber.w("Update")
                    update.onEach { Timber.w(it.toString()) }
                },
                { error ->
                    when (error) {
                        is NoInternet -> events.value =
                            MainEvent.ToastToUser("No Internet Available, verify your connection and try again")
                        is ServerError -> events.value =
                            MainEvent.ToastToUser("We experienced a problem on our end, please try again later")
                    }
                    stopCurrencyUpdates()
                }
            ).also { currencyUpdate = it }
    }

    fun stopCurrencyUpdates() {
        currencyUpdate?.dispose()
        currencyUpdate = null
    }

    sealed class MainEvent {
        data class ToastToUser(val message: String) : MainEvent()
    }

}