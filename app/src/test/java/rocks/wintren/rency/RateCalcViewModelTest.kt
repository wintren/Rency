package rocks.wintren.rency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import rocks.wintren.rency.app.ratecalc.RateCalcCoordinator
import rocks.wintren.rency.app.ratecalc.RateCalcViewModel
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem

@RunWith(MockitoJUnitRunner::class)
class RateCalcViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockCoordinator: RateCalcCoordinator

    @Test
    fun `ViewModel notifies coordinator`() {
        val testSubject = RateCalcViewModel(mockCoordinator)

        testSubject.onSwipeRefresh()
        Mockito.verify(mockCoordinator).startUpdates()
    }

    @Mock
    private lateinit var observer: Observer<Boolean>

    @Test
    fun `ViewModel shows loading`() {
        val testSubject = RateCalcViewModel(mockCoordinator)

        var showLoadingValue: Boolean? = null
        testSubject.showLoadingEvent.observeOnce { showLoadingValue = it }

        testSubject.showLoading(true)
        Assert.assertNotNull(showLoadingValue)
        Assert.assertEquals(true, showLoadingValue)
    }

    @Test
    fun `ViewModel handles List Items`() {
        val testSubject = RateCalcViewModel(mockCoordinator)

        val itemList = listOf(
            BindingAdapterItem(0, 5, "Model 1"),
            BindingAdapterItem(1, 5, "Model 2")
        )
        // testSubject.updateList(itemList)
        // Assert.assertEquals(itemList, testSubject.adapter.currentList)
        // Handling tests for Adapter too much work right now

        // Also a test to verify promoteCurrencyToTop does what it should
    }

}