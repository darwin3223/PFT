package com.example.pft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.example.pft.models.ApiClient
import com.example.pft.models.Evento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentCrearReclamo : Fragment() {
    lateinit var rootView: View

    val apiService = ApiClient.apiService

    val call: Call<List<Evento>> = apiService.getAllEventos("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJHRUREQUYiLCJzdWIiOiJkYW1pYW4udmllcmEiLCJpZFVzdWFyaW8iOjE5LCJ0aXBvVXN1YXJpbyI6IkVTVFVESUFOVEUiLCJpYXQiOjE2OTgzNTM0ODMsImV4cCI6MTY5ODM1NTI4MywianRpIjoiZWY4MzBjNWQtYzUwYy00N2JhLWFlZWUtYmQ4MjhmMGZiN2FjIn0.0nhzIzBBMspBKP6Nk1UIxIirtTwHqkK3sASKSvV-IJE")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()

        rootView= inflater.inflate(R.layout.fragment_crear_reclamo, container, false)

        return inflater.inflate(R.layout.fragment_crear_reclamo, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        volver()

        call.enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                println(response)
                if (response.isSuccessful) {
                    val events: List<Evento> = response.body() ?: emptyList()
                    prueba(events)
                } else {
                    println("Error trayendo los eventos ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                println(t.message)
            }
        })
    }
  fun volver(){
      val botonVolver = requireActivity().findViewById<ImageView>(R.id.imageArrowBackCrearReclamo)
      botonVolver.setOnClickListener {
          requireActivity().onBackPressed()

      }

  }

    fun prueba(lista: List<Evento>) {
        val listaConNull = listOf(null) + lista

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaConNull.map { it?.let { EventoSpinnerItem(it.titulo, it) } ?: "Seleccione un evento" })
        val spinnerEvento: Spinner = requireView().findViewById(R.id.spinnerCrearEventoEvento)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerEvento.adapter = adapter

        spinnerEvento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
                if (selectedItem is EventoSpinnerItem) {
                    val selectedEvento = selectedItem.evento
                    // Hacer algo con el objeto completo del evento seleccionado

                } else {
                    // El elemento seleccionado es nulo o "Seleccione un evento," manejarlo según sea necesario
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada
            }
        }
    }


}
data class EventoSpinnerItem(val nombre: String, val evento: Evento) {
    override fun toString(): String {
        return nombre
    }
}