package com.suhani.e_slambook.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suhani.e_slambook.R
import com.suhani.e_slambook.data.User

class FriendRequestAdapter(
    val context: Context,
    private var requestList: ArrayList<User>,
    private val acceptBtnClickListener: (String) -> Unit,
    private val declineBtnClickListener: (String) -> Unit
) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {

    class FriendRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView = view.findViewById(R.id.rvUserName)
        val nameofUser: TextView = view.findViewById(R.id.rvName)
        val bio: TextView = view.findViewById(R.id.rvBio)
        val profileImage: ImageView = view.findViewById(R.id.rvProfileImage)
        val acceptBtn: Button = view.findViewById(R.id.acceptBtn)
        val declineBtn: Button = view.findViewById(R.id.declineBtn)
        val friends:ImageView=view.findViewById(R.id.alreadyFriends)
        //val removeFriend:ImageView=view.findViewById(R.id.removeBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val requestView = LayoutInflater.from(parent.context)
            .inflate(R.layout.friendrequests_item, parent, false)
        return FriendRequestViewHolder(requestView)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        val request = requestList[position]
        holder.userName.text = request.userName
        holder.nameofUser.text = request.Name
        holder.bio.text = request.bio
        Glide.with(context).load(request.url).error(R.drawable.profile).into(holder.profileImage)

        when (request.status) {
            "friends" -> {
                holder.acceptBtn.visibility = View.GONE
                holder.declineBtn.visibility = View.GONE
                holder.friends.visibility = View.VISIBLE

            }
            "nothing_happen"->{
                holder.acceptBtn.visibility = View.VISIBLE
                holder.declineBtn.visibility = View.VISIBLE
                holder.friends.visibility=View.GONE
            }
        }

        holder.acceptBtn.setOnClickListener {
            acceptBtnClickListener(request.uId.toString())
        }

        holder.declineBtn.setOnClickListener {
            declineBtnClickListener(request.uId.toString())
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
        }
}

