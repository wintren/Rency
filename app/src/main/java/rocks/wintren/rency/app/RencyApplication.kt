package rocks.wintren.rency.app

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import rocks.wintren.rency.app.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class RencyApplication: Application(), HasAndroidInjector {

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        Timber.plant(Timber.DebugTree())
    }

}