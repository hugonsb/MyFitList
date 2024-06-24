package com.happs.myfitlist.model.treino

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.happs.myfitlist.model.usuario.Usuario

// essa tabela vai guardar os nomes dos planos de treino
@Entity(
    "planotreino",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["idUsuario"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlanoTreino(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("nome") val nome: String,
    @ColumnInfo("idUsuario") val idUsuario: Int
)