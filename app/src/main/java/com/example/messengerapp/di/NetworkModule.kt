package com.example.messengerapp.di

import com.example.messengerapp.data.network.ZulipApi
import com.example.messengerapp.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesLogInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideBasicCredentials(): String = Credentials.basic(Constants.USERNAME, Constants.API_KEY)

    @Singleton
    @Provides
    fun provideAuthInterceptor(basicCredentials: String): Interceptor = Interceptor {
        val newRequest =
            it.request().newBuilder().addHeader("Authorization", basicCredentials).build()
        it.proceed(newRequest)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: Interceptor,
        logInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authorizationInterceptor)
        .addInterceptor(logInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideZulipService(client: OkHttpClient): ZulipApi {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        return retrofit.create(ZulipApi::class.java)
    }
}