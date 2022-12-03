package com.example.missourtrip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

data class CategoryAdapter(private val categoryList: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_item,
        parent, false)
        return CategoryViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]

        holder.categoryName.text = currentItem.name
        holder.categoryDescription.text = currentItem.description


    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        val categoryDescription: TextView = itemView.findViewById(R.id.tv_category_description)

    }

}