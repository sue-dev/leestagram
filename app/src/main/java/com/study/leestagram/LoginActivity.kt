package com.study.leestagram

import android.R.attr.data
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


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

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("1071232915052-avpenq3a3r9f6ej1ir376dhan6394b4l.apps.googleusercontent.com").build()

        googleSigninClient = GoogleSignIn.getClient(this, gso)
        var googleSignInIntent: Intent? = googleSigninClient?.getSignInIntent()
        var resultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result:ActivityResult ->
                Log.d("RESULT", result.toString())
                if(result.resultCode == RESULT_OK){
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult()
                    Log.d("RESULT DATA: account", account.toString())
                    Log.d("RESULT DATA: task ", task.toString())

                    firebaseAuthWithGoogle(account)
                }
            }
        google_sign_in_button.setOnClickListener {
            resultLauncher.launch(googleSignInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        var credential = GoogleAuthProvider.getCredential(account?.getIdToken(), null)
        auth?.signInWithCredential(credential)
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
        if (user != null){
            startActivity(Intent(this, MainActivity::class.java))
        }

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