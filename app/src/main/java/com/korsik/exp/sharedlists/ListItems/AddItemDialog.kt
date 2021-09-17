package com.korsik.exp.sharedlists.ListItems

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.korsik.exp.sharedlists.Firebase.FirebaseAPI
import com.korsik.exp.sharedlists.Firebase.UserRepo
import com.korsik.exp.sharedlists.MainActivity
import com.korsik.exp.sharedlists.sharedList.SharedListsScreenViewModel
import com.korsik.exp.sharedlists.user.UserComponentsViewModel

@ExperimentalMaterialApi
@Composable
fun AddItemDialog(
    openDialog: MutableState<Boolean>,
    screenContent: String,
    sharedLists: SharedListsScreenViewModel
) {
    val listName = remember { mutableStateOf(TextFieldValue()) }
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        },
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = AddItemDialogViewModel.setTitle(screenContent),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontWeight = FontWeight(700)
                )

            }

        },
        text = {
            TextField(
                value = listName.value,
                onValueChange = { listName.value = it },
                label = { Text(text = AddItemDialogViewModel.setPlaceholder(screenContent)) }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
                    if (screenContent == MainActivity.CONSTANTS.LIST_SCREEN)
                        sharedLists.addList(listName.value.text)
                    else if (screenContent == MainActivity.CONSTANTS.ITEMS_SCREEN)
                        sharedLists.addItem(listName.value.text)
                    else {
                        Log.i("IN THE EDIT PROFILE", "CHANGE USERNAME")
                        AddItemDialogViewModel.updateUserProfile(listName.value.text)
                    }
                }
            ) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        }
    )
}