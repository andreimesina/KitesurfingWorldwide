package com.andreimesina.kitesurfingworldwide.activity.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andreimesina.kitesurfingworldwide.R;
import com.andreimesina.kitesurfingworldwide.activity.filter.FilterActivity;
import com.andreimesina.kitesurfingworldwide.core.AuthenticationManager;
import com.andreimesina.kitesurfingworldwide.core.BaseActivity;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.utils.Utils;

import java.util.List;

public class ListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListViewModel viewModel;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SpotAdapter mAdapter;

    /**
     * True if user received the auth token
     */
    private boolean isAuthenticated = false;

    /**
     * True if adapter was already initialized
     */
    private boolean isAdapterInitiated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initToolbar(R.id.toolbar_list);

        /**
         * Initialize RecyclerView
         */
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        mSwipeRefreshLayout = findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.pink),
                getResources().getColor(R.color.yellow));
        mSwipeRefreshLayout.setRefreshing(true);

        /**
         * Authenticate
         */
        
        viewModel.createProfile().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean changed) {
                if(isAuthenticated == false) {
                    isAuthenticated = changed;
                    observeOnDataChanged();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Only calls refresh if user is already authenticated
         */
        if(isAuthenticated) {
            onRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Utils.setBoolean(this, "authenticated", false);
    }

    /**
     * Starts observing on the Spots list.
     * Initializes the RecyclerView adapter if needed
     * and syncs all spots details for offline availability
     */
    private void observeOnDataChanged() {
        viewModel.getSpots().observe(this, new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                if(mSwipeRefreshLayout.isRefreshing() == false) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }

                if(spots.size() > 0 && isAdapterInitiated == false) {
                    mAdapter = new SpotAdapter(ListActivity.this, spots);
                    mRecyclerView.setAdapter(mAdapter);
                    isAdapterInitiated = true;

                    viewModel.syncAllSpotsDetails(spots);
                } else if(spots.size() > 0){
                    mAdapter.updateData(spots);
                }

                if(mSwipeRefreshLayout.isRefreshing()) {
                    setRefreshingOff();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_filter) {
            Intent intent = new Intent(this, FilterActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setRefreshingOff() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        viewModel.syncSpots();
    }
}
