package com.korsik.exp.sharedlists.models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.IgnoreExtraProperties


data class SharedList(
    val uid: String,
    val name: String? = null,
    val creator: String? = null,
    val sharedUsers: MutableList<String>? = null,
    val listItems: MutableList<ListItem>? = null,
    var isChecked: Boolean = false
)

@IgnoreExtraProperties
data class SharedListsDto(
//    val uid: String? = null,
    val name: String? = null,
    val creator: String? = null,
    val sharedUsers: MutableList<String>? = null,
    val listItems: MutableList<ListItemDto>? = null,
) {
    companion object {
        fun deserializeData(key: String?, obj: HashMap<*, *>): SharedList? {

            if (key != null) {
                var listItems = mutableListOf<ListItem>()
                if (obj["listItems"] != null) {
                    val items = obj["listItems"] as HashMap<*, *>
                    for (item in items) {
                        ListItemDto.deserializeData(
                            item.key.toString(),
                            item.value as HashMap<*, *>
                        )?.let {
                            listItems.add(it)
                        }
                    }
                }

                return SharedList(
                    key,
                    obj["name"].toString(),
                    obj["creator"].toString(),
                    obj["sharedUsers"] as MutableList<String>? ,
                    listItems

                )
            }
            return null
        }
    }
}





