package com.korsik.exp.sharedlists.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.korsik.exp.sharedlists.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class SharedListsRepo {
    companion object {

        suspend fun createList(
            name: String,
            creator: String,
            sharedUsers: MutableList<String>?,
            listItems: MutableList<ListItemDto>?
        ) {
            Firebase.database.reference.child("lists").push()
                .setValue(
                    SharedListsDto(
                        name = name,
                        creator = creator,//FirebaseAuth.getInstance().currentUser!!.uid,
                        sharedUsers = sharedUsers,
                        listItems = listItems
                    )
                ).await()
        }

        suspend fun updateList(key: String, updatedList: SharedListsDto) {
            Firebase.database.reference.child("lists").child(key).setValue(updatedList).await()
            Log.i("UPDATED ", "VALUE UPDATED")
        }

        suspend fun deleteList(key: String) {
            Firebase.database.reference.child("lists").child(key).removeValue().await()
//            Log.i("DELETED ", "VALUE DELETED")
        }


        fun childChangedListener(
            _viewModelSharedLists: MutableStateFlow<List<SharedList>>,
            _listItems: MutableStateFlow<List<ListItem>>,
            selectedSharedList: MutableStateFlow<SharedList>
        ) {


            _viewModelSharedLists.value = mutableListOf()
            var sharedLists = mutableListOf<SharedList>()
            val childListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("TAG", "onChildAdded:" + dataSnapshot.key!!)
                    sharedLists = _viewModelSharedLists.value.toMutableList()
                    val key = dataSnapshot.key
                    val shareListValues = dataSnapshot.value as HashMap<*, *>
                    SharedListsDto.deserializeData(key, shareListValues)?.let {
                        if (!sharedLists.contains(it)) {
                            if (checkUsers(it)) {
                                sharedLists.add(it)
                            }
                        }
                    }

                    runBlocking {
                        launch {
                            _viewModelSharedLists.emit(sharedLists)
                        }
                    }
                    // ...
                }

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    Log.d("TAG", "onChildChanged: ${dataSnapshot.key}")

                    val key = dataSnapshot.key
                    val shareListValues = dataSnapshot.value as HashMap<*, *>
                    _viewModelSharedLists.value =
                        _viewModelSharedLists.value.toMutableList().also { list ->
                            list.removeAll(list.filter { it.uid == key })
                            SharedListsDto.deserializeData(key, shareListValues)
                                ?.let { sharedlist ->
                                    if (!sharedLists.contains(sharedlist)) {

                                        Log.i(
                                            "CHANGED A LIST",
                                            checkUsers(sharedlist).toString()
                                        )
                                        if (checkUsers(sharedlist)) {
                                            list.add(sharedlist)
                                        }
                                    }

                                    if (selectedSharedList.value.uid == key.toString()) {
                                        _listItems.value = mutableListOf()
                                        val listItems = mutableListOf<ListItem>()
                                        sharedlist.listItems?.let { listItems.addAll(it) }
                                        runBlocking {
                                            launch {
                                                _listItems.emit(listItems)
                                            }
                                        }
                                    }
                                }
                        }

                    // ...
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    Log.d("TAG", "onChildRemoved:" + dataSnapshot.key!!)
                    val key = dataSnapshot.key
                    _viewModelSharedLists.value =
                        _viewModelSharedLists.value.toMutableList().also { list ->
                            list.removeAll(list.filter { it.uid == key })

                        }
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("TAG", "onChildMoved:" + dataSnapshot.key!!)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w("TAG", "postComments:onCancelled", databaseError.toException())

                }
            }
            Firebase.database.reference.child("lists").addChildEventListener(childListener)
        }


        suspend fun updateSharedUsersOnList(key: String, users: List<String>) {
            Firebase.database.reference.child("lists").child(key).child("sharedUsers")
                .setValue(users).await()
        }

        private fun checkUsers(sharedlist: SharedList): Boolean {
            Log.i("CURRENT USER", FirebaseAuth.getInstance().currentUser!!.uid)
            Log.i("CREATOR USER", sharedlist.creator!!)
            Log.i("SHARED USERS", sharedlist.sharedUsers.toString())
            if (FirebaseAuth.getInstance().currentUser!!.uid == sharedlist.creator) {
                return true
            } else if (sharedlist.sharedUsers?.contains(FirebaseAuth.getInstance().currentUser!!.uid) == true) {
                return true
            }
            return false
        }
    }


}