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

    fun formatDisplayString(amount: Double): String {
        return currencyFormatter.format(amount)
            .trim()
            // EditText only allows '.' but currencies want ','
            .replace(',', '.')
    }

    fun parseCurrencyAmount(displayAmount: String) : Double {
        return try {
            // EditText only allows '.' but currencies want ','
            val parsableFormat = displayAmount.replace('.', ',')
            NumberFormat.getInstance().parse(parsableFormat)!!.toDouble()
        } catch (e: Exception) { // Any problems gets caught
            1.0
        }
    }

    class CurrencyCountryException(currency: String) :
        Exception("No available country for currency $currency")

}
