package com.example.pft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.example.pft.models.ApiClient
import com.example.pft.models.ApiService
import com.example.pft.models.Evento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentCrearReclamo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).enableBackButton()

        val rootView = inflater.inflate(R.layout.fragment_crear_reclamo, container, false)
        val spinnerEvento: Spinner = rootView.findViewById(R.id.spinnerEvento);
        val apiService = ApiClient.apiService

        val call: Call<List<Evento>> = apiService.getAllEventos("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJHRUREQUYiLCJzdWIiOiJmcmFuY28uYm9yZ2lhbmkiLCJpZFVzdWFyaW8iOjIwLCJ0aXBvVXN1YXJpbyI6IlRVVE9SIiwiaWF0IjoxNjk4MzQ3NjQyLCJleHAiOjE2OTgzNDk0NDIsImp0aSI6IjIxYWM5ZDQ3LTMxMmEtNGM3Mi04YjQ2LTk5NDRkMDk1M2JiMiJ9.J_AiHHxXwhH6uOcSNrCHszQbr2oX35aTHAgAo52ZNcE")

        call.enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                println(response)
                if (response.isSuccessful) {
                    val events: List<Evento> = response.body() ?: emptyList()

                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        events.map { it.titulo })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    println("adapter $adapter")
                    println(events.map { it.titulo })
                    println("this is the response body ${response.body()}")

                    // Set the adapter for the Spinner
                    spinnerEvento.adapter = adapter

                    adapter.notifyDataSetChanged()
                } else {
                    println("Error trayendo los eventos ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                println(t.message)
            }
        })

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