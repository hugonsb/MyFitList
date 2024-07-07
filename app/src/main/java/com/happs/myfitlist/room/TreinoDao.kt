package com.happs.myfitlist.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.happs.myfitlist.model.dieta.DiaDieta
import com.happs.myfitlist.model.dieta.PlanoDieta
import com.happs.myfitlist.model.dieta.Refeicao
import com.happs.myfitlist.model.treino.DiaTreino
import com.happs.myfitlist.model.treino.Exercicio
import com.happs.myfitlist.model.treino.PlanoTreino
import com.happs.myfitlist.model.usuario.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoDao {

    @Query("SELECT * FROM usuario")
    fun getUsuario(): Flow<Usuario>

    @Insert
    suspend fun addUser(usuario: Usuario)

    @Update
    suspend fun updateUser(usuario: Usuario)

    @Delete
    suspend fun removeUser(usuario: Usuario)

    // para plano de treino

    @Query("UPDATE usuario SET idPlanoTreinoPrincipal = :planoTreinoId WHERE id = :usuarioId")
    suspend fun updatePlanoTreinoPrincipal(usuarioId: Int, planoTreinoId: Int)

    @Query("SELECT * FROM planotreino WHERE id = :idPlanoTreino")
    fun getPlanoTreino(idPlanoTreino: Int): Flow<PlanoTreino>

    @Query("SELECT * FROM planotreino")
    fun getPlanosTreino(): Flow<List<PlanoTreino>>

    @Insert
    suspend fun addPlanoTreino(planoTreino: PlanoTreino): Long

    @Update
    suspend fun updatePlanoTreino(planoTreino: PlanoTreino)

    @Delete
    suspend fun removePlanoTreino(planoTreino: PlanoTreino)

    @Query("SELECT * FROM diatreino WHERE idPlanoTreino = :idPlanoTreino")
    fun getDiasTreino(idPlanoTreino: Int): Flow<List<DiaTreino>>

    @Insert
    suspend fun addDiaTreino(diaTreino: DiaTreino): Long

    @Update
    suspend fun updateDiaTreino(diaTreino: DiaTreino)

    @Delete
    suspend fun removeDiaTreino(diaTreino: DiaTreino)

    @Query("SELECT * FROM exercicio WHERE idDiaTreino = :idDiaTreino")
    fun getExercicios(idDiaTreino: Int): Flow<List<Exercicio>>

    @Insert
    suspend fun addExercicio(exercicio: Exercicio)

    @Update
    suspend fun updateExercicio(exercicio: Exercicio)

    @Delete
    suspend fun removeExercicio(exercicio: Exercicio)

    // para plano alimentar

    @Query("UPDATE usuario SET idPlanoDietaPrincipal = :planoDietaId WHERE id = :usuarioId")
    suspend fun updatePlanoDietaPrincipal(usuarioId: Int, planoDietaId: Int)

    @Query("SELECT * FROM planodieta WHERE id = :idPlanoDieta")
    fun getPlanoDieta(idPlanoDieta: Int): Flow<PlanoDieta>

    @Query("SELECT * FROM planodieta")
    fun getPlanosDieta(): Flow<List<PlanoDieta>>

    @Insert
    suspend fun addPlanoDieta(planoDieta: PlanoDieta): Long

    @Update
    suspend fun updatePlanoDieta(planoDieta: PlanoDieta)

    @Delete
    suspend fun removePlanoDieta(planoDieta: PlanoDieta)

    @Query("SELECT * FROM diadieta WHERE idPlanoDieta = :idPlanoDieta")
    fun getDiasDieta(idPlanoDieta: Int): Flow<List<DiaDieta>>

    @Insert
    suspend fun addDiaDieta(diaDieta: DiaDieta): Long

    @Update
    suspend fun updateDiaDieta(diaDieta: DiaDieta)

    @Delete
    suspend fun removeDiaDieta(diaDieta: DiaDieta)

    @Query("SELECT * FROM refeicao WHERE idDiaDieta = :idDiaDieta")
    fun getRefeicoes(idDiaDieta: Int): Flow<List<Refeicao>>

    @Insert
    suspend fun addRefeicao(refeicao: Refeicao)

    @Update
    suspend fun updateRefeicao(refeicao: Refeicao)

    @Delete
    suspend fun removeRefeicao(refeicao: Refeicao)

    @Query("DELETE FROM usuario")
    suspend fun deleteAllData()
}