package ru.zakoulov.vkcupf.ui.groups

import ru.zakoulov.vkcupf.data.Group

interface GroupsCallback {
    fun countOfSelectedItemsChanged(count: Int)
    fun showGroupInfo(group: Group)
}
