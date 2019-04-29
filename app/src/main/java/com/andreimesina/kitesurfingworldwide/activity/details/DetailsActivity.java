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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import timber.log.Timber;

public class DetailsActivity extends BaseActivity implements OnMapReadyCallback {

    private DetailsViewModel viewModel;

    private TextView mTextViewCountry;
    private TextView mTextViewLatitude;
    private TextView mTextViewLongitude;
    private TextView mTextViewWindProbability;
    private TextView mTextViewWhenToGo;

    private SupportMapFragment mapFragment;

    private GoogleMap mGoogleMap;

    private Spot mSpot;

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
            viewModel.getSpotDetails(spotId).observe(this, new Observer<Spot>() {
                @Override
                public void onChanged(Spot spot) {
                    if(spot != null) {
                        mSpot = spot;
                        updateFields(mSpot);

                        if(mSpot.getLatitude() != 0 || mSpot.getLongitude() != 0) {
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

    private void updateFields(Spot spot) {
        setTitle(spot.getName());

        mTextViewCountry.setText(spot.getCountry());
        mTextViewLatitude.setText(String.valueOf(spot.getLatitude()));
        mTextViewLongitude.setText(String.valueOf(spot.getLongitude()));
        mTextViewWindProbability.setText(String.valueOf(spot.getWindProbability()) + "%");
        mTextViewWhenToGo.setText(spot.getWhenToGo());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mSpot.isFavorite()) {
            getMenuInflater().inflate(R.menu.toolbar_favorite_on, menu);
        } else {
            getMenuInflater().inflate(R.menu.toolbar_favorite_off, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(mSpot.getId() == null || mSpot.getId().length() == 0) {
            return false;
        }

        if(id == R.id.action_favorite_off) {
            item.setIcon(R.drawable.star_on);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            ServiceProvider.getInstance().getRepository().addSpotToFavorites(mSpot.getId());
        } else if(id == R.id.action_favorite_on) {
            item.setIcon(R.drawable.star_off_details);
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            ServiceProvider.getInstance().getRepository().removeSpotFromFavorites(mSpot.getId());
        } else {
            onBackPressed();
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
        Timber.d("onMapReady: " + mSpot.getLatitude() + " : " + mSpot.getLongitude());
        LatLng spotGeo = new LatLng(mSpot.getLatitude(), mSpot.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(spotGeo).title(mSpot.getName()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(spotGeo));
    }
}
