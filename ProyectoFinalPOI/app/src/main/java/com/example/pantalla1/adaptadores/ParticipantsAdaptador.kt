package com.example.pantalla1.adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.R
import kotlinx.android.synthetic.main.group_dessign.view.*
import kotlinx.android.synthetic.main.simple_string.view.*

class ParticipantsAdaptador (private var listaPart: MutableList<String>):
    RecyclerView.Adapter<ParticipantsAdaptador.ParticipantViewHolder>(){

    class ParticipantViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun asignarInformacion( grupoTemp: String){
            itemView.tvString.text = grupoTemp

        }
    }
    override fun getItemCount(): Int {
        return listaPart.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsAdaptador.ParticipantViewHolder {
        return ParticipantsAdaptador.ParticipantViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.simple_string, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ParticipantsAdaptador.ParticipantViewHolder, position: Int) {
        holder.asignarInformacion(listaPart[position])
    }
}