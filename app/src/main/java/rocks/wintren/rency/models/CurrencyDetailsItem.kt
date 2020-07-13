package rocks.wintren.rency.models

data class CurrencyDetailsItem(
    val flagUrl: String,
    val currencyTitle: String,
    val currencySubtitle: String,
    val rate: String,
    val onCurrencyClick: () -> Unit
) {
    var rateIsEditable = false
}