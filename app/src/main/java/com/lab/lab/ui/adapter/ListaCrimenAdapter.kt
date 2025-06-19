package com.lab.lab.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab.lab.databinding.ItemListaBinding
import com.lab.lab.model.Crimen
import java.text.SimpleDateFormat
import java.util.*

class ListaCrimenAdapter(
    private val crimenes: List<Crimen>,
    private val onConfirmarEliminarClick: (Crimen) -> Unit,
    private val onCrimenClick: (Crimen) -> Unit
) : RecyclerView.Adapter<ListaCrimenAdapter.CrimenHolder>() {

    inner class CrimenHolder(private val binding: ItemListaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crimen: Crimen) {
            binding.tituloCrimen.text = crimen.titulo

            // Formatear la fecha del crimen
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            binding.fechaCrimen.text = dateFormat.format(crimen.fecha)

            // Mostrar íconos de resuelto o no resuelto
            binding.imgResuelto.visibility = if (crimen.resuelto) View.VISIBLE else View.GONE
            binding.imgNoResuelto.visibility = if (!crimen.resuelto) View.VISIBLE else View.GONE

            // Botón de eliminar (confirmación vendrá de la lógica en el Fragment)
            binding.btnEliminarCrimen.setOnClickListener {
                onConfirmarEliminarClick(crimen)
            }

            // Click en toda la tarjeta para ver detalle del crimen
            binding.root.setOnClickListener {
                onCrimenClick(crimen)
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
