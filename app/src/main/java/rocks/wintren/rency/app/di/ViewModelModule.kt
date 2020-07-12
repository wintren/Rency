package rocks.wintren.rency.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rocks.wintren.rency.app.MainViewModel
import rocks.wintren.rency.app.di.ViewModelFactory
import rocks.wintren.rency.app.di.ViewModelKey

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    protected abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel


}
