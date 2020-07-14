package rocks.wintren.rency.app.ratecalc

import android.content.res.Resources
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import rocks.wintren.rency.BR
import rocks.wintren.rency.R
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.models.CurrencyDetailsItem
import rocks.wintren.rency.repo.NoInternet
import rocks.wintren.rency.repo.ServerError
import rocks.wintren.rency.util.RxSchedulers
import rocks.wintren.rency.util.SingleLiveEvent
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem
import rocks.wintren.rency.util.databinding.adapter.mapToBindingAdapterItem
import rocks.wintren.rency.util.delay
import timber.log.Timber
import javax.inject.Inject

class RateCalcCoordinator @Inject constructor(
    private val interactor: RateCalcInteractor,
    private val rxSchedulers: RxSchedulers,
    private val resources: Resources
) {

    val events = SingleLiveEvent<RateCalcEvent>()
    private lateinit var viewModel: RateCalcViewModel
    private var disposables = CompositeDisposable()
    private var currencyUpdate: Disposable? = null

    // TODO, implement logic of showing rates*amount
    // Rework: hold on to the adapter items' models and modify them with LiveData. Better performance and easier

    fun init(viewModel: RateCalcViewModel) {
        this.viewModel = viewModel
    }

    fun startUpdates() {
        if (currencyUpdate != null) {
            viewModel.showLoadingEvent.value = false
            return Timber.i("Currency Update already running")
        }
        interactor.fetchCurrencyUpdates()
            .map(::convertCurrencyViewData)
            .observeOn(rxSchedulers.main())
            .doOnEach { viewModel.showLoadingEvent.value = false }
            .subscribe(::onCurrencyUpdateSuccess, ::onCurrencyUpdateError)
            .also { currencyUpdate = it }
    }

    fun stopUpdates() {
        currencyUpdate?.dispose()
        currencyUpdate = null
    }

    private fun onCurrencyUpdateSuccess(update: List<BindingAdapterItem>) {
        viewModel.adapter.submitList(update)
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
    }

    private fun onCurrencyClick(details: CurrencyDetails) {
        interactor.baseCurrencyCode = details.currencyCode
        stopUpdates()
        viewModel.adapter.submitList(convertCurrencyViewData(listOf(details)))
        events.value = RateCalcEvent.ScrollToTop
        delay(200) { startUpdates() }
    }

    private fun onCurrencyUpdateError(error: Throwable) {
        when (error) {
            is NoInternet -> events.value =
                RateCalcEvent.ToastToUser(resources.getString(R.string.error_internet_not_available))
            is ServerError -> events.value =
                RateCalcEvent.ToastToUser(resources.getString(R.string.error_server_error))
        }
        stopUpdates()
    }

    fun onCleared() {
        disposables.clear()
        currencyUpdate?.dispose()
    }

}

sealed class RateCalcEvent {
    data class ToastToUser(val message: String) : RateCalcEvent()
    object ScrollToTop : RateCalcEvent()
}