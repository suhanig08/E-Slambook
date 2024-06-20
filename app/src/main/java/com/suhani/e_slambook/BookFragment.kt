package com.suhani.e_slambook

// BookFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment

class BookFragment : Fragment() {

    private lateinit var bookFlipper: ViewFlipper
    private lateinit var addPageButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_book, container, false)
        bookFlipper = root.findViewById(R.id.book_flipper)
        addPageButton = root.findViewById(R.id.add_page_button)

        // Handle adding new pages
        addPageButton.setOnClickListener {
            addNewPage()
        }

        // Handle sending requests
        for (i in 0 until bookFlipper.childCount) {
            val page = bookFlipper.getChildAt(i)
            val sendRequestButton: Button = page.findViewById(R.id.send_request_button)
            sendRequestButton.setOnClickListener {
                // Show list of friends and send request logic
                showFriendsList()
            }
        }

        return root
    }

    private fun addNewPage() {
        val inflater = LayoutInflater.from(context)
        val newPage = inflater.inflate(R.layout.slam_book_page, bookFlipper, false)
        bookFlipper.addView(newPage)

        // Handle sending requests for the new page
        val sendRequestButton: Button = newPage.findViewById(R.id.send_request_button)
        sendRequestButton.setOnClickListener {
            showFriendsList()
        }
    }

    private fun showFriendsList() {
        // Logic to show a list of friends and send request
    }
}
