package com.example.pft

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: Button
    private lateinit var textViewOlvidasteContrasenia: TextView
    private lateinit var cardViewLogin: CardView
    private lateinit var imageViewLogoUtec: ImageView
    private lateinit var plainTextRegistroNombre: EditText
    private lateinit var plainTextRegistroContrasenia: EditText

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonlogin()
//        olividasteLaConstrasenia()

    }

    fun ocultarLogin() {
        imageViewLogoUtec = findViewById<ImageView>(R.id.imageViewUtec)
        cardViewLogin = findViewById<CardView>(R.id.cardViewLogin)
        cardViewLogin.visibility = View.GONE
        imageViewLogoUtec.visibility = View.GONE
    }

    fun buttonlogin() {
        buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {


            datosLogin()

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
    }

    @SuppressLint("WrongViewCast")
    fun olividasteLaConstrasenia() {
        textViewOlvidasteContrasenia = findViewById<TextView>(R.id.textViewOlvidasteContrasenia)
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

    fun datosLogin() {
        plainTextRegistroNombre = findViewById<EditText>(R.id.plainTextRegistroNombre)
        plainTextRegistroContrasenia = findViewById<EditText>(R.id.plainTextContrasenia)

        var nombre = plainTextRegistroNombre.text.toString()
        var password = plainTextRegistroContrasenia.text.toString()

        println(nombre + password)

    }
}