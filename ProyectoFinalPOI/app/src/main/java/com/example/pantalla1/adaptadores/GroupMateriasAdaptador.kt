package com.example.pantalla1.adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.GroupActivity
import com.example.pantalla1.Grupo
import com.example.pantalla1.R
import com.example.pantalla1.chatActivity
import kotlinx.android.synthetic.main.group_dessign.view.*

class GroupMateriasAdaptador(private var listaGrupos: MutableList<Grupo>, private var mycontext:Context, private var usuario:String):
    RecyclerView.Adapter<GroupMateriasAdaptador.GrupoViewHolder>(){

    class GrupoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun asignarInformacion( grupoTemp: Grupo, pasencontexto:Context,user:String){
            itemView.setOnClickListener{
                val intentChat = Intent(pasencontexto, GroupActivity::class.java)
                intentChat.putExtra("nombreUsuario", user)
                intentChat.putExtra("referencia", grupoTemp.id)
                pasencontexto.startActivity(intentChat)
            }
            itemView.tvGrupo.text = grupoTemp.nombre
        }
    }
    override fun getItemCount(): Int {
        return listaGrupos.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMateriasAdaptador.GrupoViewHolder {
        return GroupMateriasAdaptador.GrupoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.group_dessign, parent, false)
        )
    }
    override fun onBindViewHolder(holder: GroupMateriasAdaptador.GrupoViewHolder, position: Int) {
        holder.asignarInformacion(listaGrupos[position], mycontext, usuario)

    }
}