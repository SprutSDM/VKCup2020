package ru.zakoulov.vkcupf.ui.groups

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

    private var wrappedGroups: List<GroupWrapper> = wrapGroups(groups)
    private var countOfSelectedGroups = 0

    init {
        setGroups(groups)
    }

    fun setGroups(groups: List<Group>) {
        this.wrappedGroups = wrapGroups(groups)
        countOfSelectedGroups = 0
    }

    private fun wrapGroups(groups: List<Group>) = groups.map { GroupWrapper(it, false) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_item, parent, false) as View
        return GroupViewHolder(documentView)
    }

    override fun getItemCount() = wrappedGroups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val wrappedGroup = wrappedGroups[position]
        holder.apply {
            setIcon(wrappedGroup.group.img)
            setTitle(wrappedGroup.group.title)
            setSelected(wrappedGroup.selected)
        }
        holder.groupItem.setOnClickListener {
            wrappedGroup.switchSelected()
            holder.setSelected(wrappedGroup.selected)

            countOfSelectedGroups += if (wrappedGroup.selected) 1 else -1
            callback.countOfSelectedItemsChanged(countOfSelectedGroups)
        }
    }

    fun getSelectedGroups() = wrappedGroups.filter { it.selected }.map { it.group }

    class GroupViewHolder(val groupItem: View) : RecyclerView.ViewHolder(groupItem) {

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

        fun setSelected(selected: Boolean) {
            imageWrapper.foreground = groupItem.context.getDrawable(
                if (selected) R.drawable.shape_image_border_selected else R.drawable.shape_image_border_normal
            )
            checkImage.visibility = if (selected) View.VISIBLE else View.GONE
        }

        fun setTitle(title: String) {
            groupItem.findViewById<TextView>(R.id.group_title).text = title
        }
    }

    class GroupWrapper(val group: Group, var selected: Boolean) {
        fun switchSelected() {
            selected = !selected
        }
    }
}
