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





