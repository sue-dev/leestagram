package com.study.leestagram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.firestore.FirebaseFirestore

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding> (
    private val inflate: Inflate<VB>
): Fragment() {

    private var _binding : VB? = null
    val binding get() = _binding !!
    var firestore: FirebaseFirestore? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("[BaseFragment]", "onCreateView() called")
        _binding = inflate.invoke(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}