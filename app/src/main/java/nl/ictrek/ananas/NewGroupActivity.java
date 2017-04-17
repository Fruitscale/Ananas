package nl.ictrek.ananas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class NewGroupActivity extends AppCompatActivity {
    private boolean mEnableEndToEndEncryption = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_group_main_menu);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.enable_encryption:
                mEnableEndToEndEncryption = !mEnableEndToEndEncryption;
                if (mEnableEndToEndEncryption) { // Encryption is enabled
                    item.setIcon(R.drawable.ic_lock_outline_black_48dp);
                    item.setTitle(R.string.disable_end_to_end_encryption);
                } else { // Encryption is disabled
                    item.setIcon(R.drawable.ic_lock_open_black_48dp);
                    item.setTitle(R.string.enable_end_to_end_encryption);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
