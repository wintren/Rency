package rocks.wintren.rency.models

data class CurrencyDetails(
    val currencyCode: String,
    val currencyDisplayName: String,
    val countryCode: String,
    val rate: Double,
    val flagUrl: String
)