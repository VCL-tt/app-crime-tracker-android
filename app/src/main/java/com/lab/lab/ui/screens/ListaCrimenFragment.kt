package com.lab.lab.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab.lab.databinding.FragmentListaCrimenBinding
import com.lab.lab.ui.adapter.ListaCrimenAdapter
import com.lab.lab.ui.viewmodel.ListaCrimenViewModel
import com.lab.lab.R
import com.lab.lab.ui.screens.DetalleCrimenFragment
import kotlinx.coroutines.launch

class ListaCrimenFragment : Fragment() {

    private var _binding: FragmentListaCrimenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListaCrimenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaCrimenBinding.inflate(inflater, container, false)

        binding.crimenRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.crimenes.collect { lista ->
                val adapter = ListaCrimenAdapter(
                    lista,
                    onConfirmarEliminarClick = { crimen ->
                        AlertDialog.Builder(requireContext())
                            .setTitle("¿Eliminar crimen?")
                            .setMessage("¿Estás seguro de que deseas eliminar \"${crimen.titulo}\"?")
                            .setPositiveButton("Sí") { _, _ ->
                                viewModel.eliminarCrimen(crimen)
                            }
                            .setNegativeButton("Cancelar", null)
                            .show()
                    },
                    onCrimenClick = { crimen ->
                        val fragment = DetalleCrimenFragment.newInstance(crimen.id)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                )
                binding.crimenRecyclerView.adapter = adapter

                // Mostrar u ocultar el mensaje y el botón según la lista
                if (lista.isEmpty()) {
                    binding.txtNoItems.visibility = View.VISIBLE // Mostrar el mensaje de no items

                } else {
                    binding.txtNoItems.visibility = View.GONE // Ocultar el mensaje de no items

                }
            }
        }


        binding.btnAgregarCrimenToolbar.setOnClickListener {
            Log.d("ListaCrimenFragment", "Icono Agregar Crimen presionado")
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, DetalleCrimenFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            Log.d("ListaCrimenFragment", "Navegación realizada exitosamente a DetalleCrimenFragment")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
