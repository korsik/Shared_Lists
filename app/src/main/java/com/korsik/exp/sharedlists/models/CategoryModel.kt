package com.korsik.exp.sharedlists.models

import androidx.compose.runtime.Immutable
import java.util.*

@Immutable
data class CategoryModel(val id: UUID, val title: String, var isChecked: Boolean = false)