package com.example.myweather.domain.util

sealed class State<out R>{
    object Loading :State<Nothing>()
    data class Success<out T>(val data : T) : State<T>()
    data class Error(val exception:Exception) :State<Nothing>()
}

fun <T> State<T>.getValue():T{
    return if(this is State.Success){
        this.data
    } else{
        throw (this as State.Error).exception
    }
}