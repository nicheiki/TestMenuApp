package com.example.hammerfood.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hammerfood.data.database.FoodItem
import com.example.hammerfood.databinding.FoodItemBinding
import com.squareup.picasso.Picasso

class ListAdapter : RecyclerView.Adapter<ListAdapter.MenuViewHolder>() {

    private var menu = mutableListOf<FoodItem>()

    fun setList(list: MutableList<FoodItem>) {
        menu = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menu[position])
    }

    override fun getItemCount(): Int = menu.size


    class MenuViewHolder(private val binding: FoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: FoodItem) {
            with(binding) {
                itemTitle.text = foodItem.title
                itemText.text = foodItem.text
                itemPrice.text = foodItem.price
                Picasso.get().load(foodItem.url).into(image)
            }
        }
    }

}