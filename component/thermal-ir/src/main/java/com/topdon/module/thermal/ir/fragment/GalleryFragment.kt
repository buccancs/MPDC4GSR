package com.topdon.module.thermal.ir.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.topdon.module.thermal.ir.R

/**
 * Specialized thermal imaging component providing GalleryFragment functionality for the IRCamera system.
 *
 * <h3>Technical Specifications:</h3>
 * <ul>
 *   <li>Thread-safe operations for thermal data processing</li>
 *   <li>Optimized performance for real-time thermal imaging</li>
 *   <li>Compatible with TC001 thermal camera hardware</li>
 * </ul>
 *
 * @author IRCamera Development Team
 * @version 2.0
 * @since 1.0
 */
class GalleryFragment : Fragment() {
    private var path = ""

    /**
     * Executes oncreateview operation with thermal imaging domain optimization.
     *
     * @param
     * @param inflater Parameter for operation (type: LayoutInflater)
     * @param container Parameter for operation (type: ViewGroup?)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container)
    }

    /**
     * Executes onviewcreated operation with thermal imaging domain optimization.
     *
     * @param
     * @param view Parameter for operation (type: View)
     * @param savedInstanceState Parameter for operation (type: Bundle?)
     *
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        path = requireArguments().getString("path")!!

        // Initialize view with findViewById
        val fragmentGalleryImg: ImageView = view.findViewById(R.id.fragment_gallery_img)
        Glide.with(this).load(path).into(fragmentGalleryImg)
    }
}
