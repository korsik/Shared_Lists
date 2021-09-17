package com.korsik.exp.sharedlists

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.korsik.exp.sharedlists.ListItems.AddItemDialog
import com.korsik.exp.sharedlists.ListItems.ItemsList
import com.korsik.exp.sharedlists.sharedList.SharedListsScreen
import com.korsik.exp.sharedlists.sharedList.SharedListsScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun ScafoldWrapper(
    title: String,
    navController: NavController,
    screenContent: String,
    categoriesViewModel: ViewModel
) {
    Scaffold(
        backgroundColor = Color.White,
//        drawerContent = { Text(text = " My Lists") },
        topBar = {
            TopBar(
                title,
                logOutClicked = {
                    navController.navigate("login")
                },
                categories = categoriesViewModel
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
//            if (screenContent == "My Lists")
            FloatingActionButton(categories = categoriesViewModel, screenContent)
        }
//        content = { screenContent }
    ) {
        if (screenContent == MainActivity.CONSTANTS.LIST_SCREEN) {
            SharedListsScreen(
                viewModel = categoriesViewModel as SharedListsScreenViewModel,
                listItemClicked = {
                    categoriesViewModel.updateListItems(it!!)
                    navController.navigate("item_list")
                }
            )
        }
        else {
            ItemsList(
                viewModel = categoriesViewModel as SharedListsScreenViewModel,
                listItemClicked = {
//                    navController.navigate("item_list")
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun TopBar(title: String, logOutClicked: () -> Unit, categories: ViewModel) {
    var expanded = remember { mutableStateOf(false) }
    var isShown = remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = title)
        },
        elevation = 2.dp,
        actions = {
            IconButton(
                onClick = {
                    expanded.value = !expanded.value
//                    FirebaseAuth.getInstance().signOut()
//                    logOutClicked()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dots_three_vertical),
                    tint = Color.White,
                    contentDescription = "delete action",
                )
            }
//            Box {
//                // Back arrow here
//                Row(modifier = Modifier.clickable{
//                    expanded.value = !expanded.value
//                }.padding(5.dp)) { // Anchor view
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_dots_three_vertical),
//                        tint = Color.White,
//                        contentDescription = "Open dropdown action",
//                    )
//                }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(onClick = {
                    Log.i("ON DROPDOWN", "EDIT ACCOUNT")
                    isShown.value = true
                    expanded.value = false
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "edit account action",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text("Edit Account")
                }
                DropdownMenuItem(onClick = {
//                    FirebaseAuth.getInstance().signOut()
//                    logOutClicked()
                    Log.i("ON DROPDOWN", "LOGOUT")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_log_out),
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "logout action",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text("Log out")
                }
                Divider()
                DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
                    Text("Send Feedback")
                }
            }
//            }


        }
    )

    if(isShown.value) {
        AddItemDialog(
            openDialog = isShown,
            MainActivity.CONSTANTS.EDIT_PROFILE,
            categories as SharedListsScreenViewModel
        )
    }

}

@ExperimentalMaterialApi
@Composable
fun FloatingActionButton(categories: ViewModel, screenContent: String) {
    val openDialog = remember { mutableStateOf(false) }
    FloatingActionButton(onClick = {
        openDialog.value = true
//        categories as CategoriesScreenViewModel
//        categories.add()
    }) {
        Icon(
            Icons.Filled.Add,
            null,
            tint = MaterialTheme.colors.background
        )
    }
    if (openDialog.value) {
        AddItemDialog(
            openDialog = openDialog,
            screenContent,
            categories as SharedListsScreenViewModel
        )
    }
}