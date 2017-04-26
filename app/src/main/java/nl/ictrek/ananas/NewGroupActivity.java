package nl.ictrek.ananas;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import nl.ictrek.ananas.adapters.ContactListAdapter;
import nl.ictrek.ananas.adapters.SelectedContactListAdapter;

public class NewGroupActivity extends AppCompatActivity {
    private boolean mEnableEndToEndEncryption = true;

    private RecyclerView mSelectedContactsRecyclerView;
    private SelectedContactListAdapter mSelectedContactsRecyclerViewAdapter;
    private RecyclerView.LayoutManager mSelectedContactsRecyclerViewLayoutManager;
    private Set<Contact> mSelectedContactList;

    private RecyclerView mContactsRecyclerView;
    private ContactListAdapter mContactsRecyclerViewAdapter;
    private RecyclerView.LayoutManager mContactsRecyclerViewLayoutManager;
    private Set<Contact> mContactList;

    private View mNoContactsTextView;
    private View mNoContactsSelectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_group_main_menu);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) { // This will never be sure, but just to be true.
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set contacts list.
        mContactsRecyclerView = (RecyclerView) findViewById(R.id.unselected_contacts_recyclerview);

        mContactsRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mContactsRecyclerView.setLayoutManager(mContactsRecyclerViewLayoutManager);

        mContactList = new HashSet<>();
        mContactList.add(new Contact("BudD", "ditchwater.b@mail.com", "1-234-567-8910", "Bud Ditchwater"));
        mContactList.add(new Contact("BarryTheB", "barry@bmail.com", "1-234-567-8910", "Barry the Bee"));
        mContactList.add(new Contact("VanessaB", "vanessa@bloomebusiness.com", "1-234-567-8910", "Vanessa Bloome"));
        mContactList.add(new Contact("buzzw3ll", "buzzw3ll@honey.com", "1-234-567-8910", "buzzwell"));
        mContactList.add(new Contact("cptLou", "louloduca@fightforyourhoney.com", "1-234-567-8910", "Lou Lo Duca"));
        mContactList.add(new Contact("TLayton", "layton@montgomery.com", "1-234-567-8910", "Layton T. Montgomery"));
        mContactList.add(new Contact("cool_bbton", "bumbleton@cooljudges.com", "1-234-567-8910", "Judge Bumbleton"));




        // Set selected contacts list.
        mSelectedContactsRecyclerView = (RecyclerView) findViewById(R.id.selected_contacts_recyclerview);

        mSelectedContactsRecyclerViewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSelectedContactsRecyclerView.setLayoutManager(mSelectedContactsRecyclerViewLayoutManager);


        mSelectedContactList = new LinkedHashSet<>();
        mSelectedContactList.add(mContactList.toArray(new Contact[0])[1]);
        mSelectedContactList.add(mContactList.toArray(new Contact[0])[3]);
        mSelectedContactList.add(mContactList.toArray(new Contact[0])[4]);
        mSelectedContactList.add(mContactList.toArray(new Contact[0])[6]);

        // Create adapters etc.

        mSelectedContactsRecyclerViewAdapter = new SelectedContactListAdapter(mSelectedContactList, this);

        mSelectedContactsRecyclerView.setAdapter(mSelectedContactsRecyclerViewAdapter);

        mContactsRecyclerViewAdapter = new ContactListAdapter(mContactList, mSelectedContactList);

        mContactsRecyclerView.setAdapter(mContactsRecyclerViewAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContactsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mContactsRecyclerView.addItemDecoration(dividerItemDecoration);

        // Remove unnecessary texts
        mNoContactsSelectedTextView = findViewById(R.id.no_contacts_selected);
        updateViewForContactsSelected(!mSelectedContactList.isEmpty());


        mNoContactsTextView = findViewById(R.id.text_no_contacts);
        if(!mContactList.isEmpty()) // TODO: Add this inside an onClickListener that listens for clicks on a contact or selectedContact.
            mNoContactsTextView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_group, menu);

        // https://developer.android.com/training/search/setup.html#create-sc
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_contacts).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.enable_encryption:
                mEnableEndToEndEncryption = !mEnableEndToEndEncryption;
                if (mEnableEndToEndEncryption) { // Encryption is enabled
                    item.setIcon(R.drawable.ic_lock_outline_black_24dp);
                    item.setTitle(R.string.disable_end_to_end_encryption);
                } else { // Encryption is disabled
                    item.setIcon(R.drawable.ic_lock_open_black_24dp);
                    item.setTitle(R.string.enable_end_to_end_encryption);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateViewForContactsSelected(boolean selected) {
        if(selected) {
            mNoContactsSelectedTextView.setVisibility(View.GONE);
            mSelectedContactsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mNoContactsSelectedTextView.setVisibility(View.VISIBLE);
            mSelectedContactsRecyclerView.setVisibility(View.GONE);
        }
    }
}
