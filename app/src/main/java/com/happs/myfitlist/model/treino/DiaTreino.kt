package com.happs.myfitlist.model.treino

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// essa tabela vai guardar os dias de treino e os grupos musculares
@Entity(
    "diatreino",
    foreignKeys = [ForeignKey(
        entity = PlanoTreino::class,
        parentColumns = ["id"],
        childColumns = ["idPlanoTreino"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DiaTreino(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("dia") val dia: String,
    @ColumnInfo("grupoMuscular") val grupoMuscular: String,
    @ColumnInfo("idPlanoTreino") val idPlanoTreino: Int
)