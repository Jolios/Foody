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

class SignIn : AppCompatActivity() {



    lateinit var signInEmail: String
    lateinit var signInPassword: String
    lateinit var signInInputsArray: Array<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        val etSignInEmail = findViewById(R.id.email_input) as EditText
        val etSignInPassword = findViewById(R.id.pass) as EditText

        signInInputsArray = arrayOf(etSignInEmail,etSignInPassword)

        val signInbtn = findViewById(R.id.lgn_btn) as TextView


        /* check if fields are empty */
        fun notEmpty(): Boolean = signInEmail.isNotEmpty() && signInPassword.isNotEmpty()


        fun signInUser() {
            signInEmail = etSignInEmail.text.toString().trim()
            signInPassword = etSignInPassword.text.toString().trim()

            if (notEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword)
                    .addOnCompleteListener { signIn ->
                        if (signIn.isSuccessful) {
                            startActivity(Intent(this, Home::class.java))
                            Toast.makeText(this@SignIn,"signed in successfully",Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@SignIn,"sign in failed",Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                signInInputsArray.forEach { input ->
                    if (input.text.toString().trim().isEmpty()) {
                        input.error = "${input.hint} is required"
                    }
                }
            }
        }



        signInbtn.setOnClickListener {
            signInUser()
        }
    }



    fun ToRegister(view: View) {
        val myIntent = Intent(this@SignIn, SignUp::class.java)
        this@SignIn.startActivity(myIntent)
    }
}