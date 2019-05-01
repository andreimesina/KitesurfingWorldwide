package com.andreimesina.kitesurfingworldwide.activity.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andreimesina.kitesurfingworldwide.R;
import com.andreimesina.kitesurfingworldwide.core.BaseActivity;
import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import timber.log.Timber;

public class DetailsActivity extends BaseActivity implements OnMapReadyCallback {

    private DetailsViewModel viewModel;

    private Menu mMenu;

    private TextView mTextViewCountry;
    private TextView mTextViewLatitude;
    private TextView mTextViewLongitude;
    private TextView mTextViewWindProbability;
    private TextView mTextViewWhenToGo;

    private SupportMapFragment mapFragment;

    private GoogleMap mGoogleMap;

    private SpotDetails mSpotDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initToolbar(R.id.toolbar_details);
        setBackButtonToolbar();

        String spotId = getSpotIdFromList();
        if(spotId != null) {
            initViews();

            viewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);
            viewModel.getSpotDetails(spotId).observe(this, new Observer<SpotDetails>() {
                @Override
                public void onChanged(SpotDetails spotDetails) {
                    if(spotDetails != null) {
                        mSpotDetails = spotDetails;
                        updateFields(mSpotDetails);

                        if(mSpotDetails.getLatitude() != 0 || mSpotDetails.getLongitude() != 0) {
                            mapFragment.getMapAsync(DetailsActivity.this);
                        }
                    }
                }
            });
        } else {
            onBackPressed();
            Toast.makeText(this, "Failed to open details", Toast.LENGTH_SHORT).show();
        }

    }

    private String getSpotIdFromList() {
        Intent intent = getIntent();
        String spotId = intent.getStringExtra("spotId");

        return  spotId;
    }

    private void initViews() {
        mTextViewCountry = findViewById(R.id.textView_countryValue_details);
        mTextViewLatitude = findViewById(R.id.textView_latitudeValue_details);
        mTextViewLongitude = findViewById(R.id.textView_longitudeValue_details);
        mTextViewWindProbability = findViewById(R.id.textView_windProbabilityValue_details);
        mTextViewWhenToGo = findViewById(R.id.textView_whenToGoValue_details);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    private void updateFields(SpotDetails spotDetails) {
        setTitle(spotDetails.getName());

        mTextViewCountry.setText(spotDetails.getCountry());
        mTextViewLatitude.setText(String.valueOf(spotDetails.getLatitude()));
        mTextViewLongitude.setText(String.valueOf(spotDetails.getLongitude()));
        mTextViewWindProbability.setText(spotDetails.getWindProbability() + "%");
        mTextViewWhenToGo.setText(spotDetails.getWhenToGo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mSpotDetails.isFavorite()) {
            getMenuInflater().inflate(R.menu.toolbar_favorite_on, menu);
        } else {
            getMenuInflater().inflate(R.menu.toolbar_favorite_off, menu);
        }

        mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mSpotDetails.getSpotId() == null || mSpotDetails.getSpotId().length() == 0) {
            return false;
        }

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if(mMenu.getItem(0).getTitle().equals("Favorite off")) {
            mMenu.clear();
            getMenuInflater().inflate(R.menu.toolbar_favorite_on, mMenu);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            ServiceProvider.getInstance().getRepository().addSpotToFavorites(mSpotDetails.getSpotId());
        } else if(mMenu.getItem(0).getTitle().equals("Favorite on")) {
            mMenu.clear();
            getMenuInflater().inflate(R.menu.toolbar_favorite_off, mMenu);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            ServiceProvider.getInstance().getRepository().removeSpotFromFavorites(mSpotDetails.getSpotId());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Add a marker and move the camera
        Timber.d("onMapReady: " + mSpotDetails.getLatitude() + " : " + mSpotDetails.getLongitude());
        LatLng spotGeo = new LatLng(mSpotDetails.getLatitude(), mSpotDetails.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(spotGeo).title(mSpotDetails.getName()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(spotGeo));
    }
}
