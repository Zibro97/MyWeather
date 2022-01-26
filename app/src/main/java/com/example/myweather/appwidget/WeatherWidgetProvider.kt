package com.example.myweather.appwidget

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.myweather.R
import com.example.myweather.util.RetrofitClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.roundToInt

//BroadCastReceiver
class WeatherWidgetProvider: AppWidgetProvider() {

    //위치정보를 가져와 날씨 정보를 업데이트하기 위한 메서드
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        //서비스 시작
        ContextCompat.startForegroundService(
            context!!,
            Intent(context,UpdateWidgetService::class.java)
        )
    }

    //Service 정의
    class UpdateWidgetService : LifecycleService(){
        override fun onCreate() {
            super.onCreate()
            createChannelIfNeeded()
            startForeground(
                NOTIFICATION_ID,
                createNotification()
            )
        }

        //Start_STicky : Default값, 시스템에 의해 서비스가 종료될때, 서비스가 가용한 상태가 되면 recreateService
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            //기존에 갱신된 위치정보를 가져옴
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //권한이 없는 경우
                val updateViews = RemoteViews(packageName,R.layout.widget_weather).apply {
                    setTextViewText(R.id.widget_temp_text_view,"권한 없음")
                    setViewVisibility(R.id.widget_city_text_view, View.GONE)
                    setViewVisibility(R.id.widget_description_text_view, View.GONE)
                    setViewVisibility(R.id.widget_max_min_text_view, View.GONE)

                }
                updateWidget(updateViews)
                //서비스 종료
                stopSelf()
                return super.onStartCommand(intent, flags, startId)
            }

            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener { location->
                    lifecycleScope.launch{
                        try {
                            val address = Geocoder(applicationContext).getFromLocation(location.latitude,location.longitude,1)
                            val weather = RetrofitClient.weatherService.getWeather(latitude = location.latitude,longitude = location.longitude)
                            val updateViews = RemoteViews(packageName,R.layout.widget_weather).apply {
                                setViewVisibility(R.id.widget_city_text_view, View.VISIBLE)
                                setViewVisibility(R.id.widget_description_text_view, View.VISIBLE)
                                setViewVisibility(R.id.widget_max_min_text_view, View.VISIBLE)

                                val currentTemp = weather.current.temp.roundToInt()
                                val descriptionWeather = weather.current.weather.first().description
                                val minTemp = weather.daily.first().temp.minTemp.roundToInt()
                                val maxTemp = weather.daily.first().temp.maxTemp.roundToInt()

                                setTextViewText(R.id.widget_city_text_view, address[0].locality)
                                setTextViewText(R.id.widget_temp_text_view,"$currentTemp°")
                                setTextViewText(R.id.widget_description_text_view,"$descriptionWeather")
                                setTextViewText(R.id.widget_max_min_text_view,"최고:$maxTemp° 최저:$minTemp°")
                            }
                            updateWidget(updateViews)
                        }catch (exception : Exception){
                            exception.printStackTrace()
                        }finally {
                            stopSelf()
                        }
                    }
                }
            return super.onStartCommand(intent, flags, startId)
        }

        override fun onDestroy() {
            super.onDestroy()
            //notification 지
            stopForeground(true)
        }

        private fun createChannelIfNeeded(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.createNotificationChannel(
                    NotificationChannel(
                        WIDGET_REFRESH_CHANNEL_ID,
                        "위젯 갱신 채널",
                        NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        }
        private fun createNotification() : Notification =
            NotificationCompat.Builder(this,WIDGET_REFRESH_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_refresh_24)
                .build()
        //위젯 뷰를 갱신
        private fun updateWidget(updateViews:RemoteViews){
            val widgetProvider = ComponentName(this,WeatherWidgetProvider::class.java)
            AppWidgetManager.getInstance(this).updateAppWidget(widgetProvider,updateViews)
        }
    }

    companion object{
        private const val WIDGET_REFRESH_CHANNEL_ID = "WIDGET_REFRESH_CHANNEL_ID"
        private const val NOTIFICATION_ID = 101
    }
}