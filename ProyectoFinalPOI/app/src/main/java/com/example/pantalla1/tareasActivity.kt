package com.example.pantalla1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.tareas.*

import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.pantalla1.adaptadores.DataPickerFragment
import com.example.pantalla1.adaptadores.DatePickerFragment2
import java.util.*

class tareasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.tareas)

        etDate.setOnClickListener {
            showDatePickerDialog()
        }

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