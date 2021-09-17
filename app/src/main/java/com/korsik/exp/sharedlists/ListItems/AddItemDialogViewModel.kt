package com.korsik.exp.sharedlists.ListItems

import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korsik.exp.sharedlists.Firebase.FirebaseAPI
import com.korsik.exp.sharedlists.MainActivity
import com.korsik.exp.sharedlists.R
import kotlinx.coroutines.launch

object AddItemDialogViewModel: ViewModel() {

    @ExperimentalMaterialApi
    fun setTitle(screenContent: String): String {
        return when(screenContent) {
            MainActivity.CONSTANTS.LIST_SCREEN -> MainActivity.getContext().getString(R.string.my_lists_screen_title)
            MainActivity.CONSTANTS.ITEMS_SCREEN -> MainActivity.getContext().getString(R.string.list_items_screen_title)
            else -> MainActivity.getContext().getString(R.string.edit_profile)
        }
    }

    fun updateUserProfile(username: String) {
        viewModelScope.launch {
            FirebaseAPI.updateUserProfile(username = username)
        }
    }

    @ExperimentalMaterialApi
    fun setPlaceholder(screenContent: String): String {
        return when(screenContent) {
            MainActivity.CONSTANTS.LIST_SCREEN -> MainActivity.getContext().getString(R.string.add_list)
            MainActivity.CONSTANTS.ITEMS_SCREEN -> MainActivity.getContext().getString(R.string.add_item)
            else -> MainActivity.getContext().getString(R.string.change_username)
        }
    }
}