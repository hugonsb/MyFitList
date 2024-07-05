package com.happs.myfitlist.model.dieta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.happs.myfitlist.model.usuario.Usuario


// essa tabela vai guardar os nomes dos planos alimentares
@Entity(
    "planodieta",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["idUsuario"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlanoDieta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("nome") val nome: String,
    @ColumnInfo("idUsuario") val idUsuario: Int
)