package com.sardordev.uploaded

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


import com.sardordev.uploaded.databinding.ActivityGoogleAuthBinding

class GoogleAuth : AppCompatActivity() {
    private val binding by lazy { ActivityGoogleAuthBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        binding.login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }


       binding.btnregister.setOnClickListener {
           auth.createUserWithEmailAndPassword(
               binding.edemail.text.toString(),
               binding.edpassword.text.toString()
           ).addOnCompleteListener {task->
               if (task.isSuccessful){
                   Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
               }else{
                   Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
               }
           }
       }

    }
}