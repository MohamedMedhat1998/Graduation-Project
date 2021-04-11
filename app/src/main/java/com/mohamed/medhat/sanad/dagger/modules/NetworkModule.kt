package com.mohamed.medhat.sanad.dagger.modules

import com.mohamed.medhat.sanad.networking.NetworkInterceptor
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.utils.NETWORK_BASE_URL
import com.mohamed.medhat.sanad.utils.NETWORK_CONNECTION_TIMEOUT
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Responsible for providing network-related objects.
 * Usually, objects provided by this module are singletons.
 */
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideWebApi(retrofit: Retrofit): WebApi {
        return retrofit.create(WebApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .baseUrl(NETWORK_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(networkInterceptor: NetworkInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NETWORK_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(networkInterceptor)
            .build()
    }
}