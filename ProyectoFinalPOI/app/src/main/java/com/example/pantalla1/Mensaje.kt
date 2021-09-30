package com.example.pantalla1

import android.media.Image
import com.google.firebase.database.Exclude

class Mensaje2 (
    var id:String="",
    var contenido:String="",
    var de:String="",
    val timeStamp: Any?=null,
    val cifrado: Boolean=true

){
    @Exclude
    var esMio:Boolean = false
}

class tareateams (
        var id:String="",
        var nombretarea:String="",
        var instrucciones:String="",
        var de:String="",
        var materiatarea:String="",
        val timeStamp: Any?=null

){
    @Exclude
    var esMio:Boolean = false
}


class Post(
    var id:String,
    var de:String,
    var contenido: String,
    var timeStamp: Any?=null

)




class ParticipantesChat(
        val p1: String,
        val p2: String
)

class Tarea(
    val titulo: String,
    val puntos: Int

)
class Usuario(
    val username: String,
    val contraseña:String,
    val email:String,
    var perfil:String="",
    var role:String=""
)
class Chat(
    var id:String,
    var Participantes: ParticipantesChat,
    var tipo: Boolean
)
class ChatFromGroup(
    var id:String,
    var nombre:String,
    var Participantes: MutableList<String>,
    var tipo: Boolean
)
class Grupo(
    var id:String,
    var nombre: String,
    var Participantes: MutableList<String>
    //var Icono: AdaptiveIconDrawable
)
//class Carrera(
//    val nombre:String,
//    val materias: List<Materia>
//)