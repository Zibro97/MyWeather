package com.example.myweather.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.view.MainActivity
import com.example.myweather.view.adapter.DailyAdapter
import com.example.myweather.view.adapter.HourlyAdapter
import com.example.myweather.viewmodel.WeatherViewModel
import java.util.*
import kotlin.math.roundToInt
import kotlin.system.exitProcess

class WeatherFragment : Fragment() {
    //데이터 바인딩을 위한 바인딩 객체
    private lateinit var binding : FragmentWeatherBinding
    //매 시각 날씨 RecyclerView에 붙일 Adapter
    private lateinit var hourlyAdapter : HourlyAdapter
    private lateinit var dailyAdapter : DailyAdapter
    //WeatherViewModel
    private val viewModel : WeatherViewModel by viewModels()
    //Fragment는 Context를 갖지 않으므로 Context를 참조할 변수
    private lateinit var mActivity:MainActivity
    //navigation Controller
    private lateinit var navController : NavController
    //locationManager
    private var locationManager:LocationManager? =null
    private var locationListener:LocationListener? =null
    //위치 권한
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    getLocation()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //프래그먼트가 액티비티에 붙을때 Context를 액티비티로 형변환해서 할당
        mActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        requestPermission()
        initViews()
        bindingRecyclerView()
        setBackground()
    }

    private fun initViews() {
        binding.favoriteListImageButton.setOnClickListener {
            navController.navigate(R.id.action_weatherContainer_to_favoriteContainer)
        }
    }

    //RecyclerView Settings
    private fun bindingRecyclerView() {
        //Adapter 객체 초기화
        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()
        //RecyclerView가 어떻게 그려질 것인지 정의
        binding.hourlyRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.dailyRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        //RecyclerView Adapter 연결
        binding.hourlyRv.adapter = hourlyAdapter
        binding.dailyRv.adapter = dailyAdapter
    }

    //날씨에 따라 배경 gif를 설정
    private fun setBackground(){
        //배경 이미지를 띄우고자 Glide 라이브러리 사용
        //.asGif() : GIF를 로딩하고자 할때 사용하는 함수
        //.with() : View,Fragment 혹은 Activity로 부터 Context 가져오는 함수
        //.load() : 이미지를 로드하는 함수. 다양한 방법으로 이미지를 불러올 수 있음(Bitmap,Drawable,String,Uri,File,ByteArray).
        //.into() : 이미지를 보여줄 View를 지정하는 합수
        //.diskCacheStrategy(): 디스크에 캐싱하지 않으려면 DiskCacheStrategy.NONE 사용
        //.fitCenter() : 실제 이미지가 이미지뷰의 사이즈와 다를 때, 이미지와 이미지뷰의 중간을 맞춰서 이미지 크기를 스케일링하는 함수
        Glide.with(this)
            .asGif()
            .fitCenter()
            .load(R.drawable.ic_cloudy_background)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.weatherBackground)
    }
    //날씨 받아오는 함수
    private fun getWeather(latitude:Double,longitude :Double){
        viewModel.getWeather(latitude = latitude,longitude = longitude)
        viewModel.weatherLiveData.observe(viewLifecycleOwner, { weather ->
            binding.weatherDTO = weather
            binding.dailyModel = weather.daily.first()
            binding.weatherModel =weather.current.weather.first()
            binding.invalidateAll()
            weather?.let {
                Log.d("TAG", "getWeather: ${it.daily}")
                hourlyAdapter.submitList(it.hourly)
                dailyAdapter.submitList(it.daily)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates(){
        //Callback 등록 해제 : 앱이 종료되거나 백그라운드로 변경될 시 더 이상 위치 정보를 받을 필요 없어 콜백 등록 해제
        locationManager?.let {locationManager->
            locationListener?.let { locationListener->
                locationManager.removeUpdates(locationListener)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        //listener 생성
        locationListener = LocationListener { location ->
            Log.d("TAG", "getLocation: $location")
            getWeather(latitude = location.latitude,longitude = location.longitude)
        }
        locationManager?.let { locationManager->
            locationListener?.let { locationListener->
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    10000,//10s interval
                    10.0f,
                    locationListener
                    )
            }
        }
    }
    //위치 권한 요청
    private fun requestPermission(){
        when {
            //위치 권한 승인 시
            ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                getLocation()
            }
            //사용자가 권한 요청을 명시적으로 거부한 경우 true를 반환
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)||
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                //교육용 팝업
                showPermissionContextPopup()
            }
            //권한 거부
            else -> {
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    }
    //교육용 팝업
    private fun showPermissionContextPopup(){
        AlertDialog.Builder(mActivity)
            .setTitle("위치 권한이 필요합니다.")
            .setMessage("My Weather 앱에서 현위치의 날씨를 가져오기위해 권한이 필요합니다.")
            .setPositiveButton("동의하기"){_,_->
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
            }
            .setNegativeButton("취소하기"){_,_->
                //취소 버튼 클릭 시 앱 종료
                exitProcess(0)
            }
            .create()
            .show()
    }
    //주소로 변환
    private fun getAddress(lat:Double,lng:Double) :String {
        // TODO: 2022/01/21 locationManager에 있는 Geocoder를 통해 주소로 변환
        return ""
    }

    companion object{
        //temp_textview 뒤에 °붙이기 위한 함수
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setMaxTempFormat")
        @JvmStatic
        fun setMaxTempFormat(view: TextView, temp:Double){
            view.text = "최고 : ${temp.roundToInt()}°"
        }
        //temp_textview 뒤에 °붙이기 위한 함수
        @SuppressLint("SetTextI18n")
        @BindingAdapter("setMinTempFormat")
        @JvmStatic
        fun setMinTempFormat(view: TextView, temp:Double){
            view.text = "최저 : ${temp.roundToInt()}°"
        }
    }
}