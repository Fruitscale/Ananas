package com.fruitscale.ananas.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fruitscale.ananas.Contact
import com.fruitscale.ananas.NewGroupActivity
import com.fruitscale.ananas.R
import kotlinx.android.synthetic.main.new_group_selected_contact.view.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by wouter on 18-4-17.
 *
 * Listadapter for the selected contacts.
 *
 * @param mContactList The list with selected contacts.
 * @param parent The parent activity.
 */
//TODO: Add logic for clicking etc.
class SelectedContactListAdapter(private val mContactList: Set<Contact>, private val parent: NewGroupActivity) : RecyclerView.Adapter<SelectedContactListAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedContactListAdapter.ViewHolder = SelectedContactListAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.new_group_selected_contact, parent, false)
    )

    override fun onBindViewHolder(holder: SelectedContactListAdapter.ViewHolder, position: Int) {
        val contact = mContactList.toTypedArray()[position]

        val contactName = StringUtils.capitalize(contact.bestName.split(" ").first()) // Get the first index of the best name split by a space. So if your name is Barry the Bee, the name will be Barry. After that, capitalize the name.
        holder.view.selected_contant_textView.text = contactName
        holder.itemView.setOnClickListener {
            //TODO: Add action
        }
        // TODO: Set contact image.
    }

    override fun getItemCount(): Int = mContactList.size
}
