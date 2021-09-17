package com.korsik.exp.sharedlists.Firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.korsik.exp.sharedlists.models.ListItemDto
import com.korsik.exp.sharedlists.models.SharedList
import kotlinx.coroutines.tasks.await

class ListItemsRepo {
    companion object {

        suspend fun createItem(key: String, name: String) {
            Firebase.database.reference.child("lists").child(key).child("listItems").push()
                .setValue(
                    ListItemDto(
                        name = name,
                        completed = false
                    )
                ).await()
        }

//        fun getLists() {
//
//        }

        suspend fun updateItem(listKey: SharedList, key: String, listItem: ListItemDto) {
            Firebase.database.reference.child("lists").child(listKey.uid).child("listItems")
                .child(key)
                .setValue(
                    listItem
                ).await()
        }

        suspend fun deleteItem(key: String, value: SharedList) {
            Firebase.database.reference.child("lists").child(value.uid).child("listItems")
                .child(key).removeValue().await()
        }
    }
}