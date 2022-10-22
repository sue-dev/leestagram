package com.study.leestagram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.study.leestagram.databinding.ActivityMainBinding
import com.study.leestagram.navigation.*

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        binding.bottomNavigation.setOnItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.action_home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
         R.id.action_home ->{
             var detailViewFragment = DetailViewFragment()
             supportFragmentManager.beginTransaction().replace(R.id.main_content, detailViewFragment).commit()
             return true
         }
         R.id.action_search ->{
             var gridFragment = GridFragment()
             supportFragmentManager.beginTransaction().replace(R.id.main_content, gridFragment).commit()
             return true
         }
         R.id.action_add_photo ->{
             if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(this, AddPhotoActivity::class.java))
             } else {
                 Toast.makeText(this, "권한이 없습니다.",Toast.LENGTH_SHORT).show()
             }
             return true
         }
         R.id.action_favorite_alarm ->{
             var alarmFragment = AlarmFragment()
             supportFragmentManager.beginTransaction().replace(R.id.main_content, alarmFragment).commit()
             return true
         }
         R.id.action_account ->{
             var userFragment = UserFragment()
             supportFragmentManager.beginTransaction().replace(R.id.main_content, userFragment).commit()
             return true
         }
        }
        return false
    }
}