package com.example.pantalla1

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pantalla1.adaptadores.DatePickerFragment2
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

import kotlinx.android.synthetic.main.tareas.*

class SpinnerActivity: AppCompatActivity() {
    lateinit var option :Spinner
    lateinit var result : TextView
    private lateinit var nombreUsuario: String
    private val db = FirebaseDatabase.getInstance()
    //private val Pers = db.setPersistenceEnabled(true)
    private val chatRef = FirebaseDatabase.getInstance().getReference("tareas")
    private lateinit var thisChat: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        //nombreUsuario = intent.getStringExtra("nombreUsuario").toString() ?: "sin_nombre"
        nombreUsuario = "Erika"
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_tareas)
        etDate.setOnClickListener {
            showDatePickerDialog()
        }
        option = findViewById(R.id.spinnerElementos) as Spinner
        result = findViewById(R.id.txtMateria) as TextView
        result.text="Hola"
        val options = arrayOf("Opcion 1", "Opcion 2")
        option.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                result.text = "Selecciona una opción"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
             result.text = options.get(position)
            }
        }
        val nombreT = txtNombreTarea.text.toString()
        val instruccionesT = txtInstrucciones.text.toString()
        val materiaT = txtMateria.text.toString()
        btnPublicarTarea.setOnClickListener{
            val objetoTarea= tareateams(
                    "",
                    nombreT,
                    instruccionesT,
                    nombreUsuario,
                    materiaT,
                    ServerValue.TIMESTAMP
            )
           /* var id:String="",
            var nombretarea:String="",
            var instrucciones:String="",
            var de:String="",
            var materiatarea:String="",
            val timeStamp: Any?=null*/
            enviarTarea(objetoTarea)
        }
    }

    private fun enviarTarea(tareita: tareateams ){

        val mensajeFirebase = thisChat.child("Tareas").push()
        tareita.id = mensajeFirebase.key ?: ""
        mensajeFirebase.setValue(tareita)
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment2.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 because January is zero
            val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
            etDate.setText(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }
}