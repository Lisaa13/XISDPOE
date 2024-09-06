
package com.example.xisdpoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var redirectReg: TextView
    private lateinit var logEmail: EditText
    private lateinit var logPass: EditText
    private lateinit var loginBtn: Button
    //creating firebaseAuth object
    private lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        //View binding
        redirectReg = findViewById(R.id.RedirectReg)
        loginBtn = findViewById(R.id.LoginBtn)
        logEmail = findViewById(R.id.LogEmail)
        logPass = findViewById(R.id.LogPass)

        //initializing Firebase auth object
        auth = Firebase.auth

        loginBtn.setOnClickListener {
            loginActivity()
        }

        redirectReg.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            //using finish() to end the activity
            finish()
        }
    }

    // Function to handle login
    private fun loginActivity() {
        val email = logEmail.text.toString().trim()
        val password = logPass.text.toString().trim()

        // Validate email and password
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign in with email and password using Firebase auth
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign-in successful
                Toast.makeText(this, "Log In Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Sign-in failed
                Toast.makeText(this, "Log In Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
