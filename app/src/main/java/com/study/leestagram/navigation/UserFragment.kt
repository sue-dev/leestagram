package com.study.leestagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.study.leestagram.BaseFragment
import com.study.leestagram.R
import com.study.leestagram.databinding.FragmentUserBinding

class UserFragment : BaseFragment<FragmentUserBinding>(FragmentUserBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    inner class UserFragmentRecyclerAdaptor: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        init {
            firestore?.collection("contents").
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

    }



}