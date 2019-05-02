package com.andreimesina.kitesurfingworldwide.data.repository;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andreimesina.kitesurfingworldwide.core.AuthenticationManager;
import com.andreimesina.kitesurfingworldwide.data.database.Database;
import com.andreimesina.kitesurfingworldwide.data.database.dao.SpotDao;
import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;
import com.andreimesina.kitesurfingworldwide.data.webservice.WebService;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.CountriesResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.ProfileResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotDetailsResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotIdResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotsResponse;
import com.andreimesina.kitesurfingworldwide.utils.Utils;

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

        if(Utils.isNetworkConnected(app) == false) {
            Toast.makeText(app, "You are offline. Check your connection", Toast.LENGTH_SHORT).show();
            isAuthenticated.setValue(AuthenticationManager.getInstance().isOfflineAuth());
            return isAuthenticated;
        }

        webService.createProfile()
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        if(response.isSuccessful()) {
                            Profile profile = response.body().getProfile();

                            AuthenticationManager.getInstance().setProfile(profile);
                            Utils.setString(app, "email", profile.getEmail());
                            Utils.setBoolean(app, "authenticated", true);
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
                dao.invalidateAllSpots();

                for(Spot spot : spots) {
                    spot.setToDisplay(true);
                }
                dao.insertAllSpots(spots);
            }
        });
    }

    public LiveData<List<Spot>> getAllSpots() {
        if(Utils.isNetworkConnected(app)) {
            syncSpots();
        }

        return getAllSpotsFromDb();
    }

    public void syncSpots() {
        if(spotsSyncedAt.isAfter(Instant.now().minus(3, ChronoUnit.SECONDS))) {
            Timber.d("Spots have been already synced in the last 3 seconds. Skipping...");
//            Toast.makeText(app, "Last sync less than 3 secs ago", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(Utils.isNetworkConnected(app) == false) {
            return ;
        }

        webService.getAllSpots()
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

    private void insertSpotDetails(SpotDetails spotDetails) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long insertId = dao.insertSpotDetails(spotDetails);
                Timber.d("************ INSERT ID: " + insertId + " **************");
            }
        });
    }

    public LiveData<SpotDetails> getSpotDetails(String spotId) {
        LiveData<SpotDetails> spotDetails = getSpotDetailsFromDb(spotId);

        if(Utils.isNetworkConnected(app) == false) {
            return spotDetails;
        }

        if(spotDetails.getValue() != null) {
            return spotDetails;
        } else {
            syncSpotDetails(spotId);
        }

        return getSpotDetailsFromDb(spotId);
    }

    private void syncSpotDetails(String spotId) {
        webService.getSpotDetails(spotId)
                .enqueue(new Callback<SpotDetailsResponse>() {
                    @Override
                    public void onResponse(Call<SpotDetailsResponse> call, Response<SpotDetailsResponse> response) {
                        if(response.isSuccessful()) {
                            insertSpotDetails(response.body().getSpotDetails());
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

    public void syncAllSpotDetails(List<Spot> spots) {
        if(Utils.isNetworkConnected(app) == false) {
            return ;
        }

        for(Spot spot : spots) {
            syncSpotDetails(spot.getId());
        }
    }

    private LiveData<SpotDetails> getSpotDetailsFromDb(String spotId) {
        return dao.getSpotDetails(spotId);
    }

    public MutableLiveData<List<String>> getAllSpotCountries() {
        MutableLiveData<List<String>> countries = new MutableLiveData<>();

        webService.getAllSpotCountries()
                .enqueue(new Callback<CountriesResponse>() {
                    @Override
                    public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                        if (response.isSuccessful()) {
                            countries.setValue(response.body().getCountries());
                        } else {
                            Toast.makeText(app, "Failed to fetch countries", Toast.LENGTH_SHORT).show();
                            Timber.d("Failed to fetch countries");
                        }
                    }

                    @Override
                    public void onFailure(Call<CountriesResponse> call, Throwable t) {
                        Toast.makeText(app, "Could not load countries", Toast.LENGTH_SHORT).show();
                        Timber.d("Could not load countries");
                    }
                });

        return countries;
    }

    public void addSpotToFavorites(String spotId) {
        webService.addSpotToFavorites(spotId)
                .enqueue(new Callback<SpotIdResponse>() {
                    @Override
                    public void onResponse(Call<SpotIdResponse> call, Response<SpotIdResponse> response) {
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
                    public void onFailure(Call<SpotIdResponse> call, Throwable t) {
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
                .enqueue(new Callback<SpotIdResponse>() {
                    @Override
                    public void onResponse(Call<SpotIdResponse> call, Response<SpotIdResponse> response) {
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
                    public void onFailure(Call<SpotIdResponse> call, Throwable t) {
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
