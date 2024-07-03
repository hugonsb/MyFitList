package com.happs.myfitlist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario

@Database(
    entities = [Usuario::class, PlanoTreino::class, DiaTreino::class, Exercicio::class],
    version = 1,
    exportSchema = false
)
abstract class TreinoDatabase : RoomDatabase() {
    abstract fun treinoDao(): TreinoDao

    companion object {
        @Volatile
        private var Instance: TreinoDatabase? = null

        fun getDatabase(context: Context): TreinoDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TreinoDatabase::class.java, "db_treino")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}