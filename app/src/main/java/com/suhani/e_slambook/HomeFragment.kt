package com.suhani.e_slambook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.suhani.e_slambook.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewFlipper: ViewFlipper

    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        viewFlipper = binding.viewFlipper

        // Set up the initial view
        setupView(binding.root)

        binding.newPageBtn.setOnClickListener {
            addNewPageAndFlip()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupView(view: View) {
        val openButton = view.findViewById<Button>(R.id.openButton)
        val sendIv = view.findViewById<ImageView>(R.id.sendIv)
        val btnPrevious = view.findViewById<Button>(R.id.btnPrevious)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val addQuestionButton = view.findViewById<Button>(R.id.addQuestionButton)
        val friendsSpinner = view.findViewById<Spinner>(R.id.friendsSpinner)
        val newPageBtn = view.findViewById<Button>(R.id.newPageBtn)

        openButton?.setOnClickListener {
            viewFlipper.flipInterval = 2000
            viewFlipper.showNext()
        }

        sendIv.setOnClickListener {
            sendIv.visibility = View.GONE
        }

        btnPrevious.setOnClickListener {
            viewFlipper.showPrevious()
        }

        btnNext.setOnClickListener {
            viewFlipper.showNext()
        }

        addQuestionButton.setOnClickListener {
            addQuestion(view)
        }

        newPageBtn.setOnClickListener {
            addNewPageAndFlip()
        }

        friendsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val friendName = parent.getItemAtPosition(position).toString()
                Toast.makeText(context, "Selected: $friendName", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        loadFriends(friendsSpinner)
    }

    private fun addNewPageAndFlip() {
        val inflater = LayoutInflater.from(context)
        val newPageView = inflater.inflate(R.layout.slam_book_page, null) as ViewGroup
        setupView(newPageView)
        viewFlipper.addView(newPageView)
        viewFlipper.showNext()
    }

    private fun addQuestion(view: View) {
        val questionLayout = LinearLayout(context)
        questionLayout.orientation = LinearLayout.VERTICAL

        val questionText = EditText(context)
        questionText.hint = "Your question"
        questionLayout.addView(questionText)

        val bookPagesContainer = view.findViewById<LinearLayout>(R.id.bookPagesContainer)
        bookPagesContainer.addView(questionLayout, bookPagesContainer.childCount - 2)
    }

    private fun loadFriends(spinner: Spinner) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("HomeFragment", "User not authenticated")
            return
        }

        val userId = currentUser.uid
        val friendsRef = database.getReference("Friends").child(userId)

        friendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val friendsIds = mutableListOf<String>()
                for (friendSnapshot in dataSnapshot.children) {
                    friendSnapshot.key?.let { friendsIds.add(it) }
                }
                fetchFriendsDetails(friendsIds, spinner)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("HomeFragment", "Database error: ${databaseError.message}")
            }
        })
    }

    private fun fetchFriendsDetails(friendIds: List<String>, spinner: Spinner) {
        if (friendIds.isEmpty()) {
            Log.e("HomeFragment", "No friends found")
            return
        }

        val usersRef = database.getReference("Users")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val friendsList = mutableListOf<String>()
                for (friendId in friendIds) {
                    val friendName = dataSnapshot.child(friendId).child("name").getValue(String::class.java)
                    if (friendName != null) {
                        friendsList.add(friendName)
                    }
                }
                updateSpinner(friendsList, spinner)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("HomeFragment", "Database error: ${databaseError.message}")
            }
        })
    }

    private fun updateSpinner(friendsList: List<String>, spinner: Spinner) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, friendsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
