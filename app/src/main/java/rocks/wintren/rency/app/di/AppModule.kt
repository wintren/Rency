package rocks.wintren.rency.app.di

import android.app.Application
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideResources(application: Application): Resources = application.resources

}