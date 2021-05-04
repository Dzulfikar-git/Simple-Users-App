package com.dzulfikar.usersapp.ui.edituser

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dzulfikar.usersapp.R
import com.dzulfikar.usersapp.data.source.entities.Users
import com.dzulfikar.usersapp.databinding.ActivityEditUserBinding
import com.dzulfikar.usersapp.utils.Utility
import com.dzulfikar.usersapp.utils.Utility.checkInput
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    private var _binding : ActivityEditUserBinding? = null
    private val binding get() = _binding!!
    private val editUserViewModel: EditUserActivityViewModel by viewModels()
    private lateinit var imageUri: Uri
    private var userDetails: Users? = null
    private var uid: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        dropDownOptions()
        getIntentData()
        setDefaultImage()
        setBackButtonListener()
        datePickerListener()
        saveFabListener()
        addImageListener()
    }

    private fun setViewBinding(){
        _binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getIntentData(){
        uid = intent.getIntExtra(EXTRA_USER_ID, 0)
        Log.d("Edit", uid.toString())
        getUserDetails()
    }

    private fun getUserDetails(){
        editUserViewModel.getUser(uid!!).observe(this, {
            userDetails = it
            imageUri = Uri.parse(it.profilePic)
            setUserDetails()
        })
    }

    private fun setUserDetails(){
        binding.editFirstNameInput.editText?.setText(userDetails?.firstName)
        binding.editLastNameInput.editText?.setText(userDetails?.lastName)
        binding.editEmailInput.editText?.setText(userDetails?.email)

        binding.editGenderText.setText(userDetails?.gender, false)
        binding.editGenderText.freezesText = false

        binding.editDateOfBirthInput.editText?.setText(userDetails?.dateOfBirth)
        binding.editPhoneNumberInput.editText?.setText(userDetails?.phoneNumber)
        binding.editCityInput.editText?.setText(userDetails?.city)
        binding.editIntroductionInput.editText?.setText(userDetails?.intro)

        Glide.with(this)
            .load(imageUri)
            .circleCrop()
            .into(binding.editProfilePic)
    }

    private fun setBackButtonListener(){
        binding.editBackButton.setOnClickListener {
            finish()
        }
    }

    private fun datePickerListener(){
        binding.editDateOfBirthInput.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                binding.editDateOfBirthInput.editText?.setText(Utility.epochToDateConverter(it))
                binding.editDateOfBirthInput.editText?.error = null
            }

        }
    }

    private fun dropDownOptions(){
        val items = listOf("Male", "Female")
        val adapter = ArrayAdapter(this, R.layout.item_gender_list, items)
        (binding.editGenderInput.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setDefaultImage(){
        imageUri =  Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.ic_person_vector))
            .appendPath(resources.getResourceTypeName(R.drawable.ic_person_vector))
            .appendPath(resources.getResourceEntryName(R.drawable.ic_person_vector))
            .build()

        Glide.with(this)
            .load(imageUri)
            .circleCrop()
            .into(binding.editProfilePic)

        Log.d("home", imageUri.toString())
    }

    private fun saveFabListener(){
        binding.editFabSaveData.setOnClickListener {
            checkAndSave()
        }
    }

    private fun checkAndSave(){
        val firstName = binding.editFirstNameInput.editText
        val lastName = binding.editLastNameInput.editText
        val email = binding.editEmailInput.editText
        val gender = binding.editGenderInput.editText
        val dateOfBirth = binding.editDateOfBirthInput.editText
        val phoneNumber = binding.editPhoneNumberInput.editText
        val city = binding.editCityInput.editText
        val intro = binding.editIntroductionInput.editText

        if(checkInput(firstName) && checkInput(lastName) && checkInput(email) && checkInput(gender) && checkInput(dateOfBirth) && checkInput(phoneNumber) && checkInput(city) && checkInput(intro)) {
            Log.d("AddUserActivity", "Not Null")
            val user = Users(
                uid = uid!!,
                firstName = firstName?.text.toString(),
                lastName = lastName?.text.toString(),
                email = email?.text.toString(),
                gender = gender?.text.toString(),
                dateOfBirth = dateOfBirth?.text.toString(),
                phoneNumber = phoneNumber?.text.toString(),
                city = city?.text.toString(),
                intro = intro?.text.toString(),
                profilePic = imageUri.toString()
            )
            editUserViewModel.saveNewData(user)
            Toast.makeText(this, "User Has Been Updated", Toast.LENGTH_LONG).show()
        }
    }

    private fun addImageListener(){
        val launchFilesActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                // your operation...
                binding.editProfilePic.setImageURI(imageUri)
                Log.d("Home", imageUri.toString())
            }
        }
        binding.editAddImageBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            launchFilesActivity.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}