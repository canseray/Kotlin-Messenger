package com.example.kotlin_messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.register_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_main)

    }





    //register button
    fun Register (view: View) {

        val email = emailText_register.text.toString()
        val password = passwordText_register.text.toString()

        if ( email.isEmpty() || password.isEmpty() ){
            Toast.makeText(this,"Please enter email/password",Toast.LENGTH_LONG).show()
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful)
                    return@addOnCompleteListener

                //else if - successfull
                Log.d("Main","successfully created user")

                uploadImageToFirebaseStorage()
            }

            .addOnFailureListener{
                Toast.makeText(this,"error entry",Toast.LENGTH_LONG).show()
            }
    }






    //already have a account button
    fun SignIn (view: View){

       val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }




    private fun saveUserToFirebaseDatabase(profileImageUrl: String){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val referance = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, usernameText_register.text.toString(),profileImageUrl )

        referance.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterAcvtivity","user saved to firebase db")
            }

    }




    private fun uploadImageToFirebaseStorage(){

        if (selectedPhotoUri != null)
            return

        val fileName = UUID.randomUUID().toString()
        val referance = FirebaseStorage.getInstance().getReference("/images/$fileName")
        referance.putFile(selectedPhotoUri!!)

            .addOnSuccessListener {
                Log.d("RegisterAcvtivity","photo uploaded")

                Toast.makeText(this,"uploaded",Toast.LENGTH_LONG).show()

                saveUserToFirebaseDatabase(it.toString())
            }

    }





    //select photo button
    fun SelectPhoto (view: View){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }





    private var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)

            selectedPhoto_immageView_register.setImageBitmap(bitmap)
            selectPhotoButton.alpha = 0f

            //SelectPhoto.setBackgroundDrawable(bitmapDrawable)
            //SelectPhoto.setText("")

        }
    }

    class User (val uid: String, val username: String, val profileImageUrl: String )
}
