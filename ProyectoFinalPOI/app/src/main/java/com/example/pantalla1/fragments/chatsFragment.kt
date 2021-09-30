package com.example.pantalla1.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pantalla1.Chat
import com.example.pantalla1.ParticipantesChat
import com.example.pantalla1.R
import com.example.pantalla1.adaptadores.ChatsDisplayAdaptador
import com.example.pantalla1.chatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chats.*


class chatsFragment : Fragment() {
    private var listaChats = mutableListOf<String>()
    private var nombresChat = mutableListOf<String>()
    private lateinit var chatDisplay: ChatsDisplayAdaptador
    private var wantCypher = true
    private lateinit var nombreUserPrincipal: String
    val UserRef = FirebaseDatabase.getInstance().getReference("Usuarios")
    val chatref = FirebaseDatabase.getInstance().getReference("chats")
    var Logrado = false
    private lateinit var nombreUsuario: String
    fun cambiarFragmento(fragmentoNuevo: Fragment, tag:String){
        val fragmentoAnterior= requireActivity().supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null) {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentoNuevo)
                    .commit()
        }
    }
    override fun onAttach(context: Context) {
        Log.d(Constraints.TAG,"onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        ChatReciever()

        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_chats,container,false)
        nombreUserPrincipal = activity?.intent?.getStringExtra("nombreUsuario").toString()
        var myBun = arguments
        val myRv = rootView.findViewById<RecyclerView>(R.id.rvChats)
        if (myBun != null) {
            wantCypher = myBun.getBoolean("myCyph")
        }


        chatDisplay = ChatsDisplayAdaptador(listaChats, requireContext(), nombreUserPrincipal, nombresChat, wantCypher)

        val btnBuscar = rootView.findViewById<FloatingActionButton>(R.id.btnNewChat)
        val mylayout= rootView.findViewById<LinearLayout>(R.id.NewMessageLayout)
        val btnSend = rootView.findViewById<Button>(R.id.btnMessageNew)
        val getText= rootView.findViewById<EditText>(R.id.messageToText)
        myRv.layoutManager = LinearLayoutManager(activity)
        myRv.adapter = chatDisplay


        btnBuscar.setOnClickListener{
            mylayout.visibility = VISIBLE
        }
        btnSend.setOnClickListener {

            val textillo = getText.text.toString()
            val parts = ParticipantesChat(nombreUserPrincipal, textillo)
            val newchat = Chat("", parts, true)
            CrearChat(newchat)
            mylayout.visibility= GONE
        }
       // ChatReciever()
        return rootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    private fun ChatReciever(){

        chatref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                listaChats.clear()
                nombresChat.clear()
                for(snap in snapshot.children){
                    val lebool = snap.child("tipo").getValue(Boolean::class.java)?:""
                    if(lebool==true) {
                        val part1 =
                            snap.child("participantes").child("p1").getValue(String::class.java)
                                .toString()
                        val part2 =
                            snap.child("participantes").child("p2").getValue(String::class.java)
                                .toString()
                        if (nombreUserPrincipal == part1) {
                            val yey = snap.child("id").getValue(String::class.java)
                            Log.d("lul", yey.toString())
                            nombresChat.add(part2)
                            listaChats.add(yey.toString())
                        }
                        if (nombreUserPrincipal == part2) {
                            val yey = snap.child("id").getValue(String::class.java)
                            Log.d("lul", yey.toString())
                            nombresChat.add(part1)
                            listaChats.add(yey.toString())
                        }
                    }
                    if(lebool==false){
                        for (snp in snap.child("participantes").children){
                            if(snp.getValue(String::class.java).toString()==nombreUserPrincipal){
                                val yey = snap.child("id").getValue(String::class.java)
                                val yay =snap.child("nombre").getValue(String::class.java).toString()
                                nombresChat.add(yay)
                                listaChats.add(yey.toString())
                            }
                        }

                    }

                }
                if(listaChats.size>0){
                    chatDisplay.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun CrearChat(chatsillo: Chat){
        val mensajeFirebase = chatref.push()
        chatsillo.id = mensajeFirebase.key ?: ""
        mensajeFirebase.setValue(chatsillo)
    }
}