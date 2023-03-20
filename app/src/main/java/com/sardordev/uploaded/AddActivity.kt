package com.sardordev.uploaded

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sardordev.uploaded.databinding.ActivityAddBinding
import com.sardordev.uploaded.model.UsersData

class AddActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var storageRef: StorageReference
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    private lateinit var auth : FirebaseAuth
    private var imgUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        initDb()

        binding.imggallery.setOnClickListener {
            resultLauncher.launch("image/*")
        }


        binding.btnSave.setOnClickListener {
            saveUser()
        }


    }

    private fun saveUser() {
        uploadImg()
    }

    private fun uploadImg() {
        binding.progressbar.isVisible = true
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        imgUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { url ->
                        val id = database.push().key.toString()
                        val userData = UsersData(
                            id,
                            binding.edfirstname.text.toString(),
                            binding.edsurname.text.toString(),
                            binding.edage.text.toString(),
                            url.toString(),
                            false
                        )
                        database.child(id).setValue(userData).addOnSuccessListener {
                            binding.progressbar.isVisible = false
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }


    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imgUri = it
        binding.imggallery.setImageURI(imgUri)
    }

    private fun initDb() {
        database = FirebaseDatabase.getInstance().getReference(auth.uid.toString())
        storageRef = FirebaseStorage.getInstance().reference.child("ImagesUser")
    }

}