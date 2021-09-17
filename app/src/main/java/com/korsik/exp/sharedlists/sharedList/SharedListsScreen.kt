package com.korsik.exp.sharedlists.sharedList


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.korsik.exp.sharedlists.R
import com.korsik.exp.sharedlists.dp
import com.korsik.exp.sharedlists.models.SharedList
import com.korsik.exp.sharedlists.user.ShareListWithUsers
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val ACTION_ITEM_SIZE = 56
const val CARD_HEIGHT = 56
const val CARD_OFFSET = 168f // 168f // we have 3 icons in a row, so that's 56 * 3

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun SharedListsScreen(
    viewModel: SharedListsScreenViewModel,
    listItemClicked: (SharedList?) -> Unit
) {
    val sharedLists = viewModel.sharedLists.collectAsState()
    val revealedSharedListsIds = viewModel.revealedCategoryIdsList.collectAsState()

    ListView(
        categories = sharedLists.value,
        revealedCategoryIds = revealedSharedListsIds.value,
        viewModel = viewModel,
        listItemClicked
    )
}


//fun DeleteAction(categoryId: String) {
//    Log.i("HELLO_TESTING", "THIS IS THE DELETE ACTION!!! ON $categoryId")
//}

fun toggle(toggle: MutableState<Boolean>) {
    toggle.value = !toggle.value
}

@ExperimentalMaterialApi
@Composable
fun ListView(
    categories: List<SharedList>,
    revealedCategoryIds: List<String>,
    viewModel: SharedListsScreenViewModel,
    listItemClicked: (SharedList) -> Unit
) {
    val toggle = remember { mutableStateOf(false) }

    LazyColumn {
        items(items = categories, key = { cat -> cat.uid }) { category ->
            Box(Modifier.fillMaxWidth()) {
                SwipeActions(
                    actionIconSize = ACTION_ITEM_SIZE.dp,
                    onDelete = { viewModel.delete(categoryId = category.uid) },
                    onShare = {
                        toggle(toggle)
                        // TO-DO
                        // assign the selected list that will be shared with more users
                        viewModel.onShareSwipedSharedList.value = category
                    }
                )
                DraggableSharedList(
                    category = category,
                    isRevealed = revealedCategoryIds.contains(category.uid),
                    cardHeight = CARD_HEIGHT.dp,
                    categoryOffset = CARD_OFFSET.dp(),
                    onExpand = { viewModel.onItemExpanded(category.uid) },
                    onCollapse = { viewModel.onItemCollapsed(category.uid) },
                    listItemClicked = listItemClicked
                )
            }
        }
    }
    if (toggle.value) {
//        CircularProgressBar()
        ShareListWithUsers(openDialog = toggle, sharedLists = viewModel)
    }
}


@Composable
fun CircularProgressBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.Center

    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun PoUps() {
    Column(
        modifier = Modifier
//                .fillMaxWidth()
            .padding(3.dp)
            .shadow(elevation = 5.dp, shape = RectangleShape),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clickable {

                },
            horizontalArrangement = Arrangement.End,
        ) {
            Text("Edit Account")
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colors.secondary,
                contentDescription = "edit account action",
            )

        }
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clickable {

                },
            horizontalArrangement = Arrangement.End
        ) {
            Text("Log out")
            Spacer(modifier = Modifier.padding(horizontal = 3.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_log_out),
                tint = MaterialTheme.colors.secondary,
                contentDescription = "logout action",
            )

        }
//                }

    }
}





