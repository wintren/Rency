package rocks.wintren.rency.repo

import io.reactivex.Single
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.util.RxSchedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyAPI: RevolutCurrencyAPI,
    private val schedulers: RxSchedulers,
    private val mapper: CurrencyDetailsMapper
) {

    fun fetchLatestCurrencyRatings(baseCurrency: String?): Single<List<CurrencyDetails>> {
        return currencyAPI
            .latestCurrencyRatings(baseCurrency ?: "EUR")
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.computation())
            .map { mapper.mapUpdate(it) }
            .convertNetworkErrors()
    }

}