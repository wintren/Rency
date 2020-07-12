package rocks.wintren.rency.util

object FlagUtil {

    private const val imageSourceFlatUrl = "https://www.countryflags.io/%s/flat/64.png"
    private const val imageSourceShinyUrl = "https://www.countryflags.io/%s/shine/64.png"

    /**
     * @param country needs to be a string of format ISO 3166-2, i.e SE, GB, EU
     */
    fun getFlagUrl(country: String) : String {
        return String.format(imageSourceFlatUrl, country)
    }

}