package com.lab.lab.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab.lab.data.CrimenRepository
import com.lab.lab.model.Crimen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ListaCrimenViewModel : ViewModel() {

    private val repository = CrimenRepository.get()

    private val _crimenes = MutableStateFlow<List<Crimen>>(emptyList())
    val crimenes: StateFlow<List<Crimen>> = _crimenes

    init {
        viewModelScope.launch {
            repository.crimenes.collect { lista ->
                val today = Date()
                val todayCrimes = lista.filter { isSameDay(it.fecha, today) }
                val otherCrimes = lista.filterNot { isSameDay(it.fecha, today) }
                _crimenes.value = todayCrimes.sortedByDescending { it.fecha } + otherCrimes.sortedByDescending { it.fecha }
            }
        }
    }
    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.time = date1
        val calendar2 = Calendar.getInstance()
        calendar2.time = date2
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
    }

    fun agregarCrimen(crimen: Crimen) {
        viewModelScope.launch {
            try {
                repository.ingresarCrimen(crimen)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun eliminarCrimen(crimen: Crimen) {
        viewModelScope.launch {
            try {
                repository.eliminarCrimen(crimen)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun obtenerCrimenPorId(id: UUID, callback: (Crimen?) -> Unit) {
        viewModelScope.launch {
            val crimenEncontrado = _crimenes.value.find { it.id == id }
            callback(crimenEncontrado)
        }
    }
    fun actualizarCrimen(crimen: Crimen) {
        val listaActual = _crimenes.value.toMutableList()
        val index = listaActual.indexOfFirst { it.id == crimen.id }
        if (index != -1) {
            listaActual[index] = crimen
            _crimenes.value = listaActual
        }
    }

}

