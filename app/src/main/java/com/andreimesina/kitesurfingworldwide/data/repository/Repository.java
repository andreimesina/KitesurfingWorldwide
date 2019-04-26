package com.andreimesina.kitesurfingworldwide.data.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andreimesina.kitesurfingworldwide.core.AuthenticationManager;
import com.andreimesina.kitesurfingworldwide.data.dao.SpotDao;
import com.andreimesina.kitesurfingworldwide.data.database.Database;
import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.ProfileResponse;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;
import com.andreimesina.kitesurfingworldwide.data.webservice.WebService;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotDetailsResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotsResponse;

import org.threeten.bp.Instant;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class Repository {

    private Application app;

    private WebService webService;
    private SpotDao dao;

    private Executor executor = Executors.newCachedThreadPool();

    private MutableLiveData<Boolean> isAuthenticated;

    private Instant spotsSyncedAt = Instant.EPOCH;

    public Repository(Application application) {
        app = application;

        webService = new WebService();

        Database database = Database.getInstance(app);
        dao = database.getSpotDao();
    }

    public MutableLiveData<Boolean> createProfile() {
        isAuthenticated = new MutableLiveData<>();

        webService.createProfile()
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if(response.isSuccessful()) {
                            Profile profile = response.body().getProfile();

                            AuthenticationManager.getInstance().setProfile(profile);
                            isAuthenticated.setValue(true);
                        } else {
                            Toast.makeText(
                                    app, "Could not authenticate.", Toast.LENGTH_SHORT)
                                    .show();
                            Timber.d("Could not authenticate.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Toast.makeText(app, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        Timber.d("Authentication failed.");
                    }
                });

        return isAuthenticated;
    }

    private void insertAllSpots(List<Spot> spots) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAllSpots(spots);
            }
        });
    }

    public LiveData<List<Spot>> getAllSpots(SpotFilter spotFilter) {
        syncSpots(spotFilter);

        return getAllSpotsFromDb();
    }

    public void syncSpots(SpotFilter spotFilter) {
        if(spotsSyncedAt.isAfter(Instant.now().minus(10, ChronoUnit.SECONDS))) {
            Timber.d("Spots have been already synced in the last 10 minutes. Skipping...");
            return ;
        }

        webService.getAllSpots(spotFilter)
                .enqueue(new Callback<SpotsResponse>() {
                    @Override
                    public void onResponse(Call<SpotsResponse> call, Response<SpotsResponse> response) {
                        if(response.isSuccessful()) {
                            spotsSyncedAt = Instant.now();
                            insertAllSpots(response.body().getSpots());
                        } else {
                            Toast.makeText(
                                    app, "Could not fetch latest spots.", Toast.LENGTH_SHORT)
                                    .show();
                            Timber.d("Could not fetch latest spots.");
                        }
                    }

                    @Override
                    public void onFailure(Call<SpotsResponse> call, Throwable t) {
                        Toast.makeText(app, "Failed to fetch latest spots", Toast.LENGTH_SHORT)
                                .show();
                        Timber.d(t);
                    }
                });
    }

    private LiveData<List<Spot>> getAllSpotsFromDb() {
        return dao.getAllSpots();
    }

    private void updateSpotDetails(Spot spot) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateSpotDetails(spot);
            }
        });
    }

    public LiveData<Spot> getSpotDetails(String spotId) {
        syncSpotDetails(spotId);

        return getSpotDetailsFromDb(spotId);
    }

    private void syncSpotDetails(String spotId) {
        webService.getSpotDetails(spotId)
                .enqueue(new Callback<SpotDetailsResponse>() {
                    @Override
                    public void onResponse(Call<SpotDetailsResponse> call, Response<SpotDetailsResponse> response) {
                        if(response.isSuccessful()) {
                            updateSpotDetails(response.body().getSpot());
                        } else {
                            Timber.d("Could not sync spot details. spotId: %s", spotId);
                        }
                    }

                    @Override
                    public void onFailure(Call<SpotDetailsResponse> call, Throwable t) {
                        Timber.d("Failed to sync spot details. spotId: %s", spotId);
                    }
                });
    }

    private LiveData<Spot> getSpotDetailsFromDb(String spotId) {
        return dao.getSpotDetails(spotId);
    }

    public void addSpotToFavorites(String spotId) {
        webService.addSpotToFavorites(spotId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(app, "Added to favorites", Toast.LENGTH_SHORT).show();
                            addSpotToFavoritesDb(spotId);
                        } else {
                            Toast.makeText(app, "Could not add to favorites.", Toast.LENGTH_SHORT)
                                    .show();
                            Timber.d("Could not add to favorites. spotId: %s", spotId);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(app, "Failed to add to favorites", Toast.LENGTH_SHORT)
                                .show();
                        Timber.d("Failed to add to favorites. spotId: %s", spotId);
                    }
                });
    }

    private void addSpotToFavoritesDb(String spotId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.addSpotToFavorites(spotId);
            }
        });
    }

    public void removeSpotFromFavorites(String spotId) {
        webService.removeSpotFromFavorites(spotId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(app, "Removed from favorites", Toast.LENGTH_SHORT)
                                    .show();
                            removeSpotFromFavoritesDb(spotId);
                        } else {
                            Toast.makeText(
                                    app, "Could not remove from favorites.", Toast.LENGTH_SHORT)
                                    .show();
                            Timber.d("Could not remove from favorites. spotId: %s", spotId);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(app, "Failed to remove from favorites",
                                Toast.LENGTH_SHORT).show();
                        Timber.d("Failed to remove from favorites. spotId: %s", spotId);
                    }
                });
    }

    private void removeSpotFromFavoritesDb(String spotId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.removeSpotFromFavorites(spotId);
            }
        });
    }
}
