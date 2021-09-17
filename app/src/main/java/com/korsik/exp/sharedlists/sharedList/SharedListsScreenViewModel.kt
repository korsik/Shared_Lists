package com.korsik.exp.sharedlists.sharedList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.korsik.exp.sharedlists.Firebase.FirebaseAPI
import com.korsik.exp.sharedlists.models.ListItem
import com.korsik.exp.sharedlists.models.ListItemDto
import com.korsik.exp.sharedlists.models.SharedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedListsScreenViewModel : ViewModel() {
    private val _sharedLists = MutableStateFlow(listOf<SharedList>())
    val sharedLists: StateFlow<List<SharedList>> get() = _sharedLists

    private val _revealedCategoryIdsList = MutableStateFlow(listOf<String>())
    val revealedCategoryIdsList: StateFlow<List<String>> get() = _revealedCategoryIdsList

    private val _listItems = MutableStateFlow(listOf<ListItem>())
    val listItems: StateFlow<List<ListItem>> get() = _listItems

    var selectedSharedList = MutableStateFlow(SharedList(""))
    var onShareSwipedSharedList = MutableStateFlow(SharedList(""))

    init {
        getFirebaseListsData()
    }

    fun getFirebaseListsData() {
        viewModelScope.launch(Dispatchers.Default) {
            FirebaseAPI.SharedListsAPI.getLists(_sharedLists, _listItems, selectedSharedList)
        }
    }

    fun onItemExpanded(categoryId: String) {
        if (_revealedCategoryIdsList.value.contains(categoryId)) return
        _revealedCategoryIdsList.value =
            _revealedCategoryIdsList.value.toMutableList().also { list ->
                list.add(categoryId)
            }
    }

    fun onItemCollapsed(categoryId: String) {
        if (!_revealedCategoryIdsList.value.contains(categoryId)) return
        _revealedCategoryIdsList.value =
            _revealedCategoryIdsList.value.toMutableList().also { list ->
                list.remove(categoryId)
            }
    }

    fun delete(categoryId: String) {
        viewModelScope.launch {
            FirebaseAPI.SharedListsAPI.deleteList(categoryId)
            _revealedCategoryIdsList.value =
                _revealedCategoryIdsList.value.toMutableList().also { list ->
                    list.remove(categoryId)
                }
            _sharedLists.value = _sharedLists.value.toMutableList().filter { it.uid != categoryId }
        }
    }

    fun addList(title: String) {
        viewModelScope.launch {
            FirebaseAPI.SharedListsAPI.createList(
                name = title,
                creator = FirebaseAuth.getInstance().currentUser!!.uid,
                sharedUsers = null,
                listItems = null
            )
            getFirebaseListsData()
        }
    }


    fun addItem(title: String) {
        viewModelScope.launch {
            FirebaseAPI.ListItems.createListItem(
                selectedSharedList.value.uid,
                name = title,
            )
            getFirebaseListsData()
        }
    }

    fun deleteItem(key: String) {
        viewModelScope.launch {
            FirebaseAPI.ListItems.deleteListItem(key, selectedSharedList.value)
        }
    }

    fun updateItem(listItem: ListItem) {
        viewModelScope.launch {
            FirebaseAPI.ListItems.updateListItem(
                selectedSharedList.value,
                listItem.uid,
                ListItemDto(listItem.name, !listItem.completed!!)
            )
        }
        getFirebaseListsData()
        Log.i("THE ITEM UPDATE", listItem.name.toString())
    }

    fun updateListItems(shareList: SharedList) {
        selectedSharedList.value = shareList
        Log.i("THE LISTS ITEMS UPDATE", selectedSharedList.toString())
        _listItems.value = _listItems.value.toMutableList().also { list ->
            list.clear()
            if (shareList.listItems != null) {
                list.addAll(shareList.listItems)
            }
        }
    }
}