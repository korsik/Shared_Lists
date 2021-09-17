package com.korsik.exp.sharedlists.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.lang.Exception


class AuthenticateUserRepo {
    companion object {
        suspend fun registerUser(email: String, password: String): String {
            try {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
                FirebaseAPI.addUserProfile()
                return "OK"
            } catch (e: Exception) {
                return e.localizedMessage!!
            }

        }


        suspend fun loginUser(email: String, password: String): String {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                Log.i("HELLOYOYO", "USER IS LOGEDIN  " + currentUser.uid)
                return "ALREADY LOGGED IN"
            }
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                return "OK"
            } catch (e: Exception) {
                return e.localizedMessage!!
            }
        }


    }
}