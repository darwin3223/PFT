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

    var listaReclamos: MutableList<ReclamoCompleto> = mutableListOf()
    var usuarioLogueado: Usuario? = null
    var tokenJWT: String? = ""
    lateinit var reclamoSeleccionado: ReclamoCompleto
    private var backButtonEnabled = false

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

   override fun onBackPressed() {
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


}



