package com.dtx12.tempertechassessment.features.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dtx12.tempertechassessment.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSignUpBinding.inflate(inflater, container, false).root
    }
}