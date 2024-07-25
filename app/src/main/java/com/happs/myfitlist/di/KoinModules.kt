package com.happs.myfitlist.di

import android.content.Context
import androidx.room.Room
import com.happs.myfitlist.room.OfflineTreinoRepository
import com.happs.myfitlist.room.TreinoDatabase
import com.happs.myfitlist.room.TreinoRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.happs.myfitlist.viewmodel.cadastro.CadastroViewModel
import com.happs.myfitlist.viewmodel.configuracoes.EditarDadosPessoaisViewModel
import com.happs.myfitlist.viewmodel.dieta.CriarPlanoDietaViewModel
import com.happs.myfitlist.viewmodel.dieta.DietaViewModel
import com.happs.myfitlist.viewmodel.dieta.EditarPlanoDietaViewModel
import com.happs.myfitlist.viewmodel.treino.CriarPlanoTreinoViewModel
import com.happs.myfitlist.viewmodel.treino.EditarPlanoTreinoViewModel
import com.happs.myfitlist.viewmodel.treino.TreinoViewModel

val appModule = module {
    viewModelOf(::CadastroViewModel)
    viewModelOf(::EditarDadosPessoaisViewModel)
    viewModelOf(::CriarPlanoDietaViewModel)
    viewModelOf(::DietaViewModel)
    viewModelOf(::EditarPlanoDietaViewModel)
    viewModelOf(::CriarPlanoTreinoViewModel)
    viewModelOf(::TreinoViewModel)
    viewModelOf(::EditarPlanoTreinoViewModel)
}

val dbModule = module {
    single { providerDatabase(androidContext = get()) }
    single { get<TreinoDatabase>().treinoDao() }
    single<TreinoRepository> { OfflineTreinoRepository(treinoDao = get()) }
}

fun providerDatabase(androidContext: Context): TreinoDatabase {
    return Room.databaseBuilder(
        androidContext,
        TreinoDatabase::class.java, "db_treino"
    )
        .fallbackToDestructiveMigration()
        .build()
}