package com.korsik.exp.sharedlists

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.korsik.exp.sharedlists.sharedList.SharedListsScreenViewModel
import com.korsik.exp.sharedlists.logInScreen.LoginScreen
import com.korsik.exp.sharedlists.logInScreen.LoginScreenViewModel
import com.korsik.exp.sharedlists.ui.theme.SharedListsTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val categoriesViewModel by viewModels<SharedListsScreenViewModel>()
    private val loginScreenViewModel by viewModels<LoginScreenViewModel>()

    object CONSTANTS {
        const val LIST_SCREEN = R.string.my_lists_screen_title.toString()
        const val ITEMS_SCREEN = R.string.list_items_screen_title.toString()
        const val EDIT_PROFILE = R.string.edit_profile.toString()
    }

    companion object {
        lateinit var appContext: Context
        fun getContext(): Context {
            return appContext
        }
    }
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this
        setContent {
            val title = remember {
                mutableStateOf("My Lists")
            }
            SharedListsTheme {
                AppContent(
                    titleChanged = { newTitle ->
                        title.value = newTitle
                    },
                    title = title.value
                )
            }
        }
    }

    lateinit var navController2: NavController

    @ExperimentalCoroutinesApi
    @Composable
    private fun AppContent(titleChanged: (String) -> Unit, title: String) {
        val navController = rememberNavController()
        var startScreen = "login"
        if (FirebaseAuth.getInstance().currentUser != null) {
            startScreen = "welcome"
        }
        NavHost(navController = navController, startDestination = startScreen) {
            navController2 = navController
            composable("login") {
                LoginScreen(
                    viewModel = loginScreenViewModel,
                    navController = navController
                )
            }

            composable("welcome") {
                titleChanged.invoke("My Lists")
                ScafoldWrapper(
                    title = title,
                    navController = navController,
                    screenContent = CONSTANTS.LIST_SCREEN,
                    categoriesViewModel = categoriesViewModel
                )

            }

            composable("item_list") {
                titleChanged.invoke("List Items")
                ScafoldWrapper(
                    title = title,
                    navController = navController,
                    screenContent = CONSTANTS.ITEMS_SCREEN,
                    categoriesViewModel = categoriesViewModel
                )
            }

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    SharedListsTheme {
//        CategoriesScreen(viewModel = CategoriesScreenViewModel())
//    }
//}