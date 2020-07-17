package rocks.wintren.rency.app.ratecalc

import rocks.wintren.rency.models.CurrencyDetails
import javax.inject.Inject

class CurrencyItemMapper @Inject constructor() {
    // Injection for mapper because it might depend on other mappers, resources etc.

    private lateinit var onCurrencyAmountEdited: (currency: String, userInput: String) -> Unit
    private lateinit var onCurrencyClick: (currency: CurrencyDetails) -> Unit

    fun init(
        onCurrencyAmountEdited: (currency: String, userInput: String) -> Unit,
        onCurrencyClick: (currency: CurrencyDetails) -> Unit
    ) {
        this.onCurrencyAmountEdited = onCurrencyAmountEdited
        this.onCurrencyClick = onCurrencyClick
    }

    fun mapCurrencyDetailItem(details: CurrencyDetails): CurrencyDetailsItem {
        return CurrencyDetailsItem(
            flagUrl = details.flagUrl,
            currencyTitle = details.currencyCode,
            currencySubtitle = details.currencyDisplayName,
            initialRate = details.rate,
            onCurrencyClick = { onCurrencyClick(details) },
            onAmountEdited = { onCurrencyAmountEdited.invoke(details.currencyCode, it) }
        )
    }


}