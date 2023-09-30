package com.example.pft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView


class FragmentCrearReclamo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_crear_reclamo, container, false)



        return view


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volver()
    }
  fun volver(){
      val botonVolver = requireActivity().findViewById<ImageView>(R.id.imageArrowBackCrearReclamo)
      botonVolver.setOnClickListener {
          // Crear un Intent para volver al menú principal o la actividad deseada
          requireActivity().onBackPressed()

      }

  }
}