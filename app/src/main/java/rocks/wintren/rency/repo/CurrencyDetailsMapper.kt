package rocks.wintren.rency.repo

import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.models.CurrencyRatesResponseDTO
import rocks.wintren.rency.util.CurrencyHelper
import rocks.wintren.rency.util.CurrencyUtil
import rocks.wintren.rency.util.FlagUtil
import javax.inject.Inject
import javax.inject.Singleton

typealias CurrencyRate = Pair<String, Double>

@Singleton
class CurrencyDetailsMapper @Inject constructor(
    private val currencyHelper: CurrencyHelper
) {

    fun mapUpdate(update: CurrencyRatesResponseDTO): List<CurrencyDetails> {
        return update.currencyRates
            .toList().toMutableList()
            .apply { add(0, Pair(update.baseCurrency, 1.0)) }
            .map { mapRateDetails(it) }
    }

    private fun mapRateDetails(currencyRate: CurrencyRate): CurrencyDetails {
        val currencyCode = currencyRate.first
        val countryCode = CurrencyUtil.countryCodeFromCurrencyCode(currencyCode)
        val flagUrl: String = FlagUtil.getFlagUrl(countryCode)

        return CurrencyDetails(
            currencyCode = currencyCode,
            currencyDisplayName = currencyHelper.getCurrencyName(currencyCode),
            countryCode = countryCode,
            rate = currencyRate.second,
            flagUrl = flagUrl
        )
    }

}