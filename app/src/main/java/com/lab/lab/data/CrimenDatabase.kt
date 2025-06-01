package com.lab.lab.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lab.lab.model.Crimen


@Database(entities = [Crimen::class], version = 1, exportSchema = false)
@TypeConverters(CrimenTypeConverter::class)
abstract class CrimenDatabase : RoomDatabase() {
    abstract fun crimenDao(): CrimenDao

    companion object {
        @Volatile
        private var INSTANCE: CrimenDatabase? = null

        fun getDatabase(context: Context): CrimenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CrimenDatabase::class.java,
                    "crimen_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
