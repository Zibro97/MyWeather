package com.example.myweather.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(private val layoutId:Int) :Fragment() {

    protected lateinit var binding:VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater,container)
        return binding.root
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater,container: ViewGroup?) : VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeData()
    }

    abstract fun initViews()
    abstract fun observeData()
}