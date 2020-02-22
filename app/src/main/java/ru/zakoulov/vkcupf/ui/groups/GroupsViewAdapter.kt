package ru.zakoulov.vkcupf.ui.groups

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.Group

class GroupsViewAdapter(
    groups: List<Group>
) : RecyclerView.Adapter<GroupsViewAdapter.GroupViewHolder>() {

    var groups: List<Group> = groups
        set(value) {
            //val documentDiffCallback = DocumentDiffCallback(field, value)
            //val documentDiffResult = DiffUtil.calculateDiff(documentDiffCallback, false)
            field = value
            //documentDiffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_item, parent, false) as View
        Log.d("abacaba", "parentWidth ${parent.width}")
        return GroupViewHolder(documentView)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.groupItem.apply {
            setIcon(this, group.img)
            setTitle(this, group.title)
        }
    }

    private fun setIcon(groupItem: View, src: String) {
        val groupImg = groupItem.findViewById<ImageView>(R.id.group_img)
        groupImg.clipToOutline = true
        Picasso.get()
            .load(src)
            .fit()
            .centerCrop()
            .into(groupImg)
    }

    private fun setTitle(groupItem: View, title: String) {
        groupItem.findViewById<TextView>(R.id.group_title).text = title
    }

    class GroupViewHolder(val groupItem: View) : RecyclerView.ViewHolder(groupItem)
}
