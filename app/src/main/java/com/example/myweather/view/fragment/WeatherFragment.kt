package com.example.myweather.view.fragment

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.view.MainActivity
import com.example.myweather.view.adapter.HourlyAdapter
import com.example.myweather.viewmodel.WeatherViewModel
import com.google.android.gms.location.*
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {
    //데이터 바인딩을 위한 바인딩 객체
    private lateinit var binding : FragmentWeatherBinding
    //매 시각 날씨 RecyclerView에 붙일 Adapter
    private lateinit var hourlyAdapter : HourlyAdapter
    //WeatherViewModel
    private val viewModel : WeatherViewModel by viewModels()
    //현재 위치를 가져오기 위한 변수
    private var fusedLocationClient : FusedLocationProviderClient? =null
    //장치 위치가 변경되었거나 더 이상 확인할 수 없는 경우 알림을 수신하기 위한 콜백객체
    private lateinit var locationCallback : LocationCallback
    //Fragment는 Context를 갖지 않으므로 Context를 참조할 변수
    private lateinit var mActivity:MainActivity
    //navigation Controller
    private lateinit var navController : NavController
    private val permissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        if(granted){
            getLocation()
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
        //RecyclerView가 어떻게 그려질 것인지 정의
        binding.hourlyRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        //RecyclerView Adapter 연결
        binding.hourlyRv.adapter = hourlyAdapter
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
    //위치 권한 요청
    private fun requestPermission(){
        permissionResult.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
    }

    //사용자 위치 받아오기
    private fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)
        //apply : 수신 객체의 함수를 사용하지 않고 수신 객체 자신을 다시 반환하려는 경우 사용
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 20 * 1000 //20초
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                //locationResult가 없으면 return
                locationResult ?: return
                for(location in locationResult.locations){
                    location?.let { location->
                        getWeather(latitude = location.latitude,longitude = location.longitude)
                    }
                }
            }
        }
        fusedLocationClient?.let { fusedLocationClient->
            if (ActivityCompat.checkSelfPermission(mActivity, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                {
                    //위치 권한이 없다면 권한 요청
                    requestPermission()
                    return
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper())
        }
    }
    private fun getWeather(latitude:Double,longitude :Double){
        viewModel.getWeather(latitude = latitude,longitude = longitude)
        viewModel.weatherLiveData.observe(this, Observer { weather ->
            binding.weatherDTO = weather
            binding.dailyModel = weather.daily.first()
            binding.weatherModel =weather.current.weather.first()
            binding.invalidateAll()
            weather?.let {
                hourlyAdapter.submitList(it.hourly)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }

    private fun stopLocationUpdates(){
        //Callback 등록 해제 : 앱이 종료되거나 백그라운드로 변경될 시 더 이상 위치 정보를 받을 필요 없어 콜백 등록 해제
        fusedLocationClient?.let { locationCallback }
    }

    companion object{
        //temp_textview 뒤에 °붙이기 위한 함수
        @BindingAdapter("setMaxTempFormat")
        @JvmStatic
        fun setMaxTempFormat(view: TextView, temp:Double){
            view.text = "최고 : ${temp.roundToInt()}°"
        }
        //temp_textview 뒤에 °붙이기 위한 함수
        @BindingAdapter("setMinTempFormat")
        @JvmStatic
        fun setMinTempFormat(view: TextView, temp:Double){
            view.text = "최저 : ${temp.roundToInt()}°"
        }
    }
}