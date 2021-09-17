package com.korsik.exp.sharedlists.sharedList

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.korsik.exp.sharedlists.R

@Composable
fun SwipeActions(
    actionIconSize: Dp,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = {
                onDelete()
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bin),
                    tint = Color.Gray,
                    contentDescription = "delete action",
                )
            }
        )

        IconButton(
            modifier = Modifier.size(actionIconSize),
            onClick = {
                onShare()
            },
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    tint = Color.Gray,
                    contentDescription = "share action",
                )
            }
        )
    }
}