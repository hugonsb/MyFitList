package com.happs.myfitlist.viewmodel.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.usuario.Usuario
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.CadastroState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CadastroViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {
    private val _cadastroState = MutableStateFlow(CadastroState())
    val cadastroState: StateFlow<CadastroState> = _cadastroState.asStateFlow()

    init {
        viewModelScope.launch {
            treinoRepository.getUsuario().collectLatest { usuario ->
                if (usuario != null) {
                    _cadastroState.update { currentState ->
                        currentState.copy(
                            usuario = usuario,
                            isUserLoaded = true
                        )
                    }
                } else {
                    _cadastroState.update { currentState ->
                        currentState.copy(isUserLoaded = true)
                    }
                }
            }
        }
    }

    suspend fun addUser(usuario: Usuario) {
        treinoRepository.addUser(usuario)
    }
}