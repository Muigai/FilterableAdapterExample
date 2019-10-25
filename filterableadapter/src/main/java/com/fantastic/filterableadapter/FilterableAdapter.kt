package com.fantastic.filterableadapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.ConcurrentHashMap
import android.widget.Filter


class FilterableAdapter<T>(original : List<T>,
                           private val createView : (ViewGroup, Int) -> Pair<View, (T) -> Unit>,
                           private val filter: (String) -> List<T>
                           ) : RecyclerView.Adapter<ViewHolder<T>>()
{

    var filtered = original.toList()

    val original = original.toList()

    val filterResults = ConcurrentHashMap<String, List<T>>()

    private fun performFiltering(constraint: String?): Int {
        if (constraint.isNullOrEmpty()) {
            return original.size
        }

        val filteredByConstraint = filter(constraint)

        filterResults[constraint] = filteredByConstraint

        return filteredByConstraint.size
    }

    private fun publishResults(constraint: String?) {

        filtered = if (constraint.isNullOrEmpty())
            original
        else
            filterResults.get(constraint)!!

        notifyDataSetChanged()
    }

    override fun getItemCount() = filtered.size

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int)=
        holder.updateView(filtered[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {

        var (view, updateView) = createView(parent, viewType);

        return  ViewHolder(view, updateView);
    }

    fun getFilter() : Filter = UserFilter(this);


    private class UserFilter<T>(private val adapter : FilterableAdapter<T>) : Filter()
    {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results =  FilterResults()

            val count = adapter.performFiltering(constraint?.toString());

            results.count = count;

            return results;
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) =
            adapter.publishResults(constraint?.toString());
    }

}


