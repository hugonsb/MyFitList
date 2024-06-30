package com.happs.myfitlist.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.happs.myfitlist.MyFitListApplication

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
            EditarPlanoViewModel(myFitList().container.treinoRepository, savedStateHandle)
        }
    }
}

fun CreationExtras.myFitList(): MyFitListApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MyFitListApplication)