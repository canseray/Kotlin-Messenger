package com.example.kotlin_messenger.loginregister

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.kotlin_messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



    }

    fun Login (view: View){

        val email = emailText_login.text.toString()
        val password = passwordText_login.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)


    }


    fun BackRegistration (view: View){

        val intent = Intent(this, RegisterActivity::class.java)

    }
}