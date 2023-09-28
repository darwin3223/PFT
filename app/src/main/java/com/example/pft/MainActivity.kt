package com.example.pft

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var textViewOlvidasteContrasenia: TextView
    private lateinit var textViewNoTienesCuenta: TextView
    private lateinit var cardViewLogin: CardView
    private lateinit var imageViewLogoUtec: ImageView

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonLogin = findViewById<Button>(R.id.buttonLogin)
        textViewOlvidasteContrasenia = findViewById<TextView>(R.id.textViewOlvidasteContrasenia)
        textViewNoTienesCuenta = findViewById<TextView>(R.id.textViewNoTienesCuenta)

        //cuando presiono el click login sucede esto...
        buttonLogin.setOnClickListener {
            //metodo para ocultar los elementos del login
            ocultarLogin()
            val analistaMenu =
                findViewById<RelativeLayout>(R.id.menuAnalista)// Reemplaza con el ID de tu diseño de inicio de sesión
            // Crear una instancia del fragmento que deseas mostrar
            val fragmentMenuAnalista = FragmentMenuAnalista()
            // Obtener el FragmentManager
            val fragmentManager = supportFragmentManager
            // Comenzar una transacción de fragmento
            val transaction = fragmentManager.beginTransaction()
            // Reemplazar el contenido del contenedor con el fragmento
            transaction.replace(R.id.menuLogin, fragmentMenuAnalista)
            // Hacer commit para aplicar la transacción
            transaction.addToBackStack(null).commit()

        }
        textViewOlvidasteContrasenia.setOnClickListener {
            ocultarLogin()
            val analistaMenu =
                findViewById<RelativeLayout>(R.id.olvidasteContrasenia)// Reemplaza con el ID de tu diseño de inicio de sesión
            // Crear una instancia del fragmento que deseas mostrar
            val fragmentMenuOlvidasteContrasenia = FragmentOlvidasteContrasenia()
            // Obtener el FragmentManager
            val fragmentManager = supportFragmentManager
            // Comenzar una transacción de fragmento
            val transaction = fragmentManager.beginTransaction()
            // Reemplazar el contenido del contenedor con el fragmento
            transaction.replace(R.id.menuLogin, fragmentMenuOlvidasteContrasenia)
            // Hacer commit para aplicar la transacción
            transaction.addToBackStack(null).commit()
        }

    }

    fun ocultarLogin() {
        imageViewLogoUtec = findViewById<ImageView>(R.id.imageViewUtec)
        cardViewLogin = findViewById<CardView>(R.id.cardViewLogin)
        cardViewLogin.visibility = View.GONE
        imageViewLogoUtec.visibility = View.GONE
    }
}
