package rocks.wintren.rency.repo

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import rocks.wintren.rency.repo.CurrencyRatesResponseDTO

interface RevolutCurrencyAPI {

    @GET("/api/android/latest")
    fun latestCurrencyRatings(@Query("base") baseCurrency: String): Single<CurrencyRatesResponseDTO>

}