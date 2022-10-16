package com.study.leestagram.navigation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.study.leestagram.R

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null

    val add_photo_image : ImageView by lazy {
        findViewById(R.id.add_photo_image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        // TODO:: FIREBASE STORAGE 이용 이미지 업로드 기능
        // 1. 앨범에서 사진 가져오기
        // 2. firebase storage에 사진 업로드하기

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

    }
}
