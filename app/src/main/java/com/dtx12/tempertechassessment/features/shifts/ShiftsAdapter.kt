package com.dtx12.tempertechassessment.features.shifts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dtx12.tempertechassessment.R
import com.dtx12.tempertechassessment.core.extensions.applyRoundedCorners
import com.dtx12.tempertechassessment.core.extensions.formatToPrice
import com.dtx12.tempertechassessment.core.extensions.formatWorkingHours
import com.dtx12.tempertechassessment.databinding.ItemHeaderBinding
import com.dtx12.tempertechassessment.databinding.ItemShiftBinding
import com.dtx12.tempertechassessment.features.shifts.ShiftsAdapter.ShiftsAdapterViewHolder
import com.dtx12.tempertechassessment.features.shifts.ShiftsAdapter.ShiftsAdapterViewHolder.HeaderViewHolder
import com.dtx12.tempertechassessment.features.shifts.ShiftsAdapter.ShiftsAdapterViewHolder.ShiftViewHolder
import org.threeten.bp.format.DateTimeFormatter

class ShiftsAdapter(private val viewModel: ShiftsViewModel) :
    PagingDataAdapter<ShiftItems, ShiftsAdapterViewHolder>(DIFF_CALLBACK) {

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShiftItems>() {
            override fun areItemsTheSame(oldItem: ShiftItems, newItem: ShiftItems): Boolean {
                if (oldItem is ShiftItems.ShiftItem && newItem is ShiftItems.ShiftItem) {
                    return oldItem.shift.id == newItem.shift.id
                }
                if (oldItem is ShiftItems.HeaderItem && newItem is ShiftItems.HeaderItem) {
                    return oldItem.date == newItem.date
                }
                return false
            }

            override fun areContentsTheSame(oldItem: ShiftItems, newItem: ShiftItems): Boolean {
                return oldItem == newItem
            }

        }

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_SHIFT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShiftsAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(ItemHeaderBinding.inflate(inflater, parent, false))
            VIEW_TYPE_SHIFT -> ShiftViewHolder(ItemShiftBinding.inflate(inflater, parent, false)) {
                val item = getItem(it) as ShiftItems.ShiftItem
                viewModel.selectShift(item)
            }
            else -> throw IllegalStateException()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShiftsAdapterViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ShiftItems.HeaderItem -> {
                (holder as HeaderViewHolder).bind(item)
            }
            is ShiftItems.ShiftItem -> {
                (holder as ShiftViewHolder).bind(item)
            }
            null -> throw IllegalStateException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: throw IllegalStateException()
        return when (item) {
            is ShiftItems.HeaderItem -> VIEW_TYPE_HEADER
            is ShiftItems.ShiftItem -> VIEW_TYPE_SHIFT
        }
    }

    sealed class ShiftsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class HeaderViewHolder(private val binding: ItemHeaderBinding) :
            ShiftsAdapterViewHolder(binding.root) {
            fun bind(item: ShiftItems.HeaderItem) {
                binding.headerTextView.text = item.date.format(
                    DateTimeFormatter.ofPattern("EEEE dd MMMM")
                )
            }
        }

        class ShiftViewHolder(
            private val binding: ItemShiftBinding,
            val onClickListener: (Int) -> Unit
        ) :
            ShiftsAdapterViewHolder(binding.root) {

            fun bind(item: ShiftItems.ShiftItem) {
                binding.apply {
                    shiftImageView.applyRoundedCorners(
                        shiftImageView.resources.getDimensionPixelSize(R.dimen.photo_corners_radius)
                            .toFloat()
                    )
                    val imageLink = item.shift.job.project.client.links?.heroImage
                    Glide.with(shiftImageView).load(imageLink).into(shiftImageView)

                    if (imageLink != null) {
                        Glide.with(shiftImageView).load(imageLink).into(shiftImageView)
                    } else {
                        shiftImageView.setImageDrawable(null)
                    }
                    val distanceToJobInMeters = item.distanceToJobInMeters
                    if (distanceToJobInMeters != null) {
                        val formattedDistance =
                            DistanceFormatter.format(distanceToJobInMeters, root.context)
                        categoryAndDistanceTextView.text = root.context.getString(
                            R.string.shifts_category_and_distance_format,
                            item.shift.job.category.name,
                            formattedDistance
                        )
                    } else {
                        categoryAndDistanceTextView.text = item.shift.job.category.name
                    }
                    clientTextView.text = item.shift.job.project.client.name
                    priceTextView.text = item.shift.averageEstimatedEarningPerHour.formatToPrice()
                    timeTextView.text = item.shift.formatWorkingHours()
                }
            }

            init {
                itemView.setOnClickListener {
                    val adapterPosition = bindingAdapterPosition
                    if (adapterPosition >= 0) {
                        onClickListener(adapterPosition)
                    }
                }
            }
        }
    }
}
