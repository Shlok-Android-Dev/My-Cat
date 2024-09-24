package com.example.myCat.MainActivity.LikedCatActivity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myCat.MainActivity.CatBreed
import com.example.myCat.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LikedCatActivity : AppCompatActivity() {

    private var TAG = "LikedCatActivity"
    private lateinit var catNotLiked : TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LikedCatAdapter
    private lateinit var catList: MutableList<CatBreed>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_cat)
        recyclerView = findViewById(R.id.LikedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        catNotLiked = findViewById(R.id.noLikedItem)

        val sharedPreferences = getSharedPreferences("CatPreferences", MODE_PRIVATE)
        val json = sharedPreferences.getString("Liked_cats", null)

        catList = if (json != null) {
            val type = object : TypeToken<MutableList<CatBreed>>() {}.type
            Gson().fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        if (catList.isEmpty()) {
            catNotLiked.visibility = View.VISIBLE
        }else{
            catNotLiked.visibility = View.GONE
            adapter = LikedCatAdapter(this,catList)
            recyclerView.adapter = adapter
        }


    }
}