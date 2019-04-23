package com.andreimesina.kitesurfingworldwide.data.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andreimesina.kitesurfingworldwide.data.dao.SpotDao;
import com.andreimesina.kitesurfingworldwide.data.database.Database;
import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;
import com.andreimesina.kitesurfingworldwide.data.webservice.Api;
import com.andreimesina.kitesurfingworldwide.data.webservice.WebService;
import com.andreimesina.kitesurfingworldwide.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private Application app;

    private Api apiService;
    private SpotDao spotDao;
    private LiveData<List<Spot>> allSpots;

    public Repository(Application application) {
        app = application;

        Database database = Database.getInstance(application);
        spotDao = database.getSpotDao();

        WebService webService = new WebService();
        apiService = webService.getApi();

        createUser(Profile.getInstance().getEmail());
        allSpots = getAllSpotsFromDb();
    }

    public void createUser(String email) {
        apiService.createProfile(email)
                .enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response) {
                        if(response.isSuccessful()) {
                            Profile.getInstance().setEmail(response.body().getEmail());
                            Profile.getInstance().setToken(response.body().getToken());
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {
                        Toast.makeText(app, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void insertAllSpots(List<Spot> spots) {
        new InsertAllSpotsAsyncTask(spotDao).execute(spots);
    }

    public LiveData<List<Spot>> getAllSpots(SpotDetails spotDetails) {
        if(Utils.isNetworkConnected(app)) {
            return getAllSpotsFromApi(spotDetails);
        }

        return getAllSpotsFromDb();
    }

    private LiveData<List<Spot>> getAllSpotsFromApi(SpotDetails spotDetails) {
        final MutableLiveData<List<Spot>> data = new MutableLiveData<>();

        apiService.getAllSpots(Profile.getInstance().getToken(), spotDetails)
                .enqueue(new Callback<List<Spot>>() {
                    @Override
                    public void onResponse(Call<List<Spot>> call, Response<List<Spot>> response) {
                        if(response.isSuccessful()) {
                            data.setValue(response.body());
                            insertAllSpots(data.getValue());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Spot>> call, Throwable t) {
                        Toast.makeText(app, "Failed to fetch latest spots", Toast.LENGTH_SHORT)
                                .show();
                        data.setValue(getAllSpotsFromDb().getValue());
                    }
                });

        return data;
    }

    private LiveData<List<Spot>> getAllSpotsFromDb() {
        return spotDao.getAllSpots();
    }

    private void updateSpotDetails(Spot spot) {
        new UpdateSpotDetailsAsyncTask(spotDao).execute(spot);
    }

    public LiveData<Spot> getSpotDetails(String spotId) {
        if(Utils.isNetworkConnected(app)) {
            return getSpotDetailsFromApi(spotId);
        }

        return getSpotDetailsFromDb(spotId);
    }

    private LiveData<Spot> getSpotDetailsFromApi(String spotId) {
        MutableLiveData<Spot> data = new MutableLiveData<>();

        apiService.getSpotDetails(Profile.getInstance().getToken(), spotId)
                .enqueue(new Callback<Spot>() {
                    @Override
                    public void onResponse(Call<Spot> call, Response<Spot> response) {
                        if(response.isSuccessful()) {
                            data.setValue(response.body());
                            updateSpotDetails(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Spot> call, Throwable t) {

                    }
                });

        return data;
    }

    private LiveData<Spot> getSpotDetailsFromDb(String spotId) {
        return spotDao.getSpotDetails(spotId);
    }

    public void addSpotToFavorites(String spotId) {
        new AddSpotToFavoritesAsyncTask(spotDao).execute(spotId);

        apiService.addSpotToFavorites(Profile.getInstance().getToken(), spotId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(app, "Added to favorites", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(app, "Failed to add to favorites", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public void removeSpotFromFavorites(String spotId) {
        new RemoveSpotFromFavoritesAsyncTask(spotDao).execute(spotId);

        apiService.removeSpotFromFavorites(Profile.getInstance().getToken(), spotId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(app, "Removed from favorites", Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(app, "Failed to remove from favorites",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static class InsertAllSpotsAsyncTask extends AsyncTask<List<Spot>, Void, Void> {
        private SpotDao spotDao;

        private InsertAllSpotsAsyncTask(SpotDao spotDao) {
            this.spotDao = spotDao;
        }

        @Override
        protected Void doInBackground(List<Spot>... lists) {
            spotDao.insertAllSpots(lists[0]);
            return null;
        }
    }

    private static class UpdateSpotDetailsAsyncTask extends AsyncTask<Spot, Void, Void> {
        private SpotDao spotDao;

        private UpdateSpotDetailsAsyncTask(SpotDao spotDao) {
            this.spotDao = spotDao;
        }

        @Override
        protected Void doInBackground(Spot... spots) {
            spotDao.updateSpotDetails(spots[0]);
            return null;
        }
    }

    private static class AddSpotToFavoritesAsyncTask extends AsyncTask<String, Void, Void> {
        private SpotDao spotDao;

        private AddSpotToFavoritesAsyncTask(SpotDao spotDao) {
            this.spotDao = spotDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            spotDao.addSpotToFavorites(strings[0]);
            return null;
        }
    }

    private static class RemoveSpotFromFavoritesAsyncTask extends AsyncTask<String, Void, Void> {
        private SpotDao spotDao;

        private RemoveSpotFromFavoritesAsyncTask(SpotDao spotDao) {
            this.spotDao = spotDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            spotDao.removeSpotFromFavorites(strings[0]);
            return null;
        }
    }
}
