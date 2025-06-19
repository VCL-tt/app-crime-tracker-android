package com.lab.lab.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab.lab.model.Crimen
import kotlinx.coroutines.flow.Flow

@Dao
interface CrimenDao {

    @Query("SELECT * FROM crimen")
    fun obtenerCrimenes(): Flow<List<Crimen>>

    @Query("SELECT * FROM crimen WHERE id = :id")
    suspend fun obtenerCrimenPorId(id: String): Crimen?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCrimen(crimen: Crimen)

    @Query("DELETE FROM crimen")
    suspend fun borrarTodo()

    @Delete
    suspend fun eliminar(crimen: Crimen)
}

