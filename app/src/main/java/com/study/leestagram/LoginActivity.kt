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
import com.study.leestagram.databinding.ActivityLoginBinding
import com.study.leestagram.databinding.ActivityMainBinding


class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var googleSigninClient: GoogleSignInClient? = null
    val GOOGLE_LOGIN_CODE = 9001

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.emailLoginButton.setOnClickListener {
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
        binding.googleSignInButton.setOnClickListener {
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
        val email_str = binding.emailEdittext.text.toString()
        val password_str = binding.passwordEdittext.text.toString()

        auth?.createUserWithEmailAndPassword(email_str, password_str)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // create a user account
                    moveMainPage(task.result?.user)
                } else if (!task.exception?.message.isNullOrEmpty()) {
                    if(task.exception?.message!!.contains("already")){
                        signinEmail()
                    }else{
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
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
        val email_str = binding.emailEdittext.text.toString()
        val password_str = binding.passwordEdittext.text.toString()
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