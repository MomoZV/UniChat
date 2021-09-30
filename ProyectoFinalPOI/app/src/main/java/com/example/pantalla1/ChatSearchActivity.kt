package com.example.pantalla1

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.chat_searcher.*

class ChatSearchActivity : AppCompatActivity(){
    val UserRef = FirebaseDatabase.getInstance().getReference("Usuarios")
    var Logrado = false
    private lateinit var nombreUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_searcher)
        nombreUsuario = intent.getStringExtra("nombreUsuario").toString()
        val btnBuscar =  findViewById<Button>(R.id.btnChatW)
        val btnAll =  findViewById<Button>(R.id.btnChatA)

        btnBuscar.setOnClickListener{
            val User = textWith.text.toString()
            if(User.isEmpty()){
                Toast.makeText(this, "Falta usuario", Toast.LENGTH_SHORT).show()
            }
            else{
                val intentChat = Intent(this, chatActivity::class.java)
                BuscarChatter(User)
                if(Logrado == false){
                    intentChat.putExtra("nombreUsuario", nombreUsuario)
                    intentChat.putExtra("nombreUsuario2", User)
                    startActivity(intentChat)
                }
                else{
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()

                }
            }

        }
        btnAll.setOnClickListener{
            val intentChat = Intent(this, chatActivity::class.java)
            intentChat.putExtra("nombreUsuario", nombreUsuario)
            intentChat.putExtra("nombreUsuario2", "All")
            startActivity(intentChat)
        }
    }

    private fun BuscarChatter(usuario: String){
        UserRef.addValueEventListener ( object :ValueEventListener{
            override fun onDataChange( snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val UserName = snap.child("username").getValue(String::class.java)
                    if (usuario == UserName.toString()) {
                        Logrado = true
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

}