package com.lab.lab.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lab.lab.databinding.FragmentDetalleCrimenBinding
import com.lab.lab.model.Crimen
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
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

    private val viewModel: ListaCrimenViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleCrimenBinding.inflate(inflater, container, false)

        // Recuperar crimenId si fue pasado como argumento
        val crimenId = arguments?.getSerializable(ARG_CRIMEN_ID) as? UUID
        if (crimenId != null) {
            viewModel.obtenerCrimenPorId(crimenId) { crimenEncontrado ->
                crimenEncontrado?.let {
                    crimen = it
                    requireActivity().runOnUiThread {
                        mostrarDatosCrimen()
                    }
                }
            }
        } else {
            mostrarDatosCrimen() // Mostrar datos por defecto si es nuevo
        }

        binding.txtTituloCrimen.doOnTextChanged { texto, _, _, _ ->
            crimen = crimen.copy(titulo = texto.toString())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            setFragmentResultListener("fechaSeleccionada") { _, bundle ->
                val fecha = bundle.getSerializable("fecha") as Date
                crimen = crimen.copy(fecha = fecha)
                binding.txtFechaCrimen.text =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            setFragmentResultListener("horaSeleccionada") { _, bundle ->
                val hora = bundle.getSerializable("hora") as Date
                val newCalendar = Calendar.getInstance()
                newCalendar.time = crimen.fecha
                newCalendar.set(Calendar.HOUR_OF_DAY, hora.hours)
                newCalendar.set(Calendar.MINUTE, hora.minutes)

                crimen = crimen.copy(fecha = newCalendar.time)
                binding.txtHoraCrimen.text =
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(newCalendar.time)
            }
        }

        // Selección de fecha y hora
        binding.iconoFecha.setOnClickListener {
            DatePickerFragment().show(parentFragmentManager, "datePicker")
        }

        binding.iconoHora.setOnClickListener {
            HoraPickerFragment().show(parentFragmentManager, "horaPicker")
        }

        // Botón guardar: actualiza si ya existe, agrega si es nuevo
        binding.btnGuardarCrimen.setOnClickListener {
            if (binding.txtTituloCrimen.text.isNullOrEmpty()) {
                Snackbar.make(binding.root, "Por favor ingrese un título para el crimen", Snackbar.LENGTH_SHORT).show()
            } else {
                val esNuevo = viewModel.crimenes.value.none { it.id == crimen.id }

                if (esNuevo) {
                    viewModel.agregarCrimen(crimen)
                    Snackbar.make(binding.root, "Crimen guardado con éxito", Snackbar.LENGTH_SHORT).show()
                } else {
                    viewModel.actualizarCrimen(crimen)
                    Snackbar.make(binding.root, "Crimen actualizado con éxito", Snackbar.LENGTH_SHORT).show()
                }

                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        binding.chkResuelto.setOnCheckedChangeListener { _, marcado ->
            crimen = crimen.copy(resuelto = marcado)
        }

        // Botones de cancelar o volver
        val volverAccion = {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, ListaCrimenFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.btnCancelar.setOnClickListener { volverAccion() }
        binding.btnVolver.setOnClickListener { volverAccion() }

        return binding.root
    }

    private fun mostrarDatosCrimen() {
        binding.txtTituloCrimen.setText(crimen.titulo)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.txtFechaCrimen.text = dateFormat.format(crimen.fecha)
        binding.txtHoraCrimen.text = timeFormat.format(crimen.fecha)
        binding.chkResuelto.isChecked = crimen.resuelto
    }

    companion object {
        private const val ARG_CRIMEN_ID = "crimen_id"

        fun newInstance(crimenId: UUID): DetalleCrimenFragment {
            val fragment = DetalleCrimenFragment()
            val args = Bundle()
            args.putSerializable(ARG_CRIMEN_ID, crimenId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
