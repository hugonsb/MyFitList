package com.happs.myfitlist.model.dieta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// essa tabela vai guardar as refeicoes que o usuario vai fazer no respectivo dia
@Entity(
    "refeicao",
    foreignKeys = [ForeignKey(
        entity = DiaDieta::class,
        parentColumns = ["id"],
        childColumns = ["idDiaDieta"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Refeicao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tipo") val tipo: String,
    @ColumnInfo(name = "detalhes") val detalhes: String,
    @ColumnInfo(name = "idDiaDieta") val idDiaDieta: Int
)