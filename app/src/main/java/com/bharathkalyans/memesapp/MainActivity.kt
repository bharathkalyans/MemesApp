package com.bharathkalyans.memesapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

    }

    private fun loadMeme() {

        //Progress Bar is Set Visible
        progressBar.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->

                currentImageURL = response.getString("url").toString()

                Glide
                    .with(this)
                    .load(currentImageURL)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                    })
                    .into(ivMemesImage);
            },
            {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
            })

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type =  "text/plain"
            putExtra(Intent.EXTRA_TEXT,"$currentImageURL")
        }
        val chooser = Intent.createChooser(intent,"Share this meme ....")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }

}