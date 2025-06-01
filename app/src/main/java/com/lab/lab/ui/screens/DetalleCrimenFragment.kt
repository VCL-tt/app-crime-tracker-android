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

        binding.txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->
            crimen = crimen.copy(titulo = texto.toString())
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        binding.btnDetalleCrimen.text = dateFormat.format(crimen.fecha)

        binding.btnDetalleCrimen.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    calendar.set(year, monthOfYear, dayOfMonth)
                    val selectedDate = calendar.time


                    val timePickerDialog = TimePickerDialog(
                        requireContext(),
                        { _, hourOfDay, minute ->
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            calendar.set(Calendar.MINUTE, minute)
                            val selectedDateTime = calendar.time

                            val formattedDate = dateFormat.format(selectedDateTime)
                            binding.btnDetalleCrimen.text = formattedDate

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

        binding.chkResuelto.setOnCheckedChangeListener { _, marcado ->
            crimen = crimen.copy(resuelto = marcado)
        }

        binding.btnGuardarCrimen.setOnClickListener {
            viewModel.agregarCrimen(crimen)

            Snackbar.make(binding.root, "Crimen guardado con éxito", Snackbar.LENGTH_SHORT)
                .setAction("Cerrar") {}
                .show()

            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            Log.d("DetalleCrimenFragment", "Redirigiendo a ListaCrimenFragment")
        }

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
