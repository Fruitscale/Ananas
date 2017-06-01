package nl.ictrek.ananas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

import nl.ictrek.ananas.adapters.ChatListAdapter

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mRecyclerViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mRecyclerViewLayoutManager: RecyclerView.LayoutManager
    private val mChatList: MutableList<Chat> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar
        setSupportActionBar(toolbar)

        fab.setClosedOnTouchOutside(true)

        val animateOut = AnimatorSet().apply {
            playTogether(
                    // rotate
                    ObjectAnimator.ofFloat(fab.menuIconView, "rotation", -180f, 0f)
            )
        }

        val animateIn = AnimatorSet().apply {
            playTogether(
                    // rotate back
                    ObjectAnimator.ofFloat(fab.menuIconView, "rotation", 180f, 0f)
            )
        }

        fab.iconToggleAnimatorSet = animateOut
        fab.menuIconView.setImageResource(R.drawable.ic_add_black_24dp)

        fab.setOnMenuButtonClickListener {
            fab.toggle(true)
            if (fab.isOpened) {
                startActivity(Intent(this, ChatActivity::class.java))
                // TODO: Replace toast with real action
                Toast.makeText(baseContext, "Create new private chat", Toast.LENGTH_SHORT).show()
            }
        }

        fab_group.setOnClickListener {
            fab.close(true)
            startActivity(Intent(this, NewGroupActivity::class.java))
        }

        fab.setOnMenuToggleListener { opened ->
            if (opened) {
                fab.menuIconView.setImageResource(R.drawable.ic_create_black_24dp)
                fab.iconToggleAnimatorSet = animateIn
            } else {
                fab.menuIconView.setImageResource(R.drawable.ic_add_black_24dp)
                fab.iconToggleAnimatorSet = animateOut
            }
        }

        val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        recyclerView_chatList.setHasFixedSize(true)

        mRecyclerViewLayoutManager = LinearLayoutManager(this)
        recyclerView_chatList.layoutManager = mRecyclerViewLayoutManager


        //TODO: replace test data with real data.
        mChatList.addAll(arrayOf(
                Chat("ICTrix development chat", "Test", "12:34", Chat.Type.GROUP),
                Chat("This is a chat with a long title to test", "It also has a long chat summary; this is for testing purposes", "10:01", Chat.Type.PERSONAL),
                Chat("Matrix HQ", "Test 3", "13:37", Chat.Type.GROUP),
                Chat("Foo Bar", "Baz", "04:20", Chat.Type.PERSONAL)
        ))

        mRecyclerViewAdapter = ChatListAdapter(mChatList)

        recyclerView_chatList.adapter = mRecyclerViewAdapter

        val dividerItemDecoration = DividerItemDecoration(recyclerView_chatList.context, DividerItemDecoration.VERTICAL)
        recyclerView_chatList.addItemDecoration(dividerItemDecoration)
    }

    override fun onBackPressed() {
        //drawer
        (findViewById(R.id.drawer_layout) as DrawerLayout).let {
            if(it.isDrawerOpen(GravityCompat.START)) {
                it.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.itemId == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here
        item.itemId.let {
            if (it == R.id.nav_new_group) {
                // Handle the new group action
            } else if (it == R.id.nav_settings) {
                // Handle the settings action
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        // drawer
        (findViewById(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)

        return true
    }
}
