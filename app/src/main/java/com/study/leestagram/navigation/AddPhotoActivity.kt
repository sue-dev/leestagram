package com.study.leestagram.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.study.leestagram.R
import com.study.leestagram.navigation.model.ContentDTO
import java.sql.Timestamp
import java.text.SimpleDateFormat

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth :FirebaseAuth? = null
    var firestore :FirebaseFirestore? = null

    val add_photo_image : ImageView by lazy {
        findViewById(R.id.add_photo_image)
    }

    val add_photo_edit_description : EditText by lazy {
        findViewById(R.id.add_photo_edit_description)
    }

    val add_photo_btn_upload : Button by lazy {
        findViewById(R.id.add_photo_btn_upload)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == RESULT_OK){
                // result.data = Intent임
                photoUri = result.data?.data
                add_photo_image.setImageURI(photoUri)
            }
        }.launch(intent)

        add_photo_btn_upload.setOnClickListener { contentUpload() }

    }

    private fun contentUpload() {
        val timestamp: String = System.currentTimeMillis().toString()
        var storagePath = storage?.reference?.child("images")?.child(timestamp)
        storagePath?.putFile(photoUri!!)
            ?.addOnCompleteListener{ task->
                if(task.isSuccessful){
                    storagePath.downloadUrl.addOnSuccessListener { uri->
                        var contentDTO = ContentDTO()
                        contentDTO.imageUrl = uri.toString()
                        contentDTO.uid = auth?.currentUser?.uid
                        contentDTO.userId = auth?.currentUser?.email
                        contentDTO.description = add_photo_edit_description.text.toString()
                        contentDTO.timestamp = System.currentTimeMillis()

                        firestore?.collection("contents")?.document()?.set(contentDTO)

                        Toast.makeText(this, "업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
    }
}
