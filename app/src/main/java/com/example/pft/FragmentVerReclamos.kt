package com.example.pft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.example.pft.models.ApiClient
import com.example.pft.models.ReclamoCompleto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentVerReclamos : Fragment() {
    private lateinit var rootView: View
    var reclamoSeleccionado : ReclamoCompleto? = null
    var callReclamos: Call<List<ReclamoCompleto>>? = null
    val apiService = ApiClient.apiService
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()
        rootView = inflater.inflate(R.layout.fragment_ver_reclamos, container, false)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        volver()
        filtros()
        cargarReclamos()
        rellenarListaReclamos()


    }

    private fun rellenarListaReclamos() {
        val lista = mainActivity.listaReclamos
        val listViewReclamos: ListView = rootView.findViewById(R.id.listViewReclamos)

        val reclamosFiltrados = when (mainActivity.usuarioLogueado?.tipoUsuario) {
            "ESTUDIANTE" -> lista.filter { it.estudiante?.idUsuario == mainActivity.usuarioLogueado!!.idUsuario }
            else -> lista
        }

        if (reclamosFiltrados.isEmpty()) {
            val emptyListMessage = arrayOf("No se encontraron Reclamos")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, emptyListMessage)
            listViewReclamos.adapter = adapter
        } else {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, reclamosFiltrados.map
            { it.titulo+" - "+it.estudiante?.nombreUsuario })
            listViewReclamos.adapter = adapter

            if (mainActivity.usuarioLogueado?.tipoUsuario == "ESTUDIANTE"){
                listViewReclamos.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem = reclamosFiltrados[position]
                    mainActivity.reclamoSeleccionado = selectedItem
                    reclamoSeleccionado = selectedItem

                    findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentoModificarEliminarReclamo2)
                }
            }
        }
    }

    private fun cargarReclamos() {
        callReclamos = apiService.getAllReclamos("Bearer "+(activity as MainActivity).tokenJWT)

        callReclamos?.enqueue(object : Callback<List<ReclamoCompleto>> {
            override fun onResponse(call: Call<List<ReclamoCompleto>>, response: Response<List<ReclamoCompleto>>) {
                if (response.isSuccessful) {
                    val reclamos: List<ReclamoCompleto> = response.body() ?: emptyList()

                    mainActivity.listaReclamos = reclamos.toMutableList()
                } else {
                    println("Error trayendo los reclamos ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ReclamoCompleto>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun filtros() {
        val btnFiltros = rootView.findViewById<Button>(R.id.buttonFiltros)
        btnFiltros.setOnClickListener(){
            findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentFiltrar)
        }
    }

    fun volver() {
        val botonVolver = rootView.findViewById<ImageView>(R.id.imageArrowBackVerReclamos)
        botonVolver.setOnClickListener {

            if (mainActivity.usuarioLogueado?.tipoUsuario == "ESTUDIANTE"){
                findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentMenuEstudiante2)
            }else if (mainActivity.usuarioLogueado?.tipoUsuario == "ANALISTA"){
                findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentMenuAnalista)
            }else if (mainActivity.usuarioLogueado?.tipoUsuario == "TUTOR"){
                findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentMenuTutor)

        }

    }
    }
}


