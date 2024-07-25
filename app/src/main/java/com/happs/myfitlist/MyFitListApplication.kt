package com.happs.myfitlist

import android.app.Application
import com.happs.myfitlist.di.appModule
import com.happs.myfitlist.di.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyFitListApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            //androidLogger(level = Level.DEBUG) //ver logs do koin
            androidContext(this@MyFitListApplication)
            modules(appModule, dbModule)
        }
    }
}