package rocks.wintren.rency.app.ratecalc

import androidx.lifecycle.ViewModel
import rocks.wintren.rency.BR
import rocks.wintren.rency.R
import rocks.wintren.rency.models.CurrencyDetails
import rocks.wintren.rency.models.CurrencyDetailsItem
import rocks.wintren.rency.repo.NoInternet
import rocks.wintren.rency.repo.ServerError
import rocks.wintren.rency.util.SingleLiveEvent
import rocks.wintren.rency.util.databinding.adapter.BindingAdapterItem
import rocks.wintren.rency.util.databinding.adapter.mapToBindingAdapterItem
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

}