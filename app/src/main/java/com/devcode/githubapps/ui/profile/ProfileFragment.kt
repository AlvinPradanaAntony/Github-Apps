package com.devcode.githubapps.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devcode.githubapps.R
import com.devcode.githubapps.databinding.FragmentHomeBinding
import com.devcode.githubapps.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
   private var _binding: FragmentProfileBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      _binding = FragmentProfileBinding.inflate(inflater, container, false)
      return binding.root
   }

}