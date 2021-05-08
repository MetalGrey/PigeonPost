package com.metalgreystudio.pigeonpost

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.google.android.gms.tasks.Task




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        register_button_reg.setOnClickListener() {
            val email = email_edittext_reg.text.toString()
            val password = password_edittext_reg.text.toString()
            val username = username_edittext_reg.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "please enter text in email or password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Log.d("MainActivity", "Password is: $password")
            Log.d("MainActivity", "Email is: $email")

            val database = FirebaseDatabase.getInstance()
            val userid = UUID.randomUUID().toString()
            val myRef = database.getReference("user/$userid")


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(){
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Activity", "User: ${it.result}")
                    uploadImageToFirebaseStorage()
                    //myRef.setValue(email, userid)

                }
                .addOnFailureListener() {
                    Log.d("MainActivity", "Failed create user ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        already_have_account_reg.setOnClickListener(){
            Log.d("MainActivity", "Already have Account")

            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)

        }

        select_image_reg.setOnClickListener(){
            Log.d("MainActivity", "download photo")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    var selectedPhotoURL: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoURL = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoURL)
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            select_image_reg.setBackgroundDrawable(bitmapDrawable)
//            select_image_reg.text = ""
            selectphoto_reg.setImageBitmap(bitmap)
            select_image_reg.alpha = 0f
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoURL == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/users/$filename")


        ref.putFile(selectedPhotoURL!!)
                .addOnSuccessListener {
                    Log.d("MainActivity", "successfully load ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d("Download1", "$it")
                        saveUserToFirebase(it.toString())
                    }

                }


    }
    private fun saveUserToFirebase(profile_image: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username_edittext_reg.text.toString(), email_edittext_reg.text.toString(), profile_image)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("MainActivity", "User saved to database, $user")
                    val intent = Intent(this, LatestMessagsesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
    }

}
data class User(val uid: String? = null, val username: String? = null, val email: String? = null, val profile_image: String? = null) {
    constructor(): this("", "", "", "")

}
