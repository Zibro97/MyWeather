package com.example.myweather.view.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myweather.R
import com.example.myweather.databinding.InsertFavoriteDialogCustomBinding
import com.example.myweather.viewmodel.FavoriteViewModel

class CustomDialog : DialogFragment() {
    //Dialog 바인딩 객체
    private var _binding : InsertFavoriteDialogCustomBinding? = null
    private val binding get() = _binding!!
    private val args:CustomDialogArgs by navArgs()
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertFavoriteDialogCustomBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            val location = args.location
            var city:String? = null
            location.title.split(" ").forEach{ s->
                if(s.contains("시")||s.contains("군")) city = s
            }
            binding.confirmInsertText.text = "${city}을(를) 추가하시겠습니까?"
            //네 버튼
            insertButton.setOnClickListener {
                viewModel.insertLocation(context = root.context,location = city!!,latitude = location.point.y,longitude = location.point.x)
                dismiss()
            }
            //아니오 버튼
            noInsertButton.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}