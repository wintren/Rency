package rocks.wintren.rency.app.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import rocks.wintren.rency.app.AppModule
import rocks.wintren.rency.app.RencyApplication
import rocks.wintren.rency.repo.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityInjector::class,
        AppModule::class,
        ViewModelModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: RencyApplication)
}
