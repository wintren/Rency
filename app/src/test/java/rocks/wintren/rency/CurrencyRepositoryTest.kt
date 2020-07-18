package rocks.wintren.rency

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.repo.*
import rocks.wintren.rency.util.RxSchedulers
import rocks.wintren.rency.util.TestSchedulers
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class CurrencyRepositoryTest {

    @Mock
    private lateinit var mockApi: RevolutCurrencyAPI

    @Mock
    private lateinit var mockMapper: CurrencyDetailsMapper

    private val rxScheduler: RxSchedulers = TestSchedulers()

    @Test
    fun `Repository handles default base currency`() {
        `when`(mockApi.latestCurrencyRatings(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(TestModels.responseDTO))

        val testSubject = CurrencyRepository(mockApi, rxScheduler, mockMapper)
        val testObserver = TestObserver<List<CurrencyDetails>>()


        testSubject
            .fetchLatestCurrencyRatings("SEK")
            .subscribe(testObserver)
        Mockito.verify(mockApi).latestCurrencyRatings("SEK")

        testSubject
            .fetchLatestCurrencyRatings(null)
            .subscribe(testObserver)
        Mockito.verify(mockApi).latestCurrencyRatings("EUR")
    }

    @Test
    fun `Repository handle server errors`() {
        `when`(mockApi.latestCurrencyRatings(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(RuntimeException("i.e HTTPException")))
        val testSubject = CurrencyRepository(mockApi, rxScheduler, mockMapper)

        val testObserver = TestObserver<List<CurrencyDetails>>()

        testSubject
            .fetchLatestCurrencyRatings("BAD")
            .subscribe(testObserver)
        testObserver.assertError(ServerError::class.java)
    }

    @Test
    fun `Repository handles no internet`() {
        `when`(mockApi.latestCurrencyRatings(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(UnknownHostException("http://thatplace.com")))

        val testSubject = CurrencyRepository(mockApi, rxScheduler, mockMapper)
        val testObserver = TestObserver<List<CurrencyDetails>>()

        testSubject
            .fetchLatestCurrencyRatings("EUR")
            .subscribe(testObserver)
        testObserver.assertError(NoInternet::class.java)
    }
}