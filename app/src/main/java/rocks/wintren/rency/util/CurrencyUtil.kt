package rocks.wintren.rency.util

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

    class CurrencyCountryException(currency: String): Exception("No available country for currency $currency")

}
