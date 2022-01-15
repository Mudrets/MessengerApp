package com.example.messengerapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.CoilUtils
import com.example.messengerapp.di.AppComponent
import com.example.messengerapp.di.DaggerAppComponent
import okhttp3.OkHttpClient
import timber.log.Timber

class App : Application(), ImageLoaderFactory {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appComponent = DaggerAppComponent.builder().context(this).build()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .build()
    }

}