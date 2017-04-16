package nl.ictrek.ananas;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import nl.ictrek.ananas.adapters.ChatListAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private List<Chat> mChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.fab);
        final FloatingActionButton fabGroup = (FloatingActionButton) findViewById(R.id.fab_group);
        fam.setClosedOnTouchOutside(true);

        final AnimatorSet animateOut = new AnimatorSet();
        ObjectAnimator rotate = ObjectAnimator.ofFloat(fam.getMenuIconView(), "rotation", -180, 0);
        animateOut.playTogether(rotate);

        final AnimatorSet animateIn = new AnimatorSet();
        ObjectAnimator rotateback = ObjectAnimator.ofFloat(fam.getMenuIconView(), "rotation", 180, 0);
        animateIn.playTogether(rotateback);

        fam.setIconToggleAnimatorSet(animateOut);
        fam.getMenuIconView().setImageResource(R.drawable.ic_add_black_24dp);

        fam.setOnMenuButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fam.toggle(true);
                if (fam.isOpened()) {
                    // TODO: Replace toast with real action
                    Toast toast = Toast.makeText(getBaseContext(), "Create new private chat", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        fabGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.close(true);
                Toast toast = Toast.makeText(getBaseContext(), "Create new group chat", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        fam.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    fam.getMenuIconView().setImageResource(R.drawable.ic_create_black_24dp);
                    fam.setIconToggleAnimatorSet(animateIn);
                } else {
                    fam.getMenuIconView().setImageResource(R.drawable.ic_add_black_24dp);
                    fam.setIconToggleAnimatorSet(animateOut);
                }
            }
        });

        // Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Chat list
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_chatList);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);

        mChatList = new ArrayList<>();
        mChatList.add(new Chat("ICTrix development chat", "Test", "12:34", Chat.Type.GROUP));
        mChatList.add(new Chat("This is a chat with a long title to test", "It also has a long chat summary; this is for testing purposes", "10:01", Chat.Type.PERSONAL));
        mChatList.add(new Chat("Matrix HQ", "Test 3", "13:37", Chat.Type.GROUP));
        mChatList.add(new Chat("Foo Bar", "Baz", "04:20", Chat.Type.PERSONAL));

        mRecyclerViewAdapter = new ChatListAdapter(mChatList);

        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new_group) {
            // Handle the new group action
        } else if (id == R.id.nav_settings) {
            // Handle the settings action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
