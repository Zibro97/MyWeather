package com.example.myweather.view.fragment

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.view.MainActivity
import com.example.myweather.view.adapter.WeatherAdapter
import com.example.myweather.viewmodel.WeatherViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

/***
 * 1. 위치 권한 받아오기(FINE ,COARSE ,BACKGROUND)
 * 2. 사용자 현재 위치 받아오기
 * 3. 현재 위치 Room에 저장
 * 4. Room에 저장된 현위치,관심지역 좌표 받아와서 날씨정보 받아오기
 * 5. 날씨 정보 받아와서 ViewPager로 넘김
 */
class WeatherFragment : Fragment(), CoroutineScope {
    //뷰 바인딩을 위한 바인딩 객체
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    //ViewPager에 붙일 Adapter
    private lateinit var weatherAdapter: WeatherAdapter

    //WeatherViewModel
    private val viewModel: WeatherViewModel by viewModels()

    //현재 위치를 가져오기 위한 변수
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var cancellationTokenSource: CancellationTokenSource? = null

    //위치 정보를 가지고 주소로 변환하기 위한 Geocoder 라이브러리
    //private lateinit var geocoder: Geocoder
    private lateinit var prefs: SharedPreferences

    //Fragment는 Context를 갖지 않으므로 Context를 참조할 변수
    private lateinit var mActivity: MainActivity

    //navigation Controller
    private lateinit var navController: NavController

    //coroutine
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    //위치 권한
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when {
                permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
                    backgroundPermissionPopup()
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
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        prefs = mActivity.getSharedPreferences("current", Context.MODE_PRIVATE)
        requestPermission()
//        initViews()
//        setBackground()
//        bindingRecyclerView()
    }
//    private fun initView(){
//        prefs = mActivity.getSharedPreferences("current",Context.MODE_PRIVATE)
//        with(binding){
//            weatherAdapter = WeatherAdapter(,context = context)
//            viewPager.adapter = weatherAdapter
//        }
//    }
//
//    private fun initViews() {
//        with(binding){
//            //하단 탭의 즐겨찾기 리스트 버튼 클릭시 이벤트
//            favoriteListImageButton.setOnClickListener {
//                navController.navigate(R.id.action_weatherContainer_to_favoriteContainer)
//            }
//        }
//    }

//    //RecyclerView Settings
//    private fun bindingRecyclerView() {
//        //Adapter 객체 초기화
//        hourlyAdapter = HourlyAdapter()
//        dailyAdapter = DailyAdapter()
//        //RecyclerView가 어떻게 그려질 것인지 정의
//        binding.hourlyRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//        binding.dailyRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
//        //RecyclerView Adapter 연결
//        binding.hourlyRv.adapter = hourlyAdapter
//        binding.dailyRv.adapter = dailyAdapter
//    }

    //    //날씨에 따라 배경 gif를 설정
//    private fun setBackground(){
//        //배경 이미지를 띄우고자 Glide 라이브러리 사용
//        //.asGif() : GIF를 로딩하고자 할때 사용하는 함수
//        //.with() : View,Fragment 혹은 Activity로 부터 Context 가져오는 함수
//        //.load() : 이미지를 로드하는 함수. 다양한 방법으로 이미지를 불러올 수 있음(Bitmap,Drawable,String,Uri,File,ByteArray).
//        //.into() : 이미지를 보여줄 View를 지정하는 합수
//        //.diskCacheStrategy(): 디스크에 캐싱하지 않으려면 DiskCacheStrategy.NONE 사용
//        //.fitCenter() : 실제 이미지가 이미지뷰의 사이즈와 다를 때, 이미지와 이미지뷰의 중간을 맞춰서 이미지 크기를 스케일링하는 함수
//        Glide.with(this)
//            .asGif()
//            .fitCenter()
//            .load(R.drawable.ic_cloudy_background)
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//            .into(binding.weatherBackground)
//    }
    //날씨 받아오는 함수
//    private fun getWeather(latitude:Double,longitude :Double){
//        viewModel.getWeather(latitude = latitude,longitude = longitude)
//        viewModel.weatherLiveData.observe(viewLifecycleOwner, { weather ->
//            weather?.let {
//                weatherAdapter = WeatherAdapter(it)
//                binding.viewPager.adapter = weatherAdapter
//                //hourlyAdapter.submitList(it.hourly)
//                //dailyAdapter.submitList(it.daily)
//            }
//        })
//    }
    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)
        fusedLocationClient!!.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->
            try {
                context?.let { context ->
                    if (prefs.getString("current", "no current").isNullOrEmpty()) {
                        viewModel.insertFavorite(
                            context = context,
                            location = "나의 위치",
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                        prefs.edit().putString("current", "나의 위치").apply()
                    } else {
                        viewModel.updateCurrentLocation(context = context,latitude = location.latitude,longitude = location.longitude)
                    }
                }
//                getWeather(latitude = location.latitude,longitude = location.longitude)
//                launch(coroutineContext) {
//                    getAddress(lat = location.latitude, lng = location.longitude)
//                }
            } catch (exception: Exception) {
                //예외 발생시 에러 경고문구 보여주고, 다른 뷰들은 alpha값 0처리
                binding.errorDescriptionTextView.visibility = View.VISIBLE
                binding.viewPager.alpha = 0F
            } finally {
                //데이터가 불러와진 상태 progressBar의 visible값을 gone,refresh 상태를 false로 변경
                binding.progressBar.visibility = View.GONE
                binding.refreshLayout.isRefreshing = false
            }
        }
    }

    //위치 권한 요청
    private fun requestPermission() {
        when {
            //위치 권한 승인 시
            ActivityCompat.checkSelfPermission(mActivity, ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                backgroundPermissionPopup()
                getLocation()
            }
            //사용자가 권한 요청을 명시적으로 거부한 경우 true를 반환
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) ||
                    shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION) -> {
                //교육용 팝업
                showPermissionContextPopup()
            }
            //권한 거부
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_BACKGROUND_LOCATION
                    )
                )
            }
        }
    }

    private fun getLatLngs(){
        context?.let { context->
            viewModel.getAllLocation(context)
        }
    }
    private fun liveData(){
        viewModel.favoriteLiveData.observe(viewLifecycleOwner,{ favorites->
            favorites.forEach{ location->
            }
        })
    }

    //지역명을 가져오는 함수
//    private suspend fun getAddress(lat:Double,lng:Double) = withContext(Dispatchers.IO) {
//        geocoder = Geocoder(mActivity)
//        val address = geocoder.getFromLocation(lat,lng,1)
//        Log.d("TAG", "getAddress: $address")
//        withContext(Dispatchers.Main){
//            binding.cityTv.text =
//                if(address[0].locality.isNullOrEmpty())
//                    address[0].getAddressLine(0).split(",")[2]
//                else
//                    address[0].locality
//        }
//    }
    //백그라운드 위치 권한 요청
    //Android 11이상 부터는 위치 권한 항상 허용 옵션이 포함되지 않아서
    //포그라운드 위치권한과 백그라운드 위치 권한을 동시에 요청하면 시스템이 요청을 무시하고 앱에 어떤 권한도 부여하지 않음
    private fun backgroundPermission() {
        ActivityCompat.requestPermissions(
            mActivity,
            arrayOf(
                ACCESS_BACKGROUND_LOCATION
            ),
            2
        )
    }

    //백그라운드 위치 권한 묻는 팝업
    private fun backgroundPermissionPopup() {
        AlertDialog.Builder(mActivity)
            .setTitle("위젯 사용 시 백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
            .setPositiveButton("네") { _, p1 ->
                backgroundPermission()
            }
            .setNegativeButton("아니오") { _, _ -> }
    }

    //교육용 팝업
    private fun showPermissionContextPopup() {
        AlertDialog.Builder(mActivity)
            .setTitle("위치 권한")
            .setMessage("My Weather 앱에서 현위치의 날씨를 가져오기위해 권한이 필요합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                locationPermissionRequest.launch(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    )
                )
            }
            .setNegativeButton("취소하기") { _, _ ->
                //취소 버튼 클릭 시 앱 종료
                exitProcess(0)
            }
            .create()
            .show()
    }
//    @SuppressLint("SetTextI18n")
//    private fun initViewsText(weather : WeatherDTO){
//        with(binding){
//            tempTv.text = "${weather.current.temp.roundToInt()}°"
//            weatherDestinationTv.text = weather.current.weather.first().description
//            maxTempTv.text = "최고:${weather.daily.first().temp.maxTemp.roundToInt()}°"
//            minTempTv.text = "최저:${weather.daily.first().temp.minTemp.roundToInt()}°"
//            val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
//            sunriseTimeTextView.text = simpleDataFormat.format(weather.current.sunrise * 1000L)
//            sunsetTimeTextView.text = "일몰: ${simpleDataFormat.format(weather.current.sunset * 1000L)}"
//            windTimeTextView.text = "${weather.current.windSpeed.roundToInt()}m/s"
//            realFeelTempTextView.text = "${weather.current.feelsLike.roundToInt()}°"
//            realHumidityTextView.text = "${weather.current.humidity}%"
//            dewPointTextView.text = "현재 이슬점이 ${weather.current.dewPoint.roundToInt()}°입니다."
//            realVisibilityTextView.text = "${weather.current.visibility/1000}KM"
//            tempDestinationTv.text = "${weather.current.temp.roundToInt()}°|${weather.current.weather.first().description}"
//            //realSnowRainTextView.text = "${weather.current.snow}MM"
//        }
//        binding.contentsLayout.animate()
//            .alpha(1.0F)
//            .start()
//    }
}