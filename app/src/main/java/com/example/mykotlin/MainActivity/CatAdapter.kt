package com.example.mykotlin.MainActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mykotlin.R
import de.hdodenhof.circleimageview.CircleImageView

class CatAdapter(val context: Context
                 , val listItem: List<CatBreed>
                 , val imageUrl: List<String>
                 , private val listener: OnItemClickListener)
    :RecyclerView.Adapter<CatAdapter.ViewHolder>() {

    private val likedItems: MutableSet<String>
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("liked_items_prefs", Context.MODE_PRIVATE)

    init {
        likedItems = sharedPreferences.getStringSet("liked_items", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatAdapter.ViewHolder, position: Int) {

        val itemId = listItem[position].name.toString()

        holder.Name.setText(listItem[position].name)
        holder.Description.setText("Description: " + listItem[position].description)

        val randomIndex = (imageUrl.indices).random()
        val randomUrl = imageUrl[randomIndex]

        Glide.with(context)
            .load(randomUrl)
            .placeholder(R.drawable.sample)
            .error(R.drawable.sample)
            .into(holder.Image)

        if (likedItems.contains(itemId)) {
            holder.likeBtn.setImageResource(R.drawable.red_heart_full)
        } else {
            holder.likeBtn.setImageResource(R.drawable.ic_heart)
        }

        holder.likeBtn.setOnClickListener {

            Log.d("CatAdapter", "likeBtn Click: $likedItems")
            if (likedItems.contains(itemId)) {
                likedItems.remove(itemId)

                Log.d("CatAdapter", "likeBtn when remove itemID: $likedItems")

                holder.likeBtn.setImageResource(R.drawable.ic_heart)
            } else {
                likedItems.add(itemId)
                holder.likeBtn.setImageResource(R.drawable.red_heart_full)
            }



            sharedPreferences.edit {
                putStringSet("liked_items", likedItems)
                apply()
            }

            val cat = listItem[position].copy(imgUrl = randomUrl)
            listener.onItemLiked(cat)
        }
    }

       /* if (likedItems.contains(position)) {
            holder.likeBtn.setImageResource(R.drawable.red_heart_full)  // Set as liked
        } else {
            holder.likeBtn.setImageResource(R.drawable.ic_heart)  // Set as not liked
        }

        holder.likeBtn.setOnClickListener {
            if (likedItems.contains(position)) {
                likedItems.remove(position)
                holder.likeBtn.setImageResource(R.drawable.ic_heart)
            } else {
                likedItems.add(position)
                holder.likeBtn.setImageResource(R.drawable.red_heart_full)
            }

            val cat = listItem[position].copy(imgUrl = randomUrl)

            listener.onItemLiked(cat)
        }
    }*/


    override fun getItemCount(): Int {
        return listItem.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var likeBtn: ImageView
        var Image: CircleImageView
        var Name: TextView
        var Description: TextView

        init {
            Name = itemView.findViewById(R.id.cardTitle)
            Image = itemView.findViewById(R.id.cardImage)
            likeBtn = itemView.findViewById(R.id.LikeBtn)
            Description = itemView.findViewById(R.id.cardDescription)

        }


    }

    interface OnItemClickListener {
        fun onItemLiked(LikedItem: CatBreed)
    }

}
