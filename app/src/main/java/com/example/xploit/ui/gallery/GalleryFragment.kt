package com.example.xploit.ui.gallery

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.xploit.R
import com.example.xploit.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val context = requireContext()

        binding.ibAddPlaylist.setOnClickListener {
            if (binding.clAddPlaylist.visibility == View.GONE) {
                binding.ibAddPlaylist.setImageDrawable(ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_remove_24
                ))
                binding.clAddPlaylist.visibility = View.VISIBLE
            } else {
                binding.ibAddPlaylist.setImageDrawable(ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_add_24
                ))
                binding.clAddPlaylist.visibility = View.GONE
            }
        }

        binding.ibVk.setOnClickListener {
            val Intent = Intent(context, ImportPlaylistActivity::class.java)
            startActivity(Intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}