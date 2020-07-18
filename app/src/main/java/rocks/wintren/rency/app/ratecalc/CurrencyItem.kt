package rocks.wintren.rency.app.ratecalc

import androidx.lifecycle.MutableLiveData
import rocks.wintren.rency.util.CurrencyUtil

class CurrencyItem(
    val flagUrl: String,
    val titleCurrencyCode: String,
    val subtitleCurrencyName: String,
    initialRate: Double,
    val onCurrencyClick: () -> Unit,
    val onAmountEdited: (newAmount: String) -> Unit
) {
    private var started = false
    var currentTopItem = false
    var currencyAmount: Double = 1.0
        set(value) {
            field = value
            updateDisplayString()
        }
    var currencyRate: Double = initialRate
        set(value) {
            field = value
            updateDisplayString()
        }
    private val currencyRateAmount: String
        get() = CurrencyUtil.formatDisplayString(currencyAmount * currencyRate)

    private fun updateDisplayString() {
        rateDisplay.value = currencyRateAmount
    }

    val rateDisplay: MutableLiveData<String> = MutableLiveData(currencyRateAmount)

    fun start() {
        if (!started) {
            rateDisplay.observeForever {
                if (currentTopItem) onAmountEdited.invoke(it)
            }
            started = !started
        }
    }

}