package com.example.myweather.presentation.util

//SwipeController에 액션을 전달하기 위한 추상 클래스
abstract class SwipeControllerActions {

    open fun onRightClicked(position: Int) {}
}