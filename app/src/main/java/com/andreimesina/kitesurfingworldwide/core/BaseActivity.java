package com.andreimesina.kitesurfingworldwide.core;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.andreimesina.kitesurfingworldwide.R;

public abstract class BaseActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // sync navigation icon state
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.isDrawerIndicatorEnabled() == false) {
            onBackPressed();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void initDrawerLayout();

    private void setBackArrowToolbar() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.open_navigation, R.string.close_navigation);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }
}
