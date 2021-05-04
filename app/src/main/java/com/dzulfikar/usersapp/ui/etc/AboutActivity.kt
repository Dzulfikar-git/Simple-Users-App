package com.dzulfikar.usersapp.ui.etc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dzulfikar.usersapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private var _binding : ActivityAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setBackButtonListener()
    }

    private fun setViewBinding(){
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setBackButtonListener(){
        binding.aboutBackButton.setOnClickListener {
            finish()
        }
    }

}