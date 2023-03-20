package com.sardordev.uploaded

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sardordev.uploaded.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestone: FirebaseFirestore
    private var imgUri: Uri? = null
    private var mList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initVars()
        registerClickEvents()
        binding.buttonshow.setOnClickListener {
            getImages()
        }
    }

    private fun registerClickEvents() {
        binding.buttonupload.setOnClickListener {
            uploadImage()
        }
        binding.buttonshow.setOnClickListener {

        }
        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    /*
    1.Save to Storage
    2.Save to FireStore
     */
    private fun uploadImage() {
        binding.progressbar.isVisible = true
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        imgUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { url ->
                        val map = HashMap<String, Any>()
                        map["pic"] = url.toString()
                        firebaseFirestone.collection("images").add(map).addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                                    binding.progressbar.isVisible = false
                                } else {
                                    Toast.makeText(this, "Uploaded Error", Toast.LENGTH_SHORT).show()
                                    binding.progressbar.isVisible = false
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestone = FirebaseFirestore.getInstance()
    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imgUri = it
        binding.imageView.setImageURI(imgUri)
    }
    private fun getImages() {
        firebaseFirestone.collection("images").get().addOnSuccessListener {
            for (i in it) {
                mList.add(i.data["pic"].toString())
            }
            Log.d("VVV", mList.toString())
        }
    }







}