package com.andreimesina.kitesurfingworldwide.activity.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;
import com.andreimesina.kitesurfingworldwide.data.repository.Repository;

import timber.log.Timber;

public class DetailsViewModel extends ViewModel {

    private Repository repository = ServiceProvider.getInstance().getRepository();

    public DetailsViewModel() {
        Timber.d("Started DetailsViewModel");
    }

    public LiveData<SpotDetails> getSpotDetails(String spotId) {
        return repository.getSpotDetails(spotId);
    }
}
