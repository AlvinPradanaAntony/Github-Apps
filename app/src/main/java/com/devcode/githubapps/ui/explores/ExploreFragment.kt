package com.devcode.githubapps.ui.explores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devcode.githubapps.R
import com.devcode.githubapps.databinding.FragmentExploreBinding
import com.devcode.githubapps.databinding.FragmentHomeBinding
import com.devcode.githubapps.databinding.FragmentProfileBinding


class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }
}