package com.suhani.e_slambook

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.suhani.e_slambook.data.User
import com.suhani.e_slambook.databinding.FragmentProfileBinding

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference:DatabaseReference
    private  var storageReference= Firebase.storage
    private lateinit var imageUri:Uri
    private lateinit var dialog:Dialog
    private lateinit var profilePhoto:ImageView
    private lateinit var fab:FloatingActionButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profilePhoto = view.findViewById(R.id.profile_image)
        fab = binding.floatingActionButton



        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        storageReference = FirebaseStorage.getInstance()

        fetchImage()

        fab.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        binding.saveBtn.setOnClickListener {
            showProgressBar()
            val userName = binding.etUserName.text.toString()
            val Name = binding.etName.text.toString()
            val bio = binding.etBio.text.toString()

            val userId = auth.currentUser!!.uid
            val dbRef = FirebaseDatabase.getInstance().getReference("UserImages").child(userId)
            dbRef.get().addOnSuccessListener {
                val url = it.child("url").value.toString()
                val user = User(userId,userName, Name, bio,url,"nothing_happen")

                if (uid != null) {
                    databaseReference.child(uid).setValue(user).addOnCompleteListener {
                        if (it.isSuccessful) {
                            uploadProfilePic()
                        } else {
                            hideProgressBar()
                            Toast.makeText(
                                context,
                                "Failed to update the profile",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun uploadProfilePic() {
//            imageUri=Uri.parse("android.resources://com.suhani.e_slambook/${R.drawable.noprofile}")
        storageReference.getReference("Users/"+auth.currentUser?.uid)
            .child(System.currentTimeMillis().toString())
        .putFile(imageUri).addOnSuccessListener {
            hideProgressBar()
                it.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {task->
                        val uid=auth.currentUser!!.uid

                        val mapImage=mapOf(
                            "url" to task.toString()
                        )

                        FirebaseDatabase.getInstance().getReference("UserImages")
                            .child(uid).setValue(mapImage)
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        hideProgressBar()
                        Toast.makeText(context, "Failed to update the profile", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showProgressBar(){
        Log.e("dialog","entered showProgressBar")
        dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){
        dialog.hide()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imageUri= data?.data!!
        profilePhoto.setImageURI(imageUri)
    }

    private fun fetchImage(){
        val userId=auth.currentUser!!.uid
        val databaseReference=FirebaseDatabase.getInstance().getReference("UserImages").child(userId)
        databaseReference.get().addOnSuccessListener {
            val url=it.child("url").value.toString()
           Glide.with(this).load(url).into(profilePhoto)
        }
    }


}