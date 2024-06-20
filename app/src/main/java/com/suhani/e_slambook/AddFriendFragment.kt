package com.suhani.e_slambook

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.suhani.e_slambook.Adapters.FriendRequestAdapter
import com.suhani.e_slambook.Adapters.SearchAdapter
import com.suhani.e_slambook.data.User
import com.suhani.e_slambook.databinding.FragmentAddfriendBinding

@Suppress("DEPRECATION")
class AddFriendFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentAddfriendBinding
    private lateinit var queryTerm: String
    private lateinit var auth:FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var searchRecyclerView:RecyclerView
    private lateinit var searchLayoutManager:RecyclerView.LayoutManager
    private lateinit var searchAdapter: SearchAdapter
    private var  searchInfo= arrayListOf<User>()

    private lateinit var reqRef:DatabaseReference
    private lateinit var friendRef:DatabaseReference

    private lateinit var friendRequestsRecyclerView: RecyclerView
    private lateinit var friendRequestsLayoutManager: RecyclerView.LayoutManager
    private lateinit var friendRequestAdapter: FriendRequestAdapter
    private var friendRequestList = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reqRef = FirebaseDatabase.getInstance().getReference("Requests")
        friendRef=FirebaseDatabase.getInstance().getReference("Friends")
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbref=FirebaseDatabase.getInstance().getReference("Users")
        auth=FirebaseAuth.getInstance()

        searchAdapter = SearchAdapter(context as Activity, searchInfo,
            ::onPerformBtnClick, ::onCancelBtnClick)

        friendRequestsRecyclerView = binding.rvFriendRequests
        friendRequestsLayoutManager = LinearLayoutManager(context as Activity)
        friendRequestAdapter = FriendRequestAdapter(context as Activity, friendRequestList,
            ::onAcceptBtnClick, ::onDeclineBtnClick)

        friendRequestsRecyclerView.adapter = friendRequestAdapter
        friendRequestsRecyclerView.layoutManager = friendRequestsLayoutManager
        friendRequestsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                friendRequestsRecyclerView.context,
                (friendRequestsLayoutManager as LinearLayoutManager).orientation
            )
        )

        fetchFriendRequests()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentAddfriendBinding.inflate(inflater, container, false)
        return binding.root

    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val searchView = menu.findItem(R.id.searchIcon)?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            queryTerm = query
            if (queryTerm.isNotEmpty()) {
                searchUsers()
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            queryTerm = newText
            if (queryTerm.isNotEmpty()) {
                searchUsers()
            } else {
                // Clear the search results when the search box is empty
                searchInfo.clear()
                searchAdapter.notifyDataSetChanged()
            }
        }
        return true
    }

    private fun searchUsers() {
        dbref = FirebaseDatabase.getInstance().getReference("Users")
        auth=FirebaseAuth.getInstance()
        searchRecyclerView=binding.rvSearch
        searchLayoutManager=LinearLayoutManager(context as Activity)

        dbref.orderByChild("userName").startAt(queryTerm).endAt(queryTerm + "\uf8ff")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        searchInfo.clear()
                        for (userSnapshot in snapshot.children) {

                            if (userSnapshot.key.toString() != auth.currentUser?.uid) {
                                val status = getStatus(userSnapshot.key.toString()) // Function to get the status
                                val obj = User(
                                    userSnapshot.key.toString(),
                                    userSnapshot.child("userName").value.toString(),
                                    userSnapshot.child("name").value.toString(),
                                    userSnapshot.child("bio").value.toString(),
                                    userSnapshot.child("url").value.toString(),
                                    status
                                )

                                searchInfo.add(obj)
                                searchAdapter = SearchAdapter(context as Activity, searchInfo,
                                    ::onPerformBtnClick, ::onCancelBtnClick)
                                searchRecyclerView.adapter = searchAdapter
                                searchRecyclerView.layoutManager = searchLayoutManager
                                searchRecyclerView.addItemDecoration(
                                    DividerItemDecoration(
                                        searchRecyclerView.context,
                                        (searchLayoutManager as LinearLayoutManager).orientation
                                    )
                                )
                        }
                        }
                    } else {
                        Log.d("userList", "No users found with the query: $queryTerm")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(tag, "Error occurred: ${error.message}")
                }
            })
        }

    private fun getStatus(uid: String): String {
        var status = "nothing_happen"
        // Implement logic to determine the status
        // For example, check the Requests and Friends nodes to determine if a request is pending, accepted, or declined
        reqRef.child(auth.currentUser!!.uid).child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                status = it.child("status").value.toString()
            }
        }
        return status
    }

    private fun onPerformBtnClick(userId: String) {
        val hashMap = hashMapOf<String, Any>(
            "status" to "pending"
        )
        reqRef.child(auth.currentUser!!.uid)
            .child(userId).updateChildren(hashMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Friend request sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateStatus(userId, "pending")
                } else {
                    Toast.makeText(
                        context,
                        it.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun onCancelBtnClick(userId: String) {
        reqRef.child(auth.currentUser!!.uid)
            .child(userId).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Friend request cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateStatus(userId, "nothing_happen")
                } else {
                    Toast.makeText(
                        context,
                        it.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun fetchFriendRequests() {
        val currentUserId = auth.currentUser?.uid.toString()
        reqRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendRequestList.clear()
                snapshot.children.forEach { requestSnapshot ->
                    val nestedSnapshot=requestSnapshot.child(currentUserId)
                    val status = nestedSnapshot.child("status").value.toString()
                    val userId=nestedSnapshot.key.toString()
                    Log.e("statuss",snapshot.key.toString())
                    Log.e("statuss",requestSnapshot.key.toString())
                    Log.e("statuss",nestedSnapshot.key.toString())
                    Log.e("statuss",status)
                    if (status == "pending"&&currentUserId==userId) {
                        // Assuming you have a way to fetch user details
                        fetchUserDetails(requestSnapshot.key.toString()) { user ->
                            friendRequestList.add(user)
                            friendRequestAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


            private fun fetchUserDetails(userId: String, callback: (User) -> Unit) {
                dbref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            callback(user)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("FriendsFragment", "Database error: ${error.message}")
                    }
                })
            }

            private fun onAcceptBtnClick(userId: String) {
                val currentUserId = auth.currentUser?.uid
                val hashMap = hashMapOf<String, Any>(
                    "status" to "friends"
                )
                if (currentUserId != null) {
                    friendRef.child(currentUserId).child(userId).updateChildren(hashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                friendRef.child(userId).child(currentUserId).updateChildren(hashMap)
                                reqRef.child(currentUserId).child(userId).removeValue()
                                reqRef.child(userId).child(currentUserId).removeValue()
                                Toast.makeText(
                                    context,
                                    "Friend request accepted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateStatusFriends(userId,"friends")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to accept friend request",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

            private fun onDeclineBtnClick(userId: String) {
                val currentUserId = auth.currentUser?.uid
                if (currentUserId != null) {
                    reqRef.child(currentUserId).child(userId).removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                reqRef.child(userId).child(currentUserId).removeValue()
                                Toast.makeText(
                                    context,
                                    "Friend request declined",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateStatusFriends(userId,"nothing_happen")
                            } else {
                                Toast.makeText(
                                    context,
                                    "Failed to decline friend request",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

            private fun updateStatus(userId: String, newStatus: String) {
                searchInfo.find { it.uId == userId }?.status = newStatus
                searchAdapter.notifyDataSetChanged()
            }

    private fun updateStatusFriends(userId: String, newStatus: String) {
        friendRequestList.find { it.uId == userId }?.status = newStatus
        friendRequestAdapter.notifyDataSetChanged()

        searchInfo.find { it.uId == userId }?.status = newStatus
        searchAdapter.notifyDataSetChanged()
    }
        }


