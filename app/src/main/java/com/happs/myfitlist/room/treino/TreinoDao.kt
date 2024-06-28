package com.happs.myfitlist.room.treino

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query("UPDATE usuario SET idPlanoTreinoPrincipal = :planoTreinoId WHERE id = :usuarioId")
    suspend fun updatePlanoTreinoPrincipal(usuarioId: Int, planoTreinoId: Int)

    @Query("SELECT * FROM planotreino WHERE id = :idPlanoTreinoPrincipal")
    fun getPlanoTreinoPrincipal(idPlanoTreinoPrincipal: Int): Flow<PlanoTreino>

    @Query("SELECT * FROM planotreino")
    fun getPlanosTreino(): Flow<List<PlanoTreino>>

    @Insert
    suspend fun addPlanoTreino(planoTreino: PlanoTreino): Long

    @Update
    suspend fun updatePlanoTreino(planoTreino: PlanoTreino)

    @Delete
    suspend fun removePlanoTreino(planoTreino: PlanoTreino)

    @Query("SELECT * FROM diaTreino WHERE idPlanoTreino = :idPlanoTreino")
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
}