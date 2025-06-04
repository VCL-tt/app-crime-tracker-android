package com.lab.lab.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.lab.lab.R
import com.lab.lab.ui.viewmodel.ListaCrimenViewModel

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

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCrimenBinding.inflate(inflater, container, false)

        // Actualizar el título del crimen cuando el texto cambie
        binding.txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->
            crimen = crimen.copy(titulo = texto.toString())
        }

        // Mostrar fecha inicial
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        binding.txtFechaCrimen.text = dateFormat.format(crimen.fecha)

        binding.layoutFecha.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)

                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val selectedDateTime = calendar.time

                            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            binding.txtFechaCrimen.text = dateFormat.format(selectedDateTime)

                            crimen = crimen.copy(fecha = selectedDateTime)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        binding.iconoFecha.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)

                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val selectedDateTime = calendar.time

                            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            binding.txtFechaCrimen.text = dateFormat.format(selectedDateTime)

                            crimen = crimen.copy(fecha = selectedDateTime)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Control de cambio en el estado del crimen (resuelto o no)
        binding.chkResuelto.setOnCheckedChangeListener { _, marcado ->
            crimen = crimen.copy(resuelto = marcado)
        }

        // Botón para guardar el crimen
        binding.btnGuardarCrimen.setOnClickListener {
            // Verificar si el título está vacío
            if (binding.txtTituloCrimen.text.isNullOrEmpty()) {
                // Si el título está vacío, mostrar un Snackbar de advertencia
                Snackbar.make(binding.root, "Por favor ingrese un título para el crimen", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                // Si el título no está vacío, procedemos a guardar el crimen
                viewModel.agregarCrimen(crimen)

                // Mostrar el Snackbar de confirmación
                Snackbar.make(binding.root, "Crimen guardado con éxito", Snackbar.LENGTH_SHORT)
                    .setAction("Cerrar") {}
                    .show()

                // Redirigir a la lista de crímenes después de guardar usando FragmentTransaction
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment()) // Reemplazar con el fragmento de lista
                fragmentTransaction.addToBackStack(null) // Permite ir atrás
                fragmentTransaction.commit()

                Log.d("DetalleCrimenFragment", "Redirigiendo a ListaCrimenFragment")
            }
        }
        binding.btnCancelar.setOnClickListener {
            // Redirigir a la lista de crímenes
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment()) // Reemplazar con el fragmento de lista
            fragmentTransaction.addToBackStack(null) // Permite ir atrás
            fragmentTransaction.commit()

            Log.d("DetalleCrimenFragment", "Redirigiendo a ListaCrimenFragment")
        }

        // Bloquear la navegación con onBackPressed si el título está vacío
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.txtTituloCrimen.text.isNullOrEmpty()) {
                    Snackbar.make(binding.root, "Por favor ingrese un título", Snackbar.LENGTH_SHORT).show()
                } else {
                    // Si el título no está vacío, permitir regresar
                    requireActivity().onBackPressed()
                }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
