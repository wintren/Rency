package rocks.wintren.rency.app

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import rocks.wintren.rency.BR
import rocks.wintren.rency.R
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.models.CurrencyDetailsItem
import rocks.wintren.rency.repo.CurrencyRepository
import rocks.wintren.rency.repo.NoInternet
import rocks.wintren.rency.repo.ServerError
import rocks.wintren.rency.util.RxSchedulers
import rocks.wintren.rency.util.SingleLiveEvent
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem
import rocks.wintren.rency.util.databinding.adapter.mapToBindingAdapterItem
import rocks.wintren.rency.util.delay
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: CurrencyRepository,
    private val rxSchedulers: RxSchedulers,
    private val resources: Resources
) : ViewModel() {

    var disposables = CompositeDisposable()
    var currencyUpdate: Disposable? = null
    var baseCurrencyCode = "SEK" // interactor
    val events = SingleLiveEvent<MainEvent>()
    // todo, There's enough logic for Coordinator/Interactor/ViewModel
    // ViewModel
    val showLoadingEvent = SingleLiveEvent<Boolean>()
    val scrollToTopEvent = SingleLiveEvent<Boolean>() // todo
    val adapter = CurrencyListAdapter() // TODO, Performance issue with Diff on main thread

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun startCurrencyUpdates() {
        if (currencyUpdate != null) {
            showLoadingEvent.value = false
            return Timber.i("Currency Update already running")
        }
        showLoadingEvent.value = true

        // TODO, implement logic of showing rates*amount
        // Rework: hold on to the adapter items' models and modify them with LiveData. Better performance and easier
        Observable.interval(0L, 1, TimeUnit.SECONDS)
            .observeOn(rxSchedulers.io())
            .flatMap { repo.fetchLatestCurrencyRatings(baseCurrencyCode).toObservable() }
            .observeOn(rxSchedulers.computation())
            .map(::convertCurrencyViewData)
            .observeOn(rxSchedulers.main())
            .doOnEach { showLoadingEvent.postValue(false) }
            .subscribe(::onCurrencyUpdateSuccess, ::onCurrencyUpdateError)
            .also { currencyUpdate = it }
    }

    fun stopCurrencyUpdates() {
        currencyUpdate?.dispose()
        currencyUpdate = null
    }

    private fun convertCurrencyViewData(currencyDetailsList: List<CurrencyDetails>): List<BindingAdapterItem> {
        return currencyDetailsList
            .mapIndexed { index, currencyDetails ->
                CurrencyDetailsItem(
                    flagUrl = currencyDetails.flagUrl,
                    currencyTitle = currencyDetails.currencyCode,
                    currencySubtitle = currencyDetails.currencyDisplayName,
                    rate = currencyDetails.rate.toString(),
                    onCurrencyClick = { onCurrencyClick(currencyDetails) }
                ).apply { rateIsEditable = index == 0 }
            }.mapToBindingAdapterItem(R.layout.item_currency_details, BR.item)
        // Todo, Adjust UI for Item
    }

    private fun onCurrencyUpdateSuccess(update: List<BindingAdapterItem>) {
        baseCurrencyCode = (update.first().model as CurrencyDetailsItem).currencyTitle
        adapter.submitList(update)
    }

    private fun onCurrencyClick(details: CurrencyDetails) {
        baseCurrencyCode = details.currencyCode
        stopCurrencyUpdates()
        adapter.submitList(convertCurrencyViewData(listOf(details)))
        scrollToTopEvent.value = true
        delay(600) { startCurrencyUpdates() }
    }

    private fun onCurrencyUpdateError(error: Throwable) {
        when (error) {
            is NoInternet -> events.value =
                MainEvent.ToastToUser(resources.getString(R.string.error_internet_not_available))
            is ServerError -> events.value =
                MainEvent.ToastToUser(resources.getString(R.string.error_server_error))
        }
        stopCurrencyUpdates()
    }

    sealed class MainEvent {
        data class ToastToUser(val message: String) : MainEvent()
    }

}