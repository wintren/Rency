package rocks.wintren.rency.models

import androidx.lifecycle.MutableLiveData
import rocks.wintren.rency.util.CurrencyUtil
import rocks.wintren.rency.util.SingleLiveEvent
import timber.log.Timber

class CurrencyDetailsItem(
    val flagUrl: String,
    val currencyTitle: String,
    val currencySubtitle: String,
    initialRate: Double,
    val onCurrencyClick: () -> Unit,
    val onAmountEdited: (currency: String, newAmount: String) -> Unit
) {
    private var started = false
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

    val editingDisabled: MutableLiveData<Boolean> = MutableLiveData(true)
    val setSelected: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val currencyRateAmount: String
        get() = CurrencyUtil.formatDisplayString(currencyAmount * currencyRate)

    private fun updateRate() {
        Timber.w("updateRate: $currencyRateAmount")
        currencyCalculationDisplay.value = currencyRateAmount

    }

    val currencyCalculationDisplay: MutableLiveData<String> = MutableLiveData(currencyRateAmount)

    fun start() {
        if (!started) {
            currencyCalculationDisplay.observeForever {
                onAmountEdited.invoke(currencyTitle, it)
            }
            started = !started
        }
    }

}