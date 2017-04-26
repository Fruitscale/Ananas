package nl.ictrek.ananas.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import nl.ictrek.ananas.Contact;
import nl.ictrek.ananas.R;

/**
 * Created by wouter on 18-4-17.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private Set<Contact> mContactList;
    private Set<Contact> mSelectedContactList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewContactImage;
        public ImageView imageViewChecked;
        public TextView textViewContactName;
        public ViewHolder(View view) {
            super(view);
            imageViewContactImage = (ImageView)view.findViewById(R.id.new_group_contact_imageView);
            textViewContactName = (TextView)view.findViewById(R.id.new_group_contact_textView);
            imageViewChecked = (ImageView)view.findViewById(R.id.new_group_contact_checked_imageView);
        }
    }

    public ContactListAdapter(Set<Contact> contactList) {
        this(contactList, null);
    }

    public ContactListAdapter(Set<Contact> contactList, Set<Contact> selectedContactList) {
        this.mContactList = contactList;
        this.mSelectedContactList = selectedContactList;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.new_group_contact, parent, false);
        return new ContactListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactListAdapter.ViewHolder holder, final int position) {
        final Contact contact = mContactList.toArray(new Contact[0])[position];
        if(mSelectedContactList.contains(contact))
            holder.imageViewChecked.setVisibility(View.VISIBLE);

        String contactName = contact.getBestName();
        holder.textViewContactName.setText(contactName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add action
            }
        });
        // TODO: Set contact image.
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}
