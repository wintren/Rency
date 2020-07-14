package rocks.wintren.rency.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import rocks.wintren.rency.app.ratecalc.RateCalcActivity

@Module
abstract class ActivityInjector {

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainActivity(): RateCalcActivity

}
