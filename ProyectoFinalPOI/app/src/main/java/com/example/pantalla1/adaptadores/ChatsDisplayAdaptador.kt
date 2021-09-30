package com.example.pantalla1.adaptadores

import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.Grupo
import com.example.pantalla1.R
import com.example.pantalla1.chatActivity
import kotlinx.android.synthetic.main.group_dessign.view.*
import kotlinx.android.synthetic.main.simple_string.view.*

class ChatsDisplayAdaptador (private var listaGrupos: MutableList<String>, private var mycontext: Context,
                             private var usuario:String, private var chatname:MutableList<String>, private var Cyph:Boolean):
    RecyclerView.Adapter<ChatsDisplayAdaptador.GrupoViewHolder>(){

    class GrupoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun asignarInformacion( grupoTemp: String, pasencontexto: Context, user: String, nombrechat:String, cyph:Boolean){
            itemView.setOnClickListener{
                val intentChat = Intent(pasencontexto, chatActivity::class.java)
                intentChat.putExtra("nombreUsuario", user)
                intentChat.putExtra("referencia", grupoTemp)
                intentChat.putExtra("cyph", cyph)
                pasencontexto.startActivity(intentChat)

            }
            itemView.tvGrupo.text = nombrechat

        }
    }
    override fun getItemCount(): Int {
        return listaGrupos.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsDisplayAdaptador.GrupoViewHolder {
        return ChatsDisplayAdaptador.GrupoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.group_dessign, parent, false)
        )
    }
    override fun onBindViewHolder(holder: ChatsDisplayAdaptador.GrupoViewHolder, position: Int) {
        holder.asignarInformacion(listaGrupos[position], mycontext, usuario, chatname[position], Cyph)

    }
}