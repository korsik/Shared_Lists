package com.korsik.exp.sharedlists.ListItems

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.korsik.exp.sharedlists.sharedList.SharedListsScreenViewModel
import com.korsik.exp.sharedlists.models.ListItem

@ExperimentalMaterialApi
@Composable
fun ItemsList(
    viewModel: SharedListsScreenViewModel,
    listItemClicked: () -> Unit
) {
    val categories = viewModel.listItems.collectAsState()
//    val revealedCategoryIds = viewModel.revealedCategoryIdsList.collectAsState()

    LazyColumn {
        items(items = categories.value, key = { cat -> cat.uid }) { category ->
//            val unread = remember { mutableStateOf(false) }
            val dismissState = rememberDismissState(
                confirmStateChange = {
//                    if (it == DismissValue.DismissedToEnd) unread.value = !unread.value
                    if (it == DismissValue.DismissedToEnd) {
                        viewModel.updateItem(category)
//                        Log.i("INTERACTIVE LIST ITEM ",  "THE INDEX ${categories.value.indexOf(category)}" +
//                                "AND THE VALUES:  ${category}")
                        categories.value.filter { listItem ->
                            listItem.uid == category.uid
                        }[0].completed = !category.completed!!
                    }

                    it != DismissValue.DismissedToEnd

                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = { BackgroundP(dismissState = dismissState) },
                dismissContent = {
                    DismissContent(
                        dismissState = dismissState,
                        category = category,
                        viewModel,
                        listItemClicked
                    )
                }
            )
        }
    }

}

@ExperimentalMaterialApi
@Composable
fun BackgroundP(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return
    val color = animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.LightGray
            DismissValue.DismissedToEnd -> Color.Green
            DismissValue.DismissedToStart -> Color.Red
        }
    )
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = when (direction) {
        DismissDirection.StartToEnd -> Icons.Default.Done
        DismissDirection.EndToStart -> Icons.Default.Delete
    }
    val scale = animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color.value)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale.value)
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun DismissContent(
    dismissState: DismissState,
    category: ListItem,
    viewModel: SharedListsScreenViewModel,
    listItemClicked: () -> Unit
) {
    if (dismissState.isDismissed(direction = DismissDirection.EndToStart)) {
//        Log.i("THIS IS THE DELETION", "YOU MAKE IT YEAAAAAAAAAAH")
        viewModel.deleteItem(category.uid)

    }
    Card(
        elevation = animateDpAsState(
            if (dismissState.dismissDirection != null) 4.dp else 0.dp
        ).value
    ) {
        ListItem(
            text = {
                Text(category.name.toString(), fontWeight = if (category.completed!!) FontWeight.Bold else null)
            },
            secondaryText = { Text("Swipe me left or right!") },
            modifier = Modifier
                .clickable {
                    listItemClicked()
                }
                .background(color = if (category.completed!!) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.background)
        )
    }
}
