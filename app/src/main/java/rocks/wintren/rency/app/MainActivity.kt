package rocks.wintren.rency.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import rocks.wintren.rency.R
import rocks.wintren.rency.app.MainViewModel.MainEvent.ToastToUser
import rocks.wintren.rency.app.di.ViewModelFactory
import rocks.wintren.rency.databinding.ActivityMainBinding
import rocks.wintren.rency.util.observe
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.list.itemAnimator?.changeDuration = 0 // Move to BindingAdapter

        viewModel.events.observe(this, ::onEvent)
    }

    private fun onEvent(event: MainViewModel.MainEvent) {
        when (event) {
            is ToastToUser -> Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startCurrencyUpdates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCurrencyUpdates()
    }

}