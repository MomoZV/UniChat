package com.example.pantalla1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.adaptadores.ChatAdaptador
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.fragment_chat1v1.*
import java.io.File

class chatActivity : AppCompatActivity() {
    private val listaMensajes = mutableListOf<Mensaje2>()
    private val adaptador = ChatAdaptador(listaMensajes)
    //private lateinit var chatAdaptador : ChatAdaptador
    private lateinit var nombreUsuario: String
    private lateinit var nombreUsuario2: String
    private lateinit var referenciaChat: DatabaseReference
    private lateinit var thisChat: DatabaseReference
    private var cypher =true
    private val db = FirebaseDatabase.getInstance()
    private val Cypher = myCypher
    private val chatRef = FirebaseDatabase.getInstance().getReference("chats")
    private val strRef = FirebaseStorage.getInstance().getReference("chats")

    override fun onCreate(savedInstanceState: Bundle?) {
        chatRef.keepSynced(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat1v1)

        val btnBuscar = findViewById<ImageView>(R.id.btnSend)
        val btnGaleria = findViewById<ImageView>(R.id.btnSubirFotoPerfil)
        val myRv = findViewById<RecyclerView>(R.id.rvChat)
        nombreUsuario = intent.getStringExtra("nombreUsuario").toString() ?: "sin_nombre"
        val instantreference = intent.getStringExtra("referencia").toString() ?: "sin_nombre"
        cypher = intent.getBooleanExtra("cyph", true)

        referenciaChat = FirebaseDatabase.getInstance().getReference("chats").child(instantreference)
        myRv.adapter = adaptador

        btnGaleria.setOnClickListener{
            val intentGaleria = ImagePicker.with(this)
                .crop()        //Crop image(Optional), Check Customization for more option
                .cropOval()    //Allow dimmed layer to have a circle inside
                .maxResultSize(1080, 1080).provider(ImageProvider.GALLERY).createIntent()//Final image resolution will be less than 1080 x 1080(Optional)
            startActivityForResult(intentGaleria, 1)
            onActivityResult(1, 1, intentGaleria)
        }

        btnBuscar.setOnClickListener{
            Toast.makeText(this, "ESTA ENTRANDO AL EVENTO CLIC", Toast.LENGTH_SHORT).show()
            val mensaje = txtMensaje.text.toString()
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()

            txtMensaje.text.clear()

            if(mensaje.isNotEmpty()){

                txtMensaje.text.clear()

                if(cypher==false) {
                    val objetoMensaje = Mensaje2(
                        "",
                        mensaje,
                        nombreUsuario,
                        ServerValue.TIMESTAMP,
                        cypher
                    )
                    enviarMensaje(objetoMensaje)
                }
                if(cypher==true){
                    val objetoMensaje = Mensaje2(
                        "",
                        Cypher.cifrar(mensaje, "M0Mo3lZvCyph3R"),
                        nombreUsuario,
                        ServerValue.TIMESTAMP,
                        cypher)
                    enviarMensaje(objetoMensaje)
                }
            }
        }

        recibirMensajes()
    }
    private fun BuscarChat(){
        //var returnable = chatRef;
        var Found = false
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val p1 = snap.child("Participantes").child("p1").getValue(String::class.java)
                    val p2 = snap.child("Participantes").child("p2").getValue(String::class.java)
                    Log.d("lul", p1.toString())
                    Log.d("lul", p2.toString())

                    if ((nombreUsuario == p1.toString() || nombreUsuario == p2.toString()) && (nombreUsuario2 == p1.toString() || nombreUsuario2 == p2.toString())) {

                        //thisChat = snap.ref
                        Found = true

                    }
                    if (nombreUsuario2 == "All" && p2.toString() == "All") {
                       // thisChat = snap.ref
                        Found = true
                    }
                }
            }
        })
        //return returnable
        if(Found == false){
            if(nombreUsuario2 == "All"){}
            else{
                val nuevoChat = chatRef.push().child("Participantes")
                val Participantes = ParticipantesChat(
                    nombreUsuario,
                    nombreUsuario2
                )
                nuevoChat.setValue(Participantes)
            }

        }
    }
    private fun enviarMensaje(mensaje: Mensaje2){

        val mensajeFirebase = referenciaChat.child("Mensajes").push()
        mensaje.id = mensajeFirebase.key ?: ""
        mensajeFirebase.setValue(mensaje)
    }
    private fun subirImagen(file: File){
        val FileUri= Uri.fromFile(file)
        val pathfile = file.name.toString()
        var pushimage = strRef.child(pathfile)
        pushimage.putFile(FileUri).addOnSuccessListener {  }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
            val objetoMensaje= Mensaje2(
                "",
                file.name.toString(),
                nombreUsuario,
                ServerValue.TIMESTAMP,
            false
            )
            enviarMensaje(objetoMensaje)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    private fun recibirMensajes(){

        referenciaChat.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                listaMensajes.clear()
                       for (sn in snapshot.child("Mensajes").children) {
                           var mensaje: Mensaje2 = sn.getValue(Mensaje2::class.java) as Mensaje2
                           mensaje.cifrado
                           Log.d("OWO", mensaje.cifrado.toString())
                           if(mensaje.cifrado == true){
                               mensaje.contenido = Cypher.descifrar(mensaje.contenido, "M0Mo3lZvCyph3R")
                           }
                           mensaje.esMio = mensaje.de == nombreUsuario
                           listaMensajes.add(mensaje)
                        }
                        if (listaMensajes.size > 0) {
                            adaptador.notifyDataSetChanged()
                            rvChat.smoothScrollToPosition(listaMensajes.size - 1)
                        }
            }
        })
    }

}

