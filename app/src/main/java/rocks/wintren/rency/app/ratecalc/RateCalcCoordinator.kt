package rocks.wintren.rency.app.ratecalc

import android.content.res.Resources
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import rocks.wintren.rency.BR
import rocks.wintren.rency.R
import rocks.wintren.rency.app.ratecalc.RateCalcEvent.ToastToUser
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.models.CurrencyDetailsItem
import rocks.wintren.rency.repo.NoInternet
import rocks.wintren.rency.repo.ServerError
import rocks.wintren.rency.util.CurrencyUtil
import rocks.wintren.rency.util.RxSchedulers
import rocks.wintren.rency.util.SingleLiveEvent
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
    private val currencyItemsMap: MutableMap<String, CurrencyDetailsItem> = mutableMapOf()

    fun init(viewModel: RateCalcViewModel) {
        this.viewModel = viewModel
    }

    fun startUpdates() {
        if (currencyUpdate != null) {
            viewModel.showLoadingEvent.value = false
            return Timber.i("Currency Update already running")
        }
        interactor.fetchCurrencyUpdates()
            .map { items ->
                items.apply {
                    forEach {
                        if (!currencyItemsMap.containsKey(it.currencyCode)) {
                            currencyItemsMap[it.currencyCode] = createCurrencyItem(it)
                        }
                    }
                }
            }
            .observeOn(rxSchedulers.main())
            .doOnEach { viewModel.showLoadingEvent.value = false }
            .subscribe(::onCurrencyUpdateSuccess, ::onCurrencyUpdateError)
            .also { currencyUpdate = it }
    }

    fun stopUpdates() {
        currencyUpdate?.dispose()
        currencyUpdate = null
    }

    private fun onCurrencyUpdateSuccess(update: List<CurrencyDetails>) {
        // Update items
        update.forEachIndexed { index, currencyDetails ->
            val currencyDetailsItem = currencyItemsMap[currencyDetails.currencyCode]!!
            currencyDetailsItem.start()
            if (index == 0) {
                // Make sure first item is editable
                currencyDetailsItem.editingDisabled.value = false
            } else {
                // Update rates for other items (First is always 1.0 and update disrupts UI)
                currencyDetailsItem.currencyRate = currencyDetails.rate
            }
        }
        // Update list if needed, new items
        if (viewModel.listCount != update.size) {
            val listItems = update
                .map { currencyItemsMap[it.currencyCode]!! }
                .mapToBindingAdapterItem(R.layout.item_currency_details, BR.item)
            viewModel.updateList(listItems)
        }
    }

    private fun createCurrencyItem(details: CurrencyDetails): CurrencyDetailsItem {
        return CurrencyDetailsItem(
            flagUrl = details.flagUrl,
            currencyTitle = details.currencyCode,
            currencySubtitle = details.currencyDisplayName,
            initialRate = details.rate,
            onCurrencyClick = { onCurrencyClick(details) },
            onAmountEdited = ::onAmountEdited
        )
    }

    private fun onCurrencyClick(details: CurrencyDetails) {
        val currencyCode = details.currencyCode
        if (currencyCode != interactor.baseCurrencyCode) {
            Timber.w("Click $details")
            stopUpdates()
            interactor.baseCurrencyCode = currencyCode
            viewModel.promoteCurrencyToTop(currencyCode)
            delay(500) { startUpdates() }
        }
    }

    private fun onAmountEdited(currency: String, userInput: String) {
        if (currency == interactor.baseCurrencyCode) {
            Timber.i("Edited by user $currency $userInput")
            val number = CurrencyUtil.parseCurrencyAmount(userInput)
            viewModel.updateCommonCurrencyAmount(number)
        }
    }

    private fun onCurrencyUpdateError(error: Throwable) {
        when (error) {
            is NoInternet -> events.value =
                ToastToUser(resources.getString(R.string.error_internet_not_available))
            is ServerError -> events.value =
                ToastToUser(resources.getString(R.string.error_server_error))
            else -> {
                events.value = ToastToUser("Unknown Error")
                Timber.e(error)
            }
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
}