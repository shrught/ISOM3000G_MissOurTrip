package com.example.missourtrip

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.statistics_layout.view.*

class StatisticsAdapter(private var summary: ArrayList<Summary>): RecyclerView.Adapter<StatisticsAdapter.StatisticsHolder>() {

    class StatisticsHolder(view: View):RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.name)
        val currencies: LinearLayout = view.findViewById(R.id.currencies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statistics_layout,parent,false)
        return StatisticsHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsAdapter.StatisticsHolder, position: Int) {
        val categorySummary = summary[position]
        val context = holder.name.context

        holder.name.text = categorySummary.category
        val newCurrencyTotals = ArrayList<TextView>()
        for ((col, value) in summary[position].currencies.withIndex()) {
            newCurrencyTotals.add(TextView(context))
            newCurrencyTotals[col].text = "%s %.2f".format(value.currency, value.total)
            holder.currencies.addView(newCurrencyTotals[col])
        }

        if (categorySummary.currencies.size == 0) {
            newCurrencyTotals.add(TextView(context))
            newCurrencyTotals[0].text = "-"
            newCurrencyTotals[0].maxWidth = 400
            holder.currencies.addView(newCurrencyTotals[0])
        }
    }

    override fun getItemCount(): Int {
        return summary.size
    }

    fun setData(summary: ArrayList<Summary>){
        this.summary = summary
        notifyDataSetChanged()
    }
}