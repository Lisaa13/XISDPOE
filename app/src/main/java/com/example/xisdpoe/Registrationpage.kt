package com.example.xisdpoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.xisdpoe.R.layout.activity_registrationpage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var register: Button
    private lateinit var textView3: TextView

    // Create Firebase authentication and database references
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrationpage)

        // View Bindings
        name = findViewById(R.id.RegName)
        email = findViewById(R.id.emailReg)
        password = findViewById(R.id.passReg)
        register = findViewById(R.id.buttonRegister)
        textView3 = findViewById(R.id.textView3)

        // Initialising auth and database references
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Register button click listener
        register.setOnClickListener {
            registrationActivity()  // Ensure this function is defined in the class
        }

        // Switch to Login Page
        textView3.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // Define the registrationActivity function as a member function
    private fun registrationActivity() {
        val nameText = name.text.toString().trim()
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        // Check email and password
        if (nameText.isBlank() || emailText.isBlank() || passwordText.isBlank()) {
            Toast.makeText(this, "Name, Email, and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        // Register the user
        auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                // Successfully registered
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                val user = auth.currentUser
                user?.let {
                    // Save user details to Realtime Database
                    val userDetails = User(it.uid, nameText, emailText)
                    database.child("users").child(it.uid).setValue(userDetails)
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to save user details: ${exception.message}", Toast.LENGTH_SHORT).show()
                            Log.e("RegistrationActivity", "Failed to save user details", exception)
                        }
                }
            } else {
                handleRegistrationError(task.exception)
            }
        }.addOnFailureListener { exception ->
            handleRegistrationError(exception)
        }
    }

    // Handle registration errors
    private fun handleRegistrationError(exception: Exception?) {
        exception?.let {
            when (it) {
                is FirebaseAuthUserCollisionException -> {
                    Toast.makeText(this, "This email address is already in use.", Toast.LENGTH_SHORT).show()
                    Log.e("RegistrationActivity", "Email already in use", it)
                }
                else -> {
                    Toast.makeText(this, "Registration Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    Log.e("RegistrationActivity", "Registration failed", it)
                }
            }
        } ?: run {
            Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
            Log.e("RegistrationActivity", "Registration failed with unknown error")
        }
    }

    // User data class to hold user information
    data class User(val uid: String, val name: String, val email: String)
}