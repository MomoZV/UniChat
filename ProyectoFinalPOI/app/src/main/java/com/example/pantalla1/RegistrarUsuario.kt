package com.example.pantalla1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.iniciarsesion.*
import kotlinx.android.synthetic.main.iniciarsesion.btnRegistrar
import kotlinx.android.synthetic.main.iniciarsesion.txtContraseña
import kotlinx.android.synthetic.main.iniciarsesion.txtEmailNew
import kotlinx.android.synthetic.main.registro_usuarios.*
import java.io.File

import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.adaptadores.ChatAdaptador
import com.example.pantalla1.adaptadores.MenuActivity2
import com.google.firebase.database.*
class RegistrarUsuario:AppCompatActivity() {
    val UserRef = FirebaseDatabase.getInstance().getReference("Usuarios")
    var lebool = false
    lateinit var nombreUsuario : String
    lateinit var Contraseña: String
    lateinit var emailUser : String
    private val strRef = FirebaseStorage.getInstance().getReference("Usuarios")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro_usuarios)
        btnRegistrar.setOnClickListener{

            Toast.makeText(this, "Te has dado de alta con éxito", Toast.LENGTH_SHORT).show()
           val intentChat = Intent(this, MainActivity::class.java)
            startActivity(intentChat)

          /*  onActivityResult(200, Activity.RESULT_OK  ,nombreUsuario,Contraseña,emailUser )*/
        }

        btnSubirFotoPerfil.setOnClickListener{
            val intentGaleria = ImagePicker.with(this)
                .crop()        //Crop image(Optional), Check Customization for more option
                .cropOval()    //Allow dimmed layer to have a circle inside
                .maxResultSize(1080, 1080).provider(ImageProvider.GALLERY).createIntent()//Final image resolution will be less than 1080 x 1080(Optional)
            startActivityForResult(intentGaleria, 1)
            onActivityResult(1, 1, intentGaleria)
        }
    }
 /*   private fun onActivityResult(requestCode: Int, result: ActivityResult, nombreUsuario,Contraseña,emailUser) {
        if(result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            when (requestCode) {


            }
            if (requestCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = intent?.data
                // var imgProfile
                // imgProfile.setImageURI(fileUri)

                //You can get File object from intent
                val file: File = ImagePicker.getFile(intent)!!

                //You can also get File Path from intent
                val filePath:String = ImagePicker.getFilePath(intent)!!
                subirImagen(file)

                ////Mi
                val objetoMensaje= Usuario(
                    username = nombreUsuario,
                    contraseña = Contraseña,
                    email = emailUser,
                    perfil = file.name.toString()
                )
                RegistrarUsuario(objetoMensaje)

            } else if (requestCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(intent), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    }*/
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     val nombreUsuario = txtUsuarioNew.text.toString()
     val Contraseña = txtContraseña.text.toString()
     val emailUser = txtEmailNew.text.toString()
     val roleUser = roleUser.text.toString()
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            // var imgProfile
            // imgProfile.setImageURI(fileUri)

            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)!!
            subirImagen(file)

            ////Mi
            val objetoMensaje= Usuario(
                username = nombreUsuario,
                contraseña = Contraseña,
                email = emailUser,
                perfil = file.name.toString(),
                role = roleUser
            )
            RegistrarUsuario(objetoMensaje)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun subirImagen(file: File){
        val FileUri= Uri.fromFile(file)
        val pathfile = file.name.toString()
        var pushimage = strRef.child(pathfile)
        pushimage.putFile(FileUri).addOnSuccessListener {  }
    }
    private fun RegistrarUsuario(usuario: Usuario){
        val sendUser = UserRef.push()
        Log.d("lul", sendUser.toString())
        sendUser.setValue(usuario)
    }
    /*private fun enviarMensaje(mensaje: Mensaje2){
        val mensajeFirebase = referenciaChat.child("Mensajes").push()
        mensaje.id = mensajeFirebase.key ?: ""
        mensajeFirebase.setValue(mensaje)
    } */
}