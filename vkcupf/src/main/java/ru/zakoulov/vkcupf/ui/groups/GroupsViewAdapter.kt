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

    var groups: List<Group> = groups
        set(value) {
            val groupsDiffCallback = GroupsDiffCallback(field, value)
            val groupsDiffResult = DiffUtil.calculateDiff(groupsDiffCallback, false)
            field = value
            groupsDiffResult.dispatchUpdatesTo(this)
            selectedGroups.clear()
            callback.countOfSelectedItemsChanged(selectedGroups.size)
        }

    val selectedGroups = mutableSetOf<Group>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val documentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_item, parent, false) as View
        return GroupViewHolder(documentView)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.apply {
            setIcon(group.img)
            setTitle(group.title)
            setSelected(group in selectedGroups)
        }
        holder.groupItem.setOnClickListener {
            if (group in selectedGroups) {
                selectedGroups.remove(group)
                holder.setSelected(false, true)
            } else {
                selectedGroups.add(group)
                holder.setSelected(true, true)
            }

            callback.countOfSelectedItemsChanged(selectedGroups.size)
        }
        holder.groupItem.setOnLongClickListener {
            callback.showGroupInfo(group)
            true
        }
    }

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
}
