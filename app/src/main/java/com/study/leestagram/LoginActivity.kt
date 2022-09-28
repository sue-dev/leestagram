package com.study.leestagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    val email_edittext: EditText by lazy {
        findViewById(R.id.email_edittext)
    }
    val password_edittext: EditText by lazy {
        findViewById(R.id.password_edittext)
    }

    private val email_login_button: AppCompatButton by lazy {
        findViewById(R.id.email_login_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        email_login_button.setOnClickListener{
            signinAndSignup()
        }
    }

    fun signinAndSignup() {
        val email_str = email_edittext.text.toString()
        val password_str = password_edittext.text.toString()
        auth?.createUserWithEmailAndPassword(email_str, password_str)
            ?.addOnCompleteListener{task ->
                if (task.isSuccessful){
                    // create a user account
                    moveMainPage(task.result?.user)
                } else if (! task.exception?.message.isNullOrEmpty()) {
                    // show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                } else {
                    // login if you have account
                    signinEmail()
                }
            }

    }

    private fun moveMainPage(user: FirebaseUser?) {

    }

    fun signinEmail(){
        val email_str = email_edittext.text.toString()
        val password_str = password_edittext.text.toString()
        auth?.signInWithEmailAndPassword(email_str, password_str)
            ?.addOnCompleteListener{task ->
                if (task.isSuccessful){
                    // create a user account
                    moveMainPage(task.result?.user)
                } else if (! task.exception?.message.isNullOrEmpty()) {
                    // show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                } else {
                    // login if you have account
                    signinEmail()
                }
            }
    }


}