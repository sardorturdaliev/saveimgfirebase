package com.sardordev.uploaded

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sardordev.uploaded.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()



        binding.btnlog.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.edemail.text.toString(),
                binding.edpassword.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    startActivity(Intent(this, AuthActivity::class.java))
                    Toast.makeText(this, auth.uid.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }






    }
}