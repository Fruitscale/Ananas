package nl.ictrek.ictrix;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.fab);
        fam.setClosedOnTouchOutside(true);

        final AnimatorSet animateOut = new AnimatorSet();
        ObjectAnimator rotate = ObjectAnimator.ofFloat(fam.getMenuIconView(), "rotation", -180, 0);
        animateOut.playTogether(rotate);

        final AnimatorSet animateIn = new AnimatorSet();
        ObjectAnimator rotateback = ObjectAnimator.ofFloat(fam.getMenuIconView(), "rotation", 180, 0);
        animateIn.playTogether(rotateback);

        fam.setIconToggleAnimatorSet(animateOut);
        fam.getMenuIconView().setImageResource(R.drawable.ic_add_black_24dp);

        fam.setOnMenuButtonClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                fam.toggle(true);
                if (fam.isOpened()) {
                    fam.getMenuIconView().setImageResource(R.drawable.ic_add_black_24dp);
                    fam.setIconToggleAnimatorSet(animateOut);
                } else {
                    fam.getMenuIconView().setImageResource(R.drawable.ic_create_black_24dp);
                    fam.setIconToggleAnimatorSet(animateIn);
                }
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        } else if(id == R.id.nav_settings) {
            // Handle the settings action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
