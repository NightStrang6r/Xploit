package com.example.xploit.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xploit.R
import com.example.xploit.api.ApiResp
import com.example.xploit.api.RetrofitInstance
import com.example.xploit.api.VkID
import com.example.xploit.databinding.ActivityImportPlaylistBinding
import com.example.xploit.databinding.ActivityMainBinding
import com.example.xploit.ui.track.MusicModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImportPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImportPlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImportPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("devlog", "Created!")


        binding.tvMainText.setOnClickListener {
            Log.d("devlog", "Click!")
            RetrofitInstance.api.getVkID(binding.etLink.text.toString()).enqueue(object : Callback<VkID> {
                override fun onResponse(call: Call<VkID>, response: Response<VkID>) {
                    if(response.isSuccessful){
                        if(response.body()?.success == true) {
                            val sharedPref = getPreferences(MODE_PRIVATE)
                            val edit = sharedPref.edit()
                            edit.putString("VkID", response.body()?.id.toString()).apply()
                            finish()
                        } else {
                            Msg("Неверная ссылка")
                            binding.tvErrText.text = "Неверная ссылка"
                        }
                    } else {
                        Msg("Не удалось подключиться к серверу")
                        binding.tvErrText.text = "Не удалось подключиться к серверу"
                    }
                }

                override fun onFailure(call: Call<VkID>, t: Throwable) {
                    Msg("Не удалось подключиться к серверу")
                    binding.tvErrText.text = "Не удалось подключиться к серверу"
                }
            })
        }
    }

    fun Msg(msg: String) {
        val myToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        myToast.show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}