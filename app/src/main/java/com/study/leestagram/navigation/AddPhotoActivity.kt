package com.study.leestagram.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.study.leestagram.R
import java.sql.Timestamp
import java.text.SimpleDateFormat

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null

    val add_photo_image : ImageView by lazy {
        findViewById(R.id.add_photo_image)
    }

    val add_photo_btn_upload : Button by lazy {
        findViewById(R.id.add_photo_btn_upload)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        // TODO:: FIREBASE STORAGE 이용 이미지 업로드 기능
        // 1. 앨범에서 사진 가져오기
        // 2. firebase storage에 사진 업로드하기

        storage = FirebaseStorage.getInstance()

        // 1. 앨범
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

    //2. 사진 업로드
    private fun contentUpload() {
        val timestamp: String = System.currentTimeMillis().toString()
        var storagePath = storage?.reference?.child("images")?.child(timestamp)
        storagePath?.putFile(photoUri!!)
            ?.addOnCompleteListener{ task->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    Toast.makeText(this, "업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
