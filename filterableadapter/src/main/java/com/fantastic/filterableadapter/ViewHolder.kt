package com.fantastic.filterableadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ViewHolder<T>(view : View, internal val updateView: (T) -> Unit) : RecyclerView.ViewHolder(view)