package rocks.wintren.rency.app.ratecalc

import androidx.annotation.CheckResult
import io.reactivex.Observable
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.repo.CurrencyRepository
import rocks.wintren.rency.util.RxSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RateCalcInteractor @Inject constructor(
    private val repo: CurrencyRepository,
    private val rxSchedulers: RxSchedulers
) {

    var baseCurrencyCode: String = DEFAULT_CURRENCY

    @CheckResult
    fun fetchCurrencyUpdates(): Observable<List<CurrencyDetails>> {
        return Observable.interval(NO_DELAY, UPDATE_INTERVAL, TimeUnit.SECONDS)
            .observeOn(rxSchedulers.io())
            .flatMap { repo.fetchLatestCurrencyRatings(baseCurrencyCode).toObservable() }
    }

    private companion object {
        const val DEFAULT_CURRENCY = "EUR"
        const val UPDATE_INTERVAL = 1L
        const val NO_DELAY = 0L
    }

}