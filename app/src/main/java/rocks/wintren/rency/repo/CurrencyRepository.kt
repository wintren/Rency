package rocks.wintren.rency.repo

import io.reactivex.Single
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.util.RxSchedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyAPI: RevolutCurrencyAPI,
    private val schedulers: RxSchedulers,
    private val mapper: CurrencyUpdateMapper
) {

    fun fetchLatestCurrencyRatings(baseCurrency: String?): Single<List<CurrencyDetails>> {
        Timber.e("fetch thread: ${Thread.currentThread().name}")
        return currencyAPI
            .latestCurrencyRatings(baseCurrency ?: "EUR")
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.computation())
            .map { mapper.mapUpdate(it) }
            .convertErrors()
    }

}