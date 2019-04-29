package com.andreimesina.kitesurfingworldwide.activity.filter;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andreimesina.kitesurfingworldwide.R;
import com.andreimesina.kitesurfingworldwide.core.BaseActivity;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;

public class FilterActivity extends BaseActivity {

    private EditText editTextCountry;
    private EditText editTextWindProbability;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initToolbar(R.id.toolbar_layout_filter);
        setBackButtonToolbar();
        setTitle("Filter");

        initViews();
        listenOnApplyBtn();
    }

    private void initViews() {
        editTextCountry = findViewById(R.id.editText_country_filter);
        editTextWindProbability = findViewById(R.id.editText_windProbability_filter);
        btnApply = findViewById(R.id.btn_apply_filter);

        if (SpotFilter.getInstance().getCountry() != null) {
            editTextCountry.setText(SpotFilter.getInstance().getCountry());
        }

        if (SpotFilter.getInstance().getWindProbability() != null) {
            editTextWindProbability.setText(String.valueOf(SpotFilter.getInstance().getWindProbability()));
        }
    }

    private void listenOnApplyBtn() {
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = editTextCountry.getText().toString();
                String windProbability = editTextWindProbability.getText().toString();

                SpotFilter.getInstance().setCountry(country);
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
