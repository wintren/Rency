package rocks.wintren.rency.util

import android.icu.util.Currency
import rocks.wintren.rency.util.extensions.capitalizeWords

class AppCurrencyHelper : CurrencyHelper {

    override fun getCurrencyName(currencyCode: String): String {
        return Currency.getInstance(currencyCode)
            .displayName
            .capitalizeWords()
    }
}