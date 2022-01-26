package com.example.xploit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TrackList //
        val rvList = binding.rvList
        val etSearch = binding.etSearch
        val pbListLoading = binding.pbListLoading
        val adapter = MusicListAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())

        rvList.adapter = adapter
        rvList.layoutManager = layoutManager

        var trackList = mutableListOf<MusicModel>()

        fun getPopularPlaylist() {
            pbListLoading.visibility = View.VISIBLE
            RetrofitInstance.api.getPopularPlaylist().enqueue(object : Callback<ApiResp> {
                override fun onResponse(call: Call<ApiResp>, response: Response<ApiResp>) {
                    if(response.isSuccessful){
                        if(response.body()?.error == true) {
                            Msg("Не удалось получить список песен (code: resp).")
                        }
                        response.body()?.items?.forEach { it ->
                            trackList.add(MusicModel(
                                it.title,
                                it.artist,
                                it.duration,
                                it.image,
                                it.url,
                                2))
                        }
                        adapter.setData(trackList)
                        pbListLoading.visibility = View.INVISIBLE
                    } else {
                        binding.tvStatusText.text = "Не удалось получить список песен (code: resp)."
                        pbListLoading.visibility = View.INVISIBLE
                    }
                }

                override fun onFailure(call: Call<ApiResp>, t: Throwable) {
                    binding.tvStatusText.text = "Не удалось получить список песен (code: fail)."
                    pbListLoading.visibility = View.INVISIBLE
                }
            })
        }

        getPopularPlaylist()

        etSearch.addTextChangedListener {
            binding.tvStatusText.text = ""
            pbListLoading.visibility = View.VISIBLE
            if (etSearch.text.toString().isEmpty()){
                getPopularPlaylist()
            } else {
                RetrofitInstance.api.getTrackListBySearch(etSearch.text.toString())
                    .enqueue(object : Callback<ApiResp> {
                        override fun onResponse(call: Call<ApiResp>, response: Response<ApiResp>) {
                            // Если фрагмент был закрыт, binding не существует, выходим
                            if (binding == null){
                                return
                            }

                            if (response.isSuccessful) {
                                binding.tvStatusText.text = ""
                                trackList.clear()
                                if (response.body()?.items == null) {
                                    binding.tvStatusText.text = "По запросу ${etSearch.text} ничего не найдено"
                                } else {
                                    response.body()?.items?.forEach { it ->
                                        trackList.add(MusicModel(
                                            it.title,
                                            it.artist,
                                            it.duration,
                                            it.image,
                                            it.url,
                                            1))
                                    }
                                }
                                adapter.setData(trackList)
                                pbListLoading.visibility = View.INVISIBLE
                            } else {
                                trackList.clear()
                                adapter.setData(trackList)
                                binding.tvStatusText.text = "Не удалось получить список песен (code: resp)."
                                pbListLoading.visibility = View.INVISIBLE
                            }
                        }

                        override fun onFailure(call: Call<ApiResp>, t: Throwable) {
                            trackList = mutableListOf()
                            adapter.setData(trackList)
                            binding.tvStatusText.text = "Не удалось получить список песен (code: fail)."
                            pbListLoading.visibility = View.INVISIBLE
                        }
                    })
            }
        }
        // TrackList //

        return root
    }

    fun Msg(msg: String) {
        val myToast = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
        myToast.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}