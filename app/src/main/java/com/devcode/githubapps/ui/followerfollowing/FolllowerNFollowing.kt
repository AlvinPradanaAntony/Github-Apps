package com.devcode.githubapps.ui.followerfollowing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devcode.githubapps.R
import com.devcode.githubapps.databinding.FragmentFolllowerNFollowingBinding

class FolllowerNFollowing : Fragment() {
    private var _binding: FragmentFolllowerNFollowingBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolllowerNFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLabel: TextView = view.findViewById(R.id.section_label)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        tvLabel.text = getString(R.string.content_tab_text, index)
    }
}