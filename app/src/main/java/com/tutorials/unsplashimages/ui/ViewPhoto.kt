package com.tutorials.unsplashimages.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.tutorials.unsplashimages.R
import com.tutorials.unsplashimages.databinding.FragmentViewPhotoBinding


class ViewPhoto : Fragment() {
    private lateinit var binding: FragmentViewPhotoBinding
    private val args by navArgs<ViewPhotoArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageBody = args.imageBody

        binding.apply {
            imageSrc.load(imageBody.urls.regular) {
                placeholder(R.drawable.loading_anim)
                error(R.drawable.image_icon)
            }
            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }
            authorText.text = imageBody.user.username
            likesText.text = imageBody.likes.toString()
        }
    }


}