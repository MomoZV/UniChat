package com.example.pantalla1


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.pantalla1.fragments.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.fragment_chat1v1.*
import kotlinx.android.synthetic.main.messages.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MenuLatActivity : AppCompatActivity(){

    val UserRef = FirebaseDatabase.getInstance().getReference("Usuarios")
    var lebool = false
    private lateinit var nombreUsuario: String
    private lateinit var emailUsuario: String
    private lateinit var perfilUsuario: String
    private var Cifrado = true
    private val strRef = FirebaseStorage.getInstance().getReference("Usuarios")
    private lateinit var roleUser: String
    var currentUserID: String? = null
    val mAuth: FirebaseAuth? = null

    fun cambiarFragmento(fragmentoNuevo: Fragment, tag:String){
        val fragmentoAnterior= supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null) {
            var myBun = bundleOf("myCyph" to Cifrado)
            //myBun.putBoolean("myCypher", Cifrado)

            fragmentoNuevo.arguments = myBun
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.contenedor, fragmentoNuevo)
                    .commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //fragments

        super.onCreate(savedInstanceState)
        updateUserStatus("online")
      //  referenciaChat = FirebaseDatabase.getInstance().getReference("Usuarios")
        setContentView(R.layout.activity_main_2)
        nombreUsuario = intent.getStringExtra("nombreUsuario").toString() ?: "sin_nombre"
        emailUsuario = intent.getStringExtra("emailUsuario").toString() ?: "sin_nombre"
        perfilUsuario = intent.getStringExtra("perfilUsuario").toString() ?: "sin_nombre"
        roleUser = intent.getStringExtra("rolUsuario").toString() ?: "alumno"
        val stRef2 = strRef.child(perfilUsuario)
        val myNav=findViewById<NavigationView>(R.id.navN)
        val miDrawer= findViewById<DrawerLayout>(R.id.drawerN)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val menu = myNav.menu
        val itemMenu =  menu.findItem(R.id.nav_homework_maestro)




        stRef2.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it.toString()).resize(400, 400).centerCrop().into(fotoPerfil)
        }
        if(roleUser == "Maestro") {
            itemMenu.setVisible(true)
        }

        setSupportActionBar(toolbar)

        val header2: View = myNav.getHeaderView(0)
        val nameUser1 = header2.findViewById<TextView>(R.id.txtNameUser)
        nameUser1.text = nombreUsuario
        val emailUser1 = header2.findViewById<TextView>(R.id.txtemailuser)
        emailUser1.text = emailUsuario
        val imageView = ImageView(this)
        val myswitch = header2.findViewById<Switch>(R.id.switchCyph)
        myswitch.setOnCheckedChangeListener { buttonView, isChecked ->
            Cifrado = !Cifrado
            Log.d("lul", "entro al check")
            Log.d("lul", Cifrado.toString())
        }


        imageView.layoutParams = LinearLayout.LayoutParams(400, 400)
        imageView.x = 20F //setting margin from left
        imageView.y = 20F //setting margin from top


        val toggle = ActionBarDrawerToggle(this, miDrawer, toolbar, R.string.app_name, R.string.app_name)
        miDrawer.addDrawerListener(toggle)
        toggle.syncState()
        myNav.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_notification2 ->{
                    cambiarFragmento(NotificationsFragment(),"fragmento Notifications")
                }
                R.id.nav_homework_maestro ->{
                    cambiarFragmento(TareasFragment(),"fragmento Maestro Tareas")
                }
                R.id.nav_chats2 ->{
                    cambiarFragmento(chatsFragment(),"fragmento chats")
                }
                R.id.nav_calendario2 ->{
                    cambiarFragmento(CalendarioFragment(),"fragmento calendario")
                }
                R.id.nav_aulas2 ->{
                    cambiarFragmento(AulasFragment(),"fragmento aulas")
                }
                R.id.nav_homework_alumno ->{
                    cambiarFragmento(TareasAlumnosFragment(),"fragmento Alumno Tareas")
                }
                else -> {
                    TODO()
                }

            }
            miDrawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun updateUserCypher(){


    }

    /*private fun recibirMensajes(){

        referenciaChat.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                listaMensajes.clear()
                for (sn in snapshot.child("Mensajes").children) {
                    val mensaje: Mensaje2 = sn.getValue(Mensaje2::class.java) as Mensaje2
                    mensaje.esMio = mensaje.de == nombreUsuario
                    listaMensajes.add(mensaje)
                }
                if (listaMensajes.size > 0) {
                    adaptador.notifyDataSetChanged()
                    rvChat.smoothScrollToPosition(listaMensajes.size - 1)
                }
            }
        })
    }*/
   open fun updateUserStatus(state: String): Unit {
      val saveCurrentTime: String
      val saveCurrentDate: String
      val calendar: Calendar = Calendar.getInstance()
      val currentDate = SimpleDateFormat("MMM,dd,yyyy")
      saveCurrentDate = currentDate.format(calendar.getTime())
      val currentTime = SimpleDateFormat("hh:mm:a")
      saveCurrentTime = currentTime.format(calendar.getTime())
      val onlineState: HashMap<String, Any> = HashMap()
      onlineState["time"] = saveCurrentTime
      onlineState["date"] = saveCurrentDate
      onlineState["state"] = state
      //currentUserID = mAuth.getCurrentUser().getUid()
      //RootRef.child("Users").child(currentUserID).child("userState").updateChildren(onlineStateMap)
  }
    override fun onStop() {
        super.onStop()
        if (currentUserID != null) {
            updateUserStatus("offline")
        }
    }
}