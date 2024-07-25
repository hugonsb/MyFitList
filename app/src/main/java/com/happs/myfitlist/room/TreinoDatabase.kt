package com.happs.myfitlist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario

@Database(
    entities = [Usuario::class, PlanoTreino::class, DiaTreino::class, Exercicio::class, PlanoDieta::class, DiaDieta::class, Refeicao::class],
    version = 1,
    exportSchema = false
)
abstract class TreinoDatabase : RoomDatabase() {
    abstract fun treinoDao(): TreinoDao
}