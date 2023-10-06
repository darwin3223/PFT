package com.example.pft


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.exception.MyException

class FragmentMenuEstudiante : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_estudiante, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crearReclamo()
        verReclamo()
        volver()
        facebookUtec()
        instagramUtec()
        xUtec()
        cerrarSesion()
    }

    fun crearReclamo() {
        val buttonCrearReclamos = requireActivity().findViewById<Button>(R.id.buttonCrearReclamo)
        buttonCrearReclamos.setOnClickListener {
            val fragmentCrearReclamo = FragmentCrearReclamo()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.menuLogin, fragmentCrearReclamo)
            transaction.addToBackStack(null).commit()
        }
    }

    fun verReclamo() {
        val buttonVerReclamos = requireActivity().findViewById<Button>(R.id.buttonVerReclamo)
        buttonVerReclamos.setOnClickListener {
            val fragmentVerReclamo = FragmentVerReclamos()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.menuLogin, fragmentVerReclamo)
            transaction.addToBackStack(null).commit()
        }
    }

    fun facebookUtec() {
        try {
            val facebookUtec = requireActivity().findViewById<ImageView>(R.id.imageViewFacebookUtec)

            facebookUtec.setOnClickListener {
                val urlRedSocial = "https://www.facebook.com/utecuy/?locale=es_LA"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlRedSocial))
                intent.resolveActivity(requireActivity().packageManager)
                startActivity(intent)
            }

        } catch (exception: MyException) {
            val mensaje = "No se encontró una aplicación para abrir la red social"
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    fun instagramUtec() {
        try {
            val instagramUtec =
                requireActivity().findViewById<ImageView>(R.id.imageViewIntagramUtec)

            instagramUtec.setOnClickListener {

                val urlRedSocial = "https://www.instagram.com/utecuy/?hl=es"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlRedSocial))
                intent.resolveActivity(requireActivity().packageManager)
                startActivity(intent)
            }

        } catch (exception: MyException) {
            val mensaje = "No se encontró una aplicación para abrir la red social"
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        }

    }

    fun xUtec() {
        try {
            val xUtec = requireActivity().findViewById<ImageView>(R.id.imageViewXUtec)

            xUtec.setOnClickListener {

                val urlRedSocial = "https://twitter.com/UTECuy"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlRedSocial))
                intent.resolveActivity(requireActivity().packageManager)
                startActivity(intent)
            }

        } catch (exception: MyException) {
            val mensaje = "No se encontró una aplicación para abrir la red social"
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    fun volver() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mostrarFrameCerrarSesion()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        callback.isEnabled =
            true
    }

    fun cerrarSesion() {
        val cerrarSesion = requireActivity().findViewById<ImageView>(R.id.cerrarSesionEstudiante)
        cerrarSesion.setOnClickListener {
            mostrarFrameCerrarSesion()
        }
    }

    fun mostrarFrameCerrarSesion() {
        val miniFrameCloseEstu =
            requireActivity().findViewById<ConstraintLayout>(R.id.miniFrameCloseAnali)
        miniFrameCloseEstu.visibility = View.VISIBLE

        val buttonAceptarMiniFrameEstu =
            requireActivity().findViewById<Button>(R.id.buttonAceptarMiniFrameAnali)
        val buttonCancelarMiniFrameEstu =
            requireActivity().findViewById<Button>(R.id.buttonCancelarMiniFrameAnali)

        buttonAceptarMiniFrameEstu.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        buttonCancelarMiniFrameEstu.setOnClickListener {
            miniFrameCloseEstu.visibility = View.GONE
        }
    }


}