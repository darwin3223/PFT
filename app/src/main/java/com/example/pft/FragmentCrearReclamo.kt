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
        (activity as MainActivity).enableBackButton()
        return inflater.inflate(R.layout.fragment_crear_reclamo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volver()
    }
  fun volver(){
      val botonVolver = requireActivity().findViewById<ImageView>(R.id.imageArrowBackCrearReclamo)
      botonVolver.setOnClickListener {
          requireActivity().onBackPressed()

      }

  }
}