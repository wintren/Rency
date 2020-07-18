package rocks.wintren.rency

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import rocks.wintren.rency.repo.CurrencyDetailsMapper
import rocks.wintren.rency.util.CurrencyHelper

@RunWith(MockitoJUnitRunner::class)
class CurrencyDetailsMapperTest {

    @Mock
    private lateinit var mockCurrencyHelper: CurrencyHelper

    @Before
    fun setup() {
        `when`(mockCurrencyHelper.getCurrencyName("EUR"))
            .thenReturn("Euro")

        `when`(mockCurrencyHelper.getCurrencyName("SEK"))
            .thenReturn("Svensk Krona")

        `when`(mockCurrencyHelper.getCurrencyName("GBP"))
            .thenReturn("British Pound")

        `when`(mockCurrencyHelper.getCurrencyName("USD"))
            .thenReturn("US Dollar")
    }

    @Test
    fun addition_isCorrect() {
        val testSubject = CurrencyDetailsMapper(mockCurrencyHelper)

        val mappedUpdate = testSubject.mapUpdate(TestModels.responseDTO)
        assertEquals(TestModels.currencyDetails, mappedUpdate)

    }
}