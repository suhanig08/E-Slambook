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

class SearchAdapter(val context: Context,private var searchList:ArrayList<User>
,private val performBtnClickListener: (String) -> Unit, private val cancelBtnClickListener: (String) -> Unit)
    :RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(view:View):RecyclerView.ViewHolder(view) {

        val userName:TextView=view.findViewById(R.id.rvUserName)
        val nameofUser:TextView=view.findViewById(R.id.rvName)
        val bio:TextView=view.findViewById(R.id.rvBio)
        val profileImage:ImageView=view.findViewById(R.id.rvProfileImage)
        val performBtn: ImageView = view.findViewById(R.id.performBtn)
        val cancelBtn: Button = view.findViewById(R.id.cancelBtn)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val searchView=LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_search,parent, false)
        return SearchViewHolder(searchView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val list=searchList[position]
        holder.userName.text= list.userName
        holder.nameofUser.text=list.Name
        holder.bio.text=list.bio
        Glide.with(context).load(list.url).error(R.drawable.profile).into(holder.profileImage)

        when (list.status) {
            "nothing_happen" -> {
                holder.performBtn.visibility = View.VISIBLE
                holder.cancelBtn.visibility = View.GONE

            }
            "pending" -> {
                holder.performBtn.visibility = View.GONE
                holder.cancelBtn.visibility = View.VISIBLE

            }

            "friends"->{
                holder.performBtn.visibility=View.GONE
                holder.cancelBtn.visibility=View.GONE
                holder.performBtn.setImageResource(R.drawable.alreadyfriends_icon)
            }

        }

        holder.performBtn.setOnClickListener {
            performBtnClickListener(list.uId.toString())
        }

        holder.cancelBtn.setOnClickListener {
            cancelBtnClickListener(list.uId.toString())
        }

    }

    override fun getItemCount(): Int {
        return searchList.size
    }

}
