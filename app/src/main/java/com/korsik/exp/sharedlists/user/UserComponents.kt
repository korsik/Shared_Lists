package com.korsik.exp.sharedlists.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.korsik.exp.sharedlists.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.korsik.exp.sharedlists.models.User
import com.korsik.exp.sharedlists.sharedList.*

@Composable
fun ShareListWithUsers(
    openDialog: MutableState<Boolean>,
    sharedLists: SharedListsScreenViewModel
) {
//    val listName = remember { mutableStateOf(TextFieldValue()) }
    UserComponentsViewModel.retrieveUserProfiles(sharedLists.onShareSwipedSharedList)
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
                    text = stringResource(R.string.share_list_header),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontWeight = FontWeight(700)
                )

            }

        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f)
                    .background(
                        color = MaterialTheme.colors.background,
                        shape = RoundedCornerShape(5)
                    )
            ) {
                Column {
                    TextField(
                        value = UserComponentsViewModel.searchBarText.value,
                        onValueChange = {
                            UserComponentsViewModel.updateSearchBarText(it)
                        },
                        label = { Text(text = "Search Users") }
                    )
                    AvailableUsersList(
                        userList = UserComponentsViewModel.nameList.value
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openDialog.value = false
//                    sharedLists.addList(listName.value.text)
                    UserComponentsViewModel.updateSharedUsers(
                        sharedLists.onShareSwipedSharedList.value
                    ) { sharedLists.getFirebaseListsData() }
                }
            ) {
                Text(text = "Add List")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    openDialog.value = false
//                    sharedLists.onShareSwipedSharedList.resetReplayCache()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        },
        modifier = Modifier.wrapContentSize()
    )
}

@Composable
fun AvailableUsersList(userList: List<User>) {
    LazyColumn {
        items(items = userList) { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = user.username!!,
                    style = TextStyle.Default,
                    fontSize = 21.sp
                )
                CheckBoxComponent(formState = user) {
                    UserComponentsViewModel.updateCheckboxState(user)
                }//UserComponentsViewModel.checkboxState.value[0].checkState
            }
            Divider()
        }
    }
}

@Composable
fun CheckBoxComponent(
    formState: User,
    stateChange: () -> Unit
) {
    val isChecked = remember {
        mutableStateOf(formState.checkState)
    }
    Checkbox(
        checked = isChecked.value, //formState.checkState,
        onCheckedChange = {
            isChecked.value = it
            stateChange()
//            Log.i("PEW LASEWR", "IN PEW CHECKBOX  ${formState.checkState}")
        },
        colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
    )
}
