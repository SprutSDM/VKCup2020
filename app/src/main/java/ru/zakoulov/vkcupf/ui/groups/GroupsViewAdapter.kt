package ru.zakoulov.vkcupf.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.DiffUtil
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

    fun setGroups(groups: List<Group>?) {
        groups ?: return
        val newWrappedGroups = wrapGroups(groups)
        val groupsDiffCallback = GroupsDiffCallback(this.wrappedGroups, newWrappedGroups)
        val groupsDiffResult = DiffUtil.calculateDiff(groupsDiffCallback, false)
        this.wrappedGroups = newWrappedGroups
        countOfSelectedGroups = 0
        groupsDiffResult.dispatchUpdatesTo(this)
        callback.countOfSelectedItemsChanged(countOfSelectedGroups)

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
            holder.setSelected(wrappedGroup.selected, true)

            countOfSelectedGroups += if (wrappedGroup.selected) 1 else -1
            callback.countOfSelectedItemsChanged(countOfSelectedGroups)
        }
        holder.groupItem.setOnLongClickListener {
            callback.showGroupInfo(wrappedGroup.group)
            true
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

        fun setSelected(selected: Boolean, withAnimation: Boolean = false) {
            imageWrapper.foreground = groupItem.context.getDrawable(
                if (selected) R.drawable.shape_image_border_selected else R.drawable.shape_image_border_normal
            )
            checkImage.visibility = if (selected) View.VISIBLE else View.GONE
            if (withAnimation) {
                animateCheckImage(selected)
            }
        }

        private fun animateCheckImage(selected: Boolean) {
            val fromX = if (selected) 0f else 1f
            val toX = if (selected) 1f else 0f
            val fromY = if (selected) 0f else 1f
            val toY = if (selected) 1f else 0f
            val anim = ScaleAnimation(fromX, toX, fromY, toY,
                checkImage.measuredWidth.toFloat() / 2, checkImage.measuredHeight.toFloat() / 2)
            anim.duration = 200
            anim.interpolator = if (selected) OvershootInterpolator() else FastOutLinearInInterpolator()
            checkImage.startAnimation(anim)
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
