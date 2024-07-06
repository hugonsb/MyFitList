package com.happs.myfitlist.model.dieta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// essa tabela vai guardar os dias da semana com suas respectivas refeiçoes
@Entity(
    "diadieta",
    foreignKeys = [ForeignKey(
        entity = PlanoDieta::class,
        parentColumns = ["id"],
        childColumns = ["idPlanoDieta"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DiaDieta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("dia") val dia: String,
    @ColumnInfo("idPlanoDieta") val idPlanoDieta: Int
)