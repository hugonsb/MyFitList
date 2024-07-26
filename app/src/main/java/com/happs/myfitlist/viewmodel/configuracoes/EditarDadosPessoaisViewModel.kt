package com.happs.myfitlist.viewmodel.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.happs.myfitlist.model.usuario.Usuario
import com.happs.myfitlist.room.RepositoryResponse
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

    private val _editarDadosPessoaisState =
        MutableStateFlow<RepositoryResponse<EditarDadosPessoaisState>>(RepositoryResponse.Loading)
    val editarDadosPessoaisState: StateFlow<RepositoryResponse<EditarDadosPessoaisState>> =
        _editarDadosPessoaisState.asStateFlow()

    init {
        viewModelScope.launch {
            _editarDadosPessoaisState.value = RepositoryResponse.Loading
            treinoRepository.getUsuario().collectLatest { usuario ->
                if (usuario != null) {
                    _editarDadosPessoaisState.update {
                        RepositoryResponse.Success(
                            EditarDadosPessoaisState(
                                usuario = usuario,
                            )
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