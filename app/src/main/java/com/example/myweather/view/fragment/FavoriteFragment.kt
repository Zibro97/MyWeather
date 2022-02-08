package com.example.myweather.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.R
import com.example.myweather.databinding.FragmentFavoriteBinding
import com.example.myweather.view.adapter.FavoriteAdapter
import com.example.myweather.viewmodel.WeatherViewModel

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    //지역 목록 RecyclerView에 바인딩시킬 Adapter
    private lateinit var adapter : FavoriteAdapter
    //viewModel
    private val viewModel : WeatherViewModel by viewModels()

    //Layout을 inflate하는 곳
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    //onCreateView에서 반환된 View객체의 초기값 설정, LiveData옵저빙, Adapter 설정 등등하는 곳
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //우측 상단의 설정 버튼 클릭시 팝업 메뉴 띄우기
        binding.settingsImageButton.setOnClickListener{
            val popup = PopupMenu(context,binding.settingsImageButton)

            val inf = popup.menuInflater
            inf.inflate(R.menu.menu_settings,popup.menu)
            popup.show()
        }
        initRecyclerView()
        getLocations()
        liveData()
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter()
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.favoriteRecyclerView.adapter = adapter
    }

    private fun getLocations(){
        context?.let { context->
            viewModel.getAllLocation(context)
        }
    }
    private fun liveData(){
        viewModel.locationLiveData.observe(viewLifecycleOwner,{ favorite->
            adapter.submitList(favorite)
        })
    }
}