package com.example.pft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

class FragmentoModificarEliminarReclamo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()

        return inflater.inflate(R.layout.fragment_modificar_eleminar_reclamo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volver()
            }
    fun volver() {
        val botonVolver =
            requireActivity().findViewById<ImageView>(R.id.imageArrowBackModificarEliminarModificarEliminar)
        botonVolver.setOnClickListener {
            val linealFrameMostrarReclamos =
                requireActivity().findViewById<LinearLayout>(R.id.linearLayoutVerReclamos)
            linealFrameMostrarReclamos.visibility = View.VISIBLE

            requireActivity().onBackPressed()

        }

    }
        }
