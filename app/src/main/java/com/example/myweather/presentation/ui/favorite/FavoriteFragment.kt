package com.example.myweather.presentation.ui.favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.FragmentFavoriteBinding
import com.example.myweather.databinding.InsertFavoriteDialogCustomBinding
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.presentation.base.BaseFragment
import com.example.myweather.presentation.util.SwipeController
import com.example.myweather.presentation.util.SwipeControllerActions
import com.example.myweather.presentation.ui.adapter.FavoriteAdapter
import com.example.myweather.presentation.ui.adapter.LocationAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/***
 * 관심지역 추가
 * 1. 검색한 관심지역 추가 시 weather api로 해당 지역의 id 요청
 * 2. 지역의 id, 좌표, 지역명 room에 저장
 * 관심지역 RecyclerView
 * 1. Room에 저장된 지역들의 id들을 가져와 문자열로 변환
 * 2. 변환한 문자열을 가지고 지역들 날씨정보 가져옴
*/
@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {
    //지역 목록 RecyclerView에 바인딩시킬 Adapter
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var locationAdapter: LocationAdapter

    private val viewModel: FavoriteViewModel by viewModels()

    //navigation Controller
    private lateinit var navController: NavController

    //Swipe Controller
    private var swipeController : SwipeController? = null

    //onCreateView에서 반환된 View객체의 초기값 설정, LiveData옵저빙, Adapter 설정 등등하는 곳
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        //viewModel.getAllFavorites()
        initRecyclerView()
    }

    override fun initViews() = with(binding) {
        //search View 검색어 초기화,포커스 제거
        searchCanelButton.setOnClickListener {
            clearSearchView()
        }
        //searchview 포커싱 이벤트
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus -> //searchView에 포커싱에따라 visible 처리
            favoriteRecyclerView.visibility = VISIBLE
            if (hasFocus) {
                toolbarTextView.visibility = GONE
                searchCanelButton.visibility = VISIBLE
                searchResultRv.visibility = VISIBLE
                favoriteRecyclerView.alpha = 0.3F
            } else {
                toolbarTextView.visibility = VISIBLE
                searchCanelButton.visibility = GONE
                searchResultRv.visibility = GONE
                searchIcon.visibility = GONE
                noResultDescriptionTextView.visibility = GONE
                errorDescriptionSearchTextView.visibility = GONE
                favoriteRecyclerView.alpha = 1.0F
                locationAdapter.submitList(listOf())
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색 버튼 눌렀을 시 이벤트
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //글자가 쓰면서 발생하는 이벤트
            override fun onQueryTextChange(newText: String?): Boolean {
                return if (newText == "" || newText == null) {
                    binding.favoriteRecyclerView.alpha = 0.5F
                    false
                } else {
                    binding.favoriteRecyclerView.alpha = 1.0F
                    viewModel.getLocationInfo(newText)
                    true
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initRecyclerView() = with(binding){
        //검색 결과 RecyclerView
        locationAdapter = LocationAdapter(onClickItem = { location ->
            //item 클릭 시 customDialog 띄워 해당 도시 관심지역으로 추가할 것인지 묻고
            //추가 버튼 클릭시 관심지역 목록에 띄우기
            context?.let { context->
                val dialogCustomBinding = InsertFavoriteDialogCustomBinding.inflate(layoutInflater)
                val dialog = AlertDialog.Builder(context)
                    .setView(dialogCustomBinding.root)
                    .create()
                var city:String? = null
                location.title.split(" ").forEach{ s->
                    if(s.contains("시")||s.contains("군")) city = s
                }

                dialogCustomBinding.confirmInsertText.text = "${city}를 추가하시겠습니까?"
                dialogCustomBinding.insertButton.setOnClickListener {
                    viewModel.insertLocation(city!!,location.point.y,location.point.x)
                    dialog.dismiss()
                    clearSearchView()
                }
                dialog.show()
            }
        })
        searchResultRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchResultRv.adapter = locationAdapter
        //즐겨찾기 목록 RecyclerView
        favoriteAdapter = FavoriteAdapter(
            onItemClick = { position ->
                val action = FavoriteFragmentDirections.actionFavoriteContainerToWeatherContainer(position)
                navController.navigate(action)
            }
        )
        favoriteRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        favoriteRecyclerView.adapter = favoriteAdapter
        swipeController = SwipeController(object : SwipeControllerActions(){
            override fun onRightClicked(position: Int) {
                when(position){
                    0-> Toast.makeText(context,"나의 위치는 삭제하실 수 없습니다.",Toast.LENGTH_SHORT).show()
                    else -> viewModel.removeFavorite(favoriteAdapter.currentList[position])
                }
            }
        })
        ItemTouchHelper(swipeController!!).attachToRecyclerView(favoriteRecyclerView)
        favoriteRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController!!.onDraw(c)
            }
        })
    }
    //검색창 clear하고 focus 날리기
    private fun clearSearchView()= with(binding){
        searchView.setQuery("", false)
        searchView.clearFocus()
    }

    override fun observeData() = with(viewModel){
        //db 즐겨찾기 LiveData
        locationLiveData.observe(viewLifecycleOwner, { favorite ->
            getLocationsWeather(favorite)
            Handler(Looper.getMainLooper()).postDelayed({
                favoriteAdapter.submitList(favorite)
            },500)
        })
        //검색 결과 LiveData
        searchLocateLiveData.observe(viewLifecycleOwner, { locations ->
            binding.favoriteRecyclerView.visibility = GONE
            if (locations.response.result == null) {
                with(binding) {
                    searchIcon.visibility = VISIBLE
                    noResultDescriptionTextView.visibility = VISIBLE
                    errorDescriptionSearchTextView.visibility = VISIBLE
                    searchResultRv.visibility = GONE
                }
            } else {
                with(binding) {
                    searchIcon.visibility = GONE
                    noResultDescriptionTextView.visibility = GONE
                    errorDescriptionSearchTextView.visibility = GONE
                    searchResultRv.visibility = VISIBLE
                }
                locationAdapter.submitList(locations.response.result.items)
            }
        })
        weatherListLiveData.observe(viewLifecycleOwner, { weatherList ->
            // TODO: 2022/05/03 flow로 변경하여 관심지역 추가 삭제가 실시간으로 반영되지만 해당 위치의 날씨정보 리스트가 동시 반영이 안됨
            favoriteAdapter.weatherList = weatherList.favoriteList
            binding.favoriteRecyclerView.visibility = VISIBLE
        })
    }
}