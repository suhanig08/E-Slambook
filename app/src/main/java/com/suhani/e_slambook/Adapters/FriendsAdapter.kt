package com.suhani.e_slambook.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suhani.e_slambook.R
import com.suhani.e_slambook.data.Friend

class FriendsAdapter(val context:Context,private val friendsList: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName:TextView=itemView.findViewById(R.id.rvUserName)
        val bio:TextView=itemView.findViewById(R.id.rvBio)
        val profile:ImageView=itemView.findViewById(R.id.rvProfileImage)
        val textViewFriendName: TextView = itemView.findViewById(R.id.rvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friends, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendsList[position]
        holder.textViewFriendName.text = friend.name
        holder.userName.text=friend.userName
        holder.bio.text=friend.bio
        Glide.with(context).load(friend.url).error(R.drawable.profile).into(holder.profile)
    }

    override fun getItemCount() = friendsList.size
}
