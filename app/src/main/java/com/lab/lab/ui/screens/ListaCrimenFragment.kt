package com.lab.lab.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lab.lab.databinding.FragmentListaCrimenBinding
import com.lab.lab.ui.adapter.ListaCrimenAdapter
import com.lab.lab.ui.viewmodel.ListaCrimenViewModel
import com.lab.lab.R
import kotlinx.coroutines.launch

class ListaCrimenFragment : Fragment() {

    private var _binding: FragmentListaCrimenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListaCrimenViewModel by viewModels()

    private lateinit var adapter: ListaCrimenAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaCrimenBinding.inflate(inflater, container, false)

        binding.crimenRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ListaCrimenAdapter(emptyList())
        binding.crimenRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.crimenes.collect { lista ->
                adapter = ListaCrimenAdapter(lista)
                binding.crimenRecyclerView.adapter = adapter
            }
        }
        binding.btnAgregarCrimen.setOnClickListener {
            Log.d("ListaCrimenFragment", "Botón Agregar Crimen presionado")
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
