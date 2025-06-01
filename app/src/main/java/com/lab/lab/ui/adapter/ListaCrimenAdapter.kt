package com.lab.lab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab.lab.databinding.ItemListaBinding
import com.lab.lab.model.Crimen
import java.text.SimpleDateFormat
import java.util.*

class ListaCrimenAdapter(private val crimenes: List<Crimen>) :
    RecyclerView.Adapter<ListaCrimenAdapter.CrimenHolder>() {

    inner class CrimenHolder(private val binding: ItemListaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crimen: Crimen) {
            binding.tituloCrimen.text = crimen.titulo

            // Formatear la fecha y hora
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val formattedDate = dateFormat.format(crimen.fecha)
            binding.fechaCrimen.text = formattedDate

            // Mostrar el ícono de resuelto si corresponde
            if (crimen.resuelto) {
                binding.imgResuelto.visibility = View.VISIBLE
                binding.imgNoResuelto.visibility = View.GONE
            } else {
                binding.imgResuelto.visibility = View.GONE
                binding.imgNoResuelto.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimenHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListaBinding.inflate(inflater, parent, false)
        return CrimenHolder(binding)
    }

    override fun onBindViewHolder(holder: CrimenHolder, position: Int) {
        holder.bind(crimenes[position])
    }

    override fun getItemCount(): Int = crimenes.size
}
