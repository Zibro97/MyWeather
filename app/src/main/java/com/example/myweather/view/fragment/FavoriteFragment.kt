package com.example.myweather.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
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
        //search View 포커스 제거
        searchCanelButton.setOnClickListener {
            searchView.clearFocus()
        }
        //searchview 포커싱 이벤트 옵저빙
        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                //searchView에 포커싱에따라 visible 처리
                if(hasFocus){
                    settingsImageButton.visibility = View.GONE
                    toolbarTextView.visibility =View.GONE
                    searchCanelButton.visibility = View.VISIBLE
                    searchResultRv.visibility = View.VISIBLE
                    favoriteRecyclerView.alpha = 0.3F
                }else{
                    settingsImageButton.visibility = View.VISIBLE
                    toolbarTextView.visibility =View.VISIBLE
                    searchCanelButton.visibility = View.GONE
                    searchResultRv.visibility = View.GONE
                    favoriteRecyclerView.alpha = 1.0F
                }
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색 버튼 눌렀을 시 이벤트
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //글자가 쓰면서 발생하는 이벤트
            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(context,"검색 결과 : $newText",Toast.LENGTH_SHORT).show()
                return true
            }
        })
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