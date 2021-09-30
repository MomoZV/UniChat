package com.example.pantalla1.adaptadores

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.Post
import com.example.pantalla1.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.messages.view.*
import kotlinx.android.synthetic.main.posts.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PostAdaptador(private val listaMensaje: MutableList<Post>):
    RecyclerView.Adapter<PostAdaptador.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val strRef = FirebaseStorage.getInstance().getReference("chats")
        fun asignarInformacion(rolenPost: Post) {
            itemView.tvPostUser.text = rolenPost.de
            itemView.tvPostContent.text = rolenPost.contenido


            val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault())
            val fecha = dateFormat.format(Date(rolenPost.timeStamp as Long))
            itemView.tvPostDate.text = fecha
        }
    }

    override fun getItemCount(): Int {
        return listaMensaje.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.posts, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.asignarInformacion(listaMensaje[position])

    }
}