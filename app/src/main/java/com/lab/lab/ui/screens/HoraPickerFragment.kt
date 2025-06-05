package com.lab.lab.ui.screens

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.Calendar
import java.util.GregorianCalendar

class HoraPickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario = Calendar.getInstance()

        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val horaSeleccionada = GregorianCalendar().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }.time

            setFragmentResult("horaSeleccionada", bundleOf("hora" to horaSeleccionada))
        }

        return TimePickerDialog(
            requireContext(),
            listener,
            calendario.get(Calendar.HOUR_OF_DAY),
            calendario.get(Calendar.MINUTE),
            true
        )
    }
}
