package com.example.myweather.presentation.ui.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.myweather.BuildConfig
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

import androidx.navigation.NavController
import androidx.navigation.Navigation


/***
 * 1. 기본 지도 띄우기 o
 * 2. 기본 지도 위에 기온 layer 올리기 o
 * 3. 기온 범례 올리기 (keyword : legend) o
 * 4. 현재위치 받아와서 지도 카메라 현위치로 옮기기
* */
//날씨를 지도로 표현해 보여주는 Fragment
class MapFragment : Fragment(),OnMapReadyCallback {
    //바인딩 객체
    private var _binding : FragmentMapBinding? = null
    private val binding get() = _binding!!

    //MapView 객체
    private lateinit var mapView : MapView

    //WeatherFragment에서 MapFragment로 이동 시 넘겨 받을 인자
    private val args : MapFragmentArgs by navArgs()

    //GoogleMap 객체
    private lateinit var googleMap : GoogleMap

    //NavController 객체
    private lateinit var navController:NavController

    companion object{
        //layer
        private var layer = "temp_new"
        //map위에 올라갈 tile api url
        private const val MAP_URL_FORMAT = "https://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=${BuildConfig.OPEN_WEATHER_API_KEY}"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        //'완료'버튼 클릭 이벤트
        completeButton.setOnClickListener {
            //navigation stack에서 pop해버리기
            navController.popBackStack()
        }
        //현위치 버튼 클릭 이벤트
        currentPositionButton.setOnClickListener {
            //현위치로 지도 카메라 이동
            val position = LatLng(args.location.latitude,args.location.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,12f))
        }
        //관심지역 버튼 클릭 이벤트
        favoriteButton.setOnClickListener {
            //관심지역Fragment로 이동
            navController.navigate(R.id.action_mapContainer_to_favoriteContainer)
        }
        //layer버튼 클릭시 이벤트(팝업메뉴)
        layerPopupButton.setOnClickListener {
            val popup = PopupMenu(context, layerPopupButton).apply {
                menuInflater.inflate(R.menu.menu_layers,menu)
                setOnMenuItemClickListener {
                    layer = when(it.itemId){
                        R.id.rain_layer -> {
                            tempLegendLayout.visibility = View.GONE
                            "precipitation_new"
                        }
                        R.id.cloud_layer -> {
                            tempLegendLayout.visibility = View.GONE
                            "clouds_new"
                        }
                        else -> {
                            tempLegendLayout.visibility = View.VISIBLE
                            "temp_new"
                        }
                    }
                    false
                }
            }
            popup.show()
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
                val s = String.format(Locale.US, MAP_URL_FORMAT, layer,zoom,x,y)
                var url:URL? = null
                url = try {
                    URL(s)
                }catch (e:MalformedURLException){
                    throw AssertionError(e)
                }
                Log.d("TAG", "getTileUrl: $layer $s")
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