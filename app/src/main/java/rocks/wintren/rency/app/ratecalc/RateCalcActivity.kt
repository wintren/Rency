package rocks.wintren.rency.app.ratecalc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import rocks.wintren.rency.R
import rocks.wintren.rency.app.di.ViewModelFactory
import rocks.wintren.rency.databinding.ActivityRateCalcBinding
import rocks.wintren.rency.util.observe
import javax.inject.Inject

class RateCalcActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var coordinator: RateCalcCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val rateCalcViewModel = ViewModelProvider(this, viewModelFactory)
            .get(RateCalcViewModel::class.java)
        setContentView<ActivityRateCalcBinding>(this, R.layout.activity_rate_calc).run {
                viewModel = rateCalcViewModel
                lifecycleOwner = this@RateCalcActivity
            }
        coordinator = rateCalcViewModel.coordinator.apply {
            init(viewModel = rateCalcViewModel)
            events.observe(this@RateCalcActivity, ::onEvent)
        }
    }

    private fun onEvent(event: RateCalcEvent) {
        when (event) {
            is RateCalcEvent.ToastToUser -> Toast.makeText(this, event.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        coordinator.startUpdates()
    }

    override fun onPause() {
        super.onPause()
        coordinator.stopUpdates()
    }

}
