package com.happs.myfitlist

import android.app.Application
import com.happs.myfitlist.room.treino.AppContainer
import com.happs.myfitlist.room.treino.AppDataContainer

class MyFitListApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
