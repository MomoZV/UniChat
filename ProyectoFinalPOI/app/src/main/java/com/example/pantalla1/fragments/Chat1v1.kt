package com.example.pantalla1.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.Mensaje2
import com.example.pantalla1.ParticipantesChat
import com.example.pantalla1.R
import com.example.pantalla1.adaptadores.ChatAdaptador
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

import kotlinx.android.synthetic.main.fragment_chat1v1.*
import java.io.File

/*override fun onAttach(context: Context) {
       Log.d(Constraints.TAG,"onAttach")
       super.onAttach(context)
   }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
   }
   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       var rootView = inflater.inflate(R.layout.fragment_tareas,container,false)

       return rootView
   }
   override fun onActivityCreated(savedInstanceState: Bundle?) {
       super.onActivityCreated(savedInstanceState)

   }*/
class Chat1v1 : Fragment() {
    private val listaMensajes = mutableListOf<Mensaje2>()
    private val adaptador = ChatAdaptador(listaMensajes)
    private lateinit var nombreUsuario: String
    private lateinit var nombreUsuario2: String
    private lateinit var thisChat: DatabaseReference
    private val db = FirebaseDatabase.getInstance()

    private val chatRef = FirebaseDatabase.getInstance().getReference("chats")
    private val strRef = FirebaseStorage.getInstance().getReference("chats")
    override fun onAttach(context: Context) {

           super.onAttach(context)
       }

       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
       }
       override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
           var rootViewChat1 = inflater.inflate(R.layout.fragment_chat1v1,container,false)
           chatRef.keepSynced(true)
           nombreUsuario = activity?.intent?.getStringExtra("nombreUsuario").toString() ?: "sin_nombre"
           nombreUsuario2 = activity?.intent?.getStringExtra("nombreUsuario2").toString() ?: "sin_nombre"
           BuscarChat()
           val btnBuscar = rootViewChat1.findViewById<ImageView>(R.id.btnSend)
           val btnGaleria = rootViewChat1.findViewById<ImageView>(R.id.btnSubirFotoPerfil)
           val myRv = rootViewChat1.findViewById<RecyclerView>(R.id.rvChat)
           myRv.layoutManager = LinearLayoutManager(activity)
           myRv.adapter = adaptador
            btnGaleria.setOnClickListener{
                val intentGaleria = ImagePicker.with(requireActivity())
                    .crop()        //Crop image(Optional), Check Customization for more option
                    .cropOval()    //Allow dimmed layer to have a circle inside
                    .maxResultSize(1080, 1080).provider(ImageProvider.GALLERY).createIntent()//Final image resolution will be less than 1080 x 1080(Optional)
                startActivityForResult(intentGaleria, 1)
                onActivityResult(1, 1, intentGaleria)
            }

           btnBuscar.setOnClickListener{
               Toast.makeText(getActivity(), "ESTA ENTRANDO AL EVENTO CLIC", Toast.LENGTH_SHORT).show()
               Log.d("lul", thisChat.toString())
               val mensaje = txtMensaje.text.toString()
               Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show()

               txtMensaje.text.clear()


               if(mensaje.isNotEmpty()){

                   txtMensaje.text.clear()

                   val objetoMensaje= Mensaje2(
                           "",
                           mensaje,
                           nombreUsuario,
                           ServerValue.TIMESTAMP
                   )

                   enviarMensaje(objetoMensaje)

                   //..
               }

           }
           recibirMensajes()
           return rootViewChat1
       }
       override fun onActivityCreated(savedInstanceState: Bundle?) {
           super.onActivityCreated(savedInstanceState)

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
                ServerValue.TIMESTAMP
            )
            enviarMensaje(objetoMensaje)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
           // Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
          //  Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarMensaje(mensaje: Mensaje2 ){

          val mensajeFirebase = thisChat.child("Mensajes").push()
          mensaje.id = mensajeFirebase.key ?: ""
          mensajeFirebase.setValue(mensaje)
    }
    private fun BuscarChat(){
        //var returnable = chatRef;
        var Found = false
        chatRef.addValueEventListener( object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange( snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val p1 = snap.child("Participantes").child("p1").getValue(String::class.java)
                    val p2 = snap.child("Participantes").child("p2").getValue(String::class.java)
                    Log.d("lul", p1.toString())
                    Log.d("lul", p2.toString())

                    if((nombreUsuario == p1.toString() || nombreUsuario == p2.toString() ) && (nombreUsuario2 == p1.toString() || nombreUsuario2 == p2.toString() )) {

                        thisChat = snap.ref
                        Found = true

                    }
                    if(nombreUsuario2 == "All" && p2.toString() == "All"){
                        thisChat = snap.ref
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
    private fun showData(){

    }

    private fun recibirMensajes(){
        chatRef.addValueEventListener( object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange( snapshot: DataSnapshot) {
                listaMensajes.clear()
                for(snap in snapshot.children){
                    val p1 = snap.child("Participantes").child("p1").getValue(String::class.java)
                    val p2 = snap.child("Participantes").child("p2").getValue(String::class.java)
                    if((nombreUsuario == p1.toString() || nombreUsuario == p2.toString() ) && (nombreUsuario2 == p1.toString() || nombreUsuario2 == p2.toString() )) {
                        for(sn in snap.child("Mensajes").children){
                            val mensaje : Mensaje2 =  sn.getValue(Mensaje2::class.java) as Mensaje2
                            mensaje.esMio = mensaje.de == nombreUsuario
                            listaMensajes.add(mensaje)
                        }
                        if (listaMensajes.size > 0){
                            adaptador.notifyDataSetChanged()
                            rvChat.smoothScrollToPosition(listaMensajes.size -1)
                        }
                    }
                    if(nombreUsuario2=="All" && p2.toString() == "All"){
                        for(sn in snap.child("Mensajes").children){
                            val mensaje : Mensaje2 =  sn.getValue(Mensaje2::class.java) as Mensaje2
                            mensaje.esMio = mensaje.de == nombreUsuario
                            listaMensajes.add(mensaje)
                        }
                        if (listaMensajes.size > 0){
                            adaptador.notifyDataSetChanged()
                            rvChat.smoothScrollToPosition(listaMensajes.size -1)
                        }
                    }
                }

            }
        })
    }
}