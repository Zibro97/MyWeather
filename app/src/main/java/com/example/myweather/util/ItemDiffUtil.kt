package com.example.myweather.util

import android.util.Log
import androidx.recyclerview.widget.DiffUtil


class ItemDiffUtil private constructor() {

    companion object {
        private var mInstance: ItemDiffUtil? = null

        fun getInstance(): ItemDiffUtil {
            return mInstance ?: synchronized(this) {
                mInstance ?: ItemDiffUtil().also {
                    mInstance = it
                }
            }
        }
    }

    inline fun <reified T> getDiffUtil(cls: Class<T>): DiffUtil.ItemCallback<T>{
        return object: DiffUtil.ItemCallback<T>(){
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                Log.d("ItemDiffUtil","areItemsTheSame is called | oldItem : $oldItem | newItem : $newItem")
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                Log.d("ItemDiffUtil","areContentsTheSame is called | oldItem : $oldItem | newItem : $newItem")
                return oldItem == newItem
            }
        }
    }
}