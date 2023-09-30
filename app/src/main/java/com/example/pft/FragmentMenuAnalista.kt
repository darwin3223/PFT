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
import com.exception.MyException

class FragmentMenuAnalista : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu_analista, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crearReclamo()
        verReclamo()
        facebookUtec()
        instagramUtec()
        xUtec()
    }

    fun crearReclamo() {
        val buttonCrearReclamos = requireActivity().findViewById<Button>(R.id.buttonCrearReclamo)
        buttonCrearReclamos.setOnClickListener {
            val crearReclamo =
                requireActivity().findViewById<RelativeLayout>(R.id.fragmentCrearReclamo)// Reemplaza con el ID de tu diseño de inicio de sesión
            // Crear una instancia del fragmento que deseas mostrar
            val fragmentCrearReclamo = FragmentCrearReclamo()
            // Obtener el FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager
            // Comenzar una transacción de fragmento
            val transaction = fragmentManager.beginTransaction()
            // Reemplazar el contenido del contenedor con el fragmento
            transaction.replace(R.id.menuLogin, fragmentCrearReclamo)
            // Hacer commit para aplicar la transacción
            transaction.addToBackStack(null).commit()
        }
    }

    fun verReclamo() {
        val buttonVerReclamos = requireActivity().findViewById<Button>(R.id.buttonVerReclamo)
        buttonVerReclamos.setOnClickListener {
            val crearReclamo =
                requireActivity().findViewById<RelativeLayout>(R.id.fragmentoVerReclamo)// Reemplaza con el ID de tu diseño de inicio de sesión
            // Crear una instancia del fragmento que deseas mostrar
            val fragmentVerReclamo = FragmentVerReclamos()
            // Obtener el FragmentManager
            val fragmentManager = requireActivity().supportFragmentManager
            // Comenzar una transacción de fragmento
            val transaction = fragmentManager.beginTransaction()
            // Reemplazar el contenido del contenedor con el fragmento
            transaction.replace(R.id.menuLogin, fragmentVerReclamo)
            // Hacer commit para aplicar la transacción
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
}