package com.example.pft

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.pft.models.ApiClient
import com.example.pft.models.EstadoSolicitud
import com.example.pft.models.Evento
import com.example.pft.models.Reclamo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentVerReclamos : Fragment() {
    var reclamoSeleccionado : Reclamo? = null
    var callReclamos: Call<List<Reclamo>>? = null
    val apiService = ApiClient.apiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()
        cargarReclamos()
        return inflater.inflate(R.layout.fragment_ver_reclamos, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volver()
        cargarReclamos()
    }

    fun volver(){
        val botonVolver = requireActivity().findViewById<ImageView>(R.id.imageArrowBackVerReclamo)
        botonVolver.setOnClickListener {
            requireActivity().onBackPressed()

        }

    }

    private fun cargarReclamos(){
        callReclamos = apiService.getAllReclamos("Bearer "+(activity as MainActivity).tokenJWT)

        callReclamos?.enqueue(object : Callback<List<Reclamo>> {
            override fun onResponse(call: Call<List<Reclamo>>, response: Response<List<Reclamo>>) {
                println(response)
                if (response.isSuccessful) {
                    val reclamos: List<Reclamo> = response.body() ?: emptyList()
                    rellenarListaReclamos(reclamos)
                } else {
                    println("Error trayendo los reclamos ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Reclamo>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun rellenarListaReclamos(lista: List<Reclamo>){
        val linealFrameMostrarReclamos =
            requireActivity().findViewById<LinearLayout>(R.id.linearLayoutVerReclamos)
        val mainActivity = activity as MainActivity
        val listViewReclamos: ListView = requireView().findViewById(R.id.listViewReclamos)
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,lista.map { it.titulo })

        listViewReclamos.adapter = adapter

        listViewReclamos.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = lista[position]
            mainActivity.reclamoSeleccionado = selectedItem
            reclamoSeleccionado = selectedItem

            cargarVistaModificarElimianarReclamo()
            linealFrameMostrarReclamos.visibility = View.GONE
        }
    }
    fun cargarVistaModificarElimianarReclamo() {

        val fragmentoModificarEliminarReclamo = FragmentoModificarEliminarReclamo()
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentoVerReclamos, fragmentoModificarEliminarReclamo)
        transaction.addToBackStack(null).commit()

    }


}


