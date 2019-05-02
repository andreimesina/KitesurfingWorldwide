package com.andreimesina.kitesurfingworldwide.activity.filter;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andreimesina.kitesurfingworldwide.R;
import com.andreimesina.kitesurfingworldwide.core.BaseActivity;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;

import java.util.List;

public class FilterActivity extends BaseActivity {

    private FilterViewModel viewModel;

    private Spinner spinnerCountry;
    private EditText editTextWindProbability;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initToolbar(R.id.toolbar_layout_filter);
        setBackButtonToolbar();
        setTitle("Filter");

        viewModel = ViewModelProviders.of(this).get(FilterViewModel.class);

        initViews();
        listenOnApplyBtn();
    }

    private void initViews() {
        spinnerCountry = findViewById(R.id.spinner_country_filter);
        editTextWindProbability = findViewById(R.id.editText_windProbability_filter);
        btnApply = findViewById(R.id.btn_apply_filter);

        spinnerCountry.setPrompt("Select country...");

        String country = SpotFilter.getInstance().getCountry();
        viewModel.getCountries().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> countries) {
                countries.add(0, "All");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        FilterActivity.this, android.R.layout.simple_spinner_item, countries);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerCountry.setAdapter(adapter);

                if (country != null && countries.contains(country)) {
                    spinnerCountry.setSelection(countries.indexOf(country), true);
                }
            }
        });

        if (SpotFilter.getInstance().getWindProbability() != null) {
            editTextWindProbability.setText(String.valueOf(SpotFilter.getInstance().getWindProbability()));
        }
    }

    private void listenOnApplyBtn() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = spinnerCountry.getSelectedItem().toString().trim();
                String windProbability = editTextWindProbability.getText().toString();

                if(country.equals("All")) {
                    SpotFilter.getInstance().setCountry("");
                } else {
                    SpotFilter.getInstance().setCountry(country);
                }

                SpotFilter.getInstance().setWindProbability(windProbability);

                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
