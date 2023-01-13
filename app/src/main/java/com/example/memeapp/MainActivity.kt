package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding?=null

    var currentImageUrl: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        loadMeme()

    }

    private fun loadMeme(){

        // setting the progress bar as visible
        binding?.progressBar?.visibility=View.VISIBLE


        // instantiating the request queue
        val queue=Volley.newRequestQueue(this)
        var url="https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest=JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {response ->
                currentImageUrl=response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding?.progressBar?.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding?.progressBar?.visibility=View.GONE
                        return false
                    }

                }).into(binding?.ivMeme!!)
            },
            Response.ErrorListener {  })

        // Add the request to the request queue
        queue.add(jsonObjectRequest)
    }

    fun shareMeme(view: View){
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this cool meme $currentImageUrl")
        val chooser=Intent.createChooser(intent, "Share using...")
        startActivity(chooser)
    }

    fun nextMeme(view: View){
        loadMeme()
    }
}