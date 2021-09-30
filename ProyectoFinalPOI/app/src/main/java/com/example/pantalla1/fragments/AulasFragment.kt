package com.example.pantalla1.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.Grupo
import com.example.pantalla1.NewGroupActivity
import com.example.pantalla1.R
import com.example.pantalla1.adaptadores.GroupAdaptador
import com.example.pantalla1.adaptadores.PostAdaptador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AulasFragment : Fragment() {
    var listaGrupo = mutableListOf<Grupo>()
    lateinit var adaptador :GroupAdaptador
    val groupRef = FirebaseDatabase.getInstance().getReference("Grupos")
    private lateinit var nombreUsuario: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var GroupLay = inflater.inflate(R.layout.fragment_group,container,false)
        nombreUsuario = activity?.intent?.getStringExtra("nombreUsuario").toString() ?: "sin_nombre"
        adaptador = GroupAdaptador(listaGrupo, requireContext(), nombreUsuario)
        val myRv= GroupLay.findViewById<RecyclerView>(R.id.rvGroups)
        myRv.layoutManager = LinearLayoutManager(activity)
        myRv.adapter=adaptador
        val btnAdd = GroupLay.findViewById<FloatingActionButton>(R.id.btnNewGroup)

        btnAdd.setOnClickListener {
            val intentChat = Intent(requireContext(), NewGroupActivity::class.java)
            startActivity(intentChat)
        }
        recibirGrupo()
        return GroupLay
    }

    fun recibirGrupo(){
        groupRef.addValueEventListener( object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange( snapshot: DataSnapshot) {
                listaGrupo.clear()


                for(snap in snapshot.children){


                    for (snp in snap.child("participantes").children){

                        if (snp.getValue(String::class.java).toString() == nombreUsuario){
                            val groupid =  snap.child("id").getValue(String::class.java)
                            val groupname =  snap.child("nombre").getValue(String::class.java)
                            val groupTemp = Grupo(groupid.toString(),groupname.toString(), mutableListOf<String>())
                            listaGrupo.add(groupTemp)
                        }
                    }

                }
                if (listaGrupo.size > 0){
                    adaptador.notifyDataSetChanged()
                }

            }
        })
    }

}