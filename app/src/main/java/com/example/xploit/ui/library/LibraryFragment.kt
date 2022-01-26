package com.example.xploit.ui.library

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xploit.R
import com.example.xploit.databinding.FragmentLibraryBinding
import com.example.xploit.ui.playlist.PlaylistAdapter
import com.example.xploit.ui.playlist.PlaylistModel

class LibraryFragment : Fragment() {

    private lateinit var galleryViewModel: LibraryViewModel
    private var _binding: FragmentLibraryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(LibraryViewModel::class.java)

        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
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

        binding.ibSpotify.setOnClickListener {
            val myToast = Toast.makeText(requireContext(), "Coming soon...", Toast.LENGTH_SHORT)
            myToast.show()
        }

        // Playlist //
        val rvList = binding.rvPlaylists
        val adapter = PlaylistAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())

        rvList.adapter = adapter
        rvList.layoutManager = layoutManager

        var playlist = mutableListOf<PlaylistModel>()

        val sharedPref = activity?.getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)
        var VkID = sharedPref?.getString("VkID", "0")?.toInt()
        Log.d("devlog", "VKID >> $VkID")
        if(VkID != 0) {
            Log.d("devlog", "cant find VkID")
            playlist.add(PlaylistModel(
                "VK Favorites",
                "",
                1
            ))
            adapter.addData(playlist)
        }

        if(playlist.isNotEmpty()){
            binding.tvNothing.visibility = View.GONE
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}