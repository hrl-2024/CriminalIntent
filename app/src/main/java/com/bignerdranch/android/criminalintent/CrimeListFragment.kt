package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding get() = checkNotNull(_binding) {
        "Cannot access binding before onCreateView() or after onDestroyView()"
    }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        val crimes = crimeListViewModel.crimes
        val adapter = CrimeListAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        job = viewLifecycleOwner.lifecycleScope.launch {
            val crime = crimeListViewModel.loadCrimes()
            binding.crimeRecyclerView.adapter = CrimeListAdapter(crime)
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
        _binding = null
    }
}