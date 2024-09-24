package com.example.myCat.MainActivity.LikedCatActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myCat.MainActivity.CatBreed
import com.example.myCat.R
import de.hdodenhof.circleimageview.CircleImageView

class LikedCatAdapter(val context: Context, val likedImages: List<CatBreed>) : RecyclerView.Adapter<LikedCatAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedCatAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikedCatAdapter.ViewHolder, position: Int) {
        holder.Name.setText(likedImages[position].name)
        holder.Description.setText(likedImages[position].description)

        Glide.with(context)
            .load(likedImages.get(position).imgUrl)
            .placeholder(R.drawable.sample)
            .error(R.drawable.sample)
            .into(holder.imgCircle)

        holder.likeBtn.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return likedImages.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var likeBtn: ImageView
        var imgCircle: CircleImageView
        var Name: TextView
        var Description: TextView

        init {
            Name = itemView.findViewById(R.id.cardTitle)
            imgCircle = itemView.findViewById(R.id.cardImage)
            likeBtn = itemView.findViewById(R.id.LikeBtn)
            Description = itemView.findViewById(R.id.cardDescription)
        }
    }
}