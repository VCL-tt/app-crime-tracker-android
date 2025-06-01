package com.lab.lab.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lab.lab.data.CrimenTypeConverter
import androidx.room.TypeConverters
import java.util.Date
import java.util.UUID

@Entity(tableName = "crimen")
@TypeConverters(CrimenTypeConverter::class)
data class Crimen(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val titulo: String,
    val fecha: Date,
    val resuelto: Boolean
)
