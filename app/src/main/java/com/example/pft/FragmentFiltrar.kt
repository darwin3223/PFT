package com.example.pft

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pft.models.ApiClient
import com.example.pft.models.EstadoSolicitud
import com.example.pft.models.Estudiante
import com.example.pft.models.ReclamoCompleto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentFiltrar : Fragment() {
    lateinit var rootView: View
    lateinit var mainActivity: MainActivity

    private var idEstudianteSelecc: Long? = null
    private var idEstadoSelecc: Long? = null

    private val apiService = ApiClient.apiService

    private var callEstados: Call<List<EstadoSolicitud>>? = null

    private var callEstudiantes: Call<List<Estudiante>>? = null

    private var callReclamosByFilters: Call<List<ReclamoCompleto>?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_filtrar, container, false)
        mainActivity = activity as MainActivity
        cargarSpinnerEstado()
        cargarSpinnerEstudiante()

        var textViewEstudiante = rootView.findViewById<TextView>(R.id.textViewEstudiante)
        var spinnerEstudiante = rootView.findViewById<Spinner>(R.id.spinnerEstudiante)

        if (mainActivity.usuarioLogueado?.tipoUsuario == "ESTUDIANTE"){
            textViewEstudiante.visibility = View.GONE
            spinnerEstudiante.visibility = View.GONE
        }else{
            textViewEstudiante.visibility = View.VISIBLE
            spinnerEstudiante.visibility = View.VISIBLE
        }

        volver()
        filtrar()


        return rootView
    }

    fun volver() {
        val botonVolver = rootView.findViewById<ImageView>(R.id.imageArrowBackFiltrar)
        botonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentFiltrar_to_fragmentVerReclamos3)
        }
        rootView.isFocusableInTouchMode = true
        rootView.requestFocus()
        rootView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                findNavController().navigate(R.id.action_fragmentFiltrar_to_fragmentVerReclamos3)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

    }

    fun filtrar(){
        val btnFiltrar = rootView.findViewById<Button>(R.id.buttonFiltrar)
        btnFiltrar.setOnClickListener(){
            if (mainActivity.usuarioLogueado?.tipoUsuario == "ESTUDIANTE"){
                idEstudianteSelecc = mainActivity.usuarioLogueado?.idUsuario
            }
            callReclamosByFilters = apiService.getReclamosByFilters("Bearer "+(activity as MainActivity).tokenJWT,
                idEstudianteSelecc, idEstadoSelecc)
            callReclamosByFilters?.enqueue(object : Callback<List<ReclamoCompleto>?> {
                override fun onResponse(
                    call: Call<List<ReclamoCompleto>?>,
                    response: Response<List<ReclamoCompleto>?>
                ) {
                    if (response.isSuccessful){
                        mainActivity.listaReclamos = (response.body() ?: emptyList()).toMutableList()
                        findNavController().navigate(R.id.action_fragmentFiltrar_to_fragmentVerReclamos3)
                    }else{
                        Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<List<ReclamoCompleto>?>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error al filtrar.", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun cargarSpinnerEstado(){
        callEstados = apiService.getAllEstados("Bearer "+(activity as MainActivity).tokenJWT)

        callEstados?.enqueue(object : Callback<List<EstadoSolicitud>> {
            override fun onResponse(call: Call<List<EstadoSolicitud>>, response: Response<List<EstadoSolicitud>>) {
                if (response.isSuccessful) {
                    val estados: List<EstadoSolicitud> = response.body() ?: emptyList()
                    rellenarSpinnerEstado(estados)
                } else {
                    println("Error trayendo los estados ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<EstadoSolicitud>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun rellenarSpinnerEstado(lista: List<EstadoSolicitud>) {
        val listaConNull = listOf(null) + lista

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConNull.map {
                it?.let { EstadoSpinnerItem(it.nombre, it) } ?: "Seleccione un estado"
            })
        val spinnerEstado: Spinner = rootView.findViewById(R.id.spinnerEstado)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEstado.adapter = adapter

        spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem is EstadoSpinnerItem) {
                    idEstadoSelecc = selectedItem.estado.idEstado
                } else {
                    idEstadoSelecc = -1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                idEstadoSelecc = -1
            }
        }
    }

    private fun cargarSpinnerEstudiante(){
        callEstudiantes = apiService.getAllEstudiantes("Bearer "+(activity as MainActivity).tokenJWT)

        callEstudiantes?.enqueue(object : Callback<List<Estudiante>> {
            override fun onResponse(call: Call<List<Estudiante>>, response: Response<List<Estudiante>>) {
                if (response.isSuccessful) {
                    val estudiantes: List<Estudiante> = response.body() ?: emptyList()
                    rellenarSpinnerEstudiante(estudiantes)
                } else {
                    println("Error trayendo los estudiantes ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Estudiante>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun rellenarSpinnerEstudiante(lista: List<Estudiante>) {
        val listaConNull = listOf(null) + lista

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConNull.map {
                it?.let { EstudianteSpinnerItem(it.documento.toString() +" - "+ it.nombreUsuario, it) } ?: "Seleccione un estudiante"
            })
        val spinnerEstudiante: Spinner = rootView.findViewById(R.id.spinnerEstudiante)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEstudiante.adapter = adapter

        spinnerEstudiante.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem is EstudianteSpinnerItem) {
                    idEstudianteSelecc = selectedItem.estudiante.idUsuario
                } else {
                    idEstudianteSelecc = -1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                idEstudianteSelecc = -1
            }
        }
    }

    data class EstadoSpinnerItem(val nombre: String, val estado: EstadoSolicitud) {
        override fun toString(): String {
            return nombre
        }
    }

    data class EstudianteSpinnerItem(val nombre: String, val estudiante: Estudiante) {
        override fun toString(): String {
            return nombre
        }
    }

}