package com.study.leestagram

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSigninClient: GoogleSignInClient? = null
    val GOOGLE_LOGIN_CODE = 9001

    val email_edittext: EditText by lazy {
        findViewById(R.id.email_edittext)
    }
    val password_edittext: EditText by lazy {
        findViewById(R.id.password_edittext)
    }

    lateinit var registerForActivityResult: ActivityResultLauncher<Intent>

    private val email_login_button: AppCompatButton by lazy {
        findViewById(R.id.email_login_button)
    }

    private val google_sign_in_button: AppCompatButton by lazy {
        findViewById(R.id.google_sign_in_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        email_login_button.setOnClickListener {
            signinAndSignup()
        }
        google_sign_in_button.setOnClickListener {
            googleSignin()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().build()
        googleSigninClient = GoogleSignIn.getClient(this, gso)

    }

    private fun googleSignin() {
        var signInIntent = googleSigninClient?.getSignInIntent()

//        TODO:: intent 호출해서 로그인화면으로 이동
       registerForActivityResult = registerForActivityResult()
//        (signInIntent, GOOGLE_LOGIN_CODE)
    }

    fun signinAndSignup() {
        val email_str = email_edittext.text.toString()
        val password_str = password_edittext.text.toString()
        auth?.createUserWithEmailAndPassword(email_str, password_str)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // create a user account
                    moveMainPage(task.result?.user)
                } else if (!task.exception?.message.isNullOrEmpty()) {
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

    fun signinEmail() {
        val email_str = email_edittext.text.toString()
        val password_str = password_edittext.text.toString()
        auth?.signInWithEmailAndPassword(email_str, password_str)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // create a user account
                    moveMainPage(task.result?.user)
                } else if (!task.exception?.message.isNullOrEmpty()) {
                    // show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                } else {
                    // login if you have account
                    signinEmail()
                }
            }
    }


}