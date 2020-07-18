package rocks.wintren.rency.app.ratecalc

import androidx.lifecycle.MutableLiveData
import rocks.wintren.rency.util.CurrencyUtil

class CurrencyDetailsItem(
    val flagUrl: String,
    val titleCurrencyCode: String,
    val subtitleCurrencyName: String,
    initialRate: Double,
    val onCurrencyClick: () -> Unit,
    val onAmountEdited: (newAmount: String) -> Unit
) {
    private var started = false
    var userUpdate = true
    var currencyAmount: Double = 1.0
        set(value) {
            field = value
            updateRate()
        }
    var currencyRate: Double = initialRate
        set(value) {
            field = value
            updateRate()
        }
    private val currencyRateAmount: String
        get() = CurrencyUtil.formatDisplayString(currencyAmount * currencyRate)

    private fun updateRate() {
        userUpdate = false
        currencyCalculationDisplay.value = currencyRateAmount
    }

    val currencyCalculationDisplay: MutableLiveData<String> = MutableLiveData(currencyRateAmount)

    fun start() {
        if (!started) {
            currencyCalculationDisplay.observeForever {
                if (userUpdate)
                    onAmountEdited.invoke(it)
                else
                    userUpdate = true
            }
            started = !started
        }
    }

}