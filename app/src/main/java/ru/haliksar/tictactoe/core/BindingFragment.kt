package ru.haliksar.tictactoe.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding

abstract class BindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding
        get() = _binding ?: throw NullPointerException("Binding can't be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = binding(inflater, container, savedInstanceState)
        otherSetups()
        return _binding?.root
    }

    protected abstract fun otherSetups()

    protected abstract fun binding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun onBackPressed(callback: suspend () -> Unit) {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                lifecycleScope.launchWhenStarted { callback() }
            }
    }
}