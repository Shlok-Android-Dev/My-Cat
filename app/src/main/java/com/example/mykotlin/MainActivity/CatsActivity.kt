package com.example.mykotlin.MainActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlin.MainActivity.LikedCatActivity.LikedCatActivity
import com.example.mykotlin.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CatsActivity : AppCompatActivity() , CatAdapter.OnItemClickListener {


    lateinit var noDataFound: TextView
    lateinit var recyclerView: RecyclerView
    private var progressBar: ProgressBar? = null
    lateinit var toolBar : Toolbar
    private val TAG = "CatsActivity"
    var sharedPreferences : SharedPreferences? = null
    var editor:  SharedPreferences. Editor? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec)
        sharedPreferences = getSharedPreferences("CatPreferences", MODE_PRIVATE)
        editor = sharedPreferences?.edit()

        noDataFound = findViewById(R.id.noDataFound)
        toolBar = findViewById(R.id.toolBar)
        recyclerView = findViewById(R.id.recView)
        progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar

        setSupportActionBar(toolBar)
        recyclerView.layoutManager = LinearLayoutManager(this)


        fetchCatBreeds()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.item_menu,menu)

        val getItem = menu!!.findItem(R.id.Like)
        if (getItem != null) {
            val button = getItem.actionView as? AppCompatButton

            button?.setOnClickListener {
                startActivity( Intent(this, LikedCatActivity::class.java))
            }
        }
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.Like -> {
                val intent = Intent(this, LikedCatActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.exitApp -> {
                finish()
                true
            }
            R.id.refresh -> {
                if (isOnline(this)){
                    fetchCatBreeds()
                }else{
                    Toast.makeText(this, "Please Turn on your Internet!", Toast.LENGTH_SHORT).show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchCatBreeds() {
        progressBar?.visibility = View.VISIBLE
        noDataFound.visibility = View.GONE

        val list = ArrayList<CatBreed>()
        val imageUrl = listOf(
            "https://plus.unsplash.com/premium_photo-1677101221533-52b45823a2dc?q=80&w=1742&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?q=80&w=1686&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1478098711619-5ab0b478d6e6?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1511044568932-338cba0ad803?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1513360371669-4adf3dd7dff8?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1494256997604-768d1f608cac?q=80&w=1829&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1533743983669-94fa5c4338ec?q=80&w=1692&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1491485880348-85d48a9e5312?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            "https://media.istockphoto.com/id/466265904/photo/kitten-looking-out-from-under-blanket.jpg?s=2048x2048&w=is&k=20&c=3PeWi7n4RamNVtEe8xH_VcqIGOVgF70GTkoZ79f4KXE="
        )
        val call = RetrofitClient.apiService.getBreeds()

        call.enqueue(object : Callback<List<CatBreed>> {
            override fun onResponse(call: Call<List<CatBreed>>, response: Response<List<CatBreed>>) {
                progressBar?.visibility = View.GONE

                if (response.isSuccessful) {
                    recyclerView.visibility = View.VISIBLE
                    val breeds = response.body()
                    breeds?.let {
                        for (breed in it) {
                            Log.d("CatBreed", "Breed: ${breed.name}")

                            if (response.isSuccessful) {
                                val breeds = response.body()

                                breeds?.let {
                                    if (breeds.isNotEmpty()) {
                                        list.addAll(breeds)
                                        progressBar?.visibility = View.GONE

                                        val recyclerAdapter = CatAdapter(this@CatsActivity, list, imageUrl, this@CatsActivity)
                                        recyclerView.adapter = recyclerAdapter
                                    } else {
                                        noDataFound.visibility = View.VISIBLE
                                        Log.d("CatBreed", "No breeds found.")
                                    }
                                }
                            } else {
                                progressBar?.visibility = View.VISIBLE
                                Log.e("CatBreed", "Error: ${response.code()}")
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<CatBreed>>, t: Throwable) {
                progressBar?.visibility = View.GONE
                recyclerView.visibility = View.GONE
                noDataFound.visibility = View.VISIBLE
                Log.e("CatApiError", "Error fetching data: ${t.message}")
            }
        })
    }

    object RetrofitClient {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"

        val apiService: CatApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CatApiService::class.java)
        }
    }

    override fun onItemLiked(LikedItem: CatBreed) {

        val json = sharedPreferences?.getString("Liked_cats", null)

        val likedCats: MutableList<CatBreed> = if (json != null) {
            try {
                val type = object : TypeToken<MutableList<CatBreed>>() {}.type
                Gson().fromJson<MutableList<CatBreed>>(json, type) ?: mutableListOf()
            } catch (e: com.google.gson.JsonSyntaxException) {
                Log.e(TAG, "Error parsing JSON", e)
                mutableListOf()
            }
        } else { mutableListOf() }

        val existingCat = likedCats.find { it.name == LikedItem.name }

        if (existingCat == null) {
            likedCats.add(LikedItem)
            Log.d(TAG, "Item added: ${LikedItem.name}")
        } else {
            likedCats.remove(existingCat)
            Log.d(TAG, "Item removed: ${LikedItem.name}") }
//        if (!likedCats.any { it.name == LikedItem.name }) {
//            likedCats.add(LikedItem)
//        }else {
//            Log.d(TAG, "UnUnique error found!")
//        }

        val updatedJson = Gson().toJson(likedCats)
        editor?.putString("Liked_cats", updatedJson)
        editor?.apply()
        Log.d(TAG, "onItemLiked: $likedCats")
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}
