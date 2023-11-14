package com.example.pft

import ReadConfig
import com.example.pft.models.UserResponse
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.pft.models.ApiClient
import com.example.pft.models.Evento
import com.example.pft.models.Reclamo
import com.example.pft.models.ReclamoCompleto
import com.example.pft.models.UserLoginRequest
import com.example.pft.models.Usuario
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var editTextPassword: EditText
    private lateinit var imageViewEye: ImageView
    private lateinit var buttonLogin: Button
    private lateinit var textViewOlvidasteContrasenia: TextView
    private lateinit var cardViewLogin: CardView
    private lateinit var imageViewLogoUtec: ImageView
    private lateinit var plainTextRegistroNombre: EditText
    private lateinit var plainTextRegistroContrasenia: EditText
    var usuarioLogueado: Usuario? = null
    var tokenJWT: String? = ""
    lateinit var reclamoSeleccionado: ReclamoCompleto
    private var backButtonEnabled = false

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        editTextPassword = findViewById(R.id.plainTextContrasenia)
//        imageViewEye = findViewById(R.id.imageViewEyeOpen)
//        buttonlogin()
//        noTienesCuenta()
//        ocultarMensajeError()
//        olvidasteLaContrasenia()
    }
    fun togglePasswordVisibility(view: View) {
        if (editTextPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
            // Mostrar contraseña
            editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageViewEye.setImageResource(R.drawable.ic_eye_open)
        } else {
            // Ocultar contraseña
            editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imageViewEye.setImageResource(R.drawable.ic_eye_closed)
        }
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

    fun olvidasteLaContrasenia() {
        val olvidastelaContraseniaButton = findViewById<TextView>(R.id.textViewOlvidasteContrasenia)

        olvidastelaContraseniaButton.setOnClickListener {
            val readConfig = ReadConfig()
            val serverUrl = readConfig.getServerUrl()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$serverUrl/restore-password"))
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


    fun cargarMenuAnalista() {
        ocultarLogin()
        val fragmentMenuAnalista = FragmentMenuAnalista()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.menuLogin, fragmentMenuAnalista)
        transaction.addToBackStack(null).commit()

    }
    fun mostrarMensajeError(error: String?) {

        val mensajeError =
            findViewById<TextView>(R.id.textViewError)
        mensajeError.visibility = View.VISIBLE
        mensajeError.text = error

    }

   fun ocultarMensajeError() {
       val editTextNombreUsuario = findViewById<EditText>(R.id.plainTextRegistroNombre)
       val editTextContrasenia = findViewById<EditText>(R.id.plainTextContrasenia)
       editTextNombreUsuario.setOnClickListener {
           val mensajeError =
               findViewById<TextView>(R.id.textViewError)
           mensajeError.visibility = View.GONE
       }
       editTextContrasenia.setOnClickListener {
           val mensajeError =
               findViewById<TextView>(R.id.textViewError)
           mensajeError.visibility = View.GONE
       }


   }override fun onBackPressed() {
        if (backButtonEnabled) {
            super.onBackPressed() // Permite que el botón de retroceso funcione en la actividad principal.
        } else {
            // No hagas nada en este método para deshabilitar el botón de retroceso en la actividad principal.
        }
    }

    fun enableBackButton() {
        backButtonEnabled = true
    }

    fun disableBackButton() {
        backButtonEnabled = false
    }

    fun mostrarFragmento(fragment: FragmentVerReclamos) {
        // Obtenemos el FragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        // Iniciamos una transacción de fragmento
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Reemplazamos cualquier fragmento existente en el contenedor con el nuevo fragmento
        transaction.replace(R.id.fragmentModificarEliminar, fragment)

        // Añadimos la transacción a la pila para poder realizar transacciones hacia atrás
        transaction.addToBackStack(null)

        // Confirmamos la transacción
        transaction.commit()
    }

}



