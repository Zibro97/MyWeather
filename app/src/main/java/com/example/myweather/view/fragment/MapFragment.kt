package com.example.myweather.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myweather.BuildConfig
import com.example.myweather.R
import com.example.myweather.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.lang.AssertionError
import java.net.MalformedURLException
import java.net.URL
import java.util.*

//날씨를 지도로 표현해 보여주는 Fragment
class MapFragment : Fragment(),OnMapReadyCallback {

    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView : MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_map,container,false)
        mapView = rootView.findViewById(R.id.mapFragment) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        val point = LatLng( 37.414655, 126.879974)
        map.addMarker(MarkerOptions().position(point).title("여기"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,11f))

        val tileProvider = object : UrlTileProvider(256,256){
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
                val s = String.format(Locale.KOREA,"http://tile.openweathermap.org/map/precipitation/%d/%d/%d.png?appid={${BuildConfig.OPEN_WEATHER_API_KEY}}",zoom,x,y)

                if(!checkTileExists(x,y,zoom)) return null
                try {
                    return URL(s)
                }catch (e:MalformedURLException){
                    throw AssertionError(e)
                }
            }
            private fun checkTileExists(x:Int,y:Int,zoom:Int):Boolean{
                val minZoom = 12
                val maxZoom = 16

                return !(zoom<minZoom || zoom>maxZoom)
            }
        }
        map.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}