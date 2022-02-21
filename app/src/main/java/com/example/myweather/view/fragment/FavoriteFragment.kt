package com.example.myweather.view.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.Adapter
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.FragmentFavoriteBinding
import com.example.myweather.databinding.InsertFavoriteDialogCustomBinding
import com.example.myweather.util.SwipeHelperCallback
import com.example.myweather.view.adapter.FavoriteAdapter
import com.example.myweather.view.adapter.LocationAdapter
import com.example.myweather.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    //FavoriteFragment 바인딩 객체
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    //지역 목록 RecyclerView에 바인딩시킬 Adapter
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var locationAdapter: LocationAdapter

    //viewModel
    private val viewModel: FavoriteViewModel by viewModels()

    //navigation Controller
    private lateinit var navController: NavController

    //Layout을 inflate하는 곳
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        //우측 상단의 설정 버튼 클릭시 팝업 메뉴 띄우기
        settingsImageButton.setOnClickListener {
            val popup = PopupMenu(context, binding.settingsImageButton)

            val inf = popup.menuInflater
            inf.inflate(R.menu.menu_settings, popup.menu)
            popup.show()
        }
        //search View 검색어 초기화,포커스 제거
        searchCanelButton.setOnClickListener {
            searchView.setQuery("", false)
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
                return if (newText == "" || newText == null) {
                    binding.favoriteRecyclerView.alpha = 0.5F
                    false
                } else {
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
            view?.let { view ->
                val action =
                    FavoriteFragmentDirections.actionFavoriteContainerToConfirmInsertDialog(location)
                view.findNavController().navigate(action)
            }
        })
        binding.searchResultRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.searchResultRv.adapter = locationAdapter
        //즐겨찾기 목록 RecyclerView
        favoriteAdapter = FavoriteAdapter(onItemClick = { favorite ->
            navController.navigate(R.id.action_favoriteContainer_to_weatherContainer)
        },   onRemoveClick = {favorite ->
            context?.let { context->
                viewModel.removeFavorite(context,favorite.id!!)
            }
        }
        )
        binding.favoriteRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.favoriteRecyclerView.adapter = favoriteAdapter

        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.favoriteRecyclerView)
        binding.favoriteRecyclerView.setOnTouchListener { _,_ ->
            swipeHelperCallback.removePreviousClamp(binding.favoriteRecyclerView)
            false
        }
    }

    //즐겨찾기 해둔 지역 정보 가져오는 함수
    private fun getLocations() {
        context?.let { context ->
            viewModel.getAllLocation(context)
        }
    }
    //LiveData
    @SuppressLint("NotifyDataSetChanged")
    private fun liveData() {
        //db 즐겨찾기 LiveData
        viewModel.locationLiveData.observe(viewLifecycleOwner, { favorite ->
            Log.d("TAG", "liveData: dg")
            favoriteAdapter.submitList(favorite)
            favoriteAdapter.notifyDataSetChanged()
        })
        //검색 결과 LiveData
        viewModel.searchLocateLiveData.observe(viewLifecycleOwner, { locations ->
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
            locationAdapter.notifyDataSetChanged()
        })
    }
}