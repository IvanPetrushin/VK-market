package ru.vk_market.presentation.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.vk_market.databinding.RvItemBinding
import ru.vk_market.entities.ItemDTO
import kotlin.math.roundToInt

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemDTO) {
            binding.apply {
                Glide.with(itemView).load(item.itemThumb).into(imgItemThumb)
                item.offerPercentage.let {
                    val remainingPricePercentage = (100f - it) * 0.01
                    val priceAfterOffer = remainingPricePercentage * item.price
                    tvNewPrice.text = String.format("$%.2f", priceAfterOffer)

                    val discountInPercent = it.roundToInt()
                    tvDiscountPercent.text = String.format("-%d%%", discountInPercent)
                }
                tvOldPrice.text = String.format("%.2f", item.price)
                tvOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                tvItemTitle.text = item.title
                tvRating.text = String.format("%.2f", item.itemRating)
                tvItemDescription.text = item.description
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ItemDTO>() {

        override fun areItemsTheSame(oldItem: ItemDTO, newItem: ItemDTO): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ItemDTO, newItem: ItemDTO): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(
            RvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick: ((ItemDTO) -> Unit)? = null
}