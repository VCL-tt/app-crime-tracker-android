package com.lab.lab.data

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

class CrimenTypeConverter {

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(millis: Long): Date = Date(millis)

    @TypeConverter
    fun fromUUID(uuid: UUID): String = uuid.toString()

    @TypeConverter
    fun toUUID(uuid: String): UUID = UUID.fromString(uuid)
}
