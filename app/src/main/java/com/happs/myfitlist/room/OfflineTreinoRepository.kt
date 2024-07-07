package com.happs.myfitlist.room

import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
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

    override suspend fun updatePlanoTreinoPrincipal(usuarioId: Int, planoTreinoId: Int) =
        treinoDao.updatePlanoTreinoPrincipal(usuarioId, planoTreinoId)

    override fun getPlanoTreino(idPlanoTreino: Int): Flow<PlanoTreino> =
        treinoDao.getPlanoTreino(idPlanoTreino)

    override fun getPlanosTreino(): Flow<List<PlanoTreino>> = treinoDao.getPlanosTreino()

    override suspend fun addPlanoTreino(planoTreino: PlanoTreino): Long =
        treinoDao.addPlanoTreino(planoTreino)

    override suspend fun updatePlanoTreino(planoTreino: PlanoTreino) =
        treinoDao.updatePlanoTreino(planoTreino)

    override suspend fun removePlanoTreino(planoTreino: PlanoTreino) =
        treinoDao.removePlanoTreino(planoTreino)

    override fun getDiasTreino(idPlanoTreino: Int): Flow<List<DiaTreino>> =
        treinoDao.getDiasTreino(idPlanoTreino)

    override suspend fun addDiaTreino(diaTreino: DiaTreino): Long =
        treinoDao.addDiaTreino(diaTreino)

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

    override suspend fun updatePlanoDietaPrincipal(usuarioId: Int, planoDietaId: Int) =
        treinoDao.updatePlanoDietaPrincipal(usuarioId, planoDietaId)

    override fun getPlanoDieta(idPlanoDieta: Int): Flow<PlanoDieta> =
        treinoDao.getPlanoDieta(idPlanoDieta)

    override fun getPlanosDieta(): Flow<List<PlanoDieta>> = treinoDao.getPlanosDieta()

    override suspend fun addPlanoDieta(planoDieta: PlanoDieta): Long =
        treinoDao.addPlanoDieta(planoDieta)

    override suspend fun updatePlanoDieta(planoDieta: PlanoDieta) =
        treinoDao.updatePlanoDieta(planoDieta)

    override suspend fun removePlanoDieta(planoDieta: PlanoDieta) =
        treinoDao.removePlanoDieta(planoDieta)

    override fun getDiasDieta(idPlanoDieta: Int): Flow<List<DiaDieta>> =
        treinoDao.getDiasDieta(idPlanoDieta)

    override suspend fun addDiaDieta(diaDieta: DiaDieta): Long = treinoDao.addDiaDieta(diaDieta)

    override suspend fun updateDiaDieta(diaDieta: DiaDieta) = treinoDao.updateDiaDieta(diaDieta)

    override suspend fun removeDiaDieta(diaDieta: DiaDieta) = treinoDao.removeDiaDieta(diaDieta)

    override fun getRefeicoes(idDiaDieta: Int): Flow<List<Refeicao>> =
        treinoDao.getRefeicoes(idDiaDieta)

    override suspend fun addRefeicao(refeicao: Refeicao) = treinoDao.addRefeicao(refeicao)

    override suspend fun updateRefeicao(refeicao: Refeicao) = treinoDao.updateRefeicao(refeicao)

    override suspend fun removeRefeicao(refeicao: Refeicao) = treinoDao.removeRefeicao(refeicao)

    override suspend fun deleteAllData() = treinoDao.deleteAllData()
}