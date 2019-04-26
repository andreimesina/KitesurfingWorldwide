package com.andreimesina.kitesurfingworldwide.activity.list;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andreimesina.kitesurfingworldwide.R;
import com.andreimesina.kitesurfingworldwide.core.AuthenticationManager;
import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;

import java.util.List;

import timber.log.Timber;

public class ListActivity extends AppCompatActivity {

    private ListViewModel viewModel;

    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        showLoading();

        viewModel.createProfile().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAuthenticated) {
                if(isAuthenticated == true) {
                    bindLiveData();

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.syncSpots();
                        }
                    });
                }
            }
        });

    }

    private void showLoading() {
        Toast.makeText(this, "Authenticating...", Toast.LENGTH_LONG).show();
    }

    private void bindLiveData() {
        viewModel.getSpots().observe(this, new Observer<List<Spot>>() {
            @Override
            public void onChanged(List<Spot> spots) {
                textView.setText(spots.toString());
            }
        });
    }

}
