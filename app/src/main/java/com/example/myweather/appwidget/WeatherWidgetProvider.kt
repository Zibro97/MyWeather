package com.example.myweather.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

//BroadCastReceiver
class WeatherWidgetProvider: AppWidgetProvider() {

    //위치정보를 가져와 날씨 정보를 업데이트하기 위한 메서드
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}