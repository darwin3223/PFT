package com.example.pft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class FragmentVerReclamos : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_reclamos, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        volver()
    }

    fun volver(){
        val botonVolver = requireActivity().findViewById<ImageView>(R.id.imageArrowBackVerReclamo)
        botonVolver.setOnClickListener {
            // Crear un Intent para volver al menú principal o la actividad deseada
            requireActivity().onBackPressed()

        }

    }
}
