package com.example.foody.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.foody.R
import com.example.foody.util.FireBaseUtils.firebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }


    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            val user: FirebaseUser? = firebaseAuth.currentUser
            if(user !=null) {
                user?.let {
                    startActivity(Intent(this, Home::class.java))
                    Toast.makeText(this@MainActivity,"Welcome Back !",Toast.LENGTH_SHORT).show()
                }
            }else {
                startActivity(Intent(this, SignIn::class.java))
            }
            finish()
        }, 3000)
    }

}
