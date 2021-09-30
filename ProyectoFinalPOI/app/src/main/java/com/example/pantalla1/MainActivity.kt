package com.example.pantalla1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.iniciarsesion.*

class MainActivity : AppCompatActivity()  {
    //val db = FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    val UserRef = FirebaseDatabase.getInstance().getReference("Usuarios")
    var lebool = false
    var encontrado = false
     var cualNombreUser = " "
     var cualPerfil :String? = null
    var cualRole:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        lebool = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.iniciarsesion)
        val btnRegistra = findViewById<Button>(R.id.btnRegistrar)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)

        btnIniciar.setOnClickListener{
            val emailUsuario = txtEmailNew.text.toString()
            val contraUsuario = txtContraseña.text.toString()
            if (emailUsuario.isEmpty()) {

                Toast.makeText(this, "Falta email", Toast.LENGTH_SHORT).show()
            } else {
                IniciarSesion(emailUsuario, contraUsuario)
            }
        }

        btnRegistrar.setOnClickListener{
            val intentChat = Intent(this, RegistrarUsuario::class.java)
            startActivity(intentChat)
        }
        lebool = false
    }

    private fun inicia(emailusuario: String, pass: String, cualNombreUser: String){
        val intentChat = Intent(this, MenuLatActivity::class.java)
        intentChat.putExtra("nombreUsuario", cualNombreUser)
        intentChat.putExtra("emailUsuario", emailusuario)
        intentChat.putExtra("perfilUsuario", cualPerfil)
        intentChat.putExtra("rolUsuario", cualRole)
        startActivity(intentChat)
        val User = Usuario(
            username =  cualNombreUser ,
            contraseña = pass,
            email = emailusuario
        )
    }
    private fun IniciarSesion(emailusuario: String, pass: String){
        Log.d("lul", "lebool.toString()")
        UserRef.addValueEventListener ( object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange( snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val UserName = snap.child("username").getValue(String::class.java)
                    val Contra =snap.child("contraseña").getValue(String::class.java)
                    val email =snap.child("email").getValue(String::class.java)
                    val perfil =snap.child("perfil").getValue(String::class.java)
                    val role =snap.child("role").getValue(String::class.java)
                    Log.d("lul", "lebool.toString()")
                    if (emailusuario == email.toString()) {
                        if (pass == Contra.toString()){
                            lebool = true
                            encontrado = true
                            Log.d("lul", lebool.toString())
                            if (UserName != null) {
                                cualNombreUser = UserName
                                cualPerfil =  perfil
                                cualRole =  role
                                inicia( emailusuario, pass, cualNombreUser)
                            }
                        }
                    }
                }
            }

        })
        if(lebool == false){
            Toast.makeText(this, "Usuario no encontrado, registrate", Toast.LENGTH_SHORT).show()
        }
    }

}

