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
import androidx.navigation.fragment.findNavController
import com.example.pft.models.ApiClient
import com.example.pft.models.EstadoSolicitud
import com.example.pft.models.Evento
import com.example.pft.models.Reclamo
import com.example.pft.models.ReclamoCompleto
import com.exception.main
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentVerReclamos : Fragment() {
    private lateinit var rootView: View
    var reclamoSeleccionado : ReclamoCompleto? = null
    var callReclamos: Call<List<ReclamoCompleto>>? = null
    val apiService = ApiClient.apiService

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
        volver()
        cargarReclamos()


    }

    fun volver() {
        val mainActivity = activity as MainActivity
        val botonVolver = rootView.findViewById<ImageView>(R.id.imageArrowBackVerReclamo)
        botonVolver.setOnClickListener {

            if (mainActivity.usuarioLogueado?.tipoUsuario == "ESTUDIANTE"){
                findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentMenuEstudiante2)
            }

        }

    }

    fun cargarReclamos(){
        callReclamos = apiService.getAllReclamos("Bearer "+(activity as MainActivity).tokenJWT)

        callReclamos?.enqueue(object : Callback<List<ReclamoCompleto>> {
            override fun onResponse(call: Call<List<ReclamoCompleto>>, response: Response<List<ReclamoCompleto>>) {
                println(response)
                if (response.isSuccessful) {
                    val reclamos: List<ReclamoCompleto> = response.body() ?: emptyList()
                    rellenarListaReclamos(reclamos)
                } else {
                    println("Error trayendo los reclamos ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ReclamoCompleto>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun rellenarListaReclamos(lista: List<ReclamoCompleto>){

        val mainActivity = activity as MainActivity
        val listViewReclamos: ListView = rootView.findViewById(R.id.listViewReclamos)
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,lista.map { it.titulo })

        listViewReclamos.adapter = adapter

        listViewReclamos.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = lista[position]
            mainActivity.reclamoSeleccionado = selectedItem
            reclamoSeleccionado = selectedItem

            findNavController().navigate(R.id.action_fragmentVerReclamos3_to_fragmentoModificarEliminarReclamo2)

        }
    }


}


