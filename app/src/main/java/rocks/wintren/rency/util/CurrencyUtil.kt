package rocks.wintren.rency.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CurrencyUtil {

    fun countryCodeFromCurrencyCode(currencyCode: String): String {
        return when (currencyCode) {
            "SEK" -> "SE"
            "AUD" -> "AU"
            "BGN" -> "BG"
            "BRL" -> "BR"
            "CAD" -> "CA"
            "CHF" -> "CH"
            "CNY" -> "CN"
            "CZK" -> "CZ"
            "DKK" -> "DK"
            "EUR" -> "EU"
            "GBP" -> "GB"
            "HKD" -> "HK"
            "HRK" -> "HR"
            "HUF" -> "HU"
            "IDR" -> "ID"
            "ILS" -> "IL"
            "INR" -> "IN"
            "ISK" -> "IS"
            "JPY" -> "JP"
            "KRW" -> "KR"
            "MXN" -> "MX"
            "MYR" -> "MY"
            "NOK" -> "NO"
            "NZD" -> "NZ"
            "PHP" -> "PH"
            "PLN" -> "PL"
            "RON" -> "RO"
            "RUB" -> "RU"
            "SGD" -> "SG"
            "THB" -> "TH"
            "USD" -> "US"
            "ZAR" -> "ZA"
            else -> throw CurrencyCountryException(currencyCode)
        }
    }

    private val currencyFormatter = NumberFormat.getCurrencyInstance().apply {
        this as DecimalFormat
        minimumFractionDigits = 2
        // Not showing currency sign, locale irrelevant
        currency = Currency.getInstance("EUR")
        decimalFormatSymbols = decimalFormatSymbols.apply { currencySymbol = "" }
    }

    /**
     * The actual string and what's show in an EditText will be different!
     * This is based on how EditText handles decimals
     *
     * i.e 5000000 will be formatted as "5.000.000.00"
     * But the EditText will show "5 000 000.00"
     */
    fun formatDisplayString(amount: Double): String {
        return currencyFormatter.format(amount)
            .trim()
            // EditText only allows '.' but currencies uses ','
            .replace(',', '.')
    }

    fun parseCurrencyAmount(displayAmount: String): Double {
        return try {
            displayAmount
                .trim()
                .replace(" ", "")
                .toDouble()
        } catch (e: Exception) { // Any problems gets caught
            1.0
        }
    }

}

class CurrencyCountryException(currency: String) :
    Exception("No available country for currency $currency")
