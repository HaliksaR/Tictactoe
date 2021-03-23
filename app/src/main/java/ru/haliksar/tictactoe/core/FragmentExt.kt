package ru.haliksar.tictactoe.core

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder


inline fun Fragment.toast(@StringRes message: Int) {
    toast(getString(message))
}

inline fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

inline fun Fragment.showDialog(
    builder: MaterialAlertDialogBuilder.() -> MaterialAlertDialogBuilder
) {
    builder(MaterialAlertDialogBuilder(requireContext())).show()
}

fun Fragment.navigate(
    @IdRes action: Int,
    @IdRes hostId: Int? = null,
    navOptions: NavOptions? = null,
    function: Bundle.() -> Unit = {},
) {
    if (hostId == null) {
        findNavController()
    } else {
        Navigation.findNavController(requireActivity(), hostId)
    }.navigate(action, Bundle().apply(function), navOptions)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.navigateSafe(
    @IdRes action: Int,
    @IdRes hostId: Int? = null,
    @IdRes currentDestination: Int? = null,
    navOptions: NavOptions? = null,
    function: Bundle.() -> Unit = {},
) {
    val controller = if (hostId == null) {
        findNavController()
    } else {
        Navigation.findNavController(requireActivity(), hostId)
    }
    if (controller.currentDestination?.id == currentDestination) {
        controller.navigate(action, Bundle().apply(function), navOptions)
    }
}