package com.happs.myfitlist.model.usuario

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.happs.myfitlist.model.treino.PlanoTreino

@Entity(
    tableName = "usuario",
    foreignKeys = [ForeignKey(
        entity = PlanoTreino::class,
        parentColumns = ["id"],
        childColumns = ["idPlanoTreinoPrincipal"],
        onDelete = ForeignKey.CASCADE
    )])
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("nome")
    val nome: String,
    @ColumnInfo("idade")
    val idade: Byte,
    @ColumnInfo("peso")
    val peso: Float,
    @ColumnInfo("idPlanoTreinoPrincipal")
    val idPlanoTreinoPrincipal: Int?
)