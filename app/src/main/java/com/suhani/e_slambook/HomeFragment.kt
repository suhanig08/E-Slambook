package com.suhani.e_slambook

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.suhani.e_slambook.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var front_anim:AnimatorSet
    lateinit var back_anim:AnimatorSet
    var isFront=true



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val scale= context?.resources?.displayMetrics?.density
        binding.bookFront.cameraDistance=8000*scale!!
        //binding.bookPagesContainer.cameraDistance=8000*scale

        front_anim=AnimatorInflater.loadAnimator(context,R.animator.front_animator) as AnimatorSet
        back_anim=AnimatorInflater.loadAnimator(context,R.animator.back_animator) as AnimatorSet

        binding.openButton.setOnClickListener {
            if(isFront){
                front_anim.setTarget(binding.bookFront)
                //back_anim.setTarget(binding.bookPagesContainer)
                front_anim.start()
                back_anim.start()
                isFront=false
            }else{
                front_anim.setTarget(binding.bookFront)
                //back_anim.setTarget(binding.bookPagesContainer)
                front_anim.start()
                back_anim.start()
                isFront=true
            }
            findNavController().navigate(R.id.action_homeFragment2_to_bookFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


}