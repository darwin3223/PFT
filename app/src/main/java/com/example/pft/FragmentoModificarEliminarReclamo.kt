package com.example.pft

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.pft.models.ApiClient
import com.example.pft.models.EstadoSolicitud
import com.example.pft.models.Evento
import com.example.pft.models.Reclamo
import com.example.pft.models.ReclamoCompleto
import com.example.pft.models.Semestre
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentoModificarEliminarReclamo : Fragment() {
    lateinit var rootView: View
    private var eventoSeleccionado: Evento? = null
    private var semestreSeleccionado: Semestre? = null
    private var tipoSeleccionado: String? = null

    private val apiService = ApiClient.apiService

    var positionEvento: Int = -1

    var positionSemestre: Int = -1

    var positionTipo: Int = -1

    private lateinit var events: List<Evento>

    private var callDeleteReclamo: Call<Reclamo>? = null

    private var callUpdateReclamo: Call<Reclamo>? = null

    private var callEventos: Call<List<Evento>>? = null

//    private var linealFrameMostrarReclamos =
//        requireActivity().findViewById<LinearLayout>(R.id.linearLayoutVerReclamos)

//    private var botonVolver =
//        requireActivity().findViewById<ImageView>(R.id.imageArrowBackModificarEliminarModificarEliminar)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()
        return inflater.inflate(R.layout.fragment_modificar_eleminar_reclamo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity





        mainActivity.enableBackButton()
        modificarReclamos()
        eliminarReclamos()
        cargarSpinnerType()
        cargarSpinnerEvento()
        rellenarSpinnerSemester()

        val editTextTitle: EditText = view.findViewById(R.id.editTextTextModificarEliminarTitulo)
        editTextTitle.text =
            Editable.Factory.getInstance().newEditable(mainActivity.reclamoSeleccionado?.titulo)

        val editTextDetails: EditText = view.findViewById(R.id.textLineModificarEliminarDetalle)
        editTextDetails.text =
            Editable.Factory.getInstance().newEditable(mainActivity.reclamoSeleccionado?.detalle)

        setearSpinnerEvento()
        setearSpinnerType()

//        var isHandlingBackPressed = false

        // Configurar el botón de retroceso del dispositivo utilizando viewLifecycleOwner
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (!isHandlingBackPressed) {
//                    isHandlingBackPressed = true
//
//                    var fragment = requireView().findViewById<ConstraintLayout>(R.id.fragmentModificarEliminar)
//                    fragment.visibility = View.INVISIBLE
//
//                    val fragmentVerReclamos = FragmentVerReclamos()
//                    val fragmentManager = requireActivity().supportFragmentManager
//                    val transaction = fragmentManager.beginTransaction()
//                    transaction.replace(R.id.fragmentModificarEliminar, fragmentVerReclamos)
//                    transaction.addToBackStack(null).commit()
//                    fragmentManager.executePendingTransactions()
//
//                    isHandlingBackPressed = false
//                }
//            }
//        }

        // Agregar el callback al dispatcher
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
//
//        // Habilitar o deshabilitar el botón de retroceso del dispositivo según sea necesario
//        callback.isEnabled = true
//        volver()

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                findNavController().navigate(R.id.action_fragmentoModificarEliminarReclamo2_to_fragmentVerReclamos3)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        volver()
    }

    fun volver() {
        var botonVolver =
            requireActivity().findViewById<ImageView>(R.id.imageArrowBackModificarEliminarModificarEliminar)
        botonVolver.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentoModificarEliminarReclamo2_to_fragmentVerReclamos3)
        }
    }

    fun modificarReclamos(){
        val buttonModificarReclamo = requireView().findViewById<Button>(R.id.buttonModificar)
        buttonModificarReclamo.setOnClickListener {
            mostrarFrameModificarReclamo()
        }
    }
    fun setearSpinnerEvento(){
        val mainActivity = activity as MainActivity
        callEventos = apiService.getAllEventos("Bearer "+(activity as MainActivity).tokenJWT)

        callEventos?.enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                println(response)
                if (response.isSuccessful) {
                    events = response.body() ?: emptyList()
                    val spinnerEvento: Spinner? = view?.findViewById(R.id.spinnerModificarEliminarEvento)

                    var eventoSeleccionado: Evento = mainActivity.reclamoSeleccionado.evento
                    positionEvento = events.indexOf(eventoSeleccionado)
                    spinnerEvento?.setSelection(positionEvento+1)
                    setearSpinnerSemestre(eventoSeleccionado.listaSemestres)
                } else {
                    println("Error trayendo los eventos ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    fun setearSpinnerSemestre(lista: List<Semestre>){
        val mainActivity = activity as MainActivity

        semestreSeleccionado = mainActivity.reclamoSeleccionado.semestre

        val spinnerSemestre: Spinner? = view?.findViewById(R.id.spinnerModificarEliminarSemestre)

        positionSemestre = lista.indexOf(semestreSeleccionado)
        spinnerSemestre?.setSelection(positionSemestre+1)

    }

    fun setearSpinnerType(){
        val listaDeTipos = listOf("Seleccione un tipo","EVENTO_VME", "ACTIVIDAD_APE")

        val mainActivity = activity as MainActivity
        tipoSeleccionado = mainActivity.reclamoSeleccionado.tipoReclamo
        val spinnerTipo: Spinner? = view?.findViewById(R.id.spinnerModificarEliminarTipo)
        positionTipo = listaDeTipos.indexOf(tipoSeleccionado)
        spinnerTipo?.setSelection(positionTipo)
    }

    fun eliminarReclamos(){
        val buttonEliminarReclamoReclamo = requireView().findViewById<Button>(R.id.buttonEliminarReclamo)
        buttonEliminarReclamoReclamo.setOnClickListener {
            mostrarFrameEliminarReclamo()
        }
    }

    fun mostrarFrameModificarReclamo() {
        val miniFrameModificarReclamo =
            requireActivity().findViewById<ConstraintLayout>(R.id.miniFrameModificarReclamo)
        miniFrameModificarReclamo.visibility = View.VISIBLE

        val buttonAceptarMiniFrameModificarReclamo =
            requireActivity().findViewById<Button>(R.id.buttonAceptarMiniFrameModificarReclamo)
        val buttonCancelarMiniFrameCancelarReclamo =
            requireActivity().findViewById<Button>(R.id.buttonCancelarMiniFrameModificarReclamo)

        buttonAceptarMiniFrameModificarReclamo.setOnClickListener {
            modificarReclamo()
        }
        buttonCancelarMiniFrameCancelarReclamo.setOnClickListener {
            miniFrameModificarReclamo.visibility = View.GONE
        }
    }
    fun mostrarFrameEliminarReclamo() {
        val miniFrameEliminarReclamo =
            requireActivity().findViewById<ConstraintLayout>(R.id.miniFrameEliminarReclamo)
        miniFrameEliminarReclamo.visibility = View.VISIBLE

        val buttonAceptarMiniFrameEliminarReclamo =
            requireActivity().findViewById<Button>(R.id.buttonAceptarMiniFrameEliminarReclamo)
        val buttonCancelarMiniFrameCancelarReclamo =
            requireActivity().findViewById<Button>(R.id.buttonCancelarMiniFrameEliminarReclamo)

        buttonAceptarMiniFrameEliminarReclamo.setOnClickListener {
         deleteReclamo()
        }
        buttonCancelarMiniFrameCancelarReclamo.setOnClickListener {
            miniFrameEliminarReclamo.visibility = View.GONE
        }
    }

    private fun modificarReclamo(){
//        val buttonModificarReclamo = requireView().findViewById<Button>(R.id.buttonModificar)
//        buttonModificarReclamo.setOnClickListener {
            val mainActivity = activity as MainActivity
            var student = mainActivity.usuarioLogueado?.idUsuario?.toLong()

            val editTextTitle =
                requireView().findViewById<EditText>(R.id.editTextTextModificarEliminarTitulo)
            var textTitle: String? = editTextTitle.text.toString()

            val editTextDetail =
                requireView().findViewById<EditText>(R.id.textLineModificarEliminarDetalle)
            var textDetail: String? = editTextDetail.text.toString()

            val reclamo = Reclamo(
                mainActivity.reclamoSeleccionado?.idReclamo,textTitle,tipoSeleccionado,textDetail,
                semestreSeleccionado?.idSemestre,
                mainActivity.reclamoSeleccionado.estado?.idEstado,student, eventoSeleccionado?.idEvento
            )

            callUpdateReclamo = apiService.updateReclamo("Bearer "+(activity as MainActivity).tokenJWT,reclamo)
            callUpdateReclamo?.enqueue(object : Callback<Reclamo> {

                override fun onResponse(call: Call<Reclamo>, response: Response<Reclamo>) {
                    println(response)
                    if (response.isSuccessful) {
                        val mensaje = "Reclamo modificado exitosamente"
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
//        }


    }

    private fun deleteReclamo(){
//        val buttonModificarReclamo = requireView().findViewById<Button>(R.id.buttonEliminarReclamo)
//
//        buttonModificarReclamo.setOnClickListener {
            val mainActivity = activity as MainActivity

            callDeleteReclamo = apiService.deleteReclamo(mainActivity.reclamoSeleccionado?.idReclamo,"Bearer "+(activity as MainActivity).tokenJWT)
            callDeleteReclamo?.enqueue(object : Callback<Reclamo> {
                override fun onResponse(call: Call<Reclamo>, response: Response<Reclamo>) {
                    if (response.code() == 200) {
                        val mensaje = "Baja logica exitosa"
                        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                    }else{
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(requireContext(), errorBody, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Reclamo>, t: Throwable) {
                    val mensaje = "Error al realizar la baja logica"
                    Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                }

            })
//        }
    }

    private fun rellenarSpinnerEvento(lista: List<Evento>) {
        val labelSemester: TextView = requireView().findViewById(R.id.textViewModificarEliminarSemestre)
        val spinnerSemester: Spinner = requireView().findViewById(R.id.spinnerModificarEliminarSemestre)
        val listaConNull = listOf(null) + lista

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConNull.map {
                it?.let { FragmentCrearReclamo.EventoSpinnerItem(it.titulo, it) } ?: "Seleccione un evento"
            })
        val spinnerEvento: Spinner = requireView().findViewById(R.id.spinnerModificarEliminarEvento)
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
                if (selectedItem is FragmentCrearReclamo.EventoSpinnerItem) {
                    // Hacer algo con el objeto completo del evento seleccionado
                    eventoSeleccionado = selectedItem.evento
//                    spinnerSemester.visibility = View.VISIBLE
//                    labelSemester.visibility = View.VISIBLE
//                    rellenarSpinnerSemester()

                } else {
                    // El elemento seleccionado es nulo o "Seleccione un evento," manejarlo según sea necesario
                    eventoSeleccionado = null
//                    spinnerSemester.visibility = View.INVISIBLE
//                    labelSemester.visibility = View.INVISIBLE
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
            activity?.findViewById<Spinner>(R.id.spinnerModificarEliminarTipo)

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
        val mainActivity = activity as MainActivity
        val listaConNull = listOf(null) + mainActivity.reclamoSeleccionado.evento.listaSemestres

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listaConNull.map {
                it?.let { FragmentCrearReclamo.SemestreSpinnerItem(it.nombre, it) } ?: "Seleccione un Semestre"
            })
        val spinnerSemester: Spinner = requireView().findViewById(R.id.spinnerModificarEliminarSemestre)
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
                if (selectedItem is FragmentCrearReclamo.SemestreSpinnerItem) {
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

}


