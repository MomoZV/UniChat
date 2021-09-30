package com.example.pantalla1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pantalla1.adaptadores.PostAdaptador
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.add_group.*
import kotlinx.android.synthetic.main.fragment_chat1v1.*
import kotlinx.android.synthetic.main.group_posts.*
import java.sql.Timestamp


class GroupActivity :AppCompatActivity(){
    lateinit var GroupRef:DatabaseReference
    lateinit var PostingReference:DatabaseReference
    lateinit var nombreUsuario: String
    var listaPosts =  mutableListOf<Post>()
    var myAdapter = PostAdaptador(listaPosts)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_posts)
        nombreUsuario = intent.getStringExtra("nombreUsuario").toString() ?: "sin_nombre"
        val instantreference = intent.getStringExtra("referencia").toString() ?: "sin_nombre"

        GroupRef = FirebaseDatabase.getInstance().getReference("Grupos").child(instantreference)
        PostingReference = GroupRef.child("posts")
        rvGroupPost.adapter=myAdapter
        btnPost.setOnClickListener {
            val sometext = txtPost.text.toString()
            val myPost = Post("", nombreUsuario, sometext, ServerValue.TIMESTAMP)
            enviarPost(myPost)
        }
        RecibirPosts()

    }
    private fun enviarPost(myPost: Post){
        val mensajeFirebase = PostingReference.push()
        myPost.id = mensajeFirebase.key ?: ""
        mensajeFirebase.setValue(myPost)
    }
    private fun RecibirPosts(){
        PostingReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                listaPosts.clear()

                for (snap in snapshot.children) {
                    val contenido = snap.child("id").getValue(String::class.java).toString()
                    val contenido2 = snap.child("de").getValue(String::class.java).toString()
                    val contenido3 = snap.child("contenido").getValue(String::class.java).toString()
                    val contenido4 = snap.child("timeStamp").getValue(Any::class.java)

                    val myPost= Post(contenido, contenido2, contenido3, contenido4)
                    listaPosts.add(myPost)
                }
                if (listaPosts.size > 0) {
                    myAdapter.notifyDataSetChanged()
                    rvGroupPost.smoothScrollToPosition(listaPosts.size - 1)
                }
            }
        })
    }
}