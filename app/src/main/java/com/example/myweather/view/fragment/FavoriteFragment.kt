package com.example.myweather.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.R
import com.example.myweather.databinding.FragmentFavoriteBinding
import com.example.myweather.view.adapter.FavoriteAdapter
import com.example.myweather.view.adapter.LocationAdapter
import com.example.myweather.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    //지역 목록 RecyclerView에 바인딩시킬 Adapter
    private lateinit var favoriteAdapter : FavoriteAdapter
    private lateinit var locationAdapter : LocationAdapter
    //viewModel
    private val viewModel : FavoriteViewModel by viewModels()
    //navigation Controller
    private lateinit var navController: NavController

    //Layout을 inflate하는 곳
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    //onCreateView에서 반환된 View객체의 초기값 설정, LiveData옵저빙, Adapter 설정 등등하는 곳
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        initViews()
        initRecyclerView()
        getLocations()
        liveData()
    }
    private fun initViews() = with(binding){
        //우측 상단의 설정 버튼 클릭시 팝업 메뉴 띄우기
        settingsImageButton.setOnClickListener{
            val popup = PopupMenu(context,binding.settingsImageButton)

            val inf = popup.menuInflater
            inf.inflate(R.menu.menu_settings,popup.menu)
            popup.show()
        }
        //search View 검색어 초기화,포커스 제거
        searchCanelButton.setOnClickListener {
            searchView.setQuery("",false)
            searchView.clearFocus()
        }
        //searchview 포커싱 이벤트
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus -> //searchView에 포커싱에따라 visible 처리
            favoriteRecyclerView.visibility = VISIBLE
            if (hasFocus) {
                settingsImageButton.visibility = GONE
                toolbarTextView.visibility = GONE
                searchCanelButton.visibility = VISIBLE
                searchResultRv.visibility = VISIBLE
                favoriteRecyclerView.alpha = 0.3F
            } else {
                settingsImageButton.visibility = VISIBLE
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
                return if (newText == ""||newText==null){
                    binding.favoriteRecyclerView.alpha = 0.5F
                    false
                }else{
                    binding.favoriteRecyclerView.alpha = 1.0F
                    viewModel.locationInfo(newText)
                    true
                }
            }
        })
    }

    private fun initRecyclerView() {
        //검색 결과 RecyclerView
        locationAdapter = LocationAdapter(onClickItem = { location ->
            context?.let { context ->
                var city:String? = null
                location.title.split(" ").forEach{ s->
                    if(s.contains("시")) city = s
                }
                viewModel.insertLocation(context = context,location = city!!,latitude = location.point.y,longitude = location.point.x)
            }
        })
        binding.searchResultRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.searchResultRv.adapter = locationAdapter
        //즐겨찾기 목록 RecyclerView
        favoriteAdapter = FavoriteAdapter(onItemClick = { favorite ->
            navController.navigate(R.id.action_favoriteContainer_to_weatherContainer)
        })
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.favoriteRecyclerView.adapter = favoriteAdapter
    }

    private fun getLocations(){
        context?.let { context->
            viewModel.getAllLocation(context)
        }
    }
    /**
     * 1. favortieRv는 visible
     * 2. searchResultRv, ErrorDescriptionTv 는 Gone
     * 3. searchView에 포커스 되면 favoriteRv는 alpha 0.3, 그 위에 searchResultRv를 visible
     * 4. 검색을 하기 시작함에 따라, 검색 결과가 있으면 ErrorDescriptionTv Gone ,searchResultRv visible,
     * 5. 검색 결과가 없으면 ErrorDescriptionTv visible ,searchResultRv gone 처리
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun liveData(){
        //db 즐겨찾기 LiveData
        viewModel.locationLiveData.observe(viewLifecycleOwner,{ favorite->
            favoriteAdapter.submitList(favorite)
        })
        //검색 결과 LiveData
        viewModel.searchLocateLiveData.observe(viewLifecycleOwner,{ locations->
            binding.favoriteRecyclerView.visibility = GONE
            if(locations.response.result == null){
                with(binding){
                    searchIcon.visibility = VISIBLE
                    noResultDescriptionTextView.visibility = VISIBLE
                    errorDescriptionSearchTextView.visibility = VISIBLE
                    searchResultRv.visibility = GONE
                }
            }else{
                with(binding){
                    searchIcon.visibility = GONE
                    noResultDescriptionTextView.visibility = GONE
                    errorDescriptionSearchTextView.visibility = GONE
                    searchResultRv.visibility = VISIBLE
                }
                locationAdapter.submitList(locations.response.result.items)
            }
            locationAdapter.notifyDataSetChanged()
        })
    }
}