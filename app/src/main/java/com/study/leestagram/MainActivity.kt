package com.study.leestagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.study.leestagram.navigation.AlarmFragment
import com.study.leestagram.navigation.DetailViewFragment
import com.study.leestagram.navigation.GridFragment
import com.study.leestagram.navigation.UserFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    val bottom_navigation: BottomNavigationView by lazy{
        findViewById(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("ITEM", item.toString())
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