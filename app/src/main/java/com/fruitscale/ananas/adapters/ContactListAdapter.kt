package com.fruitscale.ananas.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fruitscale.ananas.Contact
import com.fruitscale.ananas.R
import kotlinx.android.synthetic.main.new_group_contact.view.*

/**
 * Created by wouter on 18-4-17.
 *
 * Listadapter for the contacts in the new group activity
 *
 * @param mContactList All the contacts
 * @param mSelectedContactList All the selected contacts
 */
class ContactListAdapter(private val mContactList: Set<Contact>, private val mSelectedContactList: Set<Contact> = emptySet()) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListAdapter.ViewHolder = ContactListAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.new_group_contact, parent, false)
    )

    override fun onBindViewHolder(holder: ContactListAdapter.ViewHolder, position: Int) {
        val contact = mContactList.toTypedArray()[position]
        if (mSelectedContactList.contains(contact)) {
            holder.view.new_group_contact_textView.visibility = View.VISIBLE
        }

        val contactName = contact.bestName
        holder.view.new_group_contact_textView.text = contactName
        holder.itemView.setOnClickListener {
            //TODO: Add action
        }
        // TODO: Set contact image.
    }

    override fun getItemCount(): Int = mContactList.size
}
