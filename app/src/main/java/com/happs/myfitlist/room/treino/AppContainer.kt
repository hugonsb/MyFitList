package com.happs.myfitlist.room.treino

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val tasksRepository: TreinoRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tasksRepository: TreinoRepository by lazy {
        OfflineTreinoRepository(TreinoDatabase.getDatabase(context).treinoDao())
    }
}