package com.andreimesina.kitesurfingworldwide.activity.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;
import com.andreimesina.kitesurfingworldwide.data.repository.Repository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private Repository repository = ServiceProvider.getInstance().getRepository();

    public ListViewModel() {
    }

    public MutableLiveData<Boolean> createProfile() {
        return repository.createProfile();
    }

    public void syncSpots() {
        repository.syncSpots(new SpotFilter());
    }

    public LiveData<List<Spot>> getSpots() {
        return repository.getAllSpots(new SpotFilter());
    }

}
