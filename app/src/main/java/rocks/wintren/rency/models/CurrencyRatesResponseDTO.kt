package rocks.wintren.rency.models

import com.google.gson.annotations.SerializedName

data class CurrencyRatesResponseDTO(
    @SerializedName("baseCurrency") val baseCurrency: String,
    @SerializedName("rates") val currencyRates: Map<String, Double>
)

