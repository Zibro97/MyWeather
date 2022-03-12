package com.example.myweather.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myweather.R
import com.example.myweather.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

class MapFragment : Fragment(),OnMapReadyCallback {

    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView : MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_map,container,false)
        mapView = layout.findViewById(R.id.mapFragment) as MapView
        mapView.getMapAsync(this)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun setUpGoogleMap(){
        mapView = childFragmentManager.findFragmentById(binding.mapFragment.id) as MapView
        mapView.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {

    }
}