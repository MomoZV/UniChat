package com.example.pantalla1.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.pantalla1.R
import com.example.pantalla1.SpinnerActivity
import com.example.pantalla1.adaptadores.DatePickerFragment2
import kotlinx.android.synthetic.main.tareas.*
class TareasFragment : Fragment() {
    val TAG= "FRAG TAREAS"
    private lateinit var spinnerElementos: Spinner
    private val options = mutableListOf("Opcion 1", "Opcion 2")
    private lateinit var adaptador: ArrayAdapter<String>
    override fun onAttach(context: Context) {
        Log.d(TAG,"onAttach")
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_tareas,container,false)

        spinnerElementos = rootView.findViewById(R.id.spinnerElementos)
        val etDate = rootView.findViewById<EditText>(R.id.etDate)
        etDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Inicializamos el adaptador
        adaptador = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_list_item_1, options)
        spinnerElementos.adapter = adaptador

        return rootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*val et:EditText = view!!.findViewById(R.id.txtMateria)
        et.text= "Hola"*/
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment2.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 because January is zero
            val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
            etDate.setText(selectedDate)
        })
        newFragment.show(requireActivity().supportFragmentManager, "datePicker")
    }
}
/*
class TareasFragment : Fragment(R.layout.fragment_tareas) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = Intent(activity, SpinnerActivity::class.java)
        startActivity(intent)
    }
}*/