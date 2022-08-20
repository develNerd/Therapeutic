package com.flepper.therapeutic.android.presentation.core

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    val binding: VB
        get() = _binding!!
    lateinit var supportFragmentManager: FragmentManager

    private var _binding: VB? = null

    abstract fun initUI()

    abstract fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initViewBinding(inflater, container)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportFragmentManager =  requireActivity().supportFragmentManager
        initUI()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    protected open fun navigate(@IdRes actionId: Int, args: Bundle? = null) {
        findNavController().navigate(actionId, args)
    }



}