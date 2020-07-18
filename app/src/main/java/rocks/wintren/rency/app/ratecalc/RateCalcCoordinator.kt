package rocks.wintren.rency.app.ratecalc

import android.content.res.Resources
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import rocks.wintren.rency.BR
import rocks.wintren.rency.R
import rocks.wintren.rency.app.ratecalc.RateCalcEvent.ToastToUser
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.repo.NoInternet
import rocks.wintren.rency.repo.ServerError
import rocks.wintren.rency.util.*
import rocks.wintren.rency.util.databinding.adapter.mapToBindingAdapterItem
import timber.log.Timber
import javax.inject.Inject

class RateCalcCoordinator @Inject constructor(
    private val interactor: RateCalcInteractor,
    private val currencyItemMapper: CurrencyItemMapper,
    private val rxSchedulers: RxSchedulers,
    private val resources: Resources
) {

    val events = SingleLiveEvent<RateCalcEvent>()
    private lateinit var viewModel: RateCalcViewModel
    private var disposables = CompositeDisposable()
    private var currencyUpdate: Disposable? = null
    private val currencyItemsMap: MutableMap<String, CurrencyItem> = mutableMapOf()

    fun init(viewModel: RateCalcViewModel) {
        this.viewModel = viewModel
        currencyItemMapper.init(::onAmountEdited, ::onCurrencyClick)
    }

    fun startUpdates() {
        if (currencyUpdate != null) {
            viewModel.showLoading(false)
            return Timber.i("Currency Update already running")
        }
        interactor.fetchCurrencyUpdates()
            .map { list ->
                list.forEach {
                    if (!currencyItemsMap.containsKey(it.currencyCode)) {
                        val currencyDetailItem = currencyItemMapper.mapCurrencyDetailItem(it)
                        currencyItemsMap[it.currencyCode] = currencyDetailItem
                    }
                }
                return@map list
            }
            .observeOn(rxSchedulers.main())
            .doOnEach { viewModel.showLoading(false) }
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
            if (index > 0) {
                // Current top item is always 1.0 and update disrupts UI
                // Update rates for other items
                currencyDetailsItem.currencyRate = currencyDetails.rate
                currencyDetailsItem.currentTopItem = false
            } else {
                currencyDetailsItem.currentTopItem = true
            }
        }

        // Add to ViewModel if needed / new items
        if (viewModel.listCount != update.size) {
            val listItems = update
                .map { currencyItemsMap[it.currencyCode]!! }
                .mapToBindingAdapterItem(R.layout.item_currency_details, BR.item)
            viewModel.updateList(listItems)
        }
    }

    private fun onCurrencyClick(details: CurrencyDetails) {
        val currencyCode = details.currencyCode
        if (currencyCode != interactor.baseCurrencyCode) {
            stopUpdates()

            viewModel.promoteCurrencyToTop(currencyCode)

            currencyItemsMap[currencyCode]!!.let {
                val displayAmount = it.rateDisplay.value!!
                val nativeAmount = CurrencyUtil.parseCurrencyAmount(displayAmount)
                it.currencyRate = 1.0
                it.currencyAmount = nativeAmount
                delay(RateCalcViewModel.PROMOTE_ANIMATION_DELAY_MS / 2) {
                    // Delay to wait for items to not be showing
                    logTime("update items") {
                        updateAmountForItems(currencyCode, nativeAmount)
                    }
                }
            }
            interactor.baseCurrencyCode = currencyCode

            // Match animation in ViewModel with delay
            delay(RateCalcViewModel.PROMOTE_ANIMATION_DELAY_MS) { startUpdates() }
        }
    }

    private fun onAmountEdited(currency: String, userInput: String) {
        if (currency != interactor.baseCurrencyCode) return
        val number = CurrencyUtil.parseCurrencyAmount(userInput)
        updateAmountForItems(currency, number)
    }

    private fun updateAmountForItems(baseCurrency: String, amount: Double) {
        currencyItemsMap.values
            .filterNot { it.titleCurrencyCode == baseCurrency }
            .forEach { it.currencyAmount = amount }
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