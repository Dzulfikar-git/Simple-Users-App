package com.dzulfikar.usersapp.ui.adduser

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
import com.dzulfikar.usersapp.databinding.ActivityAddUserBinding
import com.dzulfikar.usersapp.utils.Utility.checkInput
import com.dzulfikar.usersapp.utils.Utility.epochToDateConverter
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddUserActivity : AppCompatActivity() {
    private var _binding : ActivityAddUserBinding? = null
    private val binding get() = _binding!!
    private val addUserViewModel: AddUserViewModel by viewModels()
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setBackButtonListener()
        datePickerListener()
        dropDownOptions()
        saveFabListener()
        setDefaultImage()
        addImageListener()
    }

    private fun setViewBinding(){
        _binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setBackButtonListener(){
        binding.newBackButton.setOnClickListener {
            finish()
        }
    }

    private fun datePickerListener(){
        binding.newDateOfBirthInput.setEndIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                binding.newDateOfBirthInput.editText?.setText(epochToDateConverter(it))
                binding.newDateOfBirthInput.editText?.error = null
            }

        }
    }

    private fun dropDownOptions(){
        val items = listOf("Male", "Female")
        val adapter = ArrayAdapter(this, R.layout.item_gender_list, items)
        (binding.newGenderInput.editText as? AutoCompleteTextView)?.setAdapter(adapter)
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
                .into(binding.newProfilePic)

        Log.d("home", imageUri.toString())
    }

    private fun saveFabListener(){
        binding.newFabSaveData.setOnClickListener {
            checkAndSave()
        }
    }

    private fun checkAndSave(){
        val firstName = binding.newFirstNameInput.editText
        val lastName = binding.newLastNameInput.editText
        val email = binding.newEmailInput.editText
        val gender = binding.newGenderInput.editText
        val dateOfBirth = binding.newDateOfBirthInput.editText
        val phoneNumber = binding.newPhoneNumberInput.editText
        val city = binding.newCityInput.editText
        val intro = binding.newIntroductionInput.editText

        if(checkInput(firstName) && checkInput(lastName) && checkInput(email) && checkInput(gender) && checkInput(dateOfBirth) && checkInput(phoneNumber) && checkInput(city) && checkInput(intro)){
            Log.d("AddUserActivity", "Not Null")
            val user = Users(
                    uid = 0,
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
            addUserViewModel.addNewUser(user)
            resetInput()
            Toast.makeText(this, "User Has Been Added", Toast.LENGTH_LONG).show()
        }
    }

    private fun resetInput(){
        binding.newFirstNameInput.editText?.text = null
        binding.newLastNameInput.editText?.text = null
        binding.newEmailInput.editText?.text = null
        binding.newGenderInput.editText?.text = null
        binding.newDateOfBirthInput.editText?.text = null
        binding.newPhoneNumberInput.editText?.text = null
        binding.newCityInput.editText?.text = null
        binding.newIntroductionInput.editText?.text = null
        setDefaultImage()
    }

    private fun addImageListener(){
        val launchFilesActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                // your operation...
                binding.newProfilePic.setImageURI(imageUri)
                Log.d("Home", imageUri.toString())
            }
        }
        binding.newAddImageBtn.setOnClickListener {
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