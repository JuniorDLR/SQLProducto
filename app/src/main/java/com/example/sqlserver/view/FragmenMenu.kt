package com.example.sqlserver.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.sqlserver.R
import com.example.sqlserver.databinding.FragmentFragmenMenuBinding

class FragmenMenu : Fragment() {
    private lateinit var binding: FragmentFragmenMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFragmenMenuBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)


        return binding.root
    }

}