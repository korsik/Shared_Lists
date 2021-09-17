package com.korsik.exp.sharedlists.Firebase

import android.util.Log
import androidx.compose.runtime.MutableState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.korsik.exp.sharedlists.models.SharedList
import com.korsik.exp.sharedlists.models.User
import com.korsik.exp.sharedlists.models.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class UserRepo {

    companion object {

        suspend fun addUserProfile() {
            Firebase.database.reference.child("users").push()
                .setValue(
                    UserDto(
                        id = FirebaseAuth.getInstance().currentUser!!.uid,
                        username = FirebaseAuth.getInstance().currentUser!!.email
                    )
                ).await()
        }

        suspend fun updateUserProfile(key: String, username: String) {
            val test = Firebase.database.reference.child("users")
                .orderByChild("id").equalTo(key).get().await()
            var userKey: String? = null
            test.children.forEach {
                userKey = it.key
            }
            if (userKey == null)
                return
            Log.i("TEST THE GET REQ", userKey.toString())
            Firebase.database.reference.child("users").child(userKey!!)
                .setValue(
                    UserDto(
                        id = FirebaseAuth.getInstance().currentUser!!.uid,
                        username = username,
                        email = FirebaseAuth.getInstance().currentUser!!.email
                    )
                ).await()
        }

        suspend fun getUserProfiles(userProfiles: MutableStateFlow<List<User>>) {
            userProfiles.value = mutableListOf()
            val users = mutableListOf<User>()
            Firebase.database.reference.child("users").get().addOnSuccessListener {
                it.children.forEach { dataSnapshot ->
                    val user = dataSnapshot.getValue(UserDto().javaClass)
//                    Log.i("firebase", "Got value ${user}")
                    users.add(User(dataSnapshot.key, user!!.id, user.username, user.email))
                }  //as Array<*>

                runBlocking {
                    launch {
                        userProfiles.emit(users)
                    }
                }

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }.await()
        }
    }
}