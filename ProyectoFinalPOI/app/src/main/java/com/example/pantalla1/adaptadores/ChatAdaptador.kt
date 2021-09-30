package com.example.pantalla1.adaptadores

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.Mensaje2
import com.example.pantalla1.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.messages.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ChatAdaptador(private val listaMensaje: MutableList<Mensaje2>):
    RecyclerView.Adapter<ChatAdaptador.ChatViewHolder>() {

    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val strRef = FirebaseStorage.getInstance().getReference("chats")
        fun asignarInformacion( mensaje2: Mensaje2){
            itemView.tvNombre.text = mensaje2.de
            val stRef2 = strRef.child(mensaje2.contenido)
            val file = File(stRef2.toString())

            if(file.extension =="jpg"||file.extension =="png"||file.extension =="gif"||file.extension =="jpeg"){
                itemView.tvMensaje.text = ""
                stRef2.downloadUrl.addOnSuccessListener {
                    Picasso.get().load(it.toString()).resize(400, 400).centerCrop().into(itemView.msgView)
                }
            }
            else{
                itemView.tvMensaje.text = mensaje2.contenido
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/poifinal1.appspot.com/o/chats%2FIMG_20210518_012103032.jpg?alt=media&token=d9ecd5be-5102-4234-9d41-092b25b66174").resize(1, 1).into(itemView.msgView)

            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            val fecha = dateFormat.format(Date(mensaje2.timeStamp as Long))
            itemView.tvFecha.text = fecha

            val params = itemView.contenedorMensaje1.layoutParams
            if (mensaje2.esMio){

                val newParams = FrameLayout.LayoutParams(
                    params.width,
                    params.height,
                    Gravity.END
                )
                itemView.contenedorMensaje1.layoutParams = newParams
            } else {

                val newParams = FrameLayout.LayoutParams(
                    params.width,
                    params.height,
                    Gravity.LEFT
                )
                itemView.contenedorMensaje1.layoutParams = newParams
            }
        }
    }
    override fun getItemCount(): Int {
        return listaMensaje.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
       return ChatViewHolder(
           LayoutInflater.from(parent.context).inflate(R.layout.messages,parent,false)
       )
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.asignarInformacion(listaMensaje[position])

    }

}