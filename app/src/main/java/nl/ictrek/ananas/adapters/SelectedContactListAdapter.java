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
import nl.ictrek.ananas.NewGroupActivity;
import nl.ictrek.ananas.R;

/**
 * Created by wouter on 18-4-17.
 */

public class SelectedContactListAdapter extends RecyclerView.Adapter<SelectedContactListAdapter.ViewHolder> {
    private Set<Contact> mContactList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewContactImage;
        public TextView textViewContactName;
        public ViewHolder(View view) {
            super(view);
            imageViewContactImage = (ImageView)view.findViewById(R.id.selected_contact_imageView);
            textViewContactName = (TextView)view.findViewById(R.id.selected_contant_textView);
        }
    }

    public SelectedContactListAdapter(Set<Contact> contactList, NewGroupActivity parent) {
        this.mContactList = contactList;
    } // Add onClick action.

    @Override
    public SelectedContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.new_group_selected_contact, parent, false);
        return new SelectedContactListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectedContactListAdapter.ViewHolder holder, int position) {
        final Contact contact = mContactList.toArray(new Contact[0])[position];

        String contactName = StringUtils.capitalize(contact.getBestName().split(" ")[0]); // Get the first index of the best name split by a space. So if your name is Barry the Bee, the name will be Barry. After that, capitalize the name.
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
