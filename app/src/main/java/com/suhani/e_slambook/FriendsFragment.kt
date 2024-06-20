package com.suhani.e_slambook

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.suhani.e_slambook.Adapters.FriendsAdapter
import com.suhani.e_slambook.data.Friend

class FriendsFragment : Fragment() {

    private lateinit var recyclerViewFriends: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter
    private val friendsList = mutableListOf<Friend>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        recyclerViewFriends = view.findViewById(R.id.rvFriends)
        recyclerViewFriends.layoutManager = LinearLayoutManager(context)
        friendsAdapter = FriendsAdapter(context as Activity,friendsList)
        recyclerViewFriends.adapter = friendsAdapter

        loadFriends()

        return view
    }

    private fun loadFriends() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userId = it.uid
            val databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child(userId)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    friendsList.clear()
                    for (friendSnapshot in snapshot.children) {
                        val friendId = friendSnapshot.key
                        Log.e("friendsss",friendId.toString())
                        friendId?.let {id->
                            // Assume you have a way to get friend's name from their uid
                            getUserDetails(id) { friend ->
                                friendsList.add(friend)
                                friendsAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    private fun getUserDetails(uid: String, callback: (Friend) -> Unit) {
        val userReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: "Unknown"
                val userName = snapshot.child("userName").getValue(String::class.java).toString()
                val bio = snapshot.child("bio").getValue(String::class.java).toString()
                val url = snapshot.child("url").getValue(String::class.java).toString()
                callback(Friend(uid, name,userName,url,bio))
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
