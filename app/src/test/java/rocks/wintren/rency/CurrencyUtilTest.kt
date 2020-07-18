package rocks.wintren.rency

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import rocks.wintren.rency.util.CurrencyCountryException
import rocks.wintren.rency.util.CurrencyUtil.countryCodeFromCurrencyCode
import rocks.wintren.rency.util.CurrencyUtil.formatDisplayString
import rocks.wintren.rency.util.CurrencyUtil.parseCurrencyAmount

class CurrencyUtilTest {

    @Test
    fun `country code from currency code function`() {
        assertEquals("SE", countryCodeFromCurrencyCode("SEK"))
        assertEquals("GB", countryCodeFromCurrencyCode("GBP"))
        assertEquals("US", countryCodeFromCurrencyCode("USD"))
        assertEquals("EU", countryCodeFromCurrencyCode("EUR"))

        Assert.assertThrows(CurrencyCountryException::class.java) {
            countryCodeFromCurrencyCode("BAD")
        }
    }

    @Test
    fun `Currency format Display String`() {
        assertEquals("10.50", formatDisplayString(10.5))
        assertEquals("5.00", formatDisplayString(5.0))
        assertEquals("5.60", formatDisplayString(5.600))
        assertEquals("5.000.000.00", formatDisplayString(5000000.toDouble()))
    }

    @Test
    fun `Currency Display String parsing`() {
        assertEquals(10.5, parseCurrencyAmount("10.50"), 0.1)
        assertEquals(5000000.0, parseCurrencyAmount("5 000 000.00"), 0.1)
    }
}