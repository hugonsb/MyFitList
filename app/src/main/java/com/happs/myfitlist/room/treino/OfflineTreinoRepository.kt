package com.happs.myfitlist.room.treino

import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario
import kotlinx.coroutines.flow.Flow

class OfflineTreinoRepository(private val treinoDao: TreinoDao) : TreinoRepository {

    override fun getUsuario(): Flow<Usuario> = treinoDao.getUsuario()

    override suspend fun addUser(usuario: Usuario) = treinoDao.addUser(usuario)

    override suspend fun updateUser(usuario: Usuario) = treinoDao.updateUser(usuario)

    override suspend fun removeUser(usuario: Usuario) = treinoDao.removeUser(usuario)

    override fun getPlanosTreino(): Flow<List<PlanoTreino>> = treinoDao.getPlanosTreino()

    override suspend fun addPlanoTreino(planoTreino: PlanoTreino) =
        treinoDao.addPlanoTreino(planoTreino)

    override suspend fun updatePlanoTreino(planoTreino: PlanoTreino) =
        treinoDao.updatePlanoTreino(planoTreino)

    override suspend fun removePlanoTreino(planoTreino: PlanoTreino) =
        treinoDao.removePlanoTreino(planoTreino)

    override fun getDiasTreino(idPlanoTreino: Int): Flow<List<DiaTreino>> =
        treinoDao.getDiasTreino(idPlanoTreino)

    override suspend fun addDiaTreino(diaTreino: DiaTreino) = treinoDao.addDiaTreino(diaTreino)

    override suspend fun updateDiaTreino(diaTreino: DiaTreino) =
        treinoDao.updateDiaTreino(diaTreino)

    override suspend fun removeDiaTreino(diaTreino: DiaTreino) =
        treinoDao.removeDiaTreino(diaTreino)

    override fun getExercicios(idDiaTreino: Int): Flow<List<Exercicio>> =
        treinoDao.getExercicios(idDiaTreino)

    override suspend fun addExercicio(exercicio: Exercicio) = treinoDao.addExercicio(exercicio)

    override suspend fun updateExercicio(exercicio: Exercicio) =
        treinoDao.updateExercicio(exercicio)

    override suspend fun removeExercicio(exercicio: Exercicio) =
        treinoDao.removeExercicio(exercicio)
}