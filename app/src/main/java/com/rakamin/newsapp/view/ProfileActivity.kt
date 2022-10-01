package com.rakamin.newsapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rakamin.newsapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(resources.getIdentifier("foto_profil", "drawable", packageName))
            .circleCrop()
            .into(binding.ivProfile)

        binding.button.setOnClickListener{
            val browserDirect = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/YudaSulaiman"))
            startActivity(browserDirect)
        }
    }
}