package com.dtx12.tempertechassessment.core.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dtx12.tempertechassessment.databinding.ActivityRootBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityRootBinding.inflate(layoutInflater).root)
    }
}