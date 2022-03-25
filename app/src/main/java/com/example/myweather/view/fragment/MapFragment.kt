package com.example.myweather.view.fragment

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import android.graphics.Rect

import android.graphics.Color

import android.graphics.Paint

import android.graphics.Canvas

import android.graphics.BitmapFactory

import android.content.res.Resources
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myweather.util.MarkerInfoWindowAdapter


/***
 * 1. 기본 지도 띄우기 o
 * 2. 기본 지도 위에 기온 layer 올리기 o
 * 3. 기온 범례 올리기 (keyword : legend) o
 * 4. 현재위치 받아와서 지도 카메라 현위치로 옮기기
* */
//날씨를 지도로 표현해 보여주는 Fragment
class MapFragment : Fragment(),OnMapReadyCallback {

    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView : MapView
    private val args : MapFragmentArgs by navArgs()
    private lateinit var googleMap : GoogleMap
    private lateinit var navController:NavController
    companion object{
        //map위에 올라갈 tile api url
        private const val MAP_URL_FORMAT = "https://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=${BuildConfig.OPEN_WEATHER_API_KEY}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater,container,false)
        mapView = binding.mapFragment as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initViews()
    }
    private fun initViews() = with(binding){
        completeButton.setOnClickListener {
            navController.popBackStack()
        }
        currentPositionButton.setOnClickListener {
            val position = LatLng(args.location.latitude,args.location.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,12f))
        }
        favoriteButton.setOnClickListener {
            navController.navigate(R.id.action_mapContainer_to_favoriteContainer)
        }
        layerPopupButton.setOnClickListener {
        }
    }

    //지도 객체를 사용할 수 있을 때 자동으로 호출되는 함수
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val point = LatLng( args.location.latitude, args.location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12f))


        //이미지를 제공하는 URL을 통해서 Tile을 구현하는 부분
        val tileProvider : TileProvider = object :UrlTileProvider(256,256){
            // @Synchronized: 메서드가 정의된 인스턴스의 모니터를 통해 여러 스레드에 의해 메서드가 동시에 실행되지 않도록 보호
            @Synchronized
            //사용자가 보고있는 부분에 사용할 타일 이미지를 가리키는 URL을 반환
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
                val s = String.format(Locale.US, MAP_URL_FORMAT,zoom,x,y)
                var url:URL? = null
                url = try {
                    URL(s)
                }catch (e:MalformedURLException){
                    throw AssertionError(e)
                }
                return url
            }
        }
        //TileOverlay : 기본 지도 위에 표시되는 이미지 컬렉션
        map.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))!!
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}