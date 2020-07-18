package rocks.wintren.rency.app.ratecalc

import androidx.lifecycle.ViewModel
import rocks.wintren.rency.util.SingleLiveEvent
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem
import rocks.wintren.rency.util.delay
import javax.inject.Inject

class RateCalcViewModel @Inject constructor(
    val coordinator: RateCalcCoordinator
) : ViewModel() {

    val showLoadingEvent = SingleLiveEvent<Boolean>()
    val adapter = CurrencyListAdapter()

    fun onSwipeRefresh() {
        coordinator.startUpdates()
    }

    override fun onCleared() {
        super.onCleared()
        coordinator.onCleared()
    }

    val listCount: Int get() = adapter.itemCount

    fun showLoading(show: Boolean) {
        showLoadingEvent.value = show
    }

    fun updateList(list: List<BindingAdapterItem>) {
        adapter.submitList(list)
    }

    fun promoteCurrencyToTop(currency: String) {
        val list = adapter.currentList.toMutableList()
        val topItem = list.first {
            val detailsItem = it.model as CurrencyItem
            currency == detailsItem.titleCurrencyCode
        }

        val indexOfTopItem = list.indexOf(topItem)
        list.removeAt(indexOfTopItem)
        adapter.submitList(listOf(topItem))

        list.add(0, topItem)
        delay(PROMOTE_ANIMATION_DELAY_MS) { adapter.submitList(list) }
    }

    companion object {
        const val PROMOTE_ANIMATION_DELAY_MS = 500L
    }

}