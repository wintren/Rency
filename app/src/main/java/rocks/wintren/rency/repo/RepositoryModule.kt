package rocks.wintren.rency.repo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rocks.wintren.rency.util.RuntimeSchedulers
import rocks.wintren.rency.util.RxSchedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRxSchedulers(
    ): RxSchedulers {
        return RuntimeSchedulers()
    }

    @Provides
    @Singleton
    fun provideCurrencyAPI(
        @Named("Revolut")
        retrofit: Retrofit
    ): RevolutCurrencyAPI {
        return retrofit.create(RevolutCurrencyAPI::class.java)
    }

    @Provides
    @Singleton
    @Named("Revolut")
    fun provideRevolutRetrofit(
        gson: Gson,
        okHttp: OkHttpClient
    ): Retrofit {
        return createRetrofit(gson, okHttp, "https://hiring.revolut.codes/")
    }

    @Provides
    @Singleton
    fun provideOkhttp(): OkHttpClient = createOkHttpClient()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    //region Create helper classes
    private fun createRetrofit(gson: Gson, client: OkHttpClient, host: String): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(host)
            client(client)
            addConverterFactory(GsonConverterFactory.create(gson))
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }.build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
        }.build()
    }
    //endregion

}