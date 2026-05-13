package com.example.kutirakone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kutirakone.R
import com.example.kutirakone.model.Scrap

class ScrapAdapter(
    private val list: ArrayList<Scrap>,
    private val isHorizontal: Boolean = false
) : RecyclerView.Adapter<ScrapAdapter.ScrapViewHolder>() {

    companion object {
        private const val TYPE_GRID = 0
        private const val TYPE_HORIZONTAL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHorizontal) TYPE_HORIZONTAL else TYPE_GRID
    }

    class ScrapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMaterial: TextView = itemView.findViewById(R.id.txtMaterial)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtDistance: TextView = itemView.findViewById(R.id.txtDistance)
        val imgScrap: ImageView = itemView.findViewById(R.id.imgScrap)
        val txtUserName: TextView? = itemView.findViewById(R.id.txtUserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val layout = if (viewType == TYPE_HORIZONTAL) R.layout.item_scrap_horizontal else R.layout.item_scrap
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ScrapViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        val scrap = list[position]
        holder.txtMaterial.text = scrap.material
        holder.txtPrice.text = "₹${scrap.price} / ${scrap.unit}"
        holder.txtDistance.text = scrap.distance
        holder.txtUserName?.text = scrap.userName

        Glide.with(holder.itemView.context)
            .load(scrap.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgScrap)
    }

    override fun getItemCount(): Int = list.size
}