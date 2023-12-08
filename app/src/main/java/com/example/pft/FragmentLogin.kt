package com.example.pft

import ReadConfig
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.pft.models.ApiClient
import com.example.pft.models.UserLoginRequest
import com.example.pft.models.UserResponse
import retrofit2.Call
import retrofit2.Response

class FragmentLogin : Fragment() {
    private lateinit var rootView: View
    private lateinit var editTextPassword: EditText
    private lateinit var imageViewEye: ImageView
    private lateinit var buttonLogin: Button
    private lateinit var textViewOlvidasteContrasenia: TextView
    private lateinit var cardViewLogin: CardView
    private lateinit var imageViewLogoUtec: ImageView
    private lateinit var plainTextRegistroNombre: EditText
    private lateinit var plainTextRegistroContrasenia: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        editTextPassword = rootView.findViewById(R.id.plainTextContrasenia)
        imageViewEye = rootView.findViewById(R.id.imageViewEyeOpen)
        togglePasswordVisibility()

        buttonlogin()
        noTienesCuenta()
        ocultarMensajeError()
        olvidasteLaContrasenia()

        rootView.isFocusableInTouchMode = true
        rootView.requestFocus()
        rootView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                //dejo esto vacio para que en la ventana del login no puedas utilizar el boton volver del celular
                return@setOnKeyListener true
            }
            false
        }

        return rootView
    }

    fun togglePasswordVisibility() {
        imageViewEye.setOnClickListener(){
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

    }
    fun noTienesCuenta() {
        val noTienesCuentaButton = rootView.findViewById<TextView>(R.id.textViewNoTienesCuenta)

        noTienesCuentaButton.setOnClickListener {
            val readConfig = ReadConfig()
            val serverUrl = readConfig.getServerUrl()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$serverUrl/registro"))
            intent.resolveActivity(requireActivity().packageManager)
            startActivity(intent)
        }
    }

    fun olvidasteLaContrasenia() {
        val olvidastelaContraseniaButton = rootView.findViewById<TextView>(R.id.textViewOlvidasteContrasenia)

        olvidastelaContraseniaButton.setOnClickListener {
            val readConfig = ReadConfig()
            val serverUrl = readConfig.getServerUrl()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$serverUrl/restore-password"))
            intent.resolveActivity(requireActivity().packageManager)
            startActivity(intent)
        }
    }

    fun buttonlogin() {
        buttonLogin = rootView.findViewById<Button>(R.id.buttonLogin)

        buttonLogin.setOnClickListener {

            // Mostrar ProgressBar al presionar el botón
            val progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
            buttonLogin.isEnabled = false
            buttonLogin.text = ""
            progressBar.visibility = View.VISIBLE

            sendLoginRequest {
                Handler().postDelayed({
                    buttonLogin.isEnabled = true
                    buttonLogin.text = "Ingresar"
                    progressBar.visibility = View.GONE
                }, 2000)
            }


        }
    }

    @SuppressLint("WrongViewCast")
    private fun sendLoginRequest(callback: () -> Unit) {
        // text fields
        val editTextNombreUsuario = requireActivity().findViewById<EditText>(R.id.plainTextRegistroNombre)
        val editTextContrasenia = requireActivity().findViewById<EditText>(R.id.plainTextContrasenia)

        val nombreUsuario = editTextNombreUsuario.text.toString()
        val contrasenia = editTextContrasenia.text.toString()

        println("Nombre usuario: $nombreUsuario, contrasenia: $contrasenia")

        val apiService = ApiClient.apiService

        val userLogin = UserLoginRequest(username = nombreUsuario, password = contrasenia)
        val call = apiService.loginUsuario(userLogin)

        call.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    var mainActivity = activity as MainActivity
                    mainActivity.tokenJWT = response.body()?.token
                    mainActivity.usuarioLogueado = response.body()?.usuario
                    when (response.body()?.usuario?.tipoUsuario) {
                        "ANALISTA" -> findNavController().navigate(R.id.action_fragmentLogin_to_fragmentMenuAnalista)
                        "ESTUDIANTE" -> findNavController().navigate(R.id.action_fragmentLogin_to_fragmentMenuEstudiante2)
                        "TUTOR" -> findNavController().navigate(R.id.action_fragmentLogin_to_fragmentMenuTutor)
                    }

                } else {
                    when (response.code()) {

                        404 -> mostrarMensajeError(response.errorBody()?.string())
                        401 -> mostrarMensajeError(response.errorBody()?.string())
                        else -> mostrarMensajeError("Error en el servidor intentelo mas tarde")
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                println(t.message)
            }
        })
        callback.invoke()
    }
    fun mostrarMensajeError(error: String?) {

        val mensajeError =
            rootView.findViewById<TextView>(R.id.textViewError)
        mensajeError.visibility = View.VISIBLE
        mensajeError.text = error

    }

    fun ocultarMensajeError() {
        val editTextNombreUsuario = rootView.findViewById<EditText>(R.id.plainTextRegistroNombre)
        val editTextContrasenia = rootView.findViewById<EditText>(R.id.plainTextContrasenia)
        editTextNombreUsuario.setOnClickListener {
            val mensajeError =
                rootView.findViewById<TextView>(R.id.textViewError)
            mensajeError.visibility = View.GONE
        }
        editTextContrasenia.setOnClickListener {
            val mensajeError =
                rootView.findViewById<TextView>(R.id.textViewError)
            mensajeError.visibility = View.GONE
        }

    }
}