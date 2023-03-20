package com.sardordev.uploaded

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import com.sardordev.uploaded.adapter.UserAdapter
import com.sardordev.uploaded.databinding.ActivityAuthBinding
import com.sardordev.uploaded.databinding.Customdialog2Binding
import com.sardordev.uploaded.databinding.CustomdialogBinding
import com.sardordev.uploaded.model.UsersData

class AuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAuthBinding.inflate(layoutInflater) }
    private lateinit var database: DatabaseReference
    private lateinit var userAdapter: UserAdapter
    private var listUser = ArrayList<UsersData>()
    private lateinit var auth : FirebaseAuth
    private var imgUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference(auth.uid.toString())


        Log.d("uiid",auth.uid.toString())

        binding.floating.setOnClickListener {
            customDialog()
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            retriveDataFromDataBase()
        }




        retriveDataFromDataBase()
    }


    private fun retriveDataFromDataBase() {
        binding.progressbar.isVisible = true
        database.addValueEventListener(object : ValueEventListener, UserAdapter.ClickItem {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUser.clear()
                binding.progressbar.isVisible = false
                for (eachUser in snapshot.children) {
                    val user = eachUser.getValue(UsersData::class.java)
                    if (user != null) {
                        listUser.add(user)
                    }
                }
                userAdapter = UserAdapter(listUser, this)
                binding.rvusers.apply {
                    adapter = userAdapter
                    layoutManager = LinearLayoutManager(this@AuthActivity)
                }
                Log.d("list", listUser.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AuthActivity, "Error", Toast.LENGTH_SHORT).show()
            }
            override fun click(usersData: UsersData) {
                val alert = AlertDialog.Builder(this@AuthActivity)
                val view = CustomdialogBinding.inflate(layoutInflater)
                alert.setView(view.root)
                val dialog = alert.show()
                dialog.create()
                view.edage.setText(usersData.age)
                view.edfirstname.setText(usersData.firstName)
                view.edsurname.setText(usersData.surname)
                view.btnSave.setOnClickListener {
                    if (view.edfirstname.text.isNotEmpty() && view.edsurname.text.isNotEmpty() && view.edage.text.isNotEmpty()) {
                        val userMap = mutableMapOf<String, Any>()
                        userMap["id"] = usersData.id
                        userMap["age"] = view.edage.text.toString()
                        userMap["firstName"] = view.edfirstname.text.toString()
                        userMap["surname"] = view.edsurname.text.toString()
                        database.child(usersData.id).updateChildren(userMap).addOnCompleteListener {
                            binding.progressbar.isVisible = false
                            dialog.dismiss()
                        }
                    }
                }
                view.btndelete.setOnClickListener {
                    database.child(usersData.id).removeValue()
                }
            }
        })
    }
    private fun customDialog() {
        startActivity(Intent(this, AddActivity::class.java))
    }


}