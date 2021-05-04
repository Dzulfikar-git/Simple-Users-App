package com.dzulfikar.usersapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzulfikar.usersapp.databinding.ActivityHomeBinding
import com.dzulfikar.usersapp.ui.adduser.AddUserActivity
import com.dzulfikar.usersapp.ui.etc.AboutActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(){

    private var _binding : ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeRecycleViewAdapter: HomeRecycleViewAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setAddUserButtonListener()
        setAboutButtonListener()
        loadUsersData()
        setSearchListener()
    }

    private fun setViewBinding(){
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setSearchListener(){
        binding.homeSearchInput.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    loadUsersData()
                }else {
                    loadSearchedData(s.toString())
                    Log.d("HomeActivity", "SearchName : $s")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun setAddUserButtonListener(){
        binding.homeAddUserFab.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAboutButtonListener(){
        binding.homeAboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUsersData(){
        homeRecycleViewAdapter = HomeRecycleViewAdapter(this)
        homeViewModel.getAllUsers().observe(this, {
            try {
                homeRecycleViewAdapter.setData(it)
                Log.d("HomeActivity", it.toString())
                homeRecycleViewAdapter.notifyDataSetChanged()
            } catch (e: Exception){
                Log.e("HomeActivity", e.toString())
            }
        })

        with(binding.rvHomeUsers){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeRecycleViewAdapter
        }
    }

    private fun loadSearchedData(name: String){
        homeViewModel.getSearchedUsers(name).observe(this, {
            try {
                homeRecycleViewAdapter.setData(it)
                homeRecycleViewAdapter.notifyDataSetChanged()
                Log.d("HomeActivity", "Search : $it")
            } catch (e: Exception){
                Log.e("HomeActivity", e.toString())
            }
        })
    }

}