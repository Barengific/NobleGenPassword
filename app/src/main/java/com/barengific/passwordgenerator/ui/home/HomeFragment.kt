package com.barengific.passwordgenerator.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.barengific.passwordgenerator.CredentialsEnt
import com.barengific.passwordgenerator.MainActivity
import com.barengific.passwordgenerator.R
import com.barengific.passwordgenerator.databinding.FragmentHomeBinding
import com.barengific.passwordgenerator.ui.gallery.GalleryFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
//
//    private var here = 0;

    companion object {
        var heress: Int = 0
        fun getHeres(): Int = heress
        fun setHeres(heress: Int) {
            this.heress = heress
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if(getHeres() == 0){
            setHeres(1)
            Log.d("aaa", "inn hommmee")
            val intent = Intent(this.context, MainActivity::class.java).apply {
//            putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }else if(heress == 1){
            Log.d("aaa", "nottt inn hommmee")
//            val intent = Intent(this.context, MainActivity::class.java).apply {
////            putExtra(EXTRA_MESSAGE, message)
//            }
//            startActivity(intent)
            //setHeres(0)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

