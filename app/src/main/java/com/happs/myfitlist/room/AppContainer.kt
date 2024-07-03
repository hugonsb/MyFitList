package com.happs.myfitlist.room

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val treinoRepository: TreinoRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val treinoRepository: TreinoRepository by lazy {
        OfflineTreinoRepository(TreinoDatabase.getDatabase(context).treinoDao())
    }
}