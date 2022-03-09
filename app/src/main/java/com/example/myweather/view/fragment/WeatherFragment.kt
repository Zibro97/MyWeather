package com.example.myweather.view.fragment

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.view.MainActivity
import com.example.myweather.view.adapter.WeatherAdapter
import com.example.myweather.viewmodel.WeatherViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import java.lang.Exception
import kotlin.system.exitProcess

/***
 * 1. 위치 권한 받아오기(FINE ,COARSE ,BACKGROUND)
 * 2. 사용자 현재 위치 받아오기
 * 3. 현재 위치 Room에 저장
 * 4. Room에 저장된 현위치,관심지역 좌표 받아와서 날씨정보 받아오기
 * 5. 날씨 정보 받아와서 ViewPager로 넘김
 * 6. 하단 circleIndicator
 */
class WeatherFragment : Fragment() {
    // TODO: 2022/03/10 화면 스크롤 시 날씨 정보 ScrollView 부분이 사라지는 현상 발견 
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

    //현재 위치를 갖고있는지 체크하기위한 SharedPreferences
    private lateinit var prefs: SharedPreferences

    //Fragment는 Context를 갖지 않으므로 Context를 참조할 변수
    private lateinit var mActivity: MainActivity

    //navigation Controller
    private lateinit var navController: NavController

    //위치 권한
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundPermission()
            when {
                permissions.getOrDefault(ACCESS_FINE_LOCATION, false) -> {
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
        context?.let { viewModel.getLocationCnt(context = it) }
        requestPermission()
        initViews()
        liveDatas()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        getLocation()
        weatherAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        //위치 요청을 취소
        cancellationTokenSource?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //메모리 누수 방지
        _binding = null
    }

    //위치 가져오는 함수
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //록액티비티가 요청을 취소할 수 있도록하는 토큰
        cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)
        fusedLocationClient!!.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource!!.token
        ).addOnSuccessListener { location ->
            try {
                context?.let { context ->
                    //sharedPreference 없으면 현재 위치 insert
                    if (prefs.getString("current", "no current") == "no current") {
                        viewModel.insertLocation(
                            context = context,
                            location = "나의 위치",
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                        prefs.edit().putString("current", "나의 위치").apply()
                    }
                    //sharedPreference 있으면 현재위치 update
                    else {
                        viewModel.updateCurrentLocation(
                            context = context,
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    }
                    getLatLngs()
                }
            } catch (exception: Exception) {
                //예외 발생시 에러 경고문구 보여주고, 다른 뷰들은 alpha값 0처리
                binding.errorDescriptionTextView.visibility = View.VISIBLE
                binding.viewPager.alpha = 0F
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
                    )
                )
            }
        }
    }

    //viewPager 초기화 및 날씨 정보 넘겨주는 함수
    private fun initViews() = with(binding){
        weatherAdapter = WeatherAdapter()
        viewPager.adapter = weatherAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.setCurrentItem(0, true)
        indicatorWeather.setViewPager(viewPager)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.getWeather(latitude = weatherAdapter.currentList[position].latitude, longitude = weatherAdapter.currentList[position].longitude)

                indicatorWeather.animatePageSelected(position)
            }
        })
        //하단 탭의 즐겨찾기 리스트 버튼 클릭시 이벤트
        favoriteListImageButton.setOnClickListener {
            navController.navigate(R.id.action_weatherContainer_to_favoriteContainer)
        }
    }

    //db에 저장된 위치 가져오는 함수
    private fun getLatLngs() {
        context?.let { context ->
            viewModel.getAllLocation(context)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun liveDatas() = with(viewModel) {
        //db에서 지역 가져오는 LiveData
        locationLiveData.observe(viewLifecycleOwner, { favorites ->
            weatherAdapter.submitList(favorites)
        })
        //db에서 가져온 지역의 날씨 정보를 가져와 리스트에 담음
        weatherLiveData.observe(viewLifecycleOwner, { weather ->
            weatherAdapter.weather = weather
            //weatherAdapter.notifyDataSetChanged()
            Log.d("TAG", "liveDatas: ${binding.bottomNavigationView.visibility}")
            binding.progressBar.visibility = View.GONE
            binding.bottomNavigationView.visibility = View.VISIBLE
        })
        locationCntLiveData.observe(viewLifecycleOwner, { cnt ->
            binding.indicatorWeather.createIndicators(cnt, 0)
        })
    }

    //백그라운드 위치 권한 요청
    //Android 11이상 부터는 위치 권한 항상 허용 옵션이 포함되지 않아서
    //포그라운드 위치권한과 백그라운드 위치 권한을 동시에 요청하면 시스템이 요청을 무시하고 앱에 어떤 권한도 부여하지 않음
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun backgroundPermission() {
        ActivityCompat.requestPermissions(
            mActivity,
            arrayOf(
                ACCESS_BACKGROUND_LOCATION
            ),
            2
        )
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
}