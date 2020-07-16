package rocks.wintren.rency.app.ratecalc

import androidx.lifecycle.ViewModel
import rocks.wintren.rency.models.CurrencyDetailsItem
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

    fun updateList(list: List<BindingAdapterItem>) {
        adapter.submitList(list)
    }

    fun promoteCurrencyToTop(currency: String) {
        val list = adapter.currentList.toMutableList()
        val topItem = list.first {
            val detailsItem = it.model as CurrencyDetailsItem
            currency == detailsItem.currencyTitle
        }

        list.forEach { (it.model as CurrencyDetailsItem).editingDisabled.value = true }
        (topItem.model as CurrencyDetailsItem).let {
            it.currencyRate = 1.0
            it.editingDisabled.value = false
            it.setSelected.value = true
        }

        val indexOfTopItem = list.indexOf(topItem)
        list.removeAt(indexOfTopItem)
        adapter.submitList(listOf(topItem))

        list.add(0, topItem)
        delay(500) { adapter.submitList(list) }
    }

    fun updateCommonCurrencyAmount(amount: Double) {
        if (adapter.currentList.size > 0) {
            adapter.currentList
                .takeLast(adapter.currentList.size - 1)
                .forEach { (it.model as CurrencyDetailsItem).currencyAmount = amount }
        }
    }

}