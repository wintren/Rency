package rocks.wintren.rency.repo

import com.google.gson.annotations.SerializedName

data class CurrencyRatesResponseDTO(
    @SerializedName("baseCurrency") val baseCurrency: String,
    @SerializedName("rates") val currencyRates: Map<String, Double>
)

