package com.example.pft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pft.models.ApiClient
import com.example.pft.models.EstadoSolicitud
import com.example.pft.models.Evento
import com.example.pft.models.Reclamo
import com.example.pft.models.Semestre
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentCrearReclamo : Fragment() {
    lateinit var rootView: View
    private var eventoSeleccionado: Evento? = null
    private var semestreSeleccionado: Semestre? = null
    private var tipoSeleccionado: String? = null

    private val apiService = ApiClient.apiService

    private var callEventos: Call<List<Evento>>? = null

    private var callCreateReclamo: Call<Reclamo>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()
        rootView = inflater.inflate(R.layout.fragment_crear_reclamo, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volver()

        cargarSpinnerEvento()
        cargarSpinnerType()
        crearReclamo()
    }

    fun volver() {
        val botonVolver = requireActivity().findViewById<ImageView>(R.id.imageArrowBackCrearReclamo)
        botonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCrearReclamo2_to_fragmentMenuEstudiante2)

        }

    }

    private fun crearReclamo() {
        val buttonCrearReclamo = rootView.findViewById<Button>(R.id.buttonCrearReclamoReclamo)
        buttonCrearReclamo.setOnClickListener {
            val mainActivity = activity as MainActivity
            var student = mainActivity.usuarioLogueado?.idUsuario?.toLong()

            val editTextTitle =
                rootView.findViewById<EditText>(R.id.editTextCrearReclamoTitulo)
            var textTitle: String? = editTextTitle.text.toString()

            val editTextDetail =
                rootView.findViewById<EditText>(R.id.textLineCrearReclamoDetalle)
            var textDetail: String? = editTextDetail.text.toString()

            val reclamo = Reclamo(-1,textTitle,tipoSeleccionado,textDetail,
                semestreSeleccionado?.idSemestre,
                -1,student, eventoSeleccionado?.idEvento
            )
            callCreateReclamo = apiService.createReclamo("Bearer "+(activity as MainActivity).tokenJWT,reclamo)
            callCreateReclamo?.enqueue(object : Callback<Reclamo> {

                override fun onResponse(call: Call<Reclamo>, response: Response<Reclamo>) {
                    if (response.isSuccessful) {
                        val mensaje = "Reclamo creado exitosamente"
                        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                    }else if (response.code() == 400){
                        Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                        }
                    }

                override fun onFailure(call: Call<Reclamo>, t: Throwable) {
                    val mensaje = "Error!, al ingresar el reclamo."
                    Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun rellenarSpinnerEvento(lista: List<Evento>) {
        val labelSemester: TextView = rootView.findViewById(R.id.textViewReclamoSemestre)
        val spinnerSemester: Spinner = rootView.findViewById(R.id.spinnerCrearReclamoSemestre)
        val listaConNull = listOf(null) + lista

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConNull.map {
                it?.let { EventoSpinnerItem(it.titulo, it) } ?: "Seleccione un evento"
            })
        val spinnerEvento: Spinner = rootView.findViewById(R.id.spinnerCrearReclamoEvento)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEvento.adapter = adapter

        spinnerEvento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem is EventoSpinnerItem) {
                    // Hacer algo con el objeto completo del evento seleccionado
                    eventoSeleccionado = selectedItem.evento
                    spinnerSemester.visibility = View.VISIBLE
                    labelSemester.visibility = View.VISIBLE
                    rellenarSpinnerSemester()

                } else {
                    // El elemento seleccionado es nulo o "Seleccione un evento," manejarlo según sea necesario
                    eventoSeleccionado = null
                    spinnerSemester.visibility = View.INVISIBLE
                    labelSemester.visibility = View.INVISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerSemester.visibility = View.INVISIBLE
                labelSemester.visibility = View.INVISIBLE
            }
        }
    }

    private fun cargarSpinnerEvento(){
        callEventos = apiService.getAllEventos("Bearer "+(activity as MainActivity).tokenJWT)

        callEventos?.enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                println(response)
                if (response.isSuccessful) {
                    val events: List<Evento> = response.body() ?: emptyList()
                    rellenarSpinnerEvento(events)
                } else {
                    println("Error trayendo los eventos ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun cargarSpinnerType(){
        val listaDeTipos = listOf("Seleccione un tipo","EVENTO_VME", "ACTIVIDAD_APE")

        // Encuentra tu Spinner en el diseño XML
        val spinner =
            activity?.findViewById<Spinner>(R.id.spinnerCrearReclamoTipo)

        // Crea un ArrayAdapter utilizando la lista de tipos y un diseño de spinner predeterminado
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaDeTipos)

        // Establece el diseño que se usará cuando el Spinner esté desplegado
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Vincula el ArrayAdapter con el Spinner
        if (spinner != null) {
            spinner.adapter = adapter

        // Opcionalmente, puedes configurar un escuchador para manejar eventos de selección en el Spinner
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    // Maneja la selección aquí
                    val selectedItem = listaDeTipos[position]

                    if (selectedItem != "Seleccione un tipo"){
                        tipoSeleccionado = selectedItem
                    }else{
                        tipoSeleccionado = null
                    }
                    // Puedes usar 'selectedItem' para hacer algo con el tipo seleccionado
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    tipoSeleccionado = null
                }
            }
        }


    }

    private fun rellenarSpinnerSemester(){
        val listaConNull = listOf(null) + eventoSeleccionado!!.listaSemestres

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConNull.map {
                it?.let { SemestreSpinnerItem(it.nombre, it) } ?: "Seleccione un Semestre"
            })
        val spinnerSemester: Spinner = rootView.findViewById(R.id.spinnerCrearReclamoSemestre)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSemester.adapter = adapter

        spinnerSemester.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem is SemestreSpinnerItem) {
                    // Hacer algo con el objeto completo del semestre seleccionado
                    semestreSeleccionado = selectedItem.semestre

                } else {
                    // El elemento seleccionado es nulo o "Seleccione un evento," manejarlo según sea necesario
                    semestreSeleccionado = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                semestreSeleccionado = null
            }
        }
    }

    data class EventoSpinnerItem(val nombre: String, val evento: Evento) {
        override fun toString(): String {
            return nombre
        }
    }

    data class EstadoSpinnerItem(val nombre: String, val estado: EstadoSolicitud) {
        override fun toString(): String {
            return nombre
        }
    }

    data class SemestreSpinnerItem(val nombre: String, val semestre: Semestre){
        override fun toString(): String {
            return nombre
        }
    }
}