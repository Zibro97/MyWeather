package com.example.myweather.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweather.R
import com.example.myweather.databinding.FragmentFavoriteBinding
import com.example.myweather.util.DatabaseProvider
import com.example.myweather.view.adapter.FavoriteAdapter

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : FavoriteAdapter
    private val favoriteDao by lazy {
        context?.let{ context->
            DatabaseProvider.getAppDatabase(context).favoriteDao()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

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
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter()
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.favoriteRecyclerView.adapter = adapter
    }
    // TODO: db에서 관심지역 가져오기
    //db에서 모든 관심지역 가져오는 함수
    private fun getFavorite(){
        Thread(Runnable {
            context?.let { context->
                //adapter.submitList(getAppDatabase(context).favoriteDao().getAll())
            }
        })
    }

    // TODO: 관심지역 추가 버튼 클릭시 event
    //db에 관심 지역 추가
    private fun insertFavorite(){
        Thread(Runnable{
            context?.let { context->
                //.insertFavorite(Favorite(null,37.123,128.1231234))
            }
        }).start()
    }

}