package com.korsik.exp.sharedlists.models

data class ListItem(
    var uid: String,
    var name: String? = null,
    var completed: Boolean? = null,
)

data class ListItemDto(
    val name: String? = null,
    val completed: Boolean? = null,
) {
    companion object {
        fun deserializeData(key: String?, obj: HashMap<*, *>): ListItem? {
            if (key != null) {
                return ListItem(
                    key,
                    obj["name"].toString(),
                    obj["completed"] as Boolean
                )
            }
            return null
        }
    }
}