package rocks.wintren.rency

import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.models.CurrencyRatesResponseDTO

object TestModels {
    val responseDTO = CurrencyRatesResponseDTO(
        baseCurrency = "EUR",
        currencyRates = mapOf(
            "SEK" to 1.1,
            "USD" to 0.9,
            "GBP" to 0.8
        )
    )

    val currencyDetails: List<CurrencyDetails> = listOf(
        CurrencyDetails(
            currencyCode = "EUR",
            countryCode = "EU",
            currencyDisplayName = "Euro",
            rate = 1.0,
            flagUrl = "https://www.countryflags.io/EU/flat/64.png"
        ),
        CurrencyDetails(
            currencyCode = "SEK",
            currencyDisplayName = "Svensk Krona",
            countryCode = "SE",
            rate = 1.1,
            flagUrl = "https://www.countryflags.io/SE/flat/64.png"
        ),
        CurrencyDetails(
            currencyCode = "USD",
            currencyDisplayName = "US Dollar",
            countryCode = "US",
            rate = 0.9,
            flagUrl = "https://www.countryflags.io/US/flat/64.png"
        ),
        CurrencyDetails(
            currencyCode = "GBP",
            currencyDisplayName = "British Pound",
            countryCode = "GB",
            rate = 0.8,
            flagUrl = "https://www.countryflags.io/GB/flat/64.png"
        )
    )

}