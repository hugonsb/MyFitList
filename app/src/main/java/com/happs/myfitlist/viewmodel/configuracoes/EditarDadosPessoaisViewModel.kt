package com.happs.myfitlist.viewmodel.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.usuario.Usuario
import com.happs.myfitlist.room.TreinoRepository
import com.happs.myfitlist.state.EditarDadosPessoaisState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditarDadosPessoaisViewModel(
    private val treinoRepository: TreinoRepository
) : ViewModel() {
    private val _editarDadosPessoaisState = MutableStateFlow(EditarDadosPessoaisState())
    val editarDadosPessoaisState: StateFlow<EditarDadosPessoaisState> =
        _editarDadosPessoaisState.asStateFlow()

    init {
        viewModelScope.launch {
            treinoRepository.getUsuario().collectLatest { usuario ->
                if (usuario != null) {
                    _editarDadosPessoaisState.update { currentState ->
                        currentState.copy(
                            usuario = usuario,
                        )
                    }
                }
            }
        }
    }

    suspend fun updateUser(usuario: Usuario) {
        treinoRepository.updateUser(usuario)
    }
}