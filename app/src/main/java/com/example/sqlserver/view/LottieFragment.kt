package com.example.sqlserver.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.sqlserver.R
import com.example.sqlserver.databinding.FragmentLottieBinding

class LottieFragment : Fragment() {
    private lateinit var binding: FragmentLottieBinding
    private lateinit var animationView: LottieAnimationView

    private val animationDuration = 5100L // Duración de la animación en milisegundos
    private val showLoginRunnable = Runnable {
        // Ocultar la animación y mostrar el inicio de sesión
        animationView.visibility = View.GONE
        showLoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLottieBinding.inflate(inflater, container, false)

        animationView = binding.animationView
        animationView.playAnimation()

        binding.root.postDelayed(showLoginRunnable, animationDuration)

        return binding.root
    }

    private fun showLoginFragment() {
        Navigation.findNavController(binding.root).navigate(R.id.fragmentLogin)
    }
}
