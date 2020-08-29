package com.example.weatherapp.ui.weather.forcast

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R

class FutureListFragment : Fragment() {

    companion object {
        fun newInstance() = FutureListFragment()
    }

    private lateinit var listViewModel: FutureListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listViewModel = ViewModelProviders.of(this).get(FutureListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}