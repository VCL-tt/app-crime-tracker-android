package com.lab.lab.ui.screens

import android.app.Application
import com.lab.lab.data.CrimenRepository

class RegistroCriminalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimenRepository.inicializar(this)
    }
}
