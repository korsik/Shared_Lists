package com.korsik.exp.sharedlists.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.korsik.exp.sharedlists.Firebase.FirebaseAPI
import com.korsik.exp.sharedlists.models.SharedList
import com.korsik.exp.sharedlists.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object UserComponentsViewModel : ViewModel() {
    //    val checkboxState = mutableStateOf(listOf<Boolean>())
    val nameList = mutableStateOf(listOf<User>())
    val searchBarText = mutableStateOf("")

    private val userProfiles = MutableStateFlow(listOf<User>())

    fun updateCheckboxState(newState: User) {
//        Log.i("updateCheckboxState 1 ", "IN PEW CHECKBOX  ${newState.checkState}")
        newState.checkState = !newState.checkState
//        Log.i("updateCheckboxState 2", "IN PEW CHECKBOX  ${newState.checkState}")
        userProfiles.value = userProfiles.value.toMutableStateList().also { list ->
            list.find { it.id == newState.id }?.checkState = newState.checkState
        }
//        Log.i(
//            "updateCheckboxState 3",
//            "IN PEW CHECKBOX  ${userProfiles.value.find { it.id == newState.id }?.checkState}"
//        )
//        if(checkboxState.value.contains(newState)) return
//        checkboxState.value = checkboxState.value.toMutableList().also { list ->
//            list.add(newState)
//        }
    }

    fun updateSearchBarText(newState: String) {
        searchBarText.value = newState
    }

    fun updateSharedUsers(list: SharedList, firebaseListsData: () -> Unit) {
        Log.i("ON ADD SHARED USERS", list.name!!)
        val sharedUserList = mutableListOf<String>()
        userProfiles.value.forEach { user ->
            if (user.checkState) {
                Log.i("SHARED USER FOR LIST", user.username!!)
                sharedUserList.add(user.id!!)
            }
        }
        viewModelScope.launch {
            FirebaseAPI.SharedListsAPI.updateSharedUsersOnList(list.uid, sharedUserList)
        }
        firebaseListsData()
    }


    fun retrieveUserProfiles(sharedLists: MutableStateFlow<SharedList>) {
        viewModelScope.launch {
            FirebaseAPI.getUserProfiles(userProfiles)
            Log.i("USER PROFILES", userProfiles.value.toString())
            nameList.value = mutableListOf()
            userProfiles.value.forEach {
                nameList.value = nameList.value.toMutableStateList().also { list ->
                    if (FirebaseAuth.getInstance().currentUser?.uid != it.id &&
                            it.id != sharedLists.value.creator)
                        list.add((it))
                }
            }
            if (sharedLists.value.sharedUsers != null) {
                sharedLists.value.sharedUsers!!.forEach { user ->
                    nameList.value.find { users -> users.id == user }?.checkState = true
                }
            }
        }
    }
}

//data class UserCheckbox(val userId: UUID, val checkState: Boolean = false)