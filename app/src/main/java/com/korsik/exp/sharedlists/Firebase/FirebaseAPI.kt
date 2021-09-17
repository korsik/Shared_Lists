package com.korsik.exp.sharedlists.Firebase

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.auth.FirebaseAuth
import com.korsik.exp.sharedlists.models.*
import kotlinx.coroutines.flow.MutableStateFlow

class FirebaseAPI {
    companion object {

        suspend fun registerUser(email: String, password: String): String {
            return AuthenticateUserRepo.registerUser(email, password)
        }

        suspend fun loginUserCoroutine(email: String, password: String): String {
            return AuthenticateUserRepo.loginUser(email, password)
        }

        suspend fun addUserProfile() {
            UserRepo.addUserProfile()
        }

        suspend fun getUserProfiles(userProfiles: MutableStateFlow<List<User>>) {
            UserRepo.getUserProfiles(userProfiles)
        }

        suspend fun updateUserProfile(username: String) {
            UserRepo.updateUserProfile(FirebaseAuth.getInstance().currentUser!!.uid,username)
        }

    }

    object ListItems {
        suspend fun createListItem(key: String, name: String) {
            ListItemsRepo.createItem(key, name)
        }

        suspend fun updateListItem(listKey: SharedList, key: String, listItem: ListItemDto) {
            ListItemsRepo.updateItem(listKey, key, listItem)
        }

        suspend fun deleteListItem(key: String, value: SharedList) {
            ListItemsRepo.deleteItem(key, value)
            Log.i("THIS IS THE KEY", key)
        }
    }

    object SharedListsAPI {

        suspend fun createList(
            name: String, creator: String, sharedUsers: MutableList<String>?,
            listItems: MutableList<ListItemDto>?
        ) {
            SharedListsRepo.createList(name, creator, sharedUsers, listItems)
        }

        fun getLists(
            _categories: MutableStateFlow<List<SharedList>>,
            _listItems: MutableStateFlow<List<ListItem>>,
            selectedSharedList: MutableStateFlow<SharedList>
        ) {
            SharedListsRepo.childChangedListener(_categories, _listItems, selectedSharedList)
        }

        suspend fun updateList(key: String, updatedList: SharedListsDto) {
            SharedListsRepo.updateList(key, updatedList)
        }

        suspend fun deleteList(key: String) {
            SharedListsRepo.deleteList(key)
        }

        suspend fun updateSharedUsersOnList(key: String, users: List<String>) {
            SharedListsRepo.updateSharedUsersOnList(key, users)
        }
    }
}