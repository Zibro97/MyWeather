package com.example.myweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.myweather.R

class MainActivity : AppCompatActivity() {
    //NavHost 내에서 네비게이션을 제어하는 역할
    //NavHostFragment는 개별적으로 NavController를 가지고 있음
    //nav_graph 정보를 바탕으로 네비게이션 간 이동을 담당
    private lateinit var navController : NavController
    //Fragment 네비게이션을 위한 빈 위젯
    private lateinit var navFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //NavHostFragment는 개별적으로 NavController를 가짐
        navFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        //navigation_graph 정보를 바탕으로 네비게이션 간 이동을 담당하는 navigation을 찾음
        navController = navFragment.findNavController()
    }
}