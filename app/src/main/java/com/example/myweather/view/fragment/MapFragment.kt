package com.example.myweather.view.fragment

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.myweather.BuildConfig
import com.example.myweather.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import java.lang.AssertionError
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/***
 * TODO
 * 1. 기본 지도 띄우기 o
 * 2. 기본 지도 위에 기온 layer 올리기 o
 * 3. 기온 범례 올리기 (keyword : legend) o
 * 4. 현재위치 받아와서 지도 카메라 현위치로 옮기기
* */
//날씨를 지도로 표현해 보여주는 Fragment
class MapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var mapView : MapView
    private val args : MapFragmentArgs by navArgs()

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

    //지도 객체를 사용할 수 있을 때 자동으로 호출되는 함수
    override fun onMapReady(map: GoogleMap) {
        val point = LatLng( args.location.latitude, args.location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,12f))

        //이미지를 제공하는 URL을 통해서 Tile을 구현하는 부분
        val tileProvider : TileProvider = object :UrlTileProvider(256,256){
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

    companion object{
        //map위에 올라갈 tile api url
        private const val MAP_URL_FORMAT = "https://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=${BuildConfig.OPEN_WEATHER_API_KEY}"
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