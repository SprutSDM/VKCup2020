package ru.zakoulov.vkcupf.ui.groups

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.zakoulov.vkcupf.R
import ru.zakoulov.vkcupf.data.Group

class GroupsViewAdapter(
    groups: List<Group>,
    private val callback: GroupsCallback
) : RecyclerView.Adapter<GroupsViewAdapter.GroupViewHolder>() {

    var groups: List<Group> = groups
        set(value) {
            //val documentDiffCallback = DocumentDiffCallback(field, value)
            //val documentDiffResult = DiffUtil.calculateDiff(documentDiffCallback, false)
            field = value
            //documentDiffResult.dispatchUpdatesTo(this)
        }

    var countOfSelectedGroups = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_item, parent, false) as View
        Log.d("abacaba", "parentWidth ${parent.width}")
        return GroupViewHolder(documentView)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.apply {
            setIcon(group.img)
            setTitle(group.title)
        }
        holder.groupItem.setOnClickListener {
            countOfSelectedGroups += if (holder.selected) -1 else 1
            holder.switchSelectState()
            callback.countOfSelectedItemsChanged(countOfSelectedGroups)
        }
    }

    class GroupViewHolder(val groupItem: View, selected: Boolean = false) : RecyclerView.ViewHolder(groupItem) {

        var selected = selected
            private set

        private val imageWrapper: FrameLayout = groupItem.findViewById(R.id.group_img_wrapper)
        private val checkImage: ImageView = groupItem.findViewById(R.id.group_check_circle)

        fun setIcon(src: String) {
            val groupImg = groupItem.findViewById<ImageView>(R.id.group_img)
            groupImg.clipToOutline = true
            Picasso.get()
                .load(src)
                .fit()
                .centerCrop()
                .into(groupImg)
        }

        fun switchSelectState() {
            selected = !selected
            imageWrapper.foreground = groupItem.context.getDrawable(
                if (selected) R.drawable.shape_image_border_selected else R.drawable.shape_image_border_normal
            )
            checkImage.visibility = if (selected) View.VISIBLE else View.GONE
        }

        fun setTitle(title: String) {
            groupItem.findViewById<TextView>(R.id.group_title).text = title
        }
    }
}
