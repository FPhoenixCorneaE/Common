package com.fphoenixcorneae.util.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fphoenixcorneae.dsl.layout.LinearLayout
import com.fphoenixcorneae.ext.androidViewModel
import com.fphoenixcorneae.ext.logd

class HomeFragment : Fragment() {

    private val appViewModel by androidViewModel<AppViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return LinearLayout {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel.global.observe(viewLifecycleOwner){
            "HomeFragment: $it".logd("GlobalAndroid")
        }
    }
}