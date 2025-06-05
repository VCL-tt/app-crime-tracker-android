package com.lab.lab.ui.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lab.lab.databinding.FragmentDetalleCrimenBinding
import com.lab.lab.model.Crimen
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.lab.lab.R
import com.lab.lab.ui.viewmodel.ListaCrimenViewModel
import kotlinx.coroutines.launch

class DetalleCrimenFragment : Fragment() {

    private var _binding: FragmentDetalleCrimenBinding? = null
    private val binding get() = _binding!!

    private var crimen = Crimen(
        id = UUID.randomUUID(),
        titulo = "",
        fecha = Date(),
        resuelto = false
    )

    private val viewModel: ListaCrimenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCrimenBinding.inflate(inflater, container, false)

        binding.txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->

            crimen = crimen.copy(titulo = texto.toString())
        }

        // Mostrar fecha y hora inicial
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.txtFechaCrimen.text = dateFormat.format(crimen.fecha)

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.txtHoraCrimen.text = timeFormat.format(crimen.fecha)

        // Escuchar el cambio de fecha desde el DatePickerFragment
        viewLifecycleOwner.lifecycleScope.launch {
            setFragmentResultListener("fechaSeleccionada") { _, bundle ->
                val fecha = bundle.getSerializable("fecha") as Date
                // Actualizar la fecha con la fecha seleccionada
                crimen = crimen.copy(fecha = fecha)
                binding.txtFechaCrimen.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha)
            }
        }

        // Escuchar el cambio de hora desde el HoraPickerFragment
        viewLifecycleOwner.lifecycleScope.launch {
            setFragmentResultListener("horaSeleccionada") { _, bundle ->
                val hora = bundle.getSerializable("hora") as Date
                val newCalendar = Calendar.getInstance()
                newCalendar.time = crimen.fecha
                newCalendar.set(Calendar.HOUR_OF_DAY, hora.hours)
                newCalendar.set(Calendar.MINUTE, hora.minutes)

                // Actualizar la hora
                crimen = crimen.copy(fecha = newCalendar.time)
                binding.txtHoraCrimen.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(newCalendar.time)
            }
        }

        // Selección de la fecha
        binding.iconoFecha.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(parentFragmentManager, "datePicker")
        }

        // Selección de la hora
        binding.iconoHora.setOnClickListener {
            val horaPickerFragment = HoraPickerFragment() // Fragmento de hora
            horaPickerFragment.show(parentFragmentManager, "horaPicker")
        }

        // Botón para guardar el crimen
        binding.btnGuardarCrimen.setOnClickListener {
            if (binding.txtTituloCrimen.text.isNullOrEmpty()) {
                Snackbar.make(binding.root, "Por favor ingrese un título para el crimen", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                // Guardar el crimen con el título actualizado
                viewModel.agregarCrimen(crimen)

                // Mostrar el Snackbar de confirmación
                Snackbar.make(binding.root, "Crimen guardado con éxito", Snackbar.LENGTH_SHORT)
                    .setAction("Cerrar") {}
                    .show()

                // Redirigir a la lista de crímenes después de guardar
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        binding.chkResuelto.setOnCheckedChangeListener { _, marcado ->
            crimen = crimen.copy(resuelto = marcado)
        }

        // Botón de cancelación
        binding.btnCancelar.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
