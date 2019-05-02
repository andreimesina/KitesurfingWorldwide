package com.andreimesina.kitesurfingworldwide.activity.filter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.repository.Repository;

import java.util.List;

public class FilterViewModel extends ViewModel {

    private Repository repository = ServiceProvider.getInstance().getRepository();

    public FilterViewModel() { }

    public LiveData<List<String>> getCountries() {
        return repository.getAllSpotCountries();
    }
}
