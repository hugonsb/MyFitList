package com.happs.myfitlist.model.treino

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// essa tabela vai guardar os exercicios que o usuario vai fazer no respectivo dia
@Entity(
    "exercicio",
    foreignKeys = [ForeignKey(
        entity = DiaTreino::class,
        parentColumns = ["id"],
        childColumns = ["idDiaTreino"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Exercicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nome") val nome: String,
    @ColumnInfo(name = "numeroSeries") val numeroSeries: Int,
    @ColumnInfo(name = "numeroRepeticoes") val numeroRepeticoes: Int,
    @ColumnInfo(name = "idDiaTreino") val idDiaTreino: Int
)