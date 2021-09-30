package com.example.pantalla1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pantalla1.adaptadores.ParticipantsAdaptador
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_group.*

class NewGroupActivity : AppCompatActivity() {
    private val groupRef = FirebaseDatabase.getInstance().getReference("Grupos")
    private val chatRef = FirebaseDatabase.getInstance().getReference("chats")
    var nombreUsuario = Grupo("","", mutableListOf<String>())
    var adaptador = ParticipantsAdaptador(nombreUsuario.Participantes)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_group)
        // adaptador


        rvParts.adapter = adaptador
        rvParts.layoutManager = LinearLayoutManager(this)
        val agregarGrupo = findViewById<Button>(R.id.addGroup)
        val agregarPart = findViewById<Button>(R.id.addMemb)


        agregarPart.setOnClickListener(){
            val partTemp = membName.text.toString()
            if(partTemp.isEmpty()){ }
            else{
                nombreUsuario.Participantes.add(partTemp)
                adaptador.notifyDataSetChanged()
            }

        }
        agregarGrupo.setOnClickListener() {

            val partGName = groupName.text.toString()
            if (partGName.isEmpty()) { }
            else {
                nombreUsuario.nombre = partGName
                enviarGrupo(nombreUsuario)
                Toast.makeText(this, "Grupo creado exitosamente", Toast.LENGTH_SHORT).show()
                val intentChat = Intent(this, MenuLatActivity::class.java)
                startActivity(intentChat)
            }
        }

    }


    fun enviarGrupo(group: Grupo){
        val grupoFirebase= groupRef.push()
        group.id = grupoFirebase.key ?: ""
        grupoFirebase.setValue(group)
        val chatfirebase = chatRef.push()
        val newchasillo = ChatFromGroup(chatfirebase.key?:"", group.nombre, group.Participantes, false)
        chatfirebase.setValue(newchasillo)
    }


}