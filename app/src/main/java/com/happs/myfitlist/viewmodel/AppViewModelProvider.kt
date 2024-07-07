package com.happs.myfitlist.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.happs.myfitlist.MyFitListApplication
import com.happs.myfitlist.viewmodel.cadastro.CadastroViewModel
import com.happs.myfitlist.viewmodel.configuracoes.EditarDadosPessoaisViewModel
import com.happs.myfitlist.viewmodel.dieta.CriarPlanoDietaViewModel
import com.happs.myfitlist.viewmodel.dieta.DietaViewModel
import com.happs.myfitlist.viewmodel.dieta.EditarPlanoDietaViewModel
import com.happs.myfitlist.viewmodel.treino.CriarPlanoTreinoViewModel
import com.happs.myfitlist.viewmodel.treino.EditarPlanoTreinoViewModel
import com.happs.myfitlist.viewmodel.treino.TreinoViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CadastroViewModel(myFitList().container.treinoRepository)
        }
        initializer {
            TreinoViewModel(myFitList().container.treinoRepository)
        }
        initializer {
            CriarPlanoTreinoViewModel(myFitList().container.treinoRepository)
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            EditarPlanoTreinoViewModel(myFitList().container.treinoRepository, savedStateHandle)
        }
        initializer {
            DietaViewModel(myFitList().container.treinoRepository)
        }
        initializer {
            CriarPlanoDietaViewModel(myFitList().container.treinoRepository)
        }
        initializer {
            val savedStateHandle = createSavedStateHandle()
            EditarPlanoDietaViewModel(myFitList().container.treinoRepository, savedStateHandle)
        }
        initializer {
            EditarDadosPessoaisViewModel(myFitList().container.treinoRepository)
        }
    }
}

fun CreationExtras.myFitList(): MyFitListApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MyFitListApplication)