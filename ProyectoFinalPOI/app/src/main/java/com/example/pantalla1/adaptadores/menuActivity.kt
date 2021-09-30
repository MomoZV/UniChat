package com.example.pantalla1.adaptadores

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pantalla1.R
import com.example.pantalla1.adaptadores.menuActivity.*
import com.example.pantalla1.fragments.AulasFragment
import com.example.pantalla1.fragments.CalendarioFragment
import com.example.pantalla1.fragments.NotificationsFragment
import com.example.pantalla1.fragments.TareasFragment
import com.example.pantalla1.fragments.chatsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*

class menuActivity : AppCompatActivity(){
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_notification2 ->Toast.makeText(applicationContext,
                    "Clicked notificaciones", Toast.LENGTH_SHORT).show()
                R.id.nav_aulas2 ->Toast.makeText(applicationContext,
                    "Clicked aulas", Toast.LENGTH_SHORT).show()
                R.id.nav_calendario2->Toast.makeText(applicationContext,
                    "Clicked calendario", Toast.LENGTH_SHORT).show()
                R.id.nav_chats2->Toast.makeText(applicationContext,
                    "Clicked chats", Toast.LENGTH_SHORT).show()
                R.id.nav_homework2->Toast.makeText(applicationContext,
                    "Clicked tareas", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    /*val aulasFragment = AulasFragment()
    val calendarioFragment = CalendarioFragment()
    val chatsFragment = chatsFragment()
    val notificationsFragment = NotificationsFragment()
    val tareasFragment = TareasFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_aulas ->{
                    setCurrentFragment(aulasFragment)
                    true
                }
                R.id.nav_chats ->{
                    setCurrentFragment(chatsFragment)
                    true
                }
                R.id.nav_calendario ->{
                    setCurrentFragment(calendarioFragment)
                    true
                }
                R.id.nav_notification ->{
                    setCurrentFragment(notificationsFragment)
                    true
                }
                R.id.nav_homework ->{
                    setCurrentFragment(tareasFragment)
                    true
                }

                else -> false
            }
        }
    }

    //Empiezan fragments
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.containerView, fragment)
            commit()
        }
    }
    // terminan fragments*/
}