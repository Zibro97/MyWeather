package com.example.myweather

import android.app.Application
import com.example.myweather.presentation.util.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//Hilt 사용
//Hilt 코드 생성 트리거
@HiltAndroidApp
class MyWeatherApplication : Application() {

    companion object{
        lateinit var pref : PreferenceManager
    }

    override fun onCreate() {
        super.onCreate()

        //SharedPreferences 객체
        pref = PreferenceManager(applicationContext)

        //디버깅일때만 로그 출력
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}