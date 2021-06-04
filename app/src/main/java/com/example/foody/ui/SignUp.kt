package com.example.foody.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.foody.R
import com.example.foody.util.FireBaseUtils.firebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var createAccountInputsArray: Array<EditText>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar!!.hide()
        setContentView(R.layout.activity_sign_up)

        val etEmail = findViewById<EditText>(R.id.email_input)
        val etPassword = findViewById<EditText>(R.id.pass)
        val etConfirmPassword = findViewById<EditText>(R.id.rep_pass)
        fun notEmpty(): Boolean = etEmail.text.toString().trim().isNotEmpty() &&
                etPassword.text.toString().trim().isNotEmpty() &&
                etConfirmPassword.text.toString().trim().isNotEmpty()
        fun identicalPassword(): Boolean {
            var identical = false
            if (notEmpty() &&
                etPassword.text.toString().trim() == etConfirmPassword.text.toString().trim()
            ) {
                identical = true
            } else if (!notEmpty()) {
                createAccountInputsArray.forEach { input ->
                    if (input.text.toString().trim().isEmpty()) {
                        input.error = "${input.hint} is required"
                    }
                }
            } else {
                val toast= Toast.makeText(this@SignUp, "passwords are not matching !", Toast.LENGTH_SHORT)
                toast.show()
            }
            return identical
        }
        fun signUp() {
            if (identicalPassword()) {
                // identicalPassword() returns true only  when inputs are not empty and passwords are identical
                userEmail = etEmail.text.toString().trim()
                userPassword = etPassword.text.toString().trim()

                /*create a user*/
                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@SignUp,"created account successfully !",Toast.LENGTH_SHORT).show()
                            //sendEmailVerification()
                            startActivity(Intent(this, Home::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@SignUp,"failed to Authenticate !",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        val signup_btn = findViewById<TextView>(R.id.btn_register)

        signup_btn.setOnClickListener {
            signUp()
        }
        createAccountInputsArray = arrayOf(etEmail,etPassword,etConfirmPassword)
    }


    fun toLogin(view: View) {
        val myIntent = Intent(this@SignUp, SignIn::class.java)
        this@SignUp.startActivity(myIntent)
    }
}