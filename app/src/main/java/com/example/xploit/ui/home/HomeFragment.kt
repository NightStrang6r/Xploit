package com.example.xploit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xploit.R
import com.example.xploit.api.ApiResp
import com.example.xploit.api.RetrofitInstance
import com.example.xploit.api.Track
import com.example.xploit.databinding.FragmentHomeBinding
import com.example.xploit.ui.track.MusicListAdapter
import com.example.xploit.ui.track.MusicModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        // TrackList //
        val rvList = binding.rvList

        val adapter = MusicListAdapter(requireContext())

        val layoutManager = LinearLayoutManager(requireContext())

        rvList.adapter = adapter
        rvList.layoutManager = layoutManager

        val trackList = mutableListOf<MusicModel>()


        fun getPopularPlaylist() {
            RetrofitInstance.api.getPopularPlaylist().enqueue(object : Callback<ApiResp> {
                override fun onResponse(call: Call<ApiResp>, response: Response<ApiResp>) {
                    if(response.isSuccessful){
                        response.body()?.items?.forEach { it ->
                            trackList.add(MusicModel(
                                it.title,
                                it.artist,
                                it.duration,
                                it.image))
                        }
                        adapter.setData(trackList)
                    } else {
                        binding.tvStatusText.text = "Не удалось получить список песен (code: resp)."
                    }
                }

                override fun onFailure(call: Call<ApiResp>, t: Throwable) {
                    binding.tvStatusText.text = "Не удалось получить список песен (code: fail)."
                }
            })
        }

        getPopularPlaylist()
        // TrackList //

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}