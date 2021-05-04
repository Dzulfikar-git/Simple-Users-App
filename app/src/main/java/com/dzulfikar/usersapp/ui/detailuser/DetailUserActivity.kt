package com.dzulfikar.usersapp.ui.detailuser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dzulfikar.usersapp.R
import com.dzulfikar.usersapp.data.source.entities.Users
import com.dzulfikar.usersapp.databinding.ActivityDetailUserBinding
import com.dzulfikar.usersapp.ui.edituser.EditUserActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }
    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!
    private var userId: Int? = null
    private var menuClicked: Boolean = false
    private val detailUserViewModel : DetailUserViewModel by viewModels()
    private lateinit var detailUserRecycleViewAdapter: DetailUserRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setBackButtonListener()
        setMenuVisibility(menuClicked)
        setMenuButtonListener()
        getIntentData()
        getDetailUserData()
        getRandomUsers()
    }

    private fun setViewBinding(){
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getIntentData(){
        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        Log.d("DetailUser", userId.toString())
    }

    private fun getDetailUserData(){
        detailUserViewModel.getDetailUser(userId!!).observe(this, {
            try {
                setUserDetailData(it)
            } catch (e: Exception){
                Log.d("DetailUser", "Error Data Deleted")
            }
        })

    }

    private fun setUserDetailData(user: Users){
        Glide.with(this)
            .load(Uri.parse(user.profilePic))
            .circleCrop()
            .into(binding.circleImageView)
        binding.detailUserFullName.text = resources.getString(R.string.item_user_name, user.firstName, user.lastName)
        binding.detailUserEmail.text = user.email
        binding.detailUserGender.text = user.gender
        binding.detailUserLocation.text = user.city
        binding.detailUserCalendar.text = user.dateOfBirth
        binding.detailUserPhone.text = user.phoneNumber
        binding.detailUserIntro.text = user.intro
        binding.detailUserIntro.movementMethod = ScrollingMovementMethod()
    }

    private fun setMenuVisibility(state: Boolean){
        if(!state){
            binding.detailUserDeleteButton.visibility = View.GONE
            binding.detailUserEditButton.visibility = View.GONE
        } else {
            binding.detailUserDeleteButton.visibility = View.VISIBLE
            binding.detailUserEditButton.visibility = View.VISIBLE
        }
    }

    private fun setMenuButtonListener(){
        binding.detailUserMenuButton.setOnClickListener {
            menuClicked = if(!menuClicked){
                setMenuVisibility(true)
                true
            } else {
                setMenuVisibility(false)
                false
            }
        }

        binding.detailUserDeleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                    .setTitle("Delete Confirmation!")
                    .setMessage("Do You Want To Delete This User?")
                    .setNegativeButton("Cancel"){ _, _ ->

                    }
                    .setPositiveButton("Delete"){ _, _ ->
                        detailUserViewModel.deleteUser(userId!!)
                        finish()
                    }
                    .show()
        }
        
        binding.detailUserEditButton.setOnClickListener {
            val intent = Intent(this, EditUserActivity::class.java)
            intent.putExtra(EditUserActivity.EXTRA_USER_ID, userId)
            startActivity(intent)
        }

    }

    private fun setBackButtonListener(){
        binding.detailBackButton.setOnClickListener {
            finish()
        }
    }

    private fun getRandomUsers(){
        detailUserRecycleViewAdapter = DetailUserRecycleViewAdapter(this)
        detailUserViewModel.getRandomUsersFiltered(userId!!).observe(this, {
            try {
                detailUserRecycleViewAdapter.setData(it)
                detailUserRecycleViewAdapter.notifyDataSetChanged()
            }
            catch (e: Exception){
                Log.e("DetailUserAct", e.toString())
            }
        })

        with(binding.rvDetailOtherUsers){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = detailUserRecycleViewAdapter

        }
    }

}