package com.example.pft

import ReadConfig
import com.example.pft.models.UserResponse
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import com.example.pft.models.ApiClient
import com.example.pft.models.UserLoginRequest
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

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
        noTienesCuenta()
        volver()
        ocultarMensajeError()
//        olividasteLaConstrasenia()

    }

    fun noTienesCuenta() {
        val noTienesCuentaButton = findViewById<TextView>(R.id.textViewNoTienesCuenta)

        noTienesCuentaButton.setOnClickListener {
            val readConfig = ReadConfig()
            val serverUrl = readConfig.getServerUrl()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$serverUrl/registro"))
            intent.resolveActivity(packageManager)
            startActivity(intent)
        }
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
            sendLoginRequest()

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

    private fun sendLoginRequest() {
        // text fields
        val editTextNombreUsuario = findViewById<EditText>(R.id.plainTextRegistroNombre)
        val editTextContrasenia = findViewById<EditText>(R.id.plainTextContrasenia)

        val nombreUsuario = editTextNombreUsuario.text.toString()
        val contrasenia = editTextContrasenia.text.toString()

        println("Nombre usuario: $nombreUsuario, contrasenia: $contrasenia")

        val apiService = ApiClient.apiService

        val userLogin = UserLoginRequest(username = nombreUsuario, password = contrasenia)
        val call = apiService.loginUsuario(userLogin)

        call.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    when (response.body()?.usuario?.tipoUsuario) {
                        "ANALISTA" -> cargarMenuAnalista()
                        "ESTUDIANTE" -> cargarMenuEstudiante()
                    }

                } else {
                    when (response.code()) {
                        404 -> mostrarMensajeError("El usuario no existe")
                        401 -> mostrarMensajeError("La contraseña es incorrecta")
                        else -> mostrarMensajeError("Error en el servidor intentelo mas tarde")
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<UserResponse>, t: Throwable) {
                println(t.message)
            }
        })

    }

    fun cargarMenuEstudiante() {
        ocultarLogin()
        val fragmentMenuEstudiante = FragmentMenuEstudiante()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menuLogin, fragmentMenuEstudiante)
        transaction.addToBackStack(null).commit()
    }

    fun cargarMenuAnalista() {
        ocultarLogin()
        val fragmentMenuAnalista = FragmentMenuAnalista()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menuLogin, fragmentMenuAnalista)
        transaction.addToBackStack(null).commit()
    }

    fun volver() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                println("no vas a poder volver")

            }
        }
    }

    fun mostrarMensajeError(error: String) {

        val mensajeError =
            findViewById<TextView>(R.id.textViewError)
        mensajeError.visibility = View.VISIBLE
        mensajeError.text = error

    }

   fun ocultarMensajeError(){
       val editTextNombreUsuario = findViewById<EditText>(R.id.plainTextRegistroNombre)
       val editTextContrasenia = findViewById<EditText>(R.id.plainTextContrasenia)
       editTextNombreUsuario.setOnClickListener{
           val mensajeError =
               findViewById<TextView>(R.id.textViewError)
           mensajeError.visibility = View.GONE
       }
       editTextContrasenia.setOnClickListener{
           val mensajeError =
               findViewById<TextView>(R.id.textViewError)
           mensajeError.visibility = View.GONE
       }


    }

}