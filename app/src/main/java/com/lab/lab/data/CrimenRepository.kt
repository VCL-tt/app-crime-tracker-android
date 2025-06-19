package com.lab.lab.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import com.lab.lab.model.Crimen

class CrimenRepository(private val dao: CrimenDao) {

    val crimenes: Flow<List<Crimen>> = dao.obtenerCrimenes()

    suspend fun ingresarCrimen(crimen: Crimen) {
        dao.insertarCrimen(crimen)
    }
    suspend fun eliminarCrimen(crimen: Crimen) {
        dao.eliminar(crimen)
    }


    companion object {
        @Volatile
        private var INSTANCE: CrimenRepository? = null

        fun inicializar(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        val database = CrimenDatabase.getDatabase(context)
                        INSTANCE = CrimenRepository(database.crimenDao())
                    }
                }
            }
        }

        fun get(): CrimenRepository = INSTANCE
            ?: throw IllegalStateException("Repositorio no inicializado")
    }
}
