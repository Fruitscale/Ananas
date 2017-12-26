package com.fruitscale.ananas

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.fruitscale.ananas.adapters.ContactListAdapter
import com.fruitscale.ananas.adapters.SelectedContactListAdapter
import kotlinx.android.synthetic.main.activity_new_group.*

class NewGroupActivity : AppCompatActivity() {
    private var mEnableEndToEndEncryption = true

    private var mSelectedContactsRecyclerViewAdapter: SelectedContactListAdapter? = null
    private var mSelectedContactsRecyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    private val mSelectedContactList: MutableSet<Contact> = hashSetOf()

    private var mContactsRecyclerViewAdapter: ContactListAdapter? = null
    private var mContactsRecyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    private val mContactList: MutableSet<Contact> = hashSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        val toolbar = create_group_main_menu
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mContactsRecyclerViewLayoutManager = LinearLayoutManager(this)
        unselected_contacts_recyclerview.layoutManager = mContactsRecyclerViewLayoutManager

        mContactList.addAll(arrayOf(
                Contact("BudD", "ditchwater.b@mail.com", "1-234-567-8910", "Bud Ditchwater"),
                Contact("BarryTheB", "barry@bmail.com", "1-234-567-8910", "Barry the Bee"),
                Contact("VanessaB", "vanessa@bloomebusiness.com", "1-234-567-8910", "Vanessa Bloome"),
                Contact("buzzw3ll", "buzzw3ll@honey.com", "1-234-567-8910", "buzzwell"),
                Contact("cptLou", "louloduca@fightforyourhoney.com", "1-234-567-8910", "Lou Lo Duca"),
                Contact("TLayton", "layton@montgomery.com", "1-234-567-8910", "Layton T. Montgomery"),
                Contact("cool_bbton", "bumbleton@cooljudges.com", "1-234-567-8910", "Judge Bumbleton")
        ))

        mSelectedContactsRecyclerViewLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        selected_contacts_recyclerview.layoutManager = mSelectedContactsRecyclerViewLayoutManager

        mSelectedContactList.addAll(arrayOf(
                mContactList.toTypedArray()[1],
                mContactList.toTypedArray()[3],
                mContactList.toTypedArray()[4],
                mContactList.toTypedArray()[6]
        ))

        // Create adapters etc.

        mSelectedContactsRecyclerViewAdapter = SelectedContactListAdapter(mSelectedContactList, this)

        selected_contacts_recyclerview.adapter = mSelectedContactsRecyclerViewAdapter

        mContactsRecyclerViewAdapter = ContactListAdapter(mContactList, mSelectedContactList)

        unselected_contacts_recyclerview.adapter = mContactsRecyclerViewAdapter


        val dividerItemDecoration = DividerItemDecoration(unselected_contacts_recyclerview.context, DividerItemDecoration.VERTICAL)
        unselected_contacts_recyclerview.addItemDecoration(dividerItemDecoration)

        updateViewForContactsSelected(!mSelectedContactList.isEmpty())


        // TODO: Add this inside an onClickListener that listens for clicks on a contact or selectedContact.
        if (!mContactList.isEmpty()) {
            text_no_contacts.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_new_group, menu)

        // https://developer.android.com/training/search/setup.html#create-sc
        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = menu.findItem(R.id.search_contacts).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName)
        )

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enable_encryption -> {
                mEnableEndToEndEncryption = !mEnableEndToEndEncryption
                if (mEnableEndToEndEncryption) { // Encryption is enabled
                    item.setIcon(R.drawable.ic_lock_outline_black_24dp)
                    item.setTitle(R.string.disable_end_to_end_encryption)
                } else { // Encryption is disabled
                    item.setIcon(R.drawable.ic_lock_open_black_24dp)
                    item.setTitle(R.string.enable_end_to_end_encryption)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun updateViewForContactsSelected(selected: Boolean) {
        if (selected) {
            no_contacts_selected.visibility = View.GONE
            selected_contacts_recyclerview.visibility = View.VISIBLE
        } else {
            no_contacts_selected.visibility = View.VISIBLE
            selected_contacts_recyclerview.visibility = View.GONE
        }
    }
}
